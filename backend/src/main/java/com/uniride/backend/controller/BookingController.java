package com.uniride.backend.controller;

import com.uniride.backend.dto.BookingRequest;
import com.uniride.backend.dto.BookingResponse;
import com.uniride.backend.model.Booking;
import com.uniride.backend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    public ResponseEntity<BookingResponse> createBooking(
            @RequestBody BookingRequest request,
            Authentication authentication
    ) {
        String email = authentication.getName();
        BookingResponse response = bookingService.createBooking(request.getTripId(), email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-trips")
    public ResponseEntity<?> getMyTrips(Authentication authentication) {
        String email = authentication.getName();
        List<Booking> bookings = bookingService.getMyBookings(email);
        
        List<BookingResponse> responses = bookings.stream().map(booking -> {
            var trip = booking.getTrip();
            var driver = trip.getDriver();
            return BookingResponse.builder()
                .id(booking.getId())
                .tripId(trip.getId())
                .origin(trip.getOrigin())
                .destination(trip.getDestination())
                .departure(trip.getDeparture())
                .seats(trip.getSeats())
                .price(trip.getPrice())
                .onlyWomen(trip.getOnlyWomen())
                .passengerEmail(booking.getPassenger().getEmail())
                .passengerName(booking.getPassenger().getFullName())
                .status(booking.getStatus().name())
                .createdAt(booking.getCreatedAt())
                .driverName(driver != null ? driver.getFullName() : "Conductor")
                .driverId(driver != null ? driver.getId() : null)
                .driverRating(driver != null ? driver.getRating() : 5.0)
                .driverTotalRatings(driver != null ? driver.getTotalRatings() : 0)
                .vehiclePlate(driver != null ? driver.getVehiclePlate() : null)
                .vehicleColor(driver != null ? driver.getVehicleColor() : null)
                .build();
        }).collect(Collectors.toList());
        
        return ResponseEntity.ok(responses);
    }

    @PutMapping("/{bookingId}/confirm")
    public ResponseEntity<?> confirmBooking(
            @PathVariable Long bookingId,
            Authentication authentication
    ) {
        String email = authentication.getName();
        BookingResponse response = bookingService.confirmBooking(bookingId, email);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-driver-bookings")
    public ResponseEntity<?> getMyDriverBookings(Authentication authentication) {
        String email = authentication.getName();
        List<BookingResponse> bookings = bookingService.getBookingsForMyTrips(email);
        return ResponseEntity.ok(bookings);
    }

    @PutMapping("/{bookingId}/complete")
public ResponseEntity<?> completeBooking(@PathVariable Long bookingId, Authentication authentication) {
    bookingService.completeBooking(bookingId, authentication.getName());
    return ResponseEntity.ok().build();
}



}
