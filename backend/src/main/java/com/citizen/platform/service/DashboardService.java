package com.citizen.platform.service;

import com.citizen.platform.dto.DashboardStats;
import com.citizen.platform.dto.InitiativeResponse;
import com.citizen.platform.entity.Initiative;
import com.citizen.platform.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class DashboardService {

    private final InitiativeRepository initiativeRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final InitiativeService initiativeService;

    public DashboardService(InitiativeRepository initiativeRepository, UserRepository userRepository,
                            VoteRepository voteRepository, CommentRepository commentRepository,
                            CategoryRepository categoryRepository, InitiativeService initiativeService) {
        this.initiativeRepository = initiativeRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
        this.commentRepository = commentRepository;
        this.categoryRepository = categoryRepository;
        this.initiativeService = initiativeService;
    }

    @Transactional(readOnly = true)
    public DashboardStats getStats(Long currentUserId) {
        Map<String, Long> byStatus = new HashMap<>();
        for (Initiative.Status s : Initiative.Status.values()) {
            byStatus.put(s.name(), initiativeRepository.countByStatus(s));
        }

        Map<String, Long> byCategory = new HashMap<>();
        categoryRepository.findAll().forEach(cat -> {
            long count = initiativeRepository.findByCategoryId(cat.getId()).size();
            byCategory.put(cat.getName(), count);
        });

        List<InitiativeResponse> topInitiatives = initiativeService.findPopular(currentUserId);

        DashboardStats stats = new DashboardStats();
        stats.setTotalInitiatives(initiativeRepository.count());
        stats.setTotalUsers(userRepository.count());
        stats.setTotalVotes(voteRepository.count());
        stats.setTotalComments(commentRepository.count());
        stats.setInitiativesByStatus(byStatus);
        stats.setInitiativesByCategory(byCategory);
        stats.setTopInitiatives(topInitiatives);
        return stats;
    }
}
