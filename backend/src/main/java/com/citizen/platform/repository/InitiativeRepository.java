package com.citizen.platform.repository;

import com.citizen.platform.entity.Initiative;
import com.citizen.platform.entity.Initiative.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface InitiativeRepository extends JpaRepository<Initiative, Long> {
    List<Initiative> findByAuthorId(Long authorId);
    List<Initiative> findByStatus(Status status);
    List<Initiative> findByCategoryId(Long categoryId);
    List<Initiative> findByZoneId(Long zoneId);
    List<Initiative> findByCategoryIdAndStatus(Long categoryId, Status status);
    List<Initiative> findByZoneIdAndStatus(Long zoneId, Status status);

    @Query("SELECT i FROM Initiative i LEFT JOIN i.votes v GROUP BY i.id ORDER BY COUNT(v) DESC")
    List<Initiative> findAllOrderByVoteCountDesc();

    @Query("SELECT i FROM Initiative i WHERE i.status = 'PROPOSE' ORDER BY i.createdAt DESC")
    List<Initiative> findRecentProposed();

    long countByStatus(Status status);
}
