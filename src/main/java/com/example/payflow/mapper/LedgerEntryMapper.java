package com.example.payflow.mapper;

import com.example.payflow.dto.LedgerEntryDto;
import com.example.payflow.entity.LedgerEntry;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;


@Mapper(componentModel = "spring")
public interface LedgerEntryMapper {

    @Mapping(source = "wallet.id", target = "walletId")
    @Mapping(source = "transfer.id", target = "transferId")
    LedgerEntryDto toDto(LedgerEntry ledgerEntry);

    List<LedgerEntryDto> toDto(List<LedgerEntry> ledgerEntries);
}


