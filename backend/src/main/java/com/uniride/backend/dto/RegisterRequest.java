package com.uniride.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class RegisterRequest {

    @NotBlank
    private String fullName;

    @Email
    @NotBlank
    @Pattern(
            regexp = "^[a-zA-Z0-9._%+\\-]+@javeriana\\.edu\\.co$",
            message = "Debes usar tu correo institucional (@javeriana.edu.co)"
    )
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String phone;
}