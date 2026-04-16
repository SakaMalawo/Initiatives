package com.citizen.platform.controller;

import com.citizen.platform.dto.CommentRequest;
import com.citizen.platform.dto.CommentResponse;
import com.citizen.platform.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/comments")
@CrossOrigin(origins = "*")
public class CommentController {

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    private Long getCurrentUserId() {
        return (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @PostMapping
    public ResponseEntity<CommentResponse> create(@Valid @RequestBody CommentRequest request) {
        return ResponseEntity.ok(commentService.create(getCurrentUserId(), request));
    }

    @GetMapping("/initiative/{initiativeId}")
    public ResponseEntity<List<CommentResponse>> findByInitiative(@PathVariable Long initiativeId) {
        return ResponseEntity.ok(commentService.findByInitiative(initiativeId));
    }

    @PutMapping("/{id}/moderate")
    public ResponseEntity<CommentResponse> moderate(@PathVariable Long id, @RequestParam(required = false) String reason) {
        return ResponseEntity.ok(commentService.moderate(id, getCurrentUserId(), reason));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        commentService.delete(id, getCurrentUserId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/unmoderated")
    public ResponseEntity<List<CommentResponse>> findUnmoderated() {
        return ResponseEntity.ok(commentService.findUnmoderated());
    }
}
