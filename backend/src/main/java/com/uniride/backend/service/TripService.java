package com.uniride.backend.service;

import com.uniride.backend.dto.TripResponse;
import com.uniride.backend.dto.TripSearchRequest;
import com.uniride.backend.model.Trip;
import com.uniride.backend.model.User;
import com.uniride.backend.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import jakarta.persistence.criteria.Predicate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    public List<TripResponse> getUpcomingTrips() {
        List<Trip> trips = tripRepository.findByEstadoAndSeatsGreaterThanAndDepartureAfterOrderByDepartureAsc(
            "ACTIVE", 0, LocalDateTime.now()
        );
        return trips.stream().map(this::convertToResponse).collect(Collectors.toList());
    }

    public List<TripResponse> searchTrips(TripSearchRequest request) {
        Specification<Trip> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            
            // Solo viajes activos con asientos disponibles y fecha futura
            predicates.add(cb.equal(root.get("estado"), "ACTIVE"));
            predicates.add(cb.greaterThan(root.get("seats"), 0));
            predicates.add(cb.greaterThan(root.get("departure"), LocalDateTime.now()));
            
            // Filtro por origen
            if (request.getOrigin() != null && !request.getOrigin().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("origin")), 
                    "%" + request.getOrigin().toLowerCase() + "%"));
            }
            
            // Filtro por destino
            if (request.getDestination() != null && !request.getDestination().isEmpty()) {
                predicates.add(cb.like(cb.lower(root.get("destination")), 
                    "%" + request.getDestination().toLowerCase() + "%"));
            }
            
            // Filtro por solo mujeres
            if (request.getOnlyWomen() != null && request.getOnlyWomen()) {
                predicates.add(cb.isTrue(root.get("onlyWomen")));
            }
            
            // Filtro por precio máximo
            if (request.getMaxPrice() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("price"), request.getMaxPrice()));
            }
            
            query.orderBy(cb.asc(root.get("departure")));
            return cb.and(predicates.toArray(new Predicate[0]));
        };
        
        List<Trip> trips = tripRepository.findAll(spec);
        return trips.stream().map(this::convertToResponse).collect(Collectors.toList());
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