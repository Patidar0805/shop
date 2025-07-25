package com.example.features.Service;

import com.example.features.Domain.Purchase;
import com.example.features.repository.OrderRepository;

public class PurchaseService {
    private final OrderRepository orderRepository = new OrderRepository();

    public void processPurchase(Purchase purchase) {
        try {
            orderRepository.placeOrderWithTransaction(purchase);
            System.out.println("Purchase successful for " + purchase.getCustomerName());
        } catch (Exception e) {
            throw new RuntimeException("Purchase fail: " + e.getMessage());
        }
    }
}
