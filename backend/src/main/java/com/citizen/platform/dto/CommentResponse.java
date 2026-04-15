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
public class CommentResponse {
    private Long id;
    private String content;
    private Long authorId;
    private String authorName;
    private String authorAvatar;
    private Long initiativeId;
    private Boolean moderated;
    private String moderationReason;
    private Long moderatedBy;
    private LocalDateTime moderatedAt;
    private LocalDateTime createdAt;
}
