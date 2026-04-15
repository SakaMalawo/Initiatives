package com.citizen.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStats {
    private long totalInitiatives;
    private long totalUsers;
    private long totalVotes;
    private long totalComments;
    private Map<String, Long> initiativesByStatus;
    private Map<String, Long> initiativesByCategory;
    private List<InitiativeResponse> topInitiatives;
}
