package com.citizen.platform.service;

import com.citizen.platform.dto.DashboardStats;
import com.citizen.platform.dto.InitiativeResponse;
import com.citizen.platform.entity.Initiative;
import com.citizen.platform.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardService {

    private final InitiativeRepository initiativeRepository;
    private final UserRepository userRepository;
    private final VoteRepository voteRepository;
    private final CommentRepository commentRepository;
    private final CategoryRepository categoryRepository;
    private final InitiativeService initiativeService;

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

        return DashboardStats.builder()
                .totalInitiatives(initiativeRepository.count())
                .totalUsers(userRepository.count())
                .totalVotes(voteRepository.count())
                .totalComments(commentRepository.count())
                .initiativesByStatus(byStatus)
                .initiativesByCategory(byCategory)
                .topInitiatives(topInitiatives)
                .build();
    }
}
