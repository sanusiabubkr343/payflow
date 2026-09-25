package com.example.payflow.mapper;

import com.example.payflow.dto.UserDto;
import com.example.payflow.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserDto toDto(User user);
}
