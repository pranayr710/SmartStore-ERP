package com.smartstore.controller;

import com.smartstore.model.User;
import com.smartstore.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    public Map<String, Object> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        User user = authService.login(username, password);
        Map<String, Object> map = new HashMap<>();
        if (user == null) {
            map.put("status", "ERROR");
        } else {
            map.put("status", "OK");
            map.put("role", user.getRole());
            map.put("userId", user.getId());
        }
        return map;
    }

    @PostMapping("/signup")
    public Map<String, String> signup(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String res = authService.signupCustomer(username, password);
        Map<String, String> map = new HashMap<>();
        map.put("status", res);
        return map;
    }

    @PostMapping("/signup/customer")
    public Map<String, String> signupCustomer(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String res = authService.signupCustomer(username, password);
        Map<String, String> map = new HashMap<>();
        map.put("status", res);
        return map;
    }

    @PostMapping("/signup-staff")
    public Map<String, String> signupStaff(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String res = authService.signupStaff(username, password);
        Map<String, String> map = new HashMap<>();
        map.put("status", res);
        return map;
    }

    @PostMapping("/init-admin")
    public Map<String, String> initAdmin(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");
        String res = authService.createAdminIfNotExists(username, password);
        Map<String, String> map = new HashMap<>();
        map.put("status", res);
        return map;
    }
}
