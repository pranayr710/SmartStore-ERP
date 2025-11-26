package com.smartstore.controller;

import com.smartstore.service.ExportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/export")
@CrossOrigin(origins = "*")
public class ExportController {

    @Autowired
    private ExportService exportService;

    @GetMapping("/products/excel")
    public void productsExcel(HttpServletResponse response) throws Exception {
        exportService.exportProductsExcel(response);
    }

    @GetMapping("/orders/excel")
    public void ordersExcel(HttpServletResponse response) throws Exception {
        exportService.exportOrdersExcel(response);
    }

    @GetMapping("/products/pdf")
    public void productsPdf(HttpServletResponse response) throws Exception {
        exportService.exportProductsPdf(response);
    }

    @GetMapping("/orders/pdf")
    public void ordersPdf(HttpServletResponse response) throws Exception {
        exportService.exportOrdersPdf(response);
    }
}
