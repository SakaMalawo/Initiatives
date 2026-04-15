package com.citizen.platform.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;
    private String city;
    private String postalCode;
    private Set<String> roles;
    private Boolean active;
    private LocalDateTime createdAt;
    private long initiativeCount;
    private long voteCount;
    private long commentCount;
}
