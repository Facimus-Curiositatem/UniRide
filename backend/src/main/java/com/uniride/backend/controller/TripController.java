package com.uniride.backend.controller;

import com.uniride.backend.dto.TripResponse;
import com.uniride.backend.dto.TripSearchRequest;
import com.uniride.backend.service.TripService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TripController {

    private final TripService tripService;

    @GetMapping("/upcoming")
    public ResponseEntity<List<TripResponse>> getUpcomingTrips() {
        return ResponseEntity.ok(tripService.getUpcomingTrips());
    }

    @PostMapping("/search")
    public ResponseEntity<List<TripResponse>> searchTrips(@RequestBody TripSearchRequest request) {
        return ResponseEntity.ok(tripService.searchTrips(request));
    }
}