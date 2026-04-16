package com.citizen.platform.controller;

import com.citizen.platform.entity.Zone;
import com.citizen.platform.repository.ZoneRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/zones")
@CrossOrigin(origins = "*")
public class ZoneController {

    private final ZoneRepository zoneRepository;

    public ZoneController(ZoneRepository zoneRepository) {
        this.zoneRepository = zoneRepository;
    }

    @GetMapping
    public ResponseEntity<List<Zone>> findAll() {
        return ResponseEntity.ok(zoneRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Zone> create(@RequestBody Zone zone) {
        return ResponseEntity.ok(zoneRepository.save(zone));
    }
}
