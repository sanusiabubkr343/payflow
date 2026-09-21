package com.example.payflow.dto;

public record LoginResponse(  String tokenType,String token) {
    public static  LoginResponse of( String token) {
        return new LoginResponse("Bearer",token);
    }
}
