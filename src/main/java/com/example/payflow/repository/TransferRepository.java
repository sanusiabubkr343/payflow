package com.example.payflow.repository;

import com.example.payflow.entity.Transfer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @EntityGraph(attributePaths = {"fromWallet","toWallet"})
    Optional<Transfer> findByReference(String reference);

    boolean existsByReference(String reference);

}