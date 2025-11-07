package com.project4.products.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.project4.products.repository.ProductsRepository;
import com.project4.products.model.Products;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductsController {

    private final ProductsRepository repo;

    @GetMapping
    public List<Products> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Products getProduct(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PostMapping
    public Products create(@RequestBody Products product) {
        return repo.save(product);
    }
}
