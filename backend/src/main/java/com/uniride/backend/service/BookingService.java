package com.uniride.backend.service;

import com.uniride.backend.dto.BookingResponse;
import com.uniride.backend.model.*;
import com.uniride.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    public BookingResponse createBooking(Long tripId, String email) {

        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Trip not found"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (trip.getDriver() != null && trip.getDriver().getId().equals(user.getId())) {
            throw new RuntimeException("Driver cannot book their own trip");
        }

        if (trip.getSeats() <= 0) {
            throw new RuntimeException("No seats available");
        }

        Booking booking = Booking.builder()
                .trip(trip)
                .passenger(user)
                .status(BookingStatus.PENDING)
                .build();

        trip.setSeats(trip.getSeats() - 1);
        tripRepository.save(trip);

        Booking saved = bookingRepository.save(booking);

        // construir el DTO
        return BookingResponse.builder()
                .id(saved.getId())
                .tripId(trip.getId())
                .origin(trip.getOrigin())
                .destination(trip.getDestination())
                .passengerEmail(user.getEmail())
                .status(saved.getStatus().name())
                .createdAt(saved.getCreatedAt())
                .build();
    }

    public List<Booking> getMyBookings(String email) {
    User user = userRepository.findByEmail(email)
        .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    return bookingRepository.findByPassengerId(user.getId());
}
}