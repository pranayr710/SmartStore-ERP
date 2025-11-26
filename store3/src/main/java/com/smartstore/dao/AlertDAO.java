package com.smartstore.dao;

import com.smartstore.model.AlertNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AlertDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(AlertNotification alert) {
        String sql = "INSERT INTO alert_notifications(product_id, alert_type, severity, message, is_resolved) " +
                     "VALUES (?,?,?,?,?)";
        jdbcTemplate.update(sql, alert.getProductId(), alert.getAlertType(), alert.getSeverity(),
                alert.getMessage(), false);
    }

    public List<AlertNotification> findUnresolved() {
        String sql = "SELECT * FROM alert_notifications WHERE is_resolved = FALSE ORDER BY severity DESC, created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AlertNotification.class));
    }

    public List<AlertNotification> findAll() {
        String sql = "SELECT * FROM alert_notifications ORDER BY created_at DESC LIMIT 100";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AlertNotification.class));
    }

    public void resolveAlert(Integer id) {
        String sql = "UPDATE alert_notifications SET is_resolved=TRUE, resolved_at=NOW() WHERE id=?";
        jdbcTemplate.update(sql, id);
    }

    public int countUnresolved() {
        String sql = "SELECT COUNT(*) FROM alert_notifications WHERE is_resolved = FALSE";
        return jdbcTemplate.queryForObject(sql, Integer.class);
    }
}
