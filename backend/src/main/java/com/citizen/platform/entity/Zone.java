package com.citizen.platform.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "geographic_zones")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
