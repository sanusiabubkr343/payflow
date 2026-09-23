package com.example.payflow.dto;

public record UserRegisteredEvent(
        Long userId,
        String email,
        String username
) {
}