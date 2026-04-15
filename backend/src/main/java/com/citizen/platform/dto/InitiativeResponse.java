package com.citizen.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InitiativeResponse {
    private Long id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private String imageUrl;
    private Long authorId;
    private String authorName;
    private String authorAvatar;
    private Long categoryId;
    private String categoryName;
    private String categoryColor;
    private Long zoneId;
    private String zoneName;
    private long voteCount;
    private long soutienCount;
    private long oppositionCount;
    private long commentCount;
    private boolean hasVoted;
    private String userVoteType;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
