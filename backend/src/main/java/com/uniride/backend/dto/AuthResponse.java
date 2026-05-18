package com.uniride.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthResponse {
    private String token;
    private String email;
    private String fullName;
    private String rol;
    private String phone;
    private Double rating;
    private Integer totalRatings;
    private String vehiclePlate;   // ← AGREGAR
    private String vehicleColor;   // ← AGREGAR
}