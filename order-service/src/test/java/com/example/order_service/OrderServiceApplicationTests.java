package com.example.order_service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
    "eureka.client.enabled=false" // Menonaktifkan Eureka Client saat test
})

@ActiveProfiles("test")
class OrderServiceApplicationTests {

	@Test
	void contextLoads() {
	}

}
