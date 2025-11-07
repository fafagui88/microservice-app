package com.project4.products.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.project4.products.model.Products;

public interface ProductsRepository extends JpaRepository<Products, Long> { }
