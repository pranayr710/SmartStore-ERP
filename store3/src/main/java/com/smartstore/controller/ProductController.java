package com.smartstore.controller;

import com.smartstore.model.Product;
import com.smartstore.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> all() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Product byId(@PathVariable Integer id) {
        return productService.getById(id);
    }

    @PostMapping
    public void create(@RequestBody Product product) {
        productService.addProduct(product);
    }

    @PutMapping("/{id}")
    public void update(@PathVariable Integer id, @RequestBody Product product) {
        product.setId(id);
        productService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        productService.delete(id);
    }

    @PatchMapping("/{id}/stock")
    public void updateStock(@PathVariable Integer id, @RequestBody Integer stock) {
        productService.updateStock(id, stock);
    }

    @GetMapping("/analytics/sales-comparison")
    public List<Map<String, Object>> getSalesComparison(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        return productService.getProductWiseSalesComparison(startDate, endDate);
    }

    @GetMapping("/analytics/aging-report")
    public List<Map<String, Object>> getAgingReport() {
        return productService.getInventoryAgingReport();
    }

    @GetMapping("/analytics/profitability")
    public List<Map<String, Object>> getProfitability() {
        return productService.getProfitabilityReport();
    }

    @GetMapping("/analytics/expiring-stock")
    public List<Map<String, Object>> getExpiringStock(@RequestParam(defaultValue = "30") Integer daysThreshold) {
        return productService.getExpiringStockReport(daysThreshold);
    }

    @GetMapping("/analytics/yesterday-today")
    public List<Map<String, Object>> getYesterdayVsToday() {
        return productService.getYesterdayVsTodaySales();
    }

    @GetMapping("/analytics/turnover")
    public Double getTurnover() {
        return productService.getTurnoverValue();
    }

    @GetMapping("/analytics/volume-breakdown")
    public List<Map<String, Object>> getVolumeBreakdown() {
        return productService.getVolumeWiseSalesBreakdown();
    }

    @GetMapping("/reorder-required")
    public List<Product> getReorderRequired() {
        return productService.getProductsRequiringReorder();
    }
}
