package com.example.features.repository;

import com.example.features.Domain.Product;
import com.example.features.Domain.Purchase;
import com.example.features.Util.DBConnection;

import java.sql.*;

public class OrderRepository {

    private final ProductWriteRepository productWriteRepository = new ProductWriteRepository();
    private final ProductReadRepository productReadRepository = new ProductReadRepository();

    public void placeOrderWithTransaction(Purchase purchase) throws SQLException {
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false); // transaction start


            Product product = productReadRepository.getProductById(purchase.getProductId(), conn);
            if (product == null) {
                throw new SQLException("Product not found.");
            }

            if (product.getQuantity() < purchase.getQuantity()) {
                throw new SQLException("Insufficient stock.");
            }

            double totalPrice = product.getPrice() * purchase.getQuantity();

            String insertOrderSQL = "INSERT INTO orders (nickname, total_sell) VALUES (?, ?)";
            int orderId;
            try (PreparedStatement stmt = conn.prepareStatement(insertOrderSQL, Statement.RETURN_GENERATED_KEYS)) {
                stmt.setString(1, purchase.getCustomerName());
                stmt.setDouble(2, totalPrice);
                stmt.executeUpdate();

                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        orderId = rs.getInt(1);
                    } else {
                        throw new SQLException("Failed to retrieve order ID.");
                    }
                }
            }


            String insertMatrixSQL = "INSERT INTO order_product_matrix (order_id, product_id) VALUES (?, ?)";
            try (PreparedStatement stmt = conn.prepareStatement(insertMatrixSQL)) {
                stmt.setInt(1, orderId);
                stmt.setInt(2, purchase.getProductId());
                stmt.executeUpdate();
            }


            productWriteRepository.decreaseQuantity(purchase.getProductId(), purchase.getQuantity(), conn);

            conn.commit();
        } catch (SQLException e) {
            if (conn != null) {
                conn.rollback();
            }
            throw e;
        } finally {
            if (conn != null) {
                conn.setAutoCommit(true);
                conn.close();
            }
        }
    }
}
