package com.uniride.backend.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class TripResponse {
    private Long id;
    private String driverName;
    private String driverFaculty;
    private Double driverRating;
    private Integer driverTotalRatings;
    private String origin;
    private String destination;
    private LocalDateTime departure;
    private Integer seats;
    private Double price;
    private Boolean onlyWomen;
    private Boolean hasAC;
    private String status;
}