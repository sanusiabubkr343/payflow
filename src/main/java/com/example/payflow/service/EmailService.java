package com.example.payflow.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;

    @Async("taskExecutor")   // 👈 runs on a background thread
    public void sendWelcomeEmail(String to, String username) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@payflow.com");
            message.setTo(to);
            message.setSubject("Welcome to PayFlow!");
            message.setText("Hi " + username + ",\n\nYour wallet is ready. Start sending money!");

            mailSender.send(message);
            log.info("Welcome email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send welcome email to {}", to, e);
            // swallow — email failure should not fail registration
        }
    }

    @Async("taskExecutor")
    public void sendTransferNotification(String to, String username, BigDecimal amount, String direction) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("noreply@payflow.com");
            message.setTo(to);
            message.setSubject("Transfer " + direction);
            message.setText("Hi " + username + ",\n\nYour transfer of " + amount + " was " + direction.toLowerCase() + ".");
            mailSender.send(message);
            log.info("Transfer email sent to {}", to);
        } catch (Exception e) {
            log.error("Failed to send transfer email to {}", to, e);
        }
    }
}