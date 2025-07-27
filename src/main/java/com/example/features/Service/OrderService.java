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
                        .sum();
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
                    .sum();
            String nickname = repo.getOrderNickname(orderId, conn);

            order resp = new order();
            resp.setOrderId(orderId);
            resp.setNickname(nickname);
            resp.setProducts(products);
            resp.setTotalSell(total);
            return resp;
        }
    }

    public int placeFullOrder(String nickname, Map<Integer, Integer> productQuantities) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int orderId = repo.createOrder(nickname, conn);

                for (Map.Entry<Integer, Integer> entry : productQuantities.entrySet()) {
                    int productId = entry.getKey();
                    int quantity = entry.getValue();

                    repo.checkAndReduceProductStock(productId, quantity, conn);
                    repo.addProductToOrder(orderId, productId, quantity, conn);
                }

                List<Product> products = repo.getProductsForOrder(orderId, conn);
                double total = products.stream()
                        .mapToDouble(p -> p.getPrice() * p.getQuantity())
                        .sum();

                repo.updateOrderTotal(orderId, total, conn);

                conn.commit();
                return orderId;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public order getOrderByNickname(String nickname) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            int orderId = repo.getOrderIdByNickname(nickname, conn);
            return getOrder(orderId);
        }
    }
/*
    public void deleteOrderByNickname(String nickname) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int orderId = repo.getOrderIdByNickname(nickname, conn);
                repo.deleteOrder(orderId, conn);
                conn.commit();
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

    public void updateOrder(String nickname, Map<Integer, Integer> updatedProductQuantities) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int orderId = repo.getOrderIdByNickname(nickname, conn);

                repo.deleteOrderProducts(orderId, conn);

                for (Map.Entry<Integer, Integer> entry : updatedProductQuantities.entrySet()) {
                    int productId = entry.getKey();
                    int quantity = entry.getValue();

                    repo.checkAndReduceProductStock(productId, quantity, conn);
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

 */
    public boolean deleteOrderByNickname(String nickname) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            int orderId = repo.getOrderIdByNickname(nickname, conn);
            if (orderId == -1) return false;

            repo.deleteOrderProducts(orderId, conn);
            repo.deleteOrder(orderId, conn);
            return true;
        }
    }

    public boolean updateOrder(String nickname, Map<Integer, Integer> newProducts) throws SQLException {
        try (Connection conn = DBConnection.getConnection()) {
            conn.setAutoCommit(false);
            try {
                int orderId = repo.getOrderIdByNickname(nickname, conn);
                if (orderId == -1) return false;


                repo.deleteOrderProducts(orderId, conn);

                for (Map.Entry<Integer, Integer> entry : newProducts.entrySet()) {
                    int productId = entry.getKey();
                    int qty = entry.getValue();


                    repo.checkAndReduceProductStock(productId, qty, conn);
                    repo.addProductToOrder(orderId, productId, qty, conn);
                }

                List<Product> updated = repo.getProductsForOrder(orderId, conn);
                double total = updated.stream()
                        .mapToDouble(p -> p.getPrice() * p.getQuantity())
                        .sum();
                repo.updateOrderTotal(orderId, total, conn);

                conn.commit();
                return true;
            } catch (Exception e) {
                conn.rollback();
                throw e;
            }
        }
    }

}
