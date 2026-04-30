package com.uniride.backend.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CreateTripRequest {
    private String origin;
    private String destination;
    private LocalDateTime departure;
    private Integer seats;
    private Double price;
    private Boolean onlyWomen;
}