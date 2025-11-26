package com.smartstore.dao;

import com.smartstore.model.ProductionOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Timestamp;
import java.util.List;

@Repository
public class ProductionOrderDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(ProductionOrder po) {
        String sql = "INSERT INTO production_orders(product_id, quantity, status, reason, supplier_id, requested_by, created_at) " +
                     "VALUES (?,?,?,?,?,?,?)";
        jdbcTemplate.update(sql, po.getProductId(), po.getQuantity(), po.getStatus() != null ? po.getStatus() : "PENDING",
                po.getReason(), po.getSupplierId(), po.getRequestedBy(), Timestamp.valueOf(po.getCreatedAt()));
    }

    public List<ProductionOrder> findPending() {
        String sql = "SELECT * FROM production_orders WHERE status IN ('PENDING', 'IN_PROGRESS') ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProductionOrder.class));
    }

    public List<ProductionOrder> findAll() {
        String sql = "SELECT * FROM production_orders ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(ProductionOrder.class));
    }

    public void updateStatus(Integer id, String status) {
        String sql = "UPDATE production_orders SET status=? WHERE id=?";
        jdbcTemplate.update(sql, status, id);
    }

    public void markCompleted(Integer id) {
        String sql = "UPDATE production_orders SET status='COMPLETED', completed_at=NOW() WHERE id=?";
        jdbcTemplate.update(sql, id);
    }
}
