package com.adan.security.service;

import com.adan.security.dto.ProductResponse;
import com.adan.security.entity.Product;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface ProductServiceInterface {
    Product createProduct(Product product);
    List<ProductResponse> getAllProductResponses();
    Product updateProduct(Product updatedProduct);
    void deleteProduct(int productId);
    Product getProductById(int productId);
}
