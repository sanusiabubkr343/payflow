package com.example.payflow.controllers;


import com.example.payflow.dto.UserDto;
import com.example.payflow.entity.User;
import com.example.payflow.repository.UserRepository;
import com.example.payflow.repository.WalletRepository;
import com.example.payflow.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;


    @PostMapping(value = "/me/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UserDto> uploadAvatar(
                @AuthenticationPrincipal User currentUser,
                @RequestParam("file") MultipartFile file) {

            UserDto updated = userService.updateAvatar(currentUser.getId(), file);
            return ResponseEntity.ok(updated);

    }



}
