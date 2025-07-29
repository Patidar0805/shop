package com.example.features.repository;

import com.example.features.Domain.OrderItem;
import com.example.features.Domain.Product;

import java.sql.*;
import java.util.*;
import java.util.stream.Collectors;

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

  /*  public void checkAndReduceProductStock(int productId, int quantity, Connection conn) throws SQLException {
        String checkSql = "SELECT quantity FROM products WHERE id = ?";
        try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
            checkStmt.setInt(1, productId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                int available = rs.getInt("quantity");
                if (available < quantity) {
                    throw new SQLException("Insufficient stock for product ID " + productId);
                }
            } else {
                throw new SQLException("Product ID " + productId + " not found.");
            }
        }

        String updateSql = "UPDATE products SET quantity = quantity - ? WHERE id = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            updateStmt.setInt(1, quantity);
            updateStmt.setInt(2, productId);
            updateStmt.executeUpdate();
        }
    }
*/
    public void checkAndReduceProductStock(Map<Integer, Integer> productQuantities, Connection conn) throws SQLException {

        Set<Integer> productIds = productQuantities.keySet();

        String placeholders = productIds.stream().map(id -> "?").collect(Collectors.joining(", "));
        String sql = "SELECT id, quantity FROM products WHERE id IN (" + placeholders + ")";

        Map<Integer, Integer> availableQuantities = new HashMap<>();

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            int index = 1;
            for (Integer id : productIds) {
                stmt.setInt(index++, id);
            }

            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                int qty = rs.getInt("quantity");
                availableQuantities.put(id, qty);
            }
        }

        for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
            int productId = entry.getKey();
            int requiredQty = entry.getValue();

            Integer availableQty = availableQuantities.get(productId);

            if (availableQty == null) {
                throw new SQLException("Product ID " + productId + " not found.");
            }

            if (availableQty < requiredQty) {
                throw new SQLException("Insufficient stock for product ID " + productId);
            }
        }

        String updateSql = "UPDATE products SET quantity = quantity - ? WHERE id = ?";
        try (PreparedStatement updateStmt = conn.prepareStatement(updateSql)) {
            for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
                int productId = entry.getKey();
                int qty = entry.getValue();
                updateStmt.setInt(1, qty);
                updateStmt.setInt(2, productId);
                updateStmt.addBatch();
            }
            updateStmt.executeBatch();
        }
    }



    public void addProductToOrder(int orderId, Map<Integer, Integer> productQuantities, Connection conn) throws SQLException {
        String insertSql = "INSERT INTO order_product_matrix (order_id, product_id, quantity) " +
                "VALUES (?, ?, ?) ON DUPLICATE KEY UPDATE quantity =  VALUES(quantity)";
        try (PreparedStatement stmt = conn.prepareStatement(insertSql)) {
            for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
                int productId = entry.getKey();
                int quantity = entry.getValue();

                stmt.setInt(1, orderId);
                stmt.setInt(2, productId);
                stmt.setInt(3, quantity);
                stmt.addBatch();
            }
            stmt.executeBatch();
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

    public int getOrderIdByNickname(String nickname, Connection conn) throws SQLException {
        String sql = "SELECT order_id FROM orders WHERE nickname = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nickname);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) return rs.getInt("order_id");
            }
        }
        throw new SQLException("Order not found for nickname: " + nickname);
    }

    public void deleteOrder(int orderId, Connection conn) throws SQLException {
        String deleteMatrix = "DELETE FROM order_product_matrix WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteMatrix)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }

        String deleteOrder = "DELETE FROM orders WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(deleteOrder)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }

    public void deleteOrderProducts(int orderId, Connection conn) throws SQLException {
        String sql = "DELETE FROM order_product_matrix WHERE order_id = ?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }
}
