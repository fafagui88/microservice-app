package com.example.order_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.example.order_service.model.ProductModel;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    // Injeksi RestTemplate yang sudah di-LoadBalanced
    private final RestTemplate restTemplate;
    
    public OrderController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    /**
     * Endpoint Utama: Demonstrasi Load Balancing.
     * Order Service memanggil endpoint demo-info di Product Service.
     * Setiap kali endpoint ini dipanggil, permintaan akan diarahkan ke instansi Product Service 
     * yang berbeda (misalnya 9092, lalu 9094, lalu 9092, dst.)
     */
    @GetMapping("/create-order-demo")
    public String createOrderDemo() {
        
        // 1. URL dengan NAMA LAYANAN EUREKA (PRODUCT-SERVICE)
        final String productServiceUrl = "http://PRODUCT-SERVICE/api/products/demo-info";
        
        try {
            // RestTemplate secara otomatis akan mencari instansi "PRODUCT-SERVICE" 
            // yang UP (dari Eureka) dan memilih salah satunya (Load Balancing).
            String productResponse = restTemplate.getForObject(productServiceUrl, String.class);
            
            String orderResult = "Order created successfully! ";
            String communicationInfo = "Product Service Status: " + productResponse;
            
            return orderResult + communicationInfo;

        } catch (Exception e) {
            // Jika semua instansi Product Service down
            return "Order creation failed: Cannot connect to PRODUCT-SERVICE. Error: " + e.getMessage();
        }
    }

    /**
     * Endpoint Sekunder: Contoh pemanggilan data Product by ID.
     */
    @GetMapping("/get-product/{id}")
    public String getProductDetails(@PathVariable Long id) {
        // URL menggunakan NAMA LAYANAN EUREKA dan endpoint Product Service
        final String productUrl = "http://PRODUCT-SERVICE/api/products/" + id;
        
        try {
            // Panggil Product Service dan map hasilnya ke ProductModel
            ProductModel productDetails = restTemplate.getForObject(productUrl, ProductModel.class);
            
            return "Successfully fetched Product Details from Load Balanced instance: " + productDetails.toString();
        } catch (Exception e) {
            return "Failed to fetch product details. Error: " + e.getMessage();
        }
    }
}