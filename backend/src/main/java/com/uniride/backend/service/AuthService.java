package com.uniride.backend.service;

import com.uniride.backend.dto.*;
import com.uniride.backend.model.User;
import com.uniride.backend.model.UserRole;
import com.uniride.backend.repository.UserRepository;
import com.uniride.backend.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;  
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    public RegisterResponse register(RegisterRequest request) {

        // Validar email único
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("El correo institucional ya está registrado");
        }

        // Validar teléfono único
        if (userRepository.existsByPhone(request.getPhone())) {
            throw new RuntimeException("El número de teléfono ya está registrado");
        }

        // Validación condicional para vehículos (si es DRIVER o BOTH)
        if (request.getRol() == UserRole.CONDUCTOR || request.getRol() == UserRole.AMBOS) {
            if (request.getVehiclePlate() == null || request.getVehiclePlate().isBlank()) {
                throw new RuntimeException("La placa del vehículo es obligatoria para conductores");
            }
            if (request.getVehicleColor() == null || request.getVehicleColor().isBlank()) {
                throw new RuntimeException("El color del vehículo es obligatorio para conductores");
            }
        }

        // Crear usuario con todos los campos
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .rol(request.getRol())
                .vehiclePlate(request.getVehiclePlate())
                .vehicleColor(request.getVehicleColor())
                .rating(5.0)
                .totalRatings(0)
                .build();

        User saved = userRepository.save(user);

        return RegisterResponse.builder()
                .id(saved.getId())
                .fullName(saved.getFullName())
                .email(saved.getEmail())
                .phone(saved.getPhone())
                .rol(saved.getRol().name())
                .vehiclePlate(saved.getVehiclePlate())
                .vehicleColor(saved.getVehicleColor())
                .rating(saved.getRating())
                .totalRatings(saved.getTotalRatings())
                .build();
    }

    public AuthResponse login(LoginRequest request) {

        // Autenticar usando AuthenticationManager
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String token = jwtUtil.generateToken(user.getEmail());

        return AuthResponse.builder()
                .token(token)
                .email(user.getEmail())
                .fullName(user.getFullName())
                .rol(user.getRol().name())
                .phone(user.getPhone())
                .rating(user.getRating())
                .totalRatings(user.getTotalRatings())
                .build();
    }
}