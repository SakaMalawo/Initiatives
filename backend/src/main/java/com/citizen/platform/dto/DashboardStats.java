package com.citizen.platform.dto;

import java.util.List;
import java.util.Map;

public class DashboardStats {
    private long totalInitiatives;
    private long totalUsers;
    private long totalVotes;
    private long totalComments;
    private Map<String, Long> initiativesByStatus;
    private Map<String, Long> initiativesByCategory;
    private List<InitiativeResponse> topInitiatives;

    public DashboardStats() {}

    public long getTotalInitiatives() { return totalInitiatives; }
    public void setTotalInitiatives(long totalInitiatives) { this.totalInitiatives = totalInitiatives; }
    public long getTotalUsers() { return totalUsers; }
    public void setTotalUsers(long totalUsers) { this.totalUsers = totalUsers; }
    public long getTotalVotes() { return totalVotes; }
    public void setTotalVotes(long totalVotes) { this.totalVotes = totalVotes; }
    public long getTotalComments() { return totalComments; }
    public void setTotalComments(long totalComments) { this.totalComments = totalComments; }
    public Map<String, Long> getInitiativesByStatus() { return initiativesByStatus; }
    public void setInitiativesByStatus(Map<String, Long> initiativesByStatus) { this.initiativesByStatus = initiativesByStatus; }
    public Map<String, Long> getInitiativesByCategory() { return initiativesByCategory; }
    public void setInitiativesByCategory(Map<String, Long> initiativesByCategory) { this.initiativesByCategory = initiativesByCategory; }
    public List<InitiativeResponse> getTopInitiatives() { return topInitiatives; }
    public void setTopInitiatives(List<InitiativeResponse> topInitiatives) { this.topInitiatives = topInitiatives; }
}
