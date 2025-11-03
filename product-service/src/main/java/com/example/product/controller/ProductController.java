package com.example.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.product.model.Product;
import com.example.product.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    // Endpoint: CREATE NEW PRODUCT
    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productRepository.save(product);
    }

    // Endpoint: VIEW ALL PRODUCTS
    @GetMapping
    public List<Product> viewAllProducts() {
        return productRepository.findAll();
    }
    
    // Endpoint: VIEW PRODUCT BY ID (untuk dipanggil Order Service)
    @GetMapping("/{id}")
    public Product viewProductById(@PathVariable Long id) {
        return productRepository.findById(id).orElse(null);
    }
}