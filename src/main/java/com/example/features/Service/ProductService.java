package com.example.features.Service;


import com.example.features.Domain.Product;
import com.example.features.repository.Productrepository;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final Productrepository repository;

    public ProductService() {
        this.repository = new Productrepository();
    }

    public void createProduct(Product product) throws SQLException {
        repository.createProduct(product);
    }

    public Product getProductById(int id) throws SQLException {
        return repository.getProductById(id);
    }

    public List<Product> getAllProducts() throws SQLException {
        return repository.getAllProducts();
    }

    public void updateProduct(Product product) throws SQLException {
        repository.updateProduct(product);
    }

    public void deleteProduct(int id) throws SQLException {
        repository.deleteProduct(id);
    }

    public void restockProduct(int id, int quantity) throws SQLException {
        repository.increaseQuantity(id, quantity);
    }


}

