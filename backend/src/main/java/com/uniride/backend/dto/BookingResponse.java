package com.uniride.backend.dto;

import lombok.Builder;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Builder
public class BookingResponse {
    private Long id;
    private Long tripId;
    private String origin;
    private String destination;
    private LocalDateTime departure;
    private Integer seats;
    private Double price;
    private Boolean onlyWomen;
    private String passengerEmail;
    private String passengerName;
    private String status;
    private LocalDateTime createdAt;
    private String driverName;
    private Long driverId;
    private Double driverRating;
    private Integer driverTotalRatings;
    private String vehiclePlate;
    private String vehicleColor;
}
