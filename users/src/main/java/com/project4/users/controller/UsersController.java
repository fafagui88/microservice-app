package com.project4.users.controller;

import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.project4.users.repository.UsersRepository;
import com.project4.users.model.Users;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UsersController {

    private final UsersRepository repo;

    @GetMapping
    public List<Users> getAll() {
        return repo.findAll();
    }

    @GetMapping("/{id}")
    public Users getUser(@PathVariable Long id) {
        return repo.findById(id).orElse(null);
    }

    @PostMapping
    public Users create(@RequestBody Users user) {
        return repo.save(user);
    }

    @PutMapping("/{id}/saldo")
    public Users updateSaldo(@PathVariable Long id, @RequestParam Double saldo) {
        Users user = repo.findById(id).orElse(null);
        if (user != null) {
            user.setSaldo(saldo);
            return repo.save(user);
        }
        return null;
    }
}
