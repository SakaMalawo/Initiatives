package com.citizen.platform.service;

import com.citizen.platform.dto.*;
import com.citizen.platform.entity.*;
import com.citizen.platform.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final InitiativeRepository initiativeRepository;

    public CommentService(CommentRepository commentRepository, UserRepository userRepository, InitiativeRepository initiativeRepository) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.initiativeRepository = initiativeRepository;
    }

    @Transactional
    public CommentResponse create(Long authorId, CommentRequest request) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Initiative initiative = initiativeRepository.findById(request.getInitiativeId())
                .orElseThrow(() -> new RuntimeException("Initiative non trouvée"));

        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setAuthor(author);
        comment.setInitiative(initiative);
        comment.setIsModerated(false);

        return toResponse(commentRepository.save(comment));
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findByInitiative(Long initiativeId) {
        return commentRepository.findByInitiativeIdOrderByCreatedAtDesc(initiativeId).stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public CommentResponse moderate(Long commentId, Long adminId, String reason) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin non trouvé"));

        comment.setIsModerated(true);
        comment.setModeratedBy(admin);
        comment.setModeratedAt(java.time.LocalDateTime.now());
        comment.setModerationReason(reason);

        return toResponse(commentRepository.save(comment));
    }

    @Transactional
    public void delete(Long commentId, Long userId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Commentaire non trouvé"));

        boolean isAuthor = comment.getAuthor().getId().equals(userId);
        boolean isAdmin = userRepository.findById(userId)
                .map(u -> u.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN")))
                .orElse(false);

        if (!isAuthor && !isAdmin) {
            throw new RuntimeException("Non autorisé");
        }

        commentRepository.delete(comment);
    }

    @Transactional(readOnly = true)
    public List<CommentResponse> findUnmoderated() {
        return commentRepository.findByIsModeratedFalse().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse toResponse(Comment c) {
        CommentResponse response = new CommentResponse();
        response.setId(c.getId());
        response.setContent(c.getContent());
        response.setAuthorId(c.getAuthor().getId());
        response.setAuthorName(c.getAuthor().getFirstName() + " " + c.getAuthor().getLastName());
        response.setAuthorAvatar(null);
        response.setInitiativeId(c.getInitiative().getId());
        response.setModerated(c.getIsModerated());
        response.setCreatedAt(c.getCreatedAt());
        return response;
    }
}
