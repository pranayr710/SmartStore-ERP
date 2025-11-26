package com.smartstore.controller;

import com.smartstore.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.List;

@RestController
@RequestMapping("/api/reports")
@CrossOrigin(origins = "*")
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/summary")
    public Map<String, Object> summary() {
        return reportService.summary();
    }

    @GetMapping("/product-wise-sales")
    public Map<String, Object> productWiseSales(
            @RequestParam(defaultValue = "2025-01-01") String startDate,
            @RequestParam(defaultValue = "2025-12-31") String endDate) {
        return reportService.getProductWiseSalesReport(startDate, endDate);
    }

    @GetMapping("/volume-wise-sales")
    public Map<String, Object> volumeWiseSales() {
        return reportService.getVolumeWiseSalesReport();
    }

    @GetMapping("/turnover")
    public Map<String, Object> turnoverReport() {
        return reportService.getTurnoverReport();
    }

    @GetMapping("/yesterday-vs-today")
    public Map<String, Object> yesterdayVsToday() {
        return reportService.getYesterdayVsTodayReport();
    }

    @GetMapping("/inventory-aging")
    public Map<String, Object> inventoryAging() {
        return reportService.getInventoryAgingReport();
    }

    @GetMapping("/profitability")
    public Map<String, Object> profitability() {
        return reportService.getProfitabilityReport();
    }

    @GetMapping("/expiring-stock")
    public Map<String, Object> expiringStock(
            @RequestParam(defaultValue = "30") Integer daysThreshold) {
        return reportService.getExpiringStockReport(daysThreshold);
    }

    @GetMapping("/category-heatmap")
    public Map<String, Object> categoryHeatmap(
            @RequestParam(defaultValue = "%Y-%m") String period) {
        return reportService.getCategoryHeatmapReport(period);
    }

    @GetMapping("/reorder-required")
    public Map<String, Object> reorderRequired() {
        return reportService.getReorderRequiredReport();
    }

    @GetMapping("/forecast-reorder/{productId}")
    public Map<String, Object> forecastReorder(@PathVariable Integer productId,
            @RequestParam(defaultValue = "30") Integer days) {
        return reportService.getForecastReorderAnalysis(productId, days);
    }

    // Additional endpoints can be added here if needed

}
