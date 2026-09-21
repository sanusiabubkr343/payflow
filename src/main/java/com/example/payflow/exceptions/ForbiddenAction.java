package com.example.payflow.exceptions;

public class ForbiddenAction extends RuntimeException {
    public ForbiddenAction(String message) {
        super(message);
    }
}
