package com.uniride.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TripResponse {
    private Long id;
    private String driverName;
    private Double driverRating;
    private Integer driverTotalRatings;
    private String origin;
    private String destination;
    private LocalDateTime departure;
    private Integer seats;
    private Double price;
    private Boolean onlyWomen;
    private String estado;
}