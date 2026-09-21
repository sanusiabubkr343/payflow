package com.example.payflow.dto;

import java.math.BigDecimal;

public record WalletDto(Long id,
                        String username,
                        BigDecimal balance) {
}
