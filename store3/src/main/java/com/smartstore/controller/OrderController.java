package com.smartstore.controller;

import com.smartstore.model.Order;
import com.smartstore.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @PostMapping
    public Map<String, Object> create(@RequestBody Order body) {
        return orderService.placeOrder(body.getCustomerId(), body.getProductId(), body.getQuantity());
    }

    @GetMapping
    public List<Order> all() {
        return orderService.getAllOrders();
    }

    @GetMapping("/customer/{customerId}")
    public List<Order> byCustomer(@PathVariable Integer customerId) {
        return orderService.getOrdersForCustomer(customerId);
    }

    @GetMapping("/today-sales")
    public Double todaySales() {
        return orderService.todaySales();
    }
}
