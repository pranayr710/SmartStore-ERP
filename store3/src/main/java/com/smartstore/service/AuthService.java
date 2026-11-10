package com.smartstore.service;

import com.smartstore.dao.UserDAO;
import com.smartstore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Service
public class AuthService {

    @Autowired
    private UserDAO userDAO;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public User login(String username, String password) {
        if (username == null || password == null) return null;
        User user = userDAO.findActiveByUsername(username);
        if (user == null || user.getPassword() == null) return null;

        String stored = user.getPassword();
        if (isBcryptHash(stored)) {
            return passwordEncoder.matches(password, stored) ? user : null;
        }
        // Accounts created before passwords were hashed still hold the plain value.
        // Accept it once, then replace it with a hash so it is never compared in plain text again.
        if (MessageDigest.isEqual(stored.getBytes(StandardCharsets.UTF_8), password.getBytes(StandardCharsets.UTF_8))) {
            userDAO.updatePassword(user.getId(), passwordEncoder.encode(password));
            return user;
        }
        return null;
    }

    public String signupCustomer(String username, String password) {
        User existing = userDAO.findByUsername(username);
        if (existing != null) {
            return "USERNAME_EXISTS";
        }
        userDAO.saveCustomer(username, passwordEncoder.encode(password));
        return "OK";
    }

    public String signupStaff(String username, String password) {
        userDAO.saveStaff(username, passwordEncoder.encode(password));
        return "OK";
    }

    public String createAdminIfNotExists(String username, String password) {
        if (userDAO.adminExists()) return "ADMIN_EXISTS";
        userDAO.saveAdmin(username, passwordEncoder.encode(password));
        return "OK";
    }

    private static boolean isBcryptHash(String value) {
        return value.startsWith("$2a$") || value.startsWith("$2b$") || value.startsWith("$2y$");
    }
}
