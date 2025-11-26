package com.smartstore.controller;

import com.smartstore.model.AlertNotification;
import com.smartstore.dao.AlertDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/alerts")
@CrossOrigin(origins = "*")
public class AlertsController {

    @Autowired
    private AlertDAO alertDAO;

    @GetMapping("/unresolved")
    public List<AlertNotification> getUnresolvedAlerts() {
        return alertDAO.findUnresolved();
    }

    @GetMapping("/all")
    public List<AlertNotification> getAllAlerts() {
        return alertDAO.findAll();
    }

    @GetMapping("/count")
    public Map<String, Integer> getUnresolvedCount() {
        Map<String, Integer> result = new HashMap<>();
        result.put("unresolved_count", alertDAO.countUnresolved());
        return result;
    }

    @PatchMapping("/{id}/resolve")
    public Map<String, String> resolveAlert(@PathVariable Integer id) {
        alertDAO.resolveAlert(id);
        Map<String, String> result = new HashMap<>();
        result.put("status", "RESOLVED");
        return result;
    }
}
