package com.example.payflow.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferDto(
        Long id,
        String reference,
        BigDecimal amount,
        String status,
        String fromUsername,
        String toUsername,
        LocalDateTime createdAt
) {}