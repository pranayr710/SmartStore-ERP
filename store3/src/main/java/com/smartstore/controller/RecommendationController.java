package com.smartstore.controller;

import com.smartstore.model.Product;
import com.smartstore.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/recommend")
@CrossOrigin(origins = "*")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    @GetMapping
    public Product recommend(@RequestParam(required = false) Double budget,
                             @RequestParam(required = false) String brand,
                             @RequestParam(required = false) Integer battery,
                             @RequestParam(required = false) Integer storage,
                             @RequestParam(required = false) String camera) {
        return recommendationService.recommend(budget, brand, battery, storage, camera);
    }
}
