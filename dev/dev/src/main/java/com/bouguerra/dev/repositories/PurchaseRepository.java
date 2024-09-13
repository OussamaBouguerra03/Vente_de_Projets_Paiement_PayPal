package com.bouguerra.dev.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bouguerra.dev.models.Purchase;


@Repository
public interface  PurchaseRepository extends JpaRepository<Purchase, Long> {
    List<Purchase> findByCustomerId(Long buyerId);
    boolean existsByPaymentId(String paymentId);
    List<Purchase> findByProjectUserId(Long userId);

}
