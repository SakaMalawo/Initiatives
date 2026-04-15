package com.citizen.platform.controller;

import com.citizen.platform.dto.InitiativeRequest;
import com.citizen.platform.dto.InitiativeResponse;
import com.citizen.platform.security.JwtAuthFilter;
import com.citizen.platform.service.InitiativeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/initiatives")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class InitiativeController {

    private final InitiativeService initiativeService;

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public ResponseEntity<InitiativeResponse> create(@Valid @RequestBody InitiativeRequest request) {
        return ResponseEntity.ok(initiativeService.create(getCurrentUserId(), request));
    }

    @GetMapping
    public ResponseEntity<List<InitiativeResponse>> findAll() {
        return ResponseEntity.ok(initiativeService.findAll(getCurrentUserId()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InitiativeResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(initiativeService.findById(id, getCurrentUserId()));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<InitiativeResponse>> findByStatus(@PathVariable String status) {
        return ResponseEntity.ok(initiativeService.findByStatus(status, getCurrentUserId()));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<InitiativeResponse>> findByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(initiativeService.findByCategory(categoryId, getCurrentUserId()));
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<InitiativeResponse>> findByZone(@PathVariable Long zoneId) {
        return ResponseEntity.ok(initiativeService.findByZone(zoneId, getCurrentUserId()));
    }

    @GetMapping("/popular")
    public ResponseEntity<List<InitiativeResponse>> findPopular() {
        return ResponseEntity.ok(initiativeService.findPopular(getCurrentUserId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<InitiativeResponse> update(@PathVariable Long id, @Valid @RequestBody InitiativeRequest request) {
        return ResponseEntity.ok(initiativeService.update(id, getCurrentUserId(), request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        initiativeService.delete(id, getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status/{status}")
    public ResponseEntity<InitiativeResponse> changeStatus(@PathVariable Long id, @PathVariable String status) {
        return ResponseEntity.ok(initiativeService.changeStatus(id, status, getCurrentUserId()));
    }
}
