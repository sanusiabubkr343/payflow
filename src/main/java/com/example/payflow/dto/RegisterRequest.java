package com.example.payflow.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(
        @NotBlank @Size(min = 3,max = 50) String username,
        @NotBlank @Email String email,
        @NotBlank @Size(min = 8,message = "Password must be more than 8 characters") String password
) {
}
