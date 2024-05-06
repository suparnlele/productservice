package com.ssl.productservice.services;

import com.ssl.productservice.models.Product;

public interface ProductService {
    Product getProductById(Long id);
    Product createProduct(Product product);
    Product updateProduct(Product product);
    Product replaceProduct(Product product);
    Product deleteProduct(Long id);
}
