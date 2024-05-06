package com.ssl.productservice.controllers;

import com.ssl.productservice.models.CreateProductDTO;
import com.ssl.productservice.models.Product;
import com.ssl.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/{id}")
    public Product getProduct(
            @PathVariable Long id
    ) {
        return productService.getProductById(id);
    }

    @PostMapping
    public Product createProduct(
            @RequestBody Product product
            ) {
        return productService.createProduct(product);
    }

    @PutMapping
    public Product replaceProduct(
            @RequestBody Product product
    ) {
        return productService.replaceProduct(product);
    }

    @PatchMapping
    public Product updateProduct(
            @RequestBody Product product
    ) {
        return productService.updateProduct(product);
    }

    @DeleteMapping("/{id}")
    public Product deleteProduct(
            @PathVariable Long id
    ) {
        return productService.deleteProduct(id);
    }
}
