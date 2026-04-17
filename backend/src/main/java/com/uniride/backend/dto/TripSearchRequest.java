package com.uniride.backend.dto;

import lombok.Data;

@Data
public class TripSearchRequest {
    private String origin;
    private String destination;
    private Boolean onlyWomen;
    private Boolean hasAC;
    private Double maxPrice;
}