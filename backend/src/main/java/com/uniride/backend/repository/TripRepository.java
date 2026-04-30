package com.uniride.backend.repository;

import com.uniride.backend.model.Trip;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TripRepository extends JpaRepository<Trip, Long>, JpaSpecificationExecutor<Trip> {
    List<Trip> findByEstadoAndSeatsGreaterThanAndDepartureAfterOrderByDepartureAsc(
        String estado, Integer seats, LocalDateTime departure
    );
}