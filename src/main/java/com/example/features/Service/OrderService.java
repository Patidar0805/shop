package com.example.features.Service;

import com.example.features.Domain.Product;
import com.example.features.Domain.order;
import com.example.features.repository.OrderRepository;
import com.example.features.Util.DBConnection;
import com.example.features.request.AddProductToOrderRequest;
import com.example.features.request.OrderRequest;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class OrderService {
    private static final OrderRepository repo = new OrderRepository();

    public int placeOrder(OrderRequest req) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int orderId = repo.createOrder(req.getNickname(), conn);
                conn.commit();
                return orderId;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }
    public void addProduct(AddProductToOrderRequest req) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                repo.addProductToOrder(req.getOrderid(), req.getProductid(), req.getQuantity(), conn);
                List<Product> products = repo.getProductsForOrder(req.getOrderid(), conn);
                double total = products.stream()
                        .mapToDouble(p -> p.getPrice() * p.getQuantity())
                        .sum();  // ✅ updated line
                repo.updateOrderTotal(req.getOrderid(), total, conn);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public static order getOrder(int orderId) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            List<Product> products = repo.getProductsForOrder(orderId, conn);
            double total = products.stream()
                    .mapToDouble(p -> p.getPrice() * p.getQuantity())
                    .sum(); // Updated: price * quantity
            String nickname = repo.getOrderNickname(orderId, conn);

            order resp = new order();
            resp.setOrderId(orderId);
            resp.setNickname(nickname);
            resp.setProducts(products);
            resp.setTotalSell(total);
            return resp;
        }
    }

    public void addMultipleProducts(int orderId, Map<Integer, Integer> productQuantities) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
                    int productId = entry.getKey();
                    int quantity = entry.getValue();
                    repo.addProductToOrder(orderId, productId, quantity, conn);
                }
                List<Product> products = repo.getProductsForOrder(orderId, conn);
                double total = products.stream()
                        .mapToDouble(p -> p.getPrice() * p.getQuantity())
                        .sum();
                repo.updateOrderTotal(orderId, total, conn);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }


}
