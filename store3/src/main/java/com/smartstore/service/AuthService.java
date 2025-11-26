package com.smartstore.service;

import com.smartstore.dao.UserDAO;
import com.smartstore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserDAO userDAO;

    public User login(String username, String password) {
        return userDAO.findByUsernameAndPassword(username, password);
    }

    public String signupCustomer(String username, String password) {
        User existing = userDAO.findByUsername(username);
        if (existing != null) {
            return "USERNAME_EXISTS";
        }
        userDAO.saveCustomer(username, password);
        return "OK";
    }

    public String signupStaff(String username, String password) {
        userDAO.saveStaff(username, password);
        return "OK";
    }

    public String createAdminIfNotExists(String username, String password) {
        if (userDAO.adminExists()) return "ADMIN_EXISTS";
        userDAO.saveAdmin(username, password);
        return "OK";
    }
}
