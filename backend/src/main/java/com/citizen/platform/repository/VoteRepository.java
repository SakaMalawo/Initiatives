package com.citizen.platform.repository;

import com.citizen.platform.entity.Vote;
import com.citizen.platform.entity.Vote.VoteType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    Optional<Vote> findByUserIdAndInitiativeId(Long userId, Long initiativeId);
    long countByInitiativeId(Long initiativeId);
    long countByInitiativeIdAndType(Long initiativeId, VoteType type);
    boolean existsByUserIdAndInitiativeId(Long userId, Long initiativeId);
}
