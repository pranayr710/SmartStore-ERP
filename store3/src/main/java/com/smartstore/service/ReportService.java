package com.smartstore.service;

import com.smartstore.model.Order;
import com.smartstore.model.Product;
import com.smartstore.repository.OrderRepository;
import com.smartstore.repository.ProductRepository;
import com.smartstore.dao.ProductDAO;
import com.smartstore.dao.AlertDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.*;

@Service
public class ReportService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ProductDAO productDAO;

    @Autowired
    private AlertDAO alertDAO;

    public Map<String, Object> summary() {
        Map<String, Object> result = new LinkedHashMap<>();

        List<Order> allOrders = orderRepository.findAll();
        List<Product> allProducts = productRepository.findAll();

        LocalDate today = LocalDate.now();

        // Today's orders (if date exists)
        double todaySales = 0;
        int todayOrders = 0;
        for (Order o : allOrders) {
            if (o.getDate() != null && o.getDate().toLocalDate().isEqual(today)) {
                todaySales += o.getTotal();
                todayOrders++;
            }
        }

        double avgOrderValue = allOrders.isEmpty() ? 0 :
                allOrders.stream().mapToDouble(Order::getTotal).average().orElse(0);

        String bestSeller = allProducts.isEmpty() ? "–" : allProducts.get(0).getName();

        long lowStock = allProducts.stream().filter(p -> p.getStock() <= 5).count();

        List<String> salesLabels = List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun");
        List<Double> salesValues = List.of(20000.0, 18000.0, 25000.0, 30000.0, 22000.0, 27000.0, 31000.0);

        double turnoverValue = productDAO.getTurnoverValue();
        int criticalAlerts = alertDAO.countUnresolved();
        List<Map<String, Object>> agingInventory = productDAO.getInventoryAgingReport();
        List<Map<String, Object>> yesterdayVsToday = productDAO.getYesterdayVsTodaySales();

        result.put("todaySales", todaySales);
        result.put("totalOrders", allOrders.size());
        result.put("todayOrders", todayOrders);
        result.put("bestSeller", bestSeller);
        result.put("avgOrderValue", avgOrderValue);
        result.put("salesLabels", salesLabels);
        result.put("salesValues", salesValues);
        result.put("totalProducts", allProducts.size());
        result.put("lowStock", lowStock);
        result.put("turnover", turnoverValue);
        result.put("criticalAlerts", criticalAlerts);
        result.put("agingInventory", agingInventory);
        result.put("yesterdayVsToday", yesterdayVsToday);

        return result;
    }

    public Map<String, Object> getProductWiseSalesReport(String startDate, String endDate) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Object>> productSales = productDAO.getProductWiseSalesComparison(startDate, endDate);
        result.put("data", productSales);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("totalRecords", productSales.size());
        return result;
    }

    public Map<String, Object> getVolumeWiseSalesReport() {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Object>> volumeSales = productDAO.getVolumeWiseSalesBreakdown();
        result.put("data", volumeSales);
        double totalRevenue = volumeSales.stream()
                .mapToDouble(m -> ((Number) m.getOrDefault("revenue", 0)).doubleValue())
                .sum();
        result.put("totalRevenue", totalRevenue);
        return result;
    }

    public Map<String, Object> getTurnoverReport() {
        Map<String, Object> result = new LinkedHashMap<>();
        Double turnoverValue = productDAO.getTurnoverValue();
        List<Order> allOrders = orderRepository.findAll();
        long last30DaysOrders = allOrders.stream()
                .filter(o -> o.getDate() != null && o.getDate().toLocalDate().isAfter(LocalDate.now().minusDays(30)))
                .count();
        result.put("turnoverValue30Days", turnoverValue);
        result.put("ordersCount", last30DaysOrders);
        result.put("period", "Last 30 days");
        return result;
    }

    public Map<String, Object> getYesterdayVsTodayReport() {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Object>> comparison = productDAO.getYesterdayVsTodaySales();
        result.put("comparison", comparison);
        if (comparison.size() >= 2) {
            result.put("today", comparison.get(0));
            result.put("yesterday", comparison.get(1));
        }
        return result;
    }

    public Map<String, Object> getInventoryAgingReport() {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Object>> agingData = productDAO.getInventoryAgingReport();
        result.put("data", agingData);
        long freshCount = agingData.stream().filter(m -> "FRESH".equals(m.get("aging_status"))).count();
        long agingCount = agingData.stream().filter(m -> "AGING".equals(m.get("aging_status"))).count();
        long oldCount = agingData.stream().filter(m -> "OLD".equals(m.get("aging_status"))).count();
        result.put("freshCount", freshCount);
        result.put("agingCount", agingCount);
        result.put("oldCount", oldCount);
        return result;
    }

    public Map<String, Object> getProfitabilityReport() {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Object>> profitData = productDAO.getProfitabilityReport();
        result.put("data", profitData);
        double totalProfit = profitData.stream()
                .mapToDouble(m -> ((Number) m.getOrDefault("total_profit", 0)).doubleValue())
                .sum();
        result.put("totalProfit", totalProfit);
        return result;
    }

    public Map<String, Object> getExpiringStockReport(Integer daysThreshold) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Object>> expiringData = productDAO.getExpiringStockReport(daysThreshold);
        result.put("data", expiringData);
        result.put("daysThreshold", daysThreshold);
        result.put("count", expiringData.size());
        return result;
    }

    public Map<String, Object> getCategoryHeatmapReport(String period) {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Map<String, Object>> heatmapData = productDAO.getCategoryWiseSalesHeatmap(period);
        result.put("data", heatmapData);
        result.put("period", period);
        return result;
    }

    public Map<String, Object> getReorderRequiredReport() {
        Map<String, Object> result = new LinkedHashMap<>();
        List<Product> reorderProducts = productDAO.getProductsRequiringReorder();
        result.put("data", reorderProducts);
        result.put("productsNeedingReorder", reorderProducts.size());
        return result;
    }

    public Map<String, Object> getForecastReorderAnalysis(Integer productId, Integer days) {
        Map<String, Object> result = new LinkedHashMap<>();
        Product product = productDAO.findById(productId);
        Double averageSales = productDAO.averageMovingForecast(productId, days);
        
        if (product != null) {
            int daysUntilStockout = (int) Math.ceil(product.getStock() / Math.max(averageSales, 0.1));
            result.put("productId", productId);
            result.put("productName", product.getName());
            result.put("currentStock", product.getStock());
            result.put("averageDailySales", averageSales);
            result.put("daysUntilStockout", daysUntilStockout);
            result.put("reorderLevel", product.getReorderLevel());
            result.put("minOrderQty", product.getMinOrderQty());
            result.put("shouldReorderNow", product.getStock() <= product.getReorderLevel());
            result.put("recommendedOrderQty", product.getMinOrderQty() * Math.max(1, (int) Math.ceil(30 / Math.max(averageSales, 0.1))));
        }
        
        return result;
    }
}
