package com.example.order_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient; // BARU DITAMBAHKAN
import org.springframework.cloud.client.loadbalancer.LoadBalanced; // BARU DITAMBAHKAN
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient // Wajib: Agar Order Service dapat mendaftar dan menemukan layanan lain dari Eureka
public class OrderServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(OrderServiceApplication.class, args);
    }

    /**
     * Konfigurasi Bean RestTemplate
     * * @LoadBalanced: ANOTASI KRUSIAL! 
     * Ini mengintegrasikan RestTemplate dengan Spring Cloud LoadBalancer.
     * RestTemplate sekarang bisa menggunakan NAMA LAYANAN (mis. "PRODUCT-SERVICE") 
     * di URL-nya, bukan IP dan Port.
     */
    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}