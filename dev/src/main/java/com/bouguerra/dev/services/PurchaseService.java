package com.bouguerra.dev.services;

import java.util.List;

import com.bouguerra.dev.models.PurchaseDTO;

public interface PurchaseService {
    void purchaseProject(Long userId, Long projectId, Double amount, String paymentId) ;
    boolean isPaymentAlreadyProcessed(String paymentId);
 List<PurchaseDTO> getUserPurchases(Long userId);
 List<PurchaseDTO> getRevenuesByDeveloper(Long userId);
}
