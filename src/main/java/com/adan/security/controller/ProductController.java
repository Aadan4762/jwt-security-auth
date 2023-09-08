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
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<String> createProduct(@RequestBody ProductRequest productRequest) {

        Product product = new Product();
        product.setName(productRequest.getName());
        product.setQty(productRequest.getQty());
        product.setPrice(productRequest.getPrice());

        Product createdProduct = productService.createProduct(product);
        if (createdProduct != null) {
            String message = "Product has been created successfully.";
            return new ResponseEntity<>(message, HttpStatus.CREATED);
        } else {
            String errorMessage = "Failed to create the product.";
            return new ResponseEntity<>(errorMessage, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/all")
    public List<ProductResponse> getAllTheProducts() {
        List<ProductResponse> productResponses = productService.getAllProductResponses();
        return productResponses;
    }

    @PutMapping("/update/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable int productId,
            @RequestBody ProductRequest productRequest) {
        Product existingProduct = productService.getProductById(productId);

        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }
        existingProduct.setName(productRequest.getName());
        existingProduct.setQty(productRequest.getQty());
        existingProduct.setPrice(productRequest.getPrice());

        productService.updateProduct(existingProduct);

        return ResponseEntity.ok().build();
    }


    @DeleteMapping("/delete/{productId}")
    public ResponseEntity<?> deleteProduct(@PathVariable int productId) {
        Product existingProduct = productService.getProductById(productId);

        if (existingProduct == null) {
            return ResponseEntity.notFound().build();
        }

        productService.deleteProduct(productId);

        return ResponseEntity.ok().build();
    }


    @GetMapping("/get/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable int productId) {
        Product existingProduct = productService.getProductById(productId);

        if (existingProduct == null) {
            String errorMessage = "Product not found with ID: " + productId;
            return ResponseEntity.notFound().build();
        }
        ProductResponse productResponse = new ProductResponse();
        productResponse.setProductId(existingProduct.getProductId());
        productResponse.setName(existingProduct.getName());
        productResponse.setQty(existingProduct.getQty());
        productResponse.setPrice(existingProduct.getPrice());

        return ResponseEntity.ok(productResponse);
    }






}
