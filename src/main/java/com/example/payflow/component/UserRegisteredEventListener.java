package com.example.payflow.component;

import com.example.payflow.dto.UserRegisteredEvent;
import com.example.payflow.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class UserRegisteredEventListener {

    private final EmailService emailService;

    @TransactionalEventListener(
            phase = TransactionPhase.AFTER_COMMIT
    )
    public void handleUserRegistered(UserRegisteredEvent event) {

        emailService.sendWelcomeEmail(
                event.email(),
                event.username()
        );
    }
}