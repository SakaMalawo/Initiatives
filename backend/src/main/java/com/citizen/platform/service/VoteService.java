package com.citizen.platform.service;

import com.citizen.platform.dto.VoteRequest;
import com.citizen.platform.entity.*;
import com.citizen.platform.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class VoteService {

    private final VoteRepository voteRepository;
    private final UserRepository userRepository;
    private final InitiativeRepository initiativeRepository;

    public VoteService(VoteRepository voteRepository, UserRepository userRepository, InitiativeRepository initiativeRepository) {
        this.voteRepository = voteRepository;
        this.userRepository = userRepository;
        this.initiativeRepository = initiativeRepository;
    }

    @Transactional
    public void vote(Long userId, VoteRequest request) {
        if (voteRepository.existsByUserIdAndInitiativeId(userId, request.getInitiativeId())) {
            throw new RuntimeException("Vous avez déjà voté pour cette initiative");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé"));
        Initiative initiative = initiativeRepository.findById(request.getInitiativeId())
                .orElseThrow(() -> new RuntimeException("Initiative non trouvée"));

        Vote.VoteType type = request.getType() != null
                ? Vote.VoteType.valueOf(request.getType())
                : Vote.VoteType.UP;

        Vote vote = new Vote();
        vote.setUser(user);
        vote.setInitiative(initiative);
        vote.setType(type);

        voteRepository.save(vote);

        initiative.setVoteCount((int) voteRepository.countByInitiativeId(initiative.getId()));
        initiativeRepository.save(initiative);
    }

    @Transactional
    public void removeVote(Long userId, Long initiativeId) {
        Vote vote = voteRepository.findByUserIdAndInitiativeId(userId, initiativeId)
                .orElseThrow(() -> new RuntimeException("Vote non trouvé"));
        voteRepository.delete(vote);

        Initiative initiative = initiativeRepository.findById(initiativeId)
                .orElseThrow(() -> new RuntimeException("Initiative non trouvée"));
        initiative.setVoteCount((int) voteRepository.countByInitiativeId(initiativeId));
        initiativeRepository.save(initiative);
    }
}
