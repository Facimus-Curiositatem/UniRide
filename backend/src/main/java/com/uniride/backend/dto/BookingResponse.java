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
    private String passengerEmail;
    private String status;
    private LocalDateTime createdAt;
}