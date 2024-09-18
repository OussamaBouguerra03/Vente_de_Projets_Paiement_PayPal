package com.bouguerra.dev.controllers;

import com.paypal.base.rest.PayPalRESTException;
import com.bouguerra.dev.models.User;
import com.bouguerra.dev.repositories.UserRepository;
import com.bouguerra.dev.services.PayPalService;
import com.bouguerra.dev.services.PurchaseService;
import com.paypal.api.payments.Payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.Optional;

@RestController
@RequestMapping("/api/paypal")
public class PayPalController {

    @Autowired
    private PayPalService payPalService;

    @Autowired
    private PurchaseService purchaseService;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/pay")
    public ResponseEntity<String> pay(@RequestParam("amount") Double amount, @RequestParam("projectId") Long projectId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            String username = authentication.getName();
            
            if ("anonymousUser".equals(username)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User is not authenticated");
            }

            Long userId = getUserIdByUsername(username);

            Payment payment = payPalService.createPayment(
                amount,
                "USD",
                "paypal",
                "sale",
                "Payment description",
                "http://localhost:8980/api/paypal/cancel",
                "http://localhost:8980/api/paypal/success?projectId=" + projectId + "&userId=" + userId
            );

            String approvalUrl = payment.getLinks().stream()
                .filter(link -> "approval_url".equals(link.getRel()))
                .map(link -> link.getHref())
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Approval URL not found"));

            return ResponseEntity.ok(approvalUrl);
        } catch (PayPalRESTException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error occurred: " + e.getMessage());
        }
    }
   @GetMapping("/success")
public ResponseEntity<Void> success(
    @RequestParam("paymentId") String paymentId, 
    @RequestParam("PayerID") String payerId, 
    @RequestParam("projectId") Long projectId, 
    @RequestParam("userId") Long userId) {
    
    try {
        // Vérifier si le paiement a déjà été traité
        if (purchaseService.isPaymentAlreadyProcessed(paymentId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .location(URI.create("http://localhost:4200/customer/home?status=conflict&message=PaymentAlreadyProcessed"))
                    .build();
        }

        // Exécuter le paiement
        Payment payment = payPalService.executePayment(paymentId, payerId);
        Double amount = Double.valueOf(payment.getTransactions().get(0).getAmount().getTotal());

        // Traiter l'achat après paiement réussi
        purchaseService.purchaseProject(userId, projectId, amount, paymentId);

        // Redirection vers la page frontend avec message de succès
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create("http://localhost:4200/customer/home?status=success&message=PaymentSuccessful"))
                .build();
        
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .location(URI.create("http://localhost:4200/customer/home?status=error&message=PaymentFailed"))
                .build();
    }
}

    @GetMapping("/cancel")
    public String cancel() {
        return "Payment cancelled";
    }

    public Long getUserIdByUsername(String username) {
        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get().getId();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }
}
