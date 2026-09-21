package com.example.payflow.controllers;


import com.example.payflow.repository.UserRepository;
import com.example.payflow.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserRepository userRepository;
    private final WalletRepository walletRepository;



}
