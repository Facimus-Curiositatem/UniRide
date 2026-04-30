package com.uniride.backend.repository;

import com.uniride.backend.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long>, JpaSpecificationExecutor<Booking> {
    
    // Contar por passengerId y status
    @Query("SELECT COUNT(b) FROM Booking b WHERE b.passenger.id = :passengerId AND b.status IN :statuses")
    Integer countByPassengerIdAndStatusIn(
        @Param("passengerId") Long passengerId, 
        @Param("statuses") List<String> statuses
    );
    
    // Sumar precios de viajes completados por un pasajero
    @Query("SELECT COALESCE(SUM(b.trip.price), 0) FROM Booking b WHERE b.passenger.id = :passengerId")
    Double sumPriceByPassengerId(@Param("passengerId") Long passengerId);
    
    // Buscar bookings por trip
    List<Booking> findByTripId(Long tripId);
}