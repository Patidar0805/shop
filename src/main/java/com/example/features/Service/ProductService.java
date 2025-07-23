package com.example.features.Service;


import com.example.features.Domain.Product;
import com.example.features.repository.ProductReadRepository;
import com.example.features.repository.ProductWriteRepository;

import java.sql.SQLException;
import java.util.List;

public class ProductService {
    private final ProductReadRepository ReadRepository;
    private final ProductWriteRepository WriteRepository;
    public ProductService() {
        this.ReadRepository = new ProductReadRepository();
        this.WriteRepository = new ProductWriteRepository();
    }

    public void createProduct(Product product) throws SQLException {
        WriteRepository.createProduct(product);
    }

    public Product getProductById(int id) throws SQLException {
        return ReadRepository.getProductById(id);
    }

    public List<Product> getAllProducts() throws SQLException {
        return ReadRepository.getAllProducts();
    }

    public void updateProduct(Product product) throws SQLException {
        WriteRepository.updateProduct(product);
    }

    public void deleteProduct(int id) throws SQLException {
        WriteRepository.deleteProduct(id);
    }

    public void restockProduct(int id, int quantity) throws SQLException {
        WriteRepository.increaseQuantity(id, quantity);
    }


}

