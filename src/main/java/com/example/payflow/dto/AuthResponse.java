package com.example.payflow.dto;

public record AuthResponse(
    String token,
    String tokenType,
    WalletDto wallet
) {
    public static AuthResponse of(String token, WalletDto wallet) {
        return new AuthResponse(token, "Bearer", wallet);
    }
}