package com.example.payflow.dto;

import com.example.payflow.entity.Transfer;
import com.example.payflow.entity.Wallet;
import com.example.payflow.enums.EntryType;

import java.math.BigDecimal;
import java.time.LocalDateTime;



public record LedgerEntryDto(
        Long id,
        Long walletId,
        Long transferId,
        EntryType type,
        BigDecimal amount,
        LocalDateTime createdAt
) {}