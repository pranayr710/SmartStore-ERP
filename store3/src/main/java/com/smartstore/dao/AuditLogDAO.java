package com.smartstore.dao;

import com.smartstore.model.AuditLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AuditLogDAO {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(AuditLog log) {
        String sql = "INSERT INTO audit_logs(action_type, entity_type, entity_id, old_value, new_value, performed_by) " +
                     "VALUES (?,?,?,?,?,?)";
        jdbcTemplate.update(sql, log.getActionType(), log.getEntityType(), log.getEntityId(),
                log.getOldValue(), log.getNewValue(), log.getPerformedBy());
    }

    public List<AuditLog> findByEntity(String entityType, Integer entityId) {
        String sql = "SELECT * FROM audit_logs WHERE entity_type=? AND entity_id=? ORDER BY created_at DESC";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AuditLog.class), entityType, entityId);
    }

    public List<AuditLog> findAll() {
        String sql = "SELECT * FROM audit_logs ORDER BY created_at DESC LIMIT 500";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(AuditLog.class));
    }
}
