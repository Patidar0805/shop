package com.example.features.response;

import com.example.features.Domain.Product;

import java.util.List;

public class OrderResponse {
    private int id;
    private String nickname;
    private List<Product> productList;
    private Double totalCost;
}
