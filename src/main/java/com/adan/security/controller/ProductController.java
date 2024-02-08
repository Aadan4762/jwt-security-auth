package com.adan.security.controller;

import com.adan.security.dto.ProductRequest;
import com.adan.security.dto.ProductResponse;
import com.adan.security.entity.Product;
import com.adan.security.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController // Indicates that this class serves the role of a controller in Spring MVC and provides RESTful endpoints
@RequiredArgsConstructor // Generates a constructor with required arguments for the fields in the class
@RequestMapping("/api/v1/products") // Base mapping for all endpoints in this controller
public class ProductController {

    private final ProductService productService; // Dependency injection for ProductService

    // Endpoint for creating a new product
    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest) {
        // Create a new Product object and populate it with data from the request
        Product product = new Product();
        product.setName(productRequest.getName());
        product.setQty(productRequest.getQty());
        product.setPrice(productRequest.getPrice());

        // Invoke the ProductService to create the product
        Product createdProduct = productService.createProduct(product);

        // Respond with appropriate HTTP status and message based on whether the product creation was successful
        if (createdProduct != null) {
            String message = "Product has been created successfully.";
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            String errorMessage = "Failed to create the product.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Endpoint for retrieving all products
    @GetMapping("/all")
    public ResponseEntity<?> getAllTheProducts() {
        // Retrieve all products from the ProductService
        List<ProductResponse> productResponses = productService.getAllProductResponses();

        // Respond with appropriate message and products list
        if (productResponses.isEmpty()) {
            return ResponseEntity.ok("No products available.");
        } else {
            return ResponseEntity.ok(productResponses);
        }
    }

    // Endpoint for updating an existing product
    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable int productId,
            @RequestBody ProductRequest productRequest) {
        // Retrieve the existing product by ID
        Product existingProduct = productService.getProductById(productId);

        // Check if the product exists
        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        // Update the existing product with data from the request
        existingProduct.setName(productRequest.getName());
        existingProduct.setQty(productRequest.getQty());
        existingProduct.setPrice(productRequest.getPrice());

        // Invoke the ProductService to update the product
        productService.updateProduct(existingProduct);

        // Respond with success message
        return ResponseEntity.ok("Product updated successfully.");
    }

    // Endpoint for deleting a product by ID
    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable int productId) {
        // Retrieve the existing product by ID
        Product existingProduct = productService.getProductById(productId);

        // Check if the product exists
        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        // Invoke the ProductService to delete the product
        productService.deleteProduct(productId);

        // Respond with success message
        return ResponseEntity.ok("Product with ID " + productId + " deleted successfully.");
    }

    // Endpoint for retrieving a product by ID
    @GetMapping("/get/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable int productId) {
        // Retrieve the product by ID
        Product existingProduct = productService.getProductById(productId);

        // Check if the product exists
        if (existingProduct == null) {
            String errorMessage = "Product with ID " + productId + " is not available.";
            return ResponseEntity.ok(errorMessage);
        }

        // Map the product data to a response object
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductId(existingProduct.getProductId());
        productResponse.setName(existingProduct.getName());
        productResponse.setQty(existingProduct.getQty());
        productResponse.setPrice(existingProduct.getPrice());

        // Respond with the product response object
        return ResponseEntity.ok(productResponse);
    }
}

