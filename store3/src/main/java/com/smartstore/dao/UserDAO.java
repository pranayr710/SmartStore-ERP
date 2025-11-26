package com.smartstore.dao;

import com.smartstore.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public User findByUsernameAndPassword(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=? AND status='ACTIVE'";
        List<User> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), username, password);
        return list.isEmpty() ? null : list.get(0);
    }

    public User findByUsername(String username) {
        String sql = "SELECT * FROM users WHERE username=?";
        List<User> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(User.class), username);
        return list.isEmpty() ? null : list.get(0);
    }

    public void saveCustomer(String username, String password) {
        String sql = "INSERT INTO users(username,password,role,status) VALUES(?,?,'CUSTOMER','ACTIVE')";
        jdbcTemplate.update(sql, username, password);
    }

    public void saveStaff(String username, String password) {
        String sql = "INSERT INTO users(username,password,role,status) VALUES(?,?,'STAFF','ACTIVE')";
        jdbcTemplate.update(sql, username, password);
    }

    public boolean adminExists() {
        String sql = "SELECT COUNT(*) FROM users WHERE role='ADMIN'";
        Integer count = jdbcTemplate.queryForObject(sql, Integer.class);
        return count != null && count > 0;
    }

    public void saveAdmin(String username, String password) {
        String sql = "INSERT INTO users(username,password,role,status) VALUES(?,?,'ADMIN','ACTIVE')";
        jdbcTemplate.update(sql, username, password);
    }
}
