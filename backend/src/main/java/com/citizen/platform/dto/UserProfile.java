package com.citizen.platform.dto;

import java.time.LocalDateTime;
import java.util.Set;

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

    public UserProfile() {}

    public UserProfile(Long id, String firstName, String lastName, String email, String phoneNumber,
                       String city, String postalCode, Set<String> roles, Boolean active,
                       LocalDateTime createdAt, long initiativeCount, long voteCount, long commentCount) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.city = city;
        this.postalCode = postalCode;
        this.roles = roles;
        this.active = active;
        this.createdAt = createdAt;
        this.initiativeCount = initiativeCount;
        this.voteCount = voteCount;
        this.commentCount = commentCount;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public Set<String> getRoles() { return roles; }
    public void setRoles(Set<String> roles) { this.roles = roles; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public long getInitiativeCount() { return initiativeCount; }
    public void setInitiativeCount(long initiativeCount) { this.initiativeCount = initiativeCount; }
    public long getVoteCount() { return voteCount; }
    public void setVoteCount(long voteCount) { this.voteCount = voteCount; }
    public long getCommentCount() { return commentCount; }
    public void setCommentCount(long commentCount) { this.commentCount = commentCount; }
}
