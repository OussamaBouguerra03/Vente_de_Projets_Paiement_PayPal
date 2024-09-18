package com.bouguerra.dev.mappers;

import com.bouguerra.dev.models.Project;
import com.bouguerra.dev.models.Purchase;
import com.bouguerra.dev.models.PurchaseDTO;
import com.bouguerra.dev.models.User;

public class PurchaseMapper {
    public static PurchaseDTO toDTO(Purchase purchase) {
        return new PurchaseDTO(
            purchase.getId(),
            purchase.getCustomer().getId(),
            purchase.getProject().getId(),
            purchase.getPurchaseDate(),
            purchase.getAmount(),
            purchase.getPaymentId()
        );
    }

    public static Purchase toEntity(PurchaseDTO purchaseDTO, User customer, Project project) {
        return new Purchase(
            purchaseDTO.getId(),
            customer,
            project,
            purchaseDTO.getPurchaseDate(),
            purchaseDTO.getAmount(),
            purchaseDTO.getPaymentId()
        );
    }
}
