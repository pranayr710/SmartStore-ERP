package com.smartstore.service;

import com.smartstore.dao.ProductDAO;
import com.smartstore.dao.AlertDAO;
import com.smartstore.model.Product;
import com.smartstore.model.AlertNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private AlertDAO alertDAO;

    public List<Product> getAllProducts() {
        return productDAO.findAll();
    }

    public void addProduct(Product product) {
        productDAO.save(product);
    }

    public void updateProduct(Product product) {
        productDAO.update(product);
    }

    public void delete(Integer id) {
        productDAO.delete(id);
    }

    public void updateStock(Integer id, Integer stock) {
        productDAO.updateStock(id, stock);
        checkAndCreateAlerts(id);
    }

    public Product getById(Integer id) {
        return productDAO.findById(id);
    }

    public int totalProducts() {
        Integer c = productDAO.countAll();
        return c == null ? 0 : c;
    }

    public int lowStockCount(int threshold) {
        Integer c = productDAO.countLowStock(threshold);
        return c == null ? 0 : c;
    }

    public List<Map<String, Object>> getProductWiseSalesComparison(String startDate, String endDate) {
        return productDAO.getProductWiseSalesComparison(startDate, endDate);
    }

    public List<Map<String, Object>> getCategoryWiseSalesHeatmap(String period) {
        return productDAO.getCategoryWiseSalesHeatmap(period);
    }

    public List<Map<String, Object>> getInventoryAgingReport() {
        return productDAO.getInventoryAgingReport();
    }

    public List<Map<String, Object>> getProfitabilityReport() {
        return productDAO.getProfitabilityReport();
    }

    public List<Map<String, Object>> getExpiringStockReport(Integer daysThreshold) {
        return productDAO.getExpiringStockReport(daysThreshold);
    }

    public List<Map<String, Object>> getYesterdayVsTodaySales() {
        return productDAO.getYesterdayVsTodaySales();
    }

    public Double getTurnoverValue() {
        return productDAO.getTurnoverValue();
    }

    public List<Map<String, Object>> getVolumeWiseSalesBreakdown() {
        return productDAO.getVolumeWiseSalesBreakdown();
    }

    public List<Product> getProductsRequiringReorder() {
        return productDAO.getProductsRequiringReorder();
    }

    private void checkAndCreateAlerts(Integer productId) {
        Product p = productDAO.findById(productId);
        if (p != null) {
            if (p.getStock() <= 2) {
                createAlert(productId, "OUT_OF_STOCK", "CRITICAL", "Stock is critically low at " + p.getStock() + " units");
            } else if (p.getStock() <= p.getReorderLevel()) {
                createAlert(productId, "LOW_STOCK", "HIGH", "Stock is below reorder level. Current: " + p.getStock() + ", Reorder Level: " + p.getReorderLevel());
            }
        }
    }

    private void createAlert(Integer productId, String alertType, String severity, String message) {
        AlertNotification alert = new AlertNotification();
        alert.setProductId(productId);
        alert.setAlertType(alertType);
        alert.setSeverity(severity);
        alert.setMessage(message);
        alert.setIsResolved(false);
        alertDAO.save(alert);
    }
}
