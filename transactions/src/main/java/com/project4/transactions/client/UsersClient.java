package com.project4.transactions.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.project4.transactions.dto.UsersDto;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsersClient {

    private final RestTemplate restTemplate;

    @Value("${users.service.url}")
    private String usersServiceUrl;

    // GET user by ID
    public UsersDto getUser(Long id) {
        String url = usersServiceUrl + "/api/users/" + id;
        return restTemplate.getForObject(url, UsersDto.class);
    }

    // PUT update saldo
    public void updateSaldo(Long id, Double newSaldo) {
        String url = usersServiceUrl + "/api/users/" + id + "/saldo?saldo=" + newSaldo;
        restTemplate.put(url, null);
    }
}
