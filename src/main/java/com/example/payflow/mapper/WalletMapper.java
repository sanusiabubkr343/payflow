package com.example.payflow.mapper;


import com.example.payflow.dto.WalletDto;
import com.example.payflow.entity.Wallet;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public  interface WalletMapper {
    @Mapping(source = "user.username", target = "username")
    WalletDto toDto(Wallet wallet);
}