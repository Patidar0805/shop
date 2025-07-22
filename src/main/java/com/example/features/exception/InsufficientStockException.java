package com.example.features.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String message) {

        System.out.println("InsufficientStockException: " + message);
    }
}

