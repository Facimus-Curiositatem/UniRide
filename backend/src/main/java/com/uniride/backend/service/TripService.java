package com.uniride.backend.service;

import com.uniride.backend.dto.TripResponse;
import com.uniride.backend.dto.TripSearchRequest;
import com.uniride.backend.model.Trip;
import com.uniride.backend.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    public List<TripResponse> searchTrips(TripSearchRequest request) {
        List<Trip> trips = tripRepository.searchTrips(
                request.getOrigin(),
                request.getDestination(),
                request.getOnlyWomen(),
                request.getHasAC(),
                request.getMaxPrice()
        );
        return trips.stream().map(this::toResponse).toList();
    }

    public List<TripResponse> getUpcomingTrips() {
        return tripRepository
                .findTop5ByStatusOrderByDepartureAsc("ACTIVE")
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private TripResponse toResponse(Trip trip) {
        return TripResponse.builder()
                .id(trip.getId())
                .driverName(trip.getDriver().getFullName())
                .driverFaculty(trip.getDriver().getFaculty())
                .driverRating(trip.getDriver().getRating())
                .driverTotalRatings(trip.getDriver().getTotalRatings())
                .origin(trip.getOrigin())
                .destination(trip.getDestination())
                .departure(trip.getDeparture())
                .seats(trip.getSeats())
                .price(trip.getPrice())
                .onlyWomen(trip.getOnlyWomen())
                .hasAC(trip.getHasAC())
                .status(trip.getStatus())
                .build();
    }
}