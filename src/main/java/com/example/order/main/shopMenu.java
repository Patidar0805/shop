package com.example.order.main;

import com.example.features.Domain.Product;
import com.example.features.Domain.Purchase;
import com.example.features.Service.ProductService;
import com.example.features.Service.PurchaseService;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class shopMenu {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        ProductService productService = new ProductService();
        PurchaseService purchaseService = new PurchaseService();

        while (true) {
            System.out.println("\n=== SHOP MENU ===");
            System.out.println("1. Create Product");
            System.out.println("2. Get Product by ID");
            System.out.println("3. Update Product");
            System.out.println("4. Delete Product");
            System.out.println("5. Purchase Product");
            System.out.println("6. Get All Products");
            System.out.println("7. Exit");
            System.out.print("Select option: ");

            int option = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (option) {
                case 1 -> {
                    System.out.print("Enter Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter Category: ");
                    String category = scanner.nextLine();

                    System.out.print("Enter Quantity: ");
                    int quantity = scanner.nextInt();

                    System.out.print("Enter Price: ");
                    int price = scanner.nextInt();
                    scanner.nextLine(); // Clear buffer

                    Product product = new Product(name, category, price, quantity);

                    try {
                        productService.createProduct(product);
                        System.out.println("Product created!!!");
                    } catch (SQLException e) {
                        System.out.println("Error creating product: " + e.getMessage());
                    }
                }


                case 2 -> {
                    System.out.print("Enter Product ID: ");
                    int id = scanner.nextInt();

                    try {
                        Product product = productService.getProductById(id);
                        if (product != null) {
                            System.out.println(" Product Found: " + product);
                        } else {
                            System.out.println(" Product not found.");
                        }
                    } catch (SQLException e) {
                        System.out.println(" Error fetching product: " + e.getMessage());
                    }
                }

                case 3 -> {
                    System.out.print("Enter Product ID to Update: ");
                    int id = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter New Name: ");
                    String name = scanner.nextLine();

                    System.out.print("Enter New Category: ");
                    String category = scanner.nextLine();

                    System.out.print("Enter New Quantity: ");
                    int quantity = scanner.nextInt();

                    System.out.print("Enter New Price: ");
                    int price = scanner.nextInt();

                    Product product = new Product(id, name, category, quantity, price);

                    try {
                        productService.updateProduct(product);
                        System.out.println(" Product updated.");
                    } catch (SQLException e) {
                        System.out.println(" Error updating product: " + e.getMessage());
                    }
                }

                case 4 -> {
                    System.out.print("Enter Product ID to Delete: ");
                    int id = scanner.nextInt();

                    try {
                        productService.deleteProduct(id);
                        System.out.println("️ Product deleted.");
                    } catch (SQLException e) {
                        System.out.println(" Error deleting product: " + e.getMessage());
                    }
                }

                case 5 -> {
                    System.out.print("Enter Product ID to Purchase: ");
                    int id = scanner.nextInt();

                    System.out.print("Enter Quantity to Purchase: ");
                    int qty = scanner.nextInt();
                    scanner.nextLine();

                    System.out.print("Enter Customer Name: ");
                    String customerName = scanner.nextLine();

                    Purchase purchase = new Purchase(id, qty, customerName);
                    try {
                        purchaseService.processPurchase(purchase);
                        System.out.println(" Purchase completed.");
                    } catch (Exception e) {
                        System.out.println(" Purchase error: " + e.getMessage());
                    }

                }
                case 6 -> {
                    try {
                        List<Product> products = productService.getAllProducts();
                        if (products.isEmpty()) {
                            System.out.println("❗ No products available.");
                        } else {
                            System.out.println("All Products:");
                            for (Product product : products) {
                                System.out.println("ID: " + product.getId() +
                                        ", Name: " + product.getName() +
                                        ", Category: " + product.getCategory() +
                                        ", Quantity: " + product.getQuantity() +
                                        ", Price: " + product.getPrice());
                            }
                        }
                    } catch (SQLException e) {
                        System.out.println("Error retrieving products: " + e.getMessage());
                    }
                }

                case 7 -> {
                    System.out.println("Exiting shop system. Bye!");
                    System.exit(0);
                }

                default -> System.out.println(" Invalid option. Try again.");
            }
        }
    }
}
