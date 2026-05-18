package com.uniride.backend.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterResponse {
    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String role;

}