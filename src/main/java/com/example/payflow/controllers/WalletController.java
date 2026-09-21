package com.example.payflow.controllers;

import com.example.payflow.dto.LedgerEntryDto;

import com.example.payflow.dto.WalletDto;
import com.example.payflow.service.WalletService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/wallets")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class WalletController {

    private final WalletService walletService;

    @GetMapping("/{id}")
    public WalletDto get(@PathVariable Long id) {
        return walletService.getWallet(id);
    }

    @GetMapping("/{id}/ledger")
    public List<LedgerEntryDto> ledger(@PathVariable Long id) {
        return walletService.getLedger(id);
    }
}

