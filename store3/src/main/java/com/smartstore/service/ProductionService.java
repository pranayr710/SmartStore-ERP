package com.smartstore.service;

import com.smartstore.dao.ProductionOrderDAO;
import com.smartstore.dao.ProductDAO;
import com.smartstore.dao.AlertDAO;
import com.smartstore.model.ProductionOrder;
import com.smartstore.model.Product;
import com.smartstore.model.AlertNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ProductionService {

    @Autowired
    private ProductionOrderDAO productionOrderDAO;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private AlertDAO alertDAO;

    public Map<String, Object> createProductionOrder(Integer productId, Integer quantity, String reason, Integer supplierId, Integer requestedBy) {
        Map<String, Object> response = new HashMap<>();

        Product product = productDAO.findById(productId);
        if (product == null) {
            response.put("status", "ERROR");
            response.put("message", "Product not found");
            return response;
        }

        ProductionOrder order = new ProductionOrder();
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setReason(reason);
        order.setSupplierId(supplierId);
        order.setRequestedBy(requestedBy);
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        productionOrderDAO.save(order);

        // Create alert for production team
        AlertNotification alert = new AlertNotification();
        alert.setProductId(productId);
        alert.setAlertType("PRODUCTION_ORDER");
        alert.setSeverity("HIGH");
        alert.setMessage("Production Order Created: " + product.getName() + " - Qty: " + quantity + " - Reason: " + reason);
        alert.setIsResolved(false);
        alertDAO.save(alert);

        response.put("status", "OK");
        response.put("message", "Production order created successfully");
        response.put("orderId", order.getId());
        return response;
    }

    public List<ProductionOrder> getPendingOrders() {
        return productionOrderDAO.findPending();
    }

    public List<ProductionOrder> getAllProductionOrders() {
        return productionOrderDAO.findAll();
    }

    public void updateOrderStatus(Integer orderId, String status) {
        productionOrderDAO.updateStatus(orderId, status);
    }

    public void completeOrder(Integer orderId) {
        productionOrderDAO.markCompleted(orderId);
    }

    public Map<String, Object> triggerAutoReorder(Integer productId) {
        Map<String, Object> response = new HashMap<>();
        Product product = productDAO.findById(productId);

        if (product == null) {
            response.put("status", "ERROR");
            response.put("message", "Product not found");
            return response;
        }

        if (product.getStock() <= product.getReorderLevel()) {
            int suggestedQty = Math.max(product.getMinOrderQty(), product.getReorderLevel() * 2);
            createProductionOrder(productId, suggestedQty, "AUTO_REORDER", null, 1);
            response.put("status", "OK");
            response.put("message", "Auto reorder triggered");
            response.put("suggestedQuantity", suggestedQty);
        } else {
            response.put("status", "SKIPPED");
            response.put("message", "Stock is above reorder level");
        }

        return response;
    }
}
