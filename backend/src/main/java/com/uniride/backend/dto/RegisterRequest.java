package com.uniride.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import com.uniride.backend.model.UserRole;
import jakarta.validation.constraints.*;


@Data
public class RegisterRequest {

    @NotBlank(message = "El nombre completo es obligatorio")
    @Size(min = 3, max = 255, message = "El nombre debe tener entre 3 y 255 caracteres")
    private String fullName;

    @NotBlank(message = "El email es obligatorio")
    @Email(message = "Formato de email inválido")
    @Pattern(regexp = "^[a-zA-Z0-9._%+\\-]+@javeriana\\.edu\\.co$", 
             message = "Debes usar tu correo institucional (@javeriana.edu.co)")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, max = 100, message = "La contraseña debe tener entre 6 y 100 caracteres")
    private String password;

    @NotBlank(message = "El teléfono es obligatorio")
    @Pattern(regexp = "^3\\d{9}$", message = "El teléfono debe ser colombiano: 3001234567")
    private String phone;

    @NotNull(message = "El rol es obligatorio")
    private UserRole rol;

    @Pattern(regexp = "^[A-Z]{3}\\d{3}$", message = "La placa debe tener formato ABC123")
    private String vehiclePlate;

    @Size(max = 30, message = "El color no puede exceder 30 caracteres")
    private String vehicleColor;




}