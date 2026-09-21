package com.example.payflow.dto;

import com.example.payflow.entity.Wallet;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record TransferRequest(
       @NotBlank String reference,
       @NotNull Long toWalletId,
       @NotNull @DecimalMin("0.01") BigDecimal amount
) { }
