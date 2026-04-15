package com.citizen.platform.service;

import com.citizen.platform.dto.*;
import com.citizen.platform.entity.*;
import com.citizen.platform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final InitiativeRepository initiativeRepository;

    @Transactional
    public CommentResponse create(Long authorId, CommentRequest request) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Initiative initiative = initiativeRepository.findById(request.getInitiativeId())
                .orElseThrow(() -> new RuntimeException("Initiative non trouvée"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .author(author)
                .initiative(initiative)
                .isModerated(false)
                .build();

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
        return commentRepository.findByModeratedFalse().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
    }

    private CommentResponse toResponse(Comment c) {
        return CommentResponse.builder()
                .id(c.getId())
                .content(c.getContent())
                .authorId(c.getAuthor().getId())
                .authorName(c.getAuthor().getFirstName() + " " + c.getAuthor().getLastName())
                .authorAvatar(null)
                .initiativeId(c.getInitiative().getId())
                .moderated(c.getIsModerated())
                .createdAt(c.getCreatedAt())
                .build();
    }
}
