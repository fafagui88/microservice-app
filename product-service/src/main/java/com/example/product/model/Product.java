package com.example.product.model;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor; // Pilihan terbaik untuk nilai mata uang

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // Field Master Produk
    private String name;           // Nama Produk
    private BigDecimal price;      // Harga Produk (Gunakan BigDecimal)
    private Integer stock;         // Jumlah Stok
    private String description;    // Deskripsi Produk
}
