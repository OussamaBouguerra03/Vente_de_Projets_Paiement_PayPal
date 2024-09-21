package com.bouguerra.dev.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.bouguerra.dev.models.PurchaseDTO;
import com.bouguerra.dev.models.User;
import com.bouguerra.dev.services.PurchaseService;
import com.bouguerra.dev.services.UserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
@RestController
@RequestMapping("/api/purchases")
public class PurchaseController {

    @Autowired
    private PurchaseService purchaseService;
    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(PurchaseController.class);

  
    @PostMapping("/purchase")
    public ResponseEntity<String> purchaseProject(
            @RequestParam Long userId,
            @RequestParam Long projectId,
            @RequestParam String paymentId,
            @RequestParam Double amount) {

        try {
            purchaseService.purchaseProject(userId, projectId,amount, paymentId);
            return ResponseEntity.ok("Purchase successful");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Purchase failed: " + e.getMessage());
        }
    }
    @GetMapping("/user")
    public ResponseEntity<List<PurchaseDTO>> getUserPurchases(Authentication authentication) {
        logger.info("Request to get purchases for user started.");
    
        try {
            String username = authentication.getName();  
            logger.info("Authenticated user: {}", username);
    
            User user = userService.findByUsername(username); 
    
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
            }
    
            List<PurchaseDTO> purchases = purchaseService.getUserPurchases(user.getId());
            logger.info("Purchases retrieved: {}", purchases);  
    
            return ResponseEntity.ok(purchases);
        } catch (Exception e) {
            logger.error("Error retrieving purchases: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Collections.emptyList()); 
        }
    }
    
    
    @GetMapping("/developer/{userId}")
    public List<PurchaseDTO> getRevenuesByDeveloper(@PathVariable Long userId) {
        return purchaseService.getRevenuesByDeveloper(userId);
    }
   
}
