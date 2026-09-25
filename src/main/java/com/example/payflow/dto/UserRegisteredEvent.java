package com.example.payflow.dto;

import java.util.Optional;

public record UserRegisteredEvent(
        Long userId,
        String email,
        String username

) {
}