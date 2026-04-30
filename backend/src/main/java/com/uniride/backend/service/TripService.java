package com.uniride.backend.service;

import com.uniride.backend.dto.TripResponse;
import com.uniride.backend.dto.TripSearchRequest;
import com.uniride.backend.dto.UserStatsResponse;
import com.uniride.backend.model.Booking;  // ← IMPORTANTE: Agregar este import
import com.uniride.backend.model.Trip;
import com.uniride.backend.model.User;
import com.uniride.backend.model.UserRole;
import com.uniride.backend.repository.BookingRepository;
import com.uniride.backend.repository.TripRepository;
import com.uniride.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;
import org.springframework.transaction.annotation.Transactional;

import com.uniride.backend.dto.CreateTripRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;  // ← Necesitas este repositorio

    public List<TripResponse> getUpcomingTrips() {
    List<Trip> trips = tripRepository.findByEstadoAndSeatsGreaterThanAndDepartureAfterOrderByDepartureAsc(
        "ACTIVE", 0, LocalDateTime.now()  // ← seats > 0, NO seats >= 0
    );
    return trips.stream().map(this::convertToResponse).collect(Collectors.toList());
}

    public List<TripResponse> searchTrips(TripSearchRequest request) {
         Specification<Trip> spec = (root, query, cb) -> {
         List<Predicate> predicates = new ArrayList<>();
        
            predicates.add(cb.equal(root.get("estado"), "ACTIVE"));
            predicates.add(cb.greaterThan(root.get("seats"), 0));  // ← seats > 0
            predicates.add(cb.greaterThan(root.get("departure"), LocalDateTime.now()));
            
            if (request.getOrigin() != null && !request.getOrigin().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("origin")), 
                    "%" + request.getOrigin().toLowerCase() + "%"));
            }
            
            if (request.getDestination() != null && !request.getDestination().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("destination")), 
                    "%" + request.getDestination().toLowerCase() + "%"));
            }
            
            if (request.getOnlyWomen() != null && request.getOnlyWomen()) {
                predicates.add(cb.isTrue(root.get("onlyWomen")));
            }
            
            if (request.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), request.getMaxPrice()));
            }
            
            query.orderBy(cb.asc(root.get("departure")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        List<Trip> trips = tripRepository.findAll(spec);
        return trips.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public UserStatsResponse getUserStats(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    
    // ✅ Usar Specification para contar viajes disponibles
    Specification<Trip> specDisponibles = (root, query, cb) -> {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("estado"), "ACTIVE"));
        predicates.add(cb.greaterThan(root.get("seats"), 0));
        predicates.add(cb.greaterThan(root.get("departure"), LocalDateTime.now()));
        return cb.and(predicates.toArray(new Predicate[0]));
    };
    
    Long viajesDisponiblesLong = tripRepository.count(specDisponibles);
    Integer viajesDisponibles = viajesDisponiblesLong.intValue();
    
    // ✅ Para viajes completados, también usa Specification si es necesario
    Specification<Booking> specCompletados = (root, query, cb) -> {
        List<Predicate> predicates = new ArrayList<>();
        predicates.add(cb.equal(root.get("passenger").get("id"), user.getId()));
        predicates.add(root.get("status").in("CONFIRMED", "COMPLETED"));
        return cb.and(predicates.toArray(new Predicate[0]));
    };
    
    Long viajesCompletadosLong = bookingRepository.count(specCompletados);
    Integer viajesCompletados = viajesCompletadosLong.intValue();
    
    // ✅ Dinero ahorrado
    Double dineroAhorrado = bookingRepository.sumPriceByPassengerId(user.getId());
    if (dineroAhorrado == null) dineroAhorrado = 0.0;
    
    // ✅ Calificación
    Double calificacion = user.getRating() != null ? user.getRating() : 0.0;
    
    return UserStatsResponse.builder()
        .viajesDisponibles(viajesDisponibles)
        .viajesCompletados(viajesCompletados)
        .dineroAhorrado(dineroAhorrado)
        .calificacion(calificacion)
        .build();
}

    @Transactional
    public TripResponse createTrip(CreateTripRequest request, String email) {
        // Buscar al conductor por email
        User driver = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        // Validar que el usuario puede publicar viajes (CONDUCTOR o AMBOS)
        if (driver.getRol() != UserRole.CONDUCTOR && driver.getRol() != UserRole.AMBOS) {
            throw new RuntimeException("No tienes permisos para publicar viajes. Debes ser CONDUCTOR o AMBOS.");
        }
        
        // Validar que tenga datos del vehículo
        if (driver.getVehiclePlate() == null || driver.getVehiclePlate().isBlank()) {
            throw new RuntimeException("Debes registrar la placa de tu vehículo antes de publicar viajes");
        }
        
        // Crear el viaje
        Trip trip = Trip.builder()
                .driver(driver)
                .origin(request.getOrigin())
                .destination(request.getDestination())
                .departure(request.getDeparture())
                .seats(request.getSeats())
                .price(request.getPrice())
                .onlyWomen(request.getOnlyWomen() != null ? request.getOnlyWomen() : false)
                .estado("ACTIVE")
                .build();
        
        Trip saved = tripRepository.save(trip);
        return convertToResponse(saved);
    }

    private TripResponse convertToResponse(Trip trip) {
        User driver = trip.getDriver();
        return TripResponse.builder()
            .id(trip.getId())
            .driverName(driver != null ? driver.getFullName() : "Conductor")
            .driverRating(driver != null ? driver.getRating() : 5.0)
            .driverTotalRatings(driver != null ? driver.getTotalRatings() : 0)
            .origin(trip.getOrigin())
            .destination(trip.getDestination())
            .departure(trip.getDeparture())
            .seats(trip.getSeats())
            .price(trip.getPrice())
            .onlyWomen(trip.getOnlyWomen())
            .estado(trip.getEstado())
            .build();
    }
}