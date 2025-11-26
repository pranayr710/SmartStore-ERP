package com.smartstore.service;

import com.smartstore.model.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private ProductService productService;

    public Product recommend(Double budget,
                             String brand,
                             Integer battery,
                             Integer storage,
                             String camera) {

        List<Product> all = productService.getAllProducts();

        List<Product> filtered = all.stream().filter(p -> {
            if (budget != null && p.getPrice() != null && p.getPrice() > budget) return false;
            if (brand != null && !brand.isBlank()) {
                if (p.getBrand() == null || !p.getBrand().equalsIgnoreCase(brand)) return false;
            }
            if (battery != null && p.getBattery() != null && parseInt(p.getBattery()) < battery) return false;
            if (storage != null && p.getStorage() != null && parseInt(p.getStorage()) < storage) return false;
            if (camera != null && !camera.isBlank()) {
                String camLower = camera.toLowerCase(Locale.ROOT);
                if (p.getCamera() == null || !p.getCamera().toLowerCase(Locale.ROOT).contains(camLower)) return false;
            }
            return true;
        }).collect(Collectors.toList());

        if (filtered.isEmpty()) return null;

        return filtered.stream()
                .sorted(Comparator.comparingDouble((Product p) -> p.getRating() != null ? p.getRating() : 0.0)
                        .thenComparingDouble(p -> p.getPrice() != null ? -p.getPrice() : 0.0)
                        .reversed())
                .findFirst()
                .orElse(filtered.get(0));
    }

    private int parseInt(String s) {
        try {
            return Integer.parseInt(s.replaceAll("[^0-9]", ""));
        } catch (Exception e) {
            return 0;
        }
    }
}
