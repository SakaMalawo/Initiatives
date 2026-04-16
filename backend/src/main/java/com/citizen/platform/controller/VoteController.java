package com.citizen.platform.controller;

import com.citizen.platform.dto.VoteRequest;
import com.citizen.platform.service.VoteService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/votes")
@CrossOrigin(origins = "*")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public ResponseEntity<Void> vote(@Valid @RequestBody VoteRequest request) {
        voteService.vote(getCurrentUserId(), request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{initiativeId}")
    public ResponseEntity<Void> removeVote(@PathVariable Long initiativeId) {
        voteService.removeVote(getCurrentUserId(), initiativeId);
        return ResponseEntity.noContent().build();
    }
}
