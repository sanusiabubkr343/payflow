
package com.example.payflow.mapper;

import com.example.payflow.dto.TransferDto;
import com.example.payflow.entity.Transfer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface TransferMapper {

    @Mapping(source = "fromWallet.user.username", target = "fromUsername")
    @Mapping(source = "toWallet.user.username", target = "toUsername")
    TransferDto toDto(Transfer transfer);
}