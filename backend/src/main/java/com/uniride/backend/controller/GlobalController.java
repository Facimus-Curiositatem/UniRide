package com.uniride.backend.controller;

import com.uniride.backend.repository.TripRepository;
import com.uniride.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/global")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class GlobalController {

    private final UserRepository userRepository;
    private final TripRepository tripRepository;

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getGlobalStats() {
        Map<String, Object> stats = new HashMap<>();
        
        long usuariosActivos = userRepository.count();
        long viajesRealizados = tripRepository.count();
        Double calificacionPromedio = userRepository.getAverageRating();
        
        stats.put("usuariosActivos", usuariosActivos);
        stats.put("viajesRealizados", viajesRealizados);
        stats.put("calificacionPromedio", calificacionPromedio != null ? 
                   String.format("%.1f", calificacionPromedio) : "5.0");
        
        return ResponseEntity.ok(stats);
    }
}