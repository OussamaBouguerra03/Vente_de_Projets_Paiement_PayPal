package com.bouguerra.dev.services;


import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.bouguerra.dev.mappers.PurchaseMapper;
import com.bouguerra.dev.models.Project;
import com.bouguerra.dev.models.Purchase;
import com.bouguerra.dev.models.PurchaseDTO;
import com.bouguerra.dev.models.User;
import com.bouguerra.dev.repositories.ProjectRepository;
import com.bouguerra.dev.repositories.PurchaseRepository;
import com.bouguerra.dev.repositories.UserRepository;

import jakarta.transaction.Transactional;
@Service
public class PurchaseServiceImpl implements PurchaseService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;  


    @Transactional
    @Override
    public void purchaseProject(Long userId, Long projectId, Double amount, String paymentId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));

        if (amount <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid amount");
        }

        Purchase purchase = new Purchase();
        purchase.setCustomer(user);
        purchase.setProject(project);
        purchase.setAmount(amount);
        purchase.setPaymentId(paymentId);
        purchase.setPurchaseDate(LocalDateTime.now());

        purchaseRepository.save(purchase);
    }
    @Override
    public boolean isPaymentAlreadyProcessed(String paymentId) {
        return purchaseRepository.existsByPaymentId(paymentId);
    }
    @Override
     public List<PurchaseDTO> getUserPurchases(Long userId) {
        List<Purchase> purchases = purchaseRepository.findByCustomerId(userId);
        return purchases.stream().map(PurchaseMapper::toDTO).collect(Collectors.toList());
    }
@Override
    public List<PurchaseDTO> getRevenuesByDeveloper(Long userId) {
        List<Purchase> purchases = purchaseRepository.findByProjectUserId(userId);
        return purchases.stream().map(purchase -> new PurchaseDTO(
                purchase.getId(),
                purchase.getCustomer().getId(),
                purchase.getProject().getId(),
                purchase.getPurchaseDate(),
                purchase.getAmount(),
                purchase.getPaymentId()
        )).collect(Collectors.toList());
    }

   
}
