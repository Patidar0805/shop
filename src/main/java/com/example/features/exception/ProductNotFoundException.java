package com.example.features.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(String message) {

        System.out.println("ProductNotFoundException: " + message);
    }
}
