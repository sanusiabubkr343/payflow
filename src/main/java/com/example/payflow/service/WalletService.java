package com.example.payflow.service;


import com.example.payflow.dto.LedgerEntryDto;
import com.example.payflow.dto.WalletDto;
import com.example.payflow.entity.LedgerEntry;
import com.example.payflow.entity.Wallet;
import com.example.payflow.mapper.LedgerEntryMapper;
import com.example.payflow.mapper.WalletMapper;
import com.example.payflow.repository.LedgerEntryRepository;
import com.example.payflow.repository.WalletRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WalletService {
    private final WalletRepository walletRepository;
    private final WalletMapper walletMapper;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final LedgerEntryMapper ledgerEntryMapper;

    public WalletDto getWallet (Long id){
        Wallet wallet = walletRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Wallet not found"));
        return   walletMapper.toDto(wallet);
    }

    public List<LedgerEntryDto> getLedger(Long walletId) {

        walletRepository.findById(walletId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Wallet not found"));

        List<LedgerEntry> entries =
                ledgerEntryRepository
                        .findByWalletIdOrderByCreatedAtDesc(walletId);

        return ledgerEntryMapper.toDto(entries);
    }
}
