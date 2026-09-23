package com.example.payflow.dto;

import java.math.BigDecimal;

public record TransferNotificationEvent(
        String receiver,
        String username,
        BigDecimal amount,
        String  direction

) {
}
