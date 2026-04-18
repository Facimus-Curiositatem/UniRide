package com.uniride.backend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "trips")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Trip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "driver_id")
    private User driver;

    @Column(nullable = false)
    private String origin;

    @Column(nullable = false)
    private String destination;

    @Column(nullable = false)
    private LocalDateTime departure;

    @Column(nullable = false)
    private Integer seats;

    @Column(nullable = false)
    private Double price;

    @Column(name = "only_women", nullable = false)
    private Boolean onlyWomen = false;

    @Column(name = "has_ac", nullable = false)
    private Boolean hasAC = false;

    @Column(nullable = false)
    private String status = "ACTIVE";

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();

        if (onlyWomen == null) onlyWomen = false;
        if (hasAC == null) hasAC = false;
        if (status == null) status = "ACTIVE";
    }
}