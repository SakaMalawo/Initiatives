package com.citizen.platform.repository;

import com.citizen.platform.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByInitiativeIdOrderByCreatedAtDesc(Long initiativeId);
    List<Comment> findByAuthorId(Long authorId);
    List<Comment> findByIsModeratedFalse();
    long countByInitiativeId(Long initiativeId);
}
