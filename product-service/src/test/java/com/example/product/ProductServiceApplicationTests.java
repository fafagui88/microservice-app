package com.example.product;

import org.springframework.beans.factory.annotation.Value;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class ProductServiceApplicationTests {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Test
    void contextLoads() {
        System.out.println("✅ Using database URL: " + dbUrl);
    }
}
