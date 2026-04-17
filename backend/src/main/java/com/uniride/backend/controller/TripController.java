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
public class TripController {

    private final TripService tripService;

    // GET /api/trips/upcoming  → para el dashboard
    @GetMapping("/upcoming")
    public ResponseEntity<List<TripResponse>> getUpcoming() {
        return ResponseEntity.ok(tripService.getUpcomingTrips());
    }

    // POST /api/trips/search  → para buscar con filtros
    @PostMapping("/search")
    public ResponseEntity<List<TripResponse>> search(@RequestBody TripSearchRequest request) {
        return ResponseEntity.ok(tripService.searchTrips(request));
    }
}