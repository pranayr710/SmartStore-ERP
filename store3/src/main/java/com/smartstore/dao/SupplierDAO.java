package com.smartstore.dao;

import com.smartstore.model.Supplier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SupplierDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public List<Supplier> findAll() {
        String sql = "SELECT * FROM suppliers WHERE status = 'ACTIVE'";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Supplier.class));
    }

    public Supplier findById(Integer id) {
        String sql = "SELECT * FROM suppliers WHERE id = ?";
        List<Supplier> list = jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Supplier.class), id);
        return list.isEmpty() ? null : list.get(0);
    }

    public void save(Supplier supplier) {
        String sql = "INSERT INTO suppliers(name, contact_person, email, phone, address, status) VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, supplier.getName(), supplier.getContactPerson(), supplier.getEmail(),
                supplier.getPhone(), supplier.getAddress(), supplier.getStatus() != null ? supplier.getStatus() : "ACTIVE");
    }

    public void update(Supplier supplier) {
        String sql = "UPDATE suppliers SET name=?, contact_person=?, email=?, phone=?, address=?, status=? WHERE id=?";
        jdbcTemplate.update(sql, supplier.getName(), supplier.getContactPerson(), supplier.getEmail(),
                supplier.getPhone(), supplier.getAddress(), supplier.getStatus(), supplier.getId());
    }

    public void delete(Integer id) {
        String sql = "UPDATE suppliers SET status='INACTIVE' WHERE id=?";
        jdbcTemplate.update(sql, id);
    }
}
