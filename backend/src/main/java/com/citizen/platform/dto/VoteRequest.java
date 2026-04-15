package com.citizen.platform.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequest {
    @NotNull
    private Long initiativeId;
    private String type;
}
