package com.smartstore.dao;

import com.smartstore.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.sql.Timestamp;
import java.util.List;

@Repository
public class OrderDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void insert(Order order) {
        String sql = "INSERT INTO orders(customer_id, product_id, quantity, total, date) VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql,
                order.getCustomerId(),
                order.getProductId(),
                order.getQuantity(),
                order.getTotal(),
                Timestamp.valueOf(order.getDate()));
    }

    public List<Order> findAll() {
        String sql = "SELECT * FROM orders ORDER BY date DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class));
    }

    public List<Order> findByCustomer(Integer customerId) {
        String sql = "SELECT * FROM orders WHERE customer_id=? ORDER BY date DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Order.class), customerId);
    }

    public Double todaySales() {
        String sql = "SELECT COALESCE(SUM(total),0) FROM orders WHERE DATE(date) = CURDATE()";
        return jdbcTemplate.queryForObject(sql, Double.class);
    }

    public Integer countAll() {
        String sql = "SELECT COUNT(*) FROM orders";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
