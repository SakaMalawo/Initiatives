package com.citizen.platform.dto;

import jakarta.validation.constraints.NotNull;

public class VoteRequest {
    @NotNull
    private Long initiativeId;
    private String type;

    public VoteRequest() {}

    public Long getInitiativeId() { return initiativeId; }
    public void setInitiativeId(Long initiativeId) { this.initiativeId = initiativeId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
