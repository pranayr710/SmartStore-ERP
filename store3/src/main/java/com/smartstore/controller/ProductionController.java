package com.smartstore.controller;

import com.smartstore.model.ProductionOrder;
import com.smartstore.service.ProductionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/production")
@CrossOrigin(origins = "*")
public class ProductionController {

    @Autowired
    private ProductionService productionService;

    @PostMapping("/create-order")
    public Map<String, Object> createProductionOrder(
            @RequestParam Integer productId,
            @RequestParam Integer quantity,
            @RequestParam String reason,
            @RequestParam(required = false) Integer supplierId,
            @RequestParam(required = false, defaultValue = "1") Integer requestedBy) {
        return productionService.createProductionOrder(productId, quantity, reason, supplierId, requestedBy);
    }

    @GetMapping("/pending")
    public List<ProductionOrder> getPendingOrders() {
        return productionService.getPendingOrders();
    }

    @GetMapping("/all")
    public List<ProductionOrder> getAllOrders() {
        return productionService.getAllProductionOrders();
    }

    @PatchMapping("/{id}/status")
    public void updateOrderStatus(@PathVariable Integer id, @RequestParam String status) {
        productionService.updateOrderStatus(id, status);
    }

    @PatchMapping("/{id}/complete")
    public void completeOrder(@PathVariable Integer id) {
        productionService.completeOrder(id);
    }

    @PostMapping("/auto-reorder/{productId}")
    public Map<String, Object> triggerAutoReorder(@PathVariable Integer productId) {
        return productionService.triggerAutoReorder(productId);
    }
}
