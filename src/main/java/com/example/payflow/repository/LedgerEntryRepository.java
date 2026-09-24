package com.example.payflow.repository;

import com.example.payflow.entity.LedgerEntry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;

public interface LedgerEntryRepository extends JpaRepository<LedgerEntry, Long> {
    List<LedgerEntry> findByWalletIdOrderByCreatedAtDesc(Long walletId);
    @Query("""
    SELECT COALESCE(SUM(
        CASE WHEN le.type = 'CREDIT' THEN le.amount
             ELSE -le.amount END
    ), 0)
    FROM LedgerEntry le
    WHERE le.wallet.id = :walletId
""")
    BigDecimal computeBalanceFromLedger(@Param("walletId") Long walletId);
}