package com.example.features.repository;

import com.example.features.Domain.Product;
import com.example.features.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductReadRepository {

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
}
