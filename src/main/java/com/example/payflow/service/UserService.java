package com.example.payflow.service;

import com.example.payflow.dto.RegisterRequest;
import com.example.payflow.dto.RegisteredUser;
import com.example.payflow.dto.WalletDto;
import com.example.payflow.entity.User;
import com.example.payflow.entity.Wallet;
import com.example.payflow.exceptions.EmailAlreadyExistsException;
import com.example.payflow.exceptions.UsernameAlreadyExistsException;
import com.example.payflow.mapper.WalletMapper;
import com.example.payflow.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletMapper walletMapper;

    @Transactional
    public RegisteredUser register(RegisterRequest request) {


        if (userRepository.existsByUsername(request.username())) {
            throw new UsernameAlreadyExistsException("Username already exists");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("Email already exists");
        }

        User user = new User();
        user.setUsername(request.username());
        user.setEmail(request.email());
        user.setPasswordHash(passwordEncoder.encode(request.password()));

        Wallet wallet = new Wallet();
        wallet.setUser(user);
        wallet.setBalance(BigDecimal.ZERO);
        user.setWallet(wallet);

        User newUser = userRepository.save(user);   // cascades wallet
        log.info("Registered user {} with wallet {}", newUser.getUsername(), newUser.getWallet().getId());

        return new RegisteredUser(newUser, walletMapper.toDto(newUser.getWallet()));
    }


}