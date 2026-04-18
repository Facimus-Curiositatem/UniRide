package com.uniride.backend.controller;

import com.uniride.backend.dto.BookingRequest;
import com.uniride.backend.dto.BookingResponse;
import com.uniride.backend.service.BookingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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
}