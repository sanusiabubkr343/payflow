package com.example.payflow.service;

import com.example.payflow.dto.*;
import com.example.payflow.entity.User;
import com.example.payflow.entity.Wallet;
import com.example.payflow.exceptions.EmailAlreadyExistsException;
import com.example.payflow.exceptions.UserNotFoundException;
import com.example.payflow.exceptions.UsernameAlreadyExistsException;
import com.example.payflow.mapper.UserMapper;
import com.example.payflow.mapper.WalletMapper;
import com.example.payflow.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final WalletMapper walletMapper;
    private final EmailService emailService;
    private final ApplicationEventPublisher eventPublisher;
    private  final MediaService mediaService;
    private final UserMapper userMapper ;


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

        // Publish event
        eventPublisher.publishEvent(
                new UserRegisteredEvent(
                        newUser.getId(),
                        newUser.getEmail(),
                        newUser.getUsername()
                )
        );


        log.info("Registered user {} with wallet {}", newUser.getUsername(), newUser.getWallet().getId());

        return new RegisteredUser(newUser, walletMapper.toDto(newUser.getWallet()));
    }


    @Transactional
    public UserDto updateAvatar(Long userId, MultipartFile file) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(String.valueOf(userId)));

        // Upload the new one first, then delete the old (safest order)
        MediaService.UploadResult result = mediaService.uploadImage(file, "avatars");

        String oldPublicId = user.getAvatarPublicId();
        user.setAvatarUrl(result.url());
        user.setAvatarPublicId(result.publicId());
        userRepository.save(user);

        if (oldPublicId != null) {
            mediaService.deleteImage(oldPublicId);   // async or fire-and-forget
        }

        return userMapper.toDto(user);
    }


}