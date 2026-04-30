package com.uniride.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserStatsResponse {
    private Integer viajesDisponibles;      // Viajes activos en el sistema
    private Integer viajesCompletados;      // Viajes donde el usuario fue pasajero y se completaron
    private Integer viajesComoConductor;    // Viajes que el usuario publicó
    private Double dineroAhorrado;          // Suma de todos los precios de viajes donde fue pasajero
    private Double calificacion;             // Rating del usuario
}