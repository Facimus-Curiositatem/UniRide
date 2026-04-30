package com.uniride.backend.repository;  // ← Esto debe ser repository, NO service

import com.uniride.backend.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {
    
    // Obtener viajes próximos
    List<Trip> findByEstadoAndSeatsGreaterThanAndDepartureAfterOrderByDepartureAsc(
        String estado, Integer seats, LocalDateTime departure
    );
    
    // ✅ MÉTODO PARA CONTAR VIAJES DISPONIBLES (CORREGIDO)
    @Query("SELECT COUNT(t) FROM Trip t WHERE t.estado = :estado AND t.seats > :seats AND t.departure > :departure")
    Integer countByEstadoAndSeatsGreaterThanAndDepartureAfter(
        @Param("estado") String estado, 
        @Param("seats") Integer seats, 
        @Param("departure") LocalDateTime departure
    );
    
    // Contar viajes por conductor
    Integer countByDriverId(Long driverId);
    
    // Método adicional para encontrar viajes por estado y fecha
    List<Trip> findByEstadoAndDepartureBefore(String estado, LocalDateTime departure);
}