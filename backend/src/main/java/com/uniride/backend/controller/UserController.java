package com.uniride.backend.controller;

import com.uniride.backend.model.User;
import com.uniride.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", user.getId());
        response.put("fullName", user.getFullName());
        response.put("email", user.getEmail());
        response.put("phone", user.getPhone());
        response.put("rol", user.getRol().name());
        response.put("vehiclePlate", user.getVehiclePlate());
        response.put("vehicleColor", user.getVehicleColor());
        response.put("rating", user.getRating());
        response.put("totalRatings", user.getTotalRatings());
        
        return ResponseEntity.ok(response);
    }
}