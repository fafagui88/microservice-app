package com.example.product_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest

@TestPropertySource(properties = {
    "eureka.client.enabled=false" // Menonaktifkan Eureka Client saat test
})
class ProductServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
