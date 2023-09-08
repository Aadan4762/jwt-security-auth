package com.adan.security.service;

import com.adan.security.dto.ProductResponse;
import com.adan.security.entity.Product;
import com.adan.security.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;


    public Product createProduct(Product product){
        return productRepository.save(product);
    }

    public List<ProductResponse> getAllProductResponses() {
        List<Product> products = productRepository.findAll();
        List<ProductResponse> productResponses = new ArrayList<>();

        for (Product product : products) {
            ProductResponse productResponse = new ProductResponse();
            productResponse.setProductId(product.getProductId());
            productResponse.setName(product.getName());
            productResponse.setQty(product.getQty());
            productResponse.setPrice(product.getPrice());
            productResponses.add(productResponse);
        }

        return productResponses;
    }


    public Product updateProduct(Product updatedProduct) {
        // Check if the product with the given ID exists
        Product existingProduct = productRepository.findById(updatedProduct.getProductId()).orElse(null);

        if (existingProduct != null) {
            // Update the existing product's fields with the new data
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setQty(updatedProduct.getQty());
            existingProduct.setPrice(updatedProduct.getPrice());

            // Save and return the updated product
            return productRepository.save(existingProduct);
        }

        // If the product does not exist, return null or throw an exception as needed
        return null;
    }

    public void deleteProduct(int productId) {
        // Check if the product with the given ID exists
        Product existingProduct = productRepository.findById(productId).orElse(null);

        if (existingProduct != null) {
            // Delete the existing product
            productRepository.delete(existingProduct);
        }
        // You can choose to do nothing if the product does not exist or throw an exception.
    }

    public Product getProductById(int productId) {
        // Retrieve the product by its ID or return null if not found
        return productRepository.findById(productId).orElse(null);
    }



}
