package com.uniride.backend.service;

import java.time.ZoneId;
import com.uniride.backend.dto.TripResponse;
import com.uniride.backend.dto.TripSearchRequest;
import com.uniride.backend.dto.UserStatsResponse;
import com.uniride.backend.dto.CreateTripRequest;
import com.uniride.backend.model.Booking;
import com.uniride.backend.model.BookingStatus;
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
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final UserRepository userRepository;
    private final BookingRepository bookingRepository;

    public List<TripResponse> getUpcomingTrips() {
        List<Trip> trips = tripRepository.findByEstadoAndSeatsGreaterThanAndDepartureAfterOrderByDepartureAsc(
            "ACTIVE", 0, LocalDateTime.now(ZoneId.of("America/Bogota"))
        );
        return trips.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public List<TripResponse> searchTrips(TripSearchRequest request) {
        Specification<Trip> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            predicates.add(cb.equal(root.get("estado"), "ACTIVE"));
            predicates.add(cb.greaterThan(root.get("seats"), 0));
            predicates.add(cb.greaterThan(root.get("departure"), LocalDateTime.now(ZoneId.of("America/Bogota"))));
            
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
        
        Specification<Trip> specDisponibles = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("estado"), "ACTIVE"));
            predicates.add(cb.greaterThan(root.get("seats"), 0));
            predicates.add(cb.greaterThan(root.get("departure"), LocalDateTime.now(ZoneId.of("America/Bogota"))));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        Long viajesDisponiblesLong = tripRepository.count(specDisponibles);
        Integer viajesDisponibles = viajesDisponiblesLong.intValue();
        
        Specification<Booking> specCompletados = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            predicates.add(cb.equal(root.get("passenger").get("id"), user.getId()));
            predicates.add(root.get("status").in("CONFIRMED", "COMPLETED"));
            predicates.add(cb.lessThan(root.get("trip").get("departure"), LocalDateTime.now(ZoneId.of("America/Bogota"))));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        Long viajesCompletadosLong = bookingRepository.count(specCompletados);
        Integer viajesCompletados = viajesCompletadosLong.intValue();
        
        Double dineroAhorrado = 0.0;
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
        User driver = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (driver.getRol() != UserRole.CONDUCTOR && driver.getRol() != UserRole.AMBOS) {
            throw new RuntimeException("No tienes permisos para publicar viajes. Debes ser CONDUCTOR o AMBOS.");
        }
        
        if (driver.getVehiclePlate() == null || driver.getVehiclePlate().isBlank()) {
            throw new RuntimeException("Debes registrar la placa de tu vehículo antes de publicar viajes");
        }
        
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

    // ============ ESTE ES EL MÉTODO QUE DEBES TENER ============
    @Scheduled(fixedDelay = 60000) // Cada 1 minuto
    @Transactional
    public void actualizarViajesExpirados() {
        LocalDateTime ahora = LocalDateTime.now(ZoneId.of("America/Bogota"));
        List<Trip> viajesPasados = tripRepository.findByEstadoAndDepartureBefore("ACTIVE", ahora);
        
        for (Trip trip : viajesPasados) {
            // 1. Marcar viaje como COMPLETED
            trip.setEstado("COMPLETED");
            tripRepository.save(trip);
            
            // 2. Marcar TODAS las reservas como COMPLETED
            List<Booking> reservas = bookingRepository.findByTripId(trip.getId());
            for (Booking booking : reservas) {
                if (booking.getStatus() != BookingStatus.COMPLETED && 
                    booking.getStatus() != BookingStatus.CANCELLED) {
                    booking.setStatus(BookingStatus.COMPLETED);
                    bookingRepository.save(booking);
                }
            }
        }
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