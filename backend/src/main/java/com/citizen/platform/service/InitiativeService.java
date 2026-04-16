package com.citizen.platform.service;

import com.citizen.platform.dto.*;
import com.citizen.platform.entity.*;
import com.citizen.platform.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InitiativeService {

    private final InitiativeRepository initiativeRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final ZoneRepository zoneRepository;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;

    public InitiativeService(InitiativeRepository initiativeRepository, UserRepository userRepository,
                             CategoryRepository categoryRepository, ZoneRepository zoneRepository,
                             VoteRepository voteRepository, CommentRepository commentRepository) {
        this.initiativeRepository = initiativeRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.zoneRepository = zoneRepository;
        this.voteRepository = voteRepository;
        this.commentRepository = commentRepository;
    }

    @Transactional
    public InitiativeResponse create(Long authorId, InitiativeRequest request) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Catégorie non trouvée"));

        Initiative initiative = new Initiative();
        initiative.setTitle(request.getTitle());
        initiative.setDescription(request.getDescription());
        initiative.setAuthor(author);
        initiative.setCategory(category);

        if (request.getZoneId() != null) {
            zoneRepository.findById(request.getZoneId())
                    .ifPresent(initiative::setZone);
        }
        if (request.getPriority() != null) {
            initiative.setPriority(Initiative.Priority.valueOf(request.getPriority()));
        }

        Initiative saved = initiativeRepository.save(initiative);
        return toResponse(saved, authorId);
    }

    @Transactional(readOnly = true)
    public List<InitiativeResponse> findAll(Long currentUserId) {
        return initiativeRepository.findAll().stream()
                .map(i -> toResponse(i, currentUserId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InitiativeResponse> findByStatus(String status, Long currentUserId) {
        return initiativeRepository.findByStatus(Initiative.Status.valueOf(status)).stream()
                .map(i -> toResponse(i, currentUserId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InitiativeResponse> findByCategory(Long categoryId, Long currentUserId) {
        return initiativeRepository.findByCategoryId(categoryId).stream()
                .map(i -> toResponse(i, currentUserId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InitiativeResponse> findByZone(Long zoneId, Long currentUserId) {
        return initiativeRepository.findByZoneId(zoneId).stream()
                .map(i -> toResponse(i, currentUserId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<InitiativeResponse> findPopular(Long currentUserId) {
        return initiativeRepository.findAllOrderByVoteCountDesc().stream()
                .limit(10)
                .map(i -> toResponse(i, currentUserId))
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public InitiativeResponse findById(Long id, Long currentUserId) {
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Initiative non trouvée"));
        return toResponse(initiative, currentUserId);
    }

    @Transactional
    public InitiativeResponse update(Long id, Long userId, InitiativeRequest request) {
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Initiative non trouvée"));

        if (!initiative.getAuthor().getId().equals(userId)) {
            throw new RuntimeException("Non autorisé");
        }

        initiative.setTitle(request.getTitle());
        initiative.setDescription(request.getDescription());

        if (request.getCategoryId() != null) {
            categoryRepository.findById(request.getCategoryId()).ifPresent(initiative::setCategory);
        }
        if (request.getZoneId() != null) {
            zoneRepository.findById(request.getZoneId()).ifPresent(initiative::setZone);
        }
        if (request.getPriority() != null) {
            initiative.setPriority(Initiative.Priority.valueOf(request.getPriority()));
        }

        return toResponse(initiativeRepository.save(initiative), userId);
    }

    @Transactional
    public void delete(Long id, Long userId) {
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Initiative non trouvée"));

        boolean isAuthor = initiative.getAuthor().getId().equals(userId);
        boolean isAdmin = userRepository.findById(userId)
                .map(u -> u.getRoles().stream().anyMatch(r -> r.getName().equals("ADMIN")))
                .orElse(false);

        if (!isAuthor && !isAdmin) {
            throw new RuntimeException("Non autorisé");
        }

        initiativeRepository.delete(initiative);
    }

    @Transactional
    public InitiativeResponse changeStatus(Long id, String status, Long adminId) {
        Initiative initiative = initiativeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Initiative non trouvée"));

        User admin = userRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin non trouvé"));

        initiative.setStatus(Initiative.Status.valueOf(status));
        initiative.setValidatedBy(admin);
        initiative.setValidatedAt(java.time.LocalDateTime.now());

        if (status.equals("COMPLETED")) {
            initiative.setCompletedAt(java.time.LocalDateTime.now());
        }

        return toResponse(initiativeRepository.save(initiative), adminId);
    }

    private InitiativeResponse toResponse(Initiative i, Long currentUserId) {
        long voteCount = voteRepository.countByInitiativeId(i.getId());
        long upCount = voteRepository.countByInitiativeIdAndType(i.getId(), Vote.VoteType.UP);
        long downCount = voteRepository.countByInitiativeIdAndType(i.getId(), Vote.VoteType.DOWN);
        long commentCount = commentRepository.countByInitiativeId(i.getId());

        boolean hasVoted = false;
        String userVoteType = null;
        if (currentUserId != null) {
            hasVoted = voteRepository.existsByUserIdAndInitiativeId(currentUserId, i.getId());
            if (hasVoted) {
                userVoteType = voteRepository.findByUserIdAndInitiativeId(currentUserId, i.getId())
                        .map(v -> v.getType().name())
                        .orElse(null);
            }
        }

        InitiativeResponse response = new InitiativeResponse();
        response.setId(i.getId());
        response.setTitle(i.getTitle());
        response.setDescription(i.getDescription());
        response.setStatus(i.getStatus().name());
        response.setPriority(i.getPriority().name());
        response.setImageUrl(null);
        response.setAuthorId(i.getAuthor().getId());
        response.setAuthorName(i.getAuthor().getFirstName() + " " + i.getAuthor().getLastName());
        response.setAuthorAvatar(null);
        response.setCategoryId(i.getCategory() != null ? i.getCategory().getId() : null);
        response.setCategoryName(i.getCategory() != null ? i.getCategory().getName() : null);
        response.setCategoryColor(null);
        response.setZoneId(i.getZone() != null ? i.getZone().getId() : null);
        response.setZoneName(i.getZone() != null ? i.getZone().getName() : null);
        response.setVoteCount(voteCount);
        response.setSoutienCount(upCount);
        response.setOppositionCount(downCount);
        response.setCommentCount(commentCount);
        response.setHasVoted(hasVoted);
        response.setUserVoteType(userVoteType);
        response.setCreatedAt(i.getCreatedAt());
        response.setUpdatedAt(i.getUpdatedAt());
        return response;
    }
}
