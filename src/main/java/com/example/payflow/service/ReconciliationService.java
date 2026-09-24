package com.example.payflow.service;

import com.example.payflow.entity.Wallet;
import com.example.payflow.repository.LedgerEntryRepository;
import com.example.payflow.repository.WalletRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReconciliationService {

    private final WalletRepository walletRepository;
    private final LedgerEntryRepository ledgerRepository;

    @Scheduled(cron = "0 */45 * * * *")   // every 45 mins
    public void reconcileWallets() {
        log.info("🔍 Starting wallet reconciliation...");

        List<Wallet> wallets = walletRepository.findAllWallets();
        int mismatches = 0;

        for (Wallet wallet : wallets) {
            BigDecimal ledgerBalance = ledgerRepository.computeBalanceFromLedger(wallet.getId());

            if (wallet.getBalance().compareTo(ledgerBalance) != 0) {
                mismatches++;
                log.error("❌ MISMATCH wallet={} cached={} ledger={}",
                          wallet.getId(), wallet.getBalance(), ledgerBalance);
                // In production: alert PagerDuty / Slack / email
            }
        }

        log.info("✅ Reconciliation done. Checked {} wallets, {} mismatches",
                 wallets.size(), mismatches);
    }

    // Also useful: cleanup job for stale PENDING transfers
    @Scheduled(cron = "0 0 3 * * *")   // every day at 3 AM
    public void cleanupStaleTransfers() {
        log.info("🧹 Cleaning up stale transfers...");
        // find PENDING transfers older than 24h and mark FAILED
        // (left as exercise)
    }
}