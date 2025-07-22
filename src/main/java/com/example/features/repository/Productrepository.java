package com.example.features.repository;


import com.example.features.Domain.Product;
import com.example.features.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class Productrepository {


        public void createProduct(Product product) throws SQLException {
            String sql = "INSERT INTO products (id, name, category, quantity, price) VALUES (?, ?, ?, ?, ?)";


            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {


                stmt.setInt(1, product.getId());
                stmt.setString(2, product.getName());
                stmt.setString(3, product.getCategory());
                stmt.setInt(4, product.getQuantity());
                stmt.setDouble(5, product.getPrice());

                stmt.executeUpdate();
            }
        }

        public Product getProductById(int id) throws SQLException {
            String sql = "SELECT * FROM products WHERE id = ?";
            Product product = null;

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id);

                try (ResultSet rs = stmt.executeQuery()) {
                    if (rs.next()) {
                        product = new Product(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getString("category"),
                                rs.getDouble("price"),
                                rs.getInt("quantity")
                        );
                    }
                }
            }
            return product;
        }

        public List<Product> getAllProducts() throws SQLException {
            String sql = "SELECT * FROM products";
            List<Product> products = new ArrayList<>();

            try (Connection conn = DBConnection.getConnection();
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    products.add(new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getInt("quantity")
                    ));
                }
            }
            return products;
        }

        public void updateProduct(Product product) throws SQLException {
            String sql = "UPDATE products SET name = ?, category = ?, price = ?, quantity = ? WHERE id = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setString(1, product.getName());
                stmt.setString(2, product.getCategory());
                stmt.setDouble(3, product.getPrice());
                stmt.setInt(4, product.getQuantity());
                stmt.setInt(5, product.getId());

                stmt.executeUpdate();
            }
        }

        public void deleteProduct(int id) throws SQLException {
            String sql = "DELETE FROM products WHERE id = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, id);
                stmt.executeUpdate();
            }
        }

        public void decreaseQuantity(int productId, int quantity) throws SQLException {
            String sql = "UPDATE products SET quantity = quantity - ? WHERE id = ? AND quantity >= ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, quantity);
                stmt.setInt(2, productId);
                stmt.setInt(3, quantity);

                int updated = stmt.executeUpdate();
                if (updated == 0) {
                    throw new SQLException("Insufficient stock or product not found.");
                }
            }
        }

        public void increaseQuantity(int productId, int quantity) throws SQLException {
            String sql = "UPDATE products SET quantity = quantity + ? WHERE id = ?";

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {

                stmt.setInt(1, quantity);
                stmt.setInt(2, productId);

                stmt.executeUpdate();
            }
        }
    }

