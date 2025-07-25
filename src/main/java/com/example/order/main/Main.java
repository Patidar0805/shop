package com.example.order.main;

import com.example.features.Domain.Product;
import com.example.features.Response.orderResponse;
import com.example.features.Service.OrderService;
import com.example.features.Service.ProductService;
import com.example.features.request.OrderRequest;
import com.example.features.Domain.order;

import java.sql.SQLException;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        ProductService productService = new ProductService();
        OrderService orderService = new OrderService();
        Scanner scanner = new Scanner(System.in);

        try {
            // Step 1: Show all available products
            List<Product> allProducts = productService.getAllProducts();
            if (allProducts.isEmpty()) {
                System.out.println("No products available.");
                return;
            }

            System.out.println("Available Products:");
            for (Product p : allProducts) {
                System.out.println("ID: " + p.getId() + ", Name: " + p.getName() + ", Price: " + p.getPrice() + ", Qty: " + p.getQuantity());
            }

            // Step 2: Get nickname from user
            System.out.print("\nEnter your nickname to place an order: ");
            String nickname = scanner.nextLine();

            // Step 3: Place the order
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setNickname(nickname);
            int orderId = orderService.placeOrder(orderRequest);
            System.out.println("Order placed successfully. Order ID: " + orderId);

            // Step 4: Add products to the order
            System.out.print("How many products do you want to add? ");
            int count = scanner.nextInt();

            List<Integer> productIds = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                System.out.print("Enter Product ID to add: ");
                int productId = scanner.nextInt();
                productIds.add(productId);
            }

            Map<Integer, Integer> productQuantities = new HashMap<>();

            for (int productId : productIds) {
                System.out.print("Enter quantity for product ID " + productId + ": ");
                int quantity = scanner.nextInt();
                productQuantities.put(productId, quantity);
            }

            orderService.addMultipleProducts(orderId, productQuantities);

            System.out.println("Products added successfully.");


            // Step 6: Show order summary
            order orderResponse = OrderService.getOrder(orderId);
            System.out.println("\n=== Order Summary ===");
            System.out.println("Order ID: " + orderResponse.getOrderId());
            System.out.println("Nickname: " + orderResponse.getNickname());
            System.out.println("Total Cost: ₹" + orderResponse.getTotalSell());
            System.out.println("Products:");
            for (Product p : orderResponse.getProducts()) {
                System.out.println("- " + p.getName() + " (₹" + p.getPrice() + ")");
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
