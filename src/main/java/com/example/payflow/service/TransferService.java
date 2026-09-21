package com.example.payflow.service;


import com.example.payflow.dto.TransferDto;
import com.example.payflow.dto.TransferRequest;
import com.example.payflow.entity.LedgerEntry;
import com.example.payflow.entity.Transfer;
import com.example.payflow.entity.User;
import com.example.payflow.entity.Wallet;
import com.example.payflow.enums.EntryType;
import com.example.payflow.enums.TransferStatus;
import com.example.payflow.exceptions.ForbiddenAction;
import com.example.payflow.exceptions.InsufficientFundsException;
import com.example.payflow.exceptions.WalletNotFoundException;
import com.example.payflow.mapper.TransferMapper;
import com.example.payflow.repository.LedgerEntryRepository;
import com.example.payflow.repository.TransferRepository;
import com.example.payflow.repository.WalletRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
@Slf4j
public class TransferService {

    private final TransferRepository transferRepository;
    private final WalletRepository walletRepository;
    private final LedgerEntryRepository ledgerRepository;
    private final TransferMapper transferMapper;

    @Transactional
    public TransferDto transfer(
            User user,
            Long fromWalletId,
            TransferRequest request
    ) {

        // 1. Idempotency check
        if (transferRepository.existsByReference(request.reference())) {

            Transfer existing = transferRepository
                    .findByReference(request.reference())
                    .orElseThrow();

            log.info("Idempotent hit for reference {}", request.reference());

            return transferMapper.toDto(existing);
        }

        // 2. Lock BOTH wallets in deterministic order
        Long firstId = Math.min(fromWalletId, request.toWalletId());
        Long secondId = Math.max(fromWalletId, request.toWalletId());

        Wallet first = walletRepository
                .findByIdWithLock(firstId)
                .orElseThrow(()-> new WalletNotFoundException("Wallet not found"));

        Wallet second = walletRepository
                .findByIdWithLock(secondId)
                .orElseThrow(()-> new WalletNotFoundException("Wallet not found"));

        Wallet fromWallet =
                fromWalletId.equals(firstId) ? first : second;

        Wallet toWallet =
                fromWalletId.equals(firstId) ? second : first;

        // 3. Check wallet ownership
        if (!fromWallet.getUser().getId().equals(user.getId())) {
            throw new ForbiddenAction(
                    "You do not own this wallet"
            );
        }

        // 4. Validate balance
        if (fromWallet.getBalance().compareTo(request.amount()) < 0) {
            throw new InsufficientFundsException(
                    fromWallet.getId(),
                    fromWallet.getBalance()
            );
        }

        // 5. Update balances
        fromWallet.setBalance(
                fromWallet.getBalance().subtract(request.amount())
        );

        toWallet.setBalance(
                toWallet.getBalance().add(request.amount())
        );

        // 6. Create transfer
        Transfer transfer = new Transfer();

        transfer.setReference(request.reference());
        transfer.setFromWallet(fromWallet);
        transfer.setToWallet(toWallet);
        transfer.setAmount(request.amount());
        transfer.setStatus(TransferStatus.COMPLETED);

        Transfer saved = transferRepository.save(transfer);

        // 7. Debit ledger entry
        LedgerEntry debit = new LedgerEntry();

        debit.setWallet(fromWallet);
        debit.setTransfer(saved);
        debit.setType(EntryType.DEBIT);
        debit.setAmount(request.amount());

        // 8. Credit ledger entry
        LedgerEntry credit = new LedgerEntry();

        credit.setWallet(toWallet);
        credit.setTransfer(saved);
        credit.setType(EntryType.CREDIT);
        credit.setAmount(request.amount());

        ledgerRepository.save(debit);
        ledgerRepository.save(credit);

        log.info(
                "Transfer {} completed: {} -> {} amount {}",
                request.reference(),
                fromWallet.getId(),
                toWallet.getId(),
                request.amount()
        );

        return transferMapper.toDto(saved);
    }
}