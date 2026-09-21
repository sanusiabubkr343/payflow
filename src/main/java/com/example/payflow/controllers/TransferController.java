package com.example.payflow.controllers;

import com.example.payflow.dto.TransferDto;
import com.example.payflow.dto.TransferRequest;
import com.example.payflow.entity.User;
import com.example.payflow.service.TransferService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final TransferService transferService;

    @PostMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<TransferDto> transfer(
            @AuthenticationPrincipal User user,
            @RequestHeader("X-Wallet-Id") Long fromWalletId,
            @Valid @RequestBody TransferRequest request) {
        return ResponseEntity.ok(transferService.transfer(user,fromWalletId, request));
    }
}