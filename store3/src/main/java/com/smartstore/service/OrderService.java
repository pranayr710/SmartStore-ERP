package com.smartstore.service;

import com.smartstore.dao.OrderDAO;
import com.smartstore.dao.ProductDAO;
import com.smartstore.model.Order;
import com.smartstore.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    private ProductDAO productDAO;

    public Map<String, Object> placeOrder(Integer customerId, Integer productId, Integer quantity) {
        Map<String, Object> res = new HashMap<>();

        if (customerId == null || productId == null || quantity == null || quantity <= 0) {
            res.put("status", "ERROR");
            res.put("message", "Invalid order details");
            return res;
        }

        Product p = productDAO.findById(productId);
        if (p == null) {
            res.put("status", "ERROR");
            res.put("message", "Product not found");
            return res;
        }

        Integer stock = p.getStock() != null ? p.getStock() : 0;
        if (quantity > stock) {
            res.put("status", "ERROR");
            res.put("message", "Only " + stock + " items available in stock");
            return res;
        }

        double price = p.getPrice() != null ? p.getPrice() : 0.0;
        double total = price * quantity;

        Order order = new Order();
        order.setCustomerId(customerId);
        order.setProductId(productId);
        order.setQuantity(quantity);
        order.setTotal(total);
        order.setDate(LocalDateTime.now());

        orderDAO.insert(order);
        productDAO.updateStock(productId, stock - quantity);

        res.put("status", "OK");
        return res;
    }

    public List<Order> getOrdersForCustomer(Integer customerId) {
        return orderDAO.findByCustomer(customerId);
    }

    public List<Order> getAllOrders() {
        return orderDAO.findAll();
    }

    public Double todaySales() {
        return orderDAO.todaySales();
    }

    public int totalOrders() {
        Integer c = orderDAO.countAll();
        return c == null ? 0 : c;
    }
}
