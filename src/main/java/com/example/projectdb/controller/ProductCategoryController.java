package com.example.projectdb.controller;

import com.example.projectdb.entity.ProductCategory;
import com.example.projectdb.repository.ProductCategoryRepository;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
public class ProductCategoryController {

    private final ProductCategoryRepository productCategoryRepository;

    public ProductCategoryController(ProductCategoryRepository productCategoryRepository) {
        this.productCategoryRepository = productCategoryRepository;
    }

    @PostMapping
    public ProductCategory addCategory(@RequestBody ProductCategory category) {
        if (category.getProducts() != null) {
            category.getProducts().forEach(product -> product.setCategory(category));
        }

        return productCategoryRepository.save(category);
    }

    @GetMapping("/{id}")
    public ProductCategory getCategory(@PathVariable Integer id) {
        return productCategoryRepository.findById(id).orElse(null);
    }
}