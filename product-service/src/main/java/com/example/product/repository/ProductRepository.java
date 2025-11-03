package com.example.product.repository;

import org.springframework.data.jpa.repository.JpaRepository; // Import Class Entity Product
import org.springframework.stereotype.Repository;

import com.example.product.model.Product;

/**
 * Repository interface for managing Product entities.
 * Extends JpaRepository to provide CRUD operations.
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    // Tidak perlu menambahkan metode kustom di sini untuk fungsi dasar.
    // JpaRepository sudah menyediakan findAll(), save(), findById(), dll.
}
