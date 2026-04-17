package com.uniride.backend.repository;

import com.uniride.backend.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long> {

    // Buscar viajes activos con filtros opcionales
    @Query("""
        SELECT t FROM Trip t
        WHERE t.status = 'ACTIVE'
          AND t.seats > 0
          AND (:origin IS NULL OR LOWER(t.origin) LIKE LOWER(CONCAT('%', :origin, '%')))
          AND (:destination IS NULL OR LOWER(t.destination) LIKE LOWER(CONCAT('%', :destination, '%')))
          AND (:onlyWomen IS NULL OR t.onlyWomen = :onlyWomen)
          AND (:hasAC IS NULL OR t.hasAC = :hasAC)
          AND (:maxPrice IS NULL OR t.price <= :maxPrice)
        ORDER BY t.departure ASC
    """)
    List<Trip> searchTrips(
            @Param("origin") String origin,
            @Param("destination") String destination,
            @Param("onlyWomen") Boolean onlyWomen,
            @Param("hasAC") Boolean hasAC,
            @Param("maxPrice") Double maxPrice
    );

    // Próximos viajes disponibles (para el dashboard)
    List<Trip> findTop5ByStatusOrderByDepartureAsc(String status);
}