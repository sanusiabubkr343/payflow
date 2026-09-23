package com.example.payflow.component;


import com.example.payflow.dto.TransferNotificationEvent;
import com.example.payflow.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
public class TransferNotificationEventListener {

    private final EmailService emailService;

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)

    public void handleTransferNotificationEvent(TransferNotificationEvent event) {
        emailService.sendTransferNotification(
                event.receiver(),
                event.username(),
                event.amount(),
                event.direction()
        );

    }
}
