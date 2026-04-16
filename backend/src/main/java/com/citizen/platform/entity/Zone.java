package com.citizen.platform.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "geographic_zones")
public class Zone {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(length = 255)
    private String city;

    @Column(length = 255)
    private String postalCode;

    @Column(length = 255)
    private String region;

    @Column(length = 255)
    private String description;

    public Zone() {}

    public Zone(Long id, String name, String city, String postalCode, String region, String description) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.postalCode = postalCode;
        this.region = region;
        this.description = description;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    public String getPostalCode() { return postalCode; }
    public void setPostalCode(String postalCode) { this.postalCode = postalCode; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
