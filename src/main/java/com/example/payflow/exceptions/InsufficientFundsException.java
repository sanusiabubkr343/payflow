package com.example.payflow.exceptions;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {

    public InsufficientFundsException(Long walletId, BigDecimal amount) {
        super("Insufficient funds in wallet " + walletId + " for amount " + amount);
    }

}