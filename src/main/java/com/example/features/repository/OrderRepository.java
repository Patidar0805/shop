package com.example.features.repository;

import com.example.features.Domain.OrderItem;
import com.example.features.Domain.Product;
import com.example.features.Util.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {

    public int createOrder(String nickname, Connection conn) throws SQLException {
        String sql = "INSERT INTO orders (nickname, total_sell) VALUES (?, 0.0)";
        try (PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, nickname);
            stmt.executeUpdate();
            try (ResultSet rs = stmt.getGeneratedKeys()) {
                if (rs.next()) return rs.getInt(1);
            }
            throw new SQLException("Failed to generate order_id");
        }
    }

    public void addProductToOrder(int orderId, int productId, int quantity, Connection conn) throws SQLException {
        String sql = "INSERT INTO order_product_matrix (order_id, product_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.executeUpdate();
        }
    }


    public void updateOrderTotal(int orderId, double total, Connection conn) throws SQLException {
        String sql = "UPDATE orders SET total_sell = ? WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, total);
            stmt.setInt(2, orderId);
            stmt.executeUpdate();
        }
    }

    public List<OrderItem> getOrderItems(int orderId, Connection conn) throws SQLException {
        List<OrderItem> items = new ArrayList<>();
        String sql = "SELECT p.id, p.name, p.category, p.price, p.quantity as stock_quantity, opm.quantity as order_quantity " +
                "FROM products p " +
                "JOIN order_product_matrix opm ON p.id = opm.product_id " +
                "WHERE opm.order_id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Product product = new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getDouble("price"),
                            rs.getInt("stock_quantity")

                    );

                    int quantity = rs.getInt("order_quantity");
                    OrderItem item = new OrderItem(product, quantity);
                    items.add(item);
                }
            }
        }
        return items;
    }
    public List<Product> getProductsForOrder(int orderId, Connection conn) throws SQLException {
        String sql = "SELECT p.id, p.name, p.price, opm.quantity " +
                "FROM products p " +
                "JOIN order_product_matrix opm ON p.id = opm.product_id " +
                "WHERE opm.order_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, orderId);
        ResultSet rs = stmt.executeQuery();

        List<Product> products = new ArrayList<>();
        while (rs.next()) {
            Product p = new Product();
            p.setId(rs.getInt("id"));
            p.setName(rs.getString("name"));
            p.setPrice(rs.getDouble("price"));
            p.setQuantity(rs.getInt("quantity"));
            products.add(p);
        }
        return products;
    }

    public String getOrderNickname(int orderId, Connection conn) throws SQLException {
        String sql = "SELECT nickname FROM orders WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getString("nickname");
            }
        }
        throw new SQLException("Order not found for ID " + orderId);
    }


}
