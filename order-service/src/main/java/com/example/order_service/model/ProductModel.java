package com.example.order_service.model;

/**
 * Model ini digunakan untuk memetakan (map) data yang diterima 
 * dari Product Service melalui HTTP Request. 
 * Ini BUKAN entity database Order Service.
 */
public class ProductModel {
    private Long id;
    private String name;
    private Double price;

    // Default constructor
    public ProductModel() {}

    // Constructor with fields
    public ProductModel(Long id, String name, Double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Getters and Setters (atau gunakan Lombok jika tersedia)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Double getPrice() { return price; }
    public void setPrice(Double price) { this.price = price; }

    @Override
    public String toString() {
        return "ProductModel{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                '}';
    }
}