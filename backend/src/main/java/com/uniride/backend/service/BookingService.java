package com.uniride.backend.service;

import com.uniride.backend.dto.BookingResponse;
import com.uniride.backend.model.Booking;
import com.uniride.backend.model.BookingStatus;
import com.uniride.backend.model.Trip;
import com.uniride.backend.model.User;
import com.uniride.backend.repository.BookingRepository;
import com.uniride.backend.repository.TripRepository;
import com.uniride.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookingService {

    private final BookingRepository bookingRepository;
    private final TripRepository tripRepository;
    private final UserRepository userRepository;

    @Transactional
    public BookingResponse createBooking(Long tripId, String email) {
        Trip trip = tripRepository.findById(tripId)
                .orElseThrow(() -> new RuntimeException("Viaje no encontrado"));

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (trip.getDriver() != null && trip.getDriver().getId().equals(user.getId())) {
            throw new RuntimeException("No puedes reservar tu propio viaje");
        }

        if (trip.getSeats() <= 0) {
            throw new RuntimeException("No hay asientos disponibles");
        }

        Booking booking = Booking.builder()
                .trip(trip)
                .passenger(user)
                .status(BookingStatus.PENDING)
                .build();

        trip.setSeats(trip.getSeats() - 1);
        tripRepository.save(trip);

        Booking saved = bookingRepository.save(booking);

        return BookingResponse.builder()
                .id(saved.getId())
                .tripId(trip.getId())
                .origin(trip.getOrigin())
                .destination(trip.getDestination())
                .passengerEmail(user.getEmail())
                .passengerName(user.getFullName())
                .status(saved.getStatus().name())
                .createdAt(saved.getCreatedAt())
                .price(trip.getPrice())
                .build();
    }

    public List<Booking> getMyBookings(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return bookingRepository.findByPassengerId(user.getId());
    }

    @Transactional
    public BookingResponse confirmBooking(Long bookingId, String email) {
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        Trip trip = booking.getTrip();
        User currentUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (!trip.getDriver().getId().equals(currentUser.getId())) {
            throw new RuntimeException("No tienes permiso para confirmar esta reserva");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            throw new RuntimeException("La reserva ya fue procesada");
        }

        booking.setStatus(BookingStatus.CONFIRMED);
        bookingRepository.save(booking);

        return BookingResponse.builder()
                .id(booking.getId())
                .tripId(trip.getId())
                .origin(trip.getOrigin())
                .destination(trip.getDestination())
                .passengerEmail(booking.getPassenger().getEmail())
                .passengerName(booking.getPassenger().getFullName())
                .status(booking.getStatus().name())
                .createdAt(booking.getCreatedAt())
                .price(trip.getPrice())
                .build();
    }

    public List<BookingResponse> getBookingsForMyTrips(String email) {
        User driver = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<Trip> myTrips = tripRepository.findByDriverId(driver.getId());
        List<BookingResponse> responses = new ArrayList<>();

        for (Trip trip : myTrips) {
            List<Booking> bookings = bookingRepository.findByTripId(trip.getId());
            for (Booking booking : bookings) {
                if (booking.getStatus() == BookingStatus.PENDING) {
                    responses.add(BookingResponse.builder()
                            .id(booking.getId())
                            .tripId(trip.getId())
                            .origin(trip.getOrigin())
                            .destination(trip.getDestination())
                            .passengerEmail(booking.getPassenger().getEmail())
                            .passengerName(booking.getPassenger().getFullName())
                            .status(booking.getStatus().name())
                            .createdAt(booking.getCreatedAt())
                            .price(trip.getPrice())
                            .build());
                }
            }
        }
        return responses;
    }
}