package com.example.features.Service;


import com.example.features.Domain.Product;
import com.example.features.Domain.Purchase;
import com.example.features.repository.Productrepository;

import com.example.features.exception.InsufficientStockException;
import com.example.features.exception.ProductNotFoundException;

import java.sql.SQLException;

public class PurchaseService {
    private final Productrepository repository;

    public PurchaseService() {
        this.repository = new Productrepository();
    }

    public void processPurchase(Purchase purchase) {
        try {
            Product product = repository.getProductById(purchase.getProductId());

            if (product == null) {
                throw new ProductNotFoundException("Product not found with ID: " + purchase.getProductId());
            }

            if (product.getQuantity() < purchase.getQuantity()) {
                throw new InsufficientStockException("Insufficient stock. Available: " + product.getQuantity());
            }

            repository.decreaseQuantity(purchase.getProductId(), purchase.getQuantity());

            System.out.println("Purchase successful for " + purchase.getCustomerName());

        } catch (SQLException e) {
            throw new RuntimeException("Database error during purchase: " + e.getMessage());
        }
    }


}

