package com.example.payflow.dto;

import com.example.payflow.entity.User;

public record RegisteredUser(User user, WalletDto wallet) {}