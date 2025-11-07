package com.project4.histories.controller;

import org.springframework.web.bind.annotation.*;
import com.project4.histories.model.History;
import com.project4.histories.repository.HistoryRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/histories")
@RequiredArgsConstructor
public class HistoryController {

    private final HistoryRepository repo;

    @GetMapping("/{userId}")
    public List<History> getByUser(@PathVariable Long userId) {
        return repo.findByUserId(userId);
    }

    @PostMapping
    public History create(@RequestBody History history) {
        return repo.save(history);
    }
}
