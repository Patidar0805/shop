package com.example.order.main;

import com.example.features.Domain.Product;
import com.example.features.Service.OrderService;
import com.example.features.Service.ProductService;
import com.example.features.Domain.order;

import java.util.*;
public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductService productService = new ProductService();
        OrderService orderService = new OrderService();
        while (true) {
            System.out.println("\n SHOP MENU ");
            System.out.println("1. Create Order");
            System.out.println("2. View Order");
            System.out.println("3. Delete Order");
            System.out.println("4. Update Order");
            System.out.println("5. Exit");
            System.out.print("Choose an option: ");
            int choice = scanner.nextInt();
            scanner.nextLine();

            try {
                switch (choice) {
                    case 1 -> {
                        List<Product> products = productService.getAllProducts();
                        if (products.isEmpty()) {
                            System.out.println("No products available.");
                            break;
                        }
                        System.out.println("Available Products:");
                        for (Product p : products) {
                            System.out.println("ID: " + p.getId() + ", " + p.getName() + " - " + p.getPrice() + " (Qty: " + p.getQuantity() + ")");
                        }

                        System.out.print("Enter your nickname: ");
                        String nickname = scanner.nextLine();

                        System.out.print("How many products to add? ");
                        int count = scanner.nextInt();

                        Map<Integer, Integer> productQuantities = new HashMap<>();
                        for (int i = 0; i < count; i++) {
                            System.out.print("Enter Product ID: ");
                            int productId = scanner.nextInt();
                            System.out.print("Enter Quantity: ");
                            int qty = scanner.nextInt();
                            productQuantities.put(productId, qty);
                        }

                        orderService.placeFullOrder(nickname, productQuantities);
                        System.out.println("Order placed");
                        order ord = orderService.getOrderByNickname(nickname);

                            System.out.println("Order ID: " + ord.getOrderId());
                            System.out.println("Nickname: " + ord.getNickname());

                            System.out.println("Products:");
                            for (Product p : ord.getProducts()) {
                                System.out.println("- " + p.getName() + " (" + p.getPrice() + ", Qty: " + p.getQuantity() + ")"+" = "+(p.getPrice()*p.getQuantity()));
                            }
                        System.out.println("Total Cost: " + ord.getTotalSell());
                    }

                    case 2 -> {
                        System.out.print("Enter nickname to view order: ");
                        String nickname = scanner.nextLine();
                        order ord = orderService.getOrderByNickname(nickname);
                        if (ord != null) {
                            System.out.println("Order ID: " + ord.getOrderId());
                            System.out.println("Nickname: " + ord.getNickname());

                            System.out.println("Products:");
                            for (Product p : ord.getProducts()) {
                                System.out.println("- " + p.getName() + " (" + p.getPrice() + ", Qty: " + p.getQuantity() + ")"+" = "+(p.getPrice()*p.getQuantity()));
                            }
                            System.out.println("Total Cost: " + ord.getTotalSell());
                        } else {
                            System.out.println("No order found.");
                        }
                    }

                    case 3 -> {
                        System.out.print("Enter nickname to delete order: ");
                        String nickname = scanner.nextLine();
                        boolean deleted = orderService.deleteOrderByNickname(nickname);
                        System.out.println(deleted ? "Order deleted." : "Order not found.");
                    }

                    case 4 -> {
                        System.out.print("Enter nickname to update order: ");
                        String nickname = scanner.nextLine();
                        order existingOrder = orderService.getOrderByNickname(nickname);
                        if (existingOrder == null) {
                            System.out.println("Order not found.");
                            break;
                        }

                        System.out.print("How many new products to update in order? ");
                        int count = scanner.nextInt();

                        Map<Integer, Integer> newProducts = new HashMap<>();
                        for (int i = 0; i < count; i++) {
                            System.out.print("Enter Product ID: ");
                            int productId = scanner.nextInt();
                            System.out.print("Enter Quantity: ");
                            int qty = scanner.nextInt();
                            newProducts.put(productId, qty);
                        }

                        boolean updated = orderService.updateOrder(nickname, newProducts);
                        System.out.println(updated ? "Order updated." : "Update failed.");
                        order ord = orderService.getOrderByNickname(nickname);
                        if (ord != null && updated) {
                            System.out.println("Order ID: " + ord.getOrderId());
                            System.out.println("Nickname: " + ord.getNickname());

                            System.out.println("Products:");
                            for (Product p : ord.getProducts()) {
                                System.out.println("- " + p.getName() + " (" + p.getPrice() + ", Qty: " + p.getQuantity() + ")"+" = "+(p.getPrice()*p.getQuantity()));
                            }
                            System.out.println("Total Cost: " + ord.getTotalSell());
                        }
                    }

                    case 5 -> {
                        System.out.println("Exiting.");
                        return;
                    }

                    default -> System.out.println("Invalid option.");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }
}
