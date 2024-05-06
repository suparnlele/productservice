package com.ssl.productservice.services.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.ssl.productservice.configurations.RestTemplateConfiguration;
import com.ssl.productservice.dtos.FakeStoreProductDTO;
import com.ssl.productservice.models.Category;
import com.ssl.productservice.models.Product;
import com.ssl.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {
    private final RestTemplate restTemplate;
    private static final String fakeStoreGetProductByIdURI = "https://fakestoreapi.com/products/%s";
    private static final String fakeStoreCreateProductURI = "https://fakestoreapi.com/products";
    private static final String fakeStoreUpdateProductURI = "https://fakestoreapi.com/products/%s";
    private static final String fakeStoreReplaceProductURI = "https://fakestoreapi.com/products/%s";

    @Autowired
    public ProductServiceImpl(RestTemplateConfiguration restTemplateConfiguration) {
        this.restTemplate = restTemplateConfiguration.getRestTemplate();
    }

    @Override
    public Product getProductById(Long id) {
        System.out.println("Here");
        String finalURI = String.format(fakeStoreGetProductByIdURI, id);
        ResponseEntity<FakeStoreProductDTO> fakeStoreProductDTOResponseEntity = restTemplate.getForEntity(
            finalURI, FakeStoreProductDTO.class
        );

        if(fakeStoreProductDTOResponseEntity.getStatusCode().is2xxSuccessful()) {
            return mapFakeStoreProductDTOToProduct(Objects.requireNonNull(fakeStoreProductDTOResponseEntity.getBody()));
        } else {
            throw new RuntimeException("Error while processing fake store getProductById API");
        }
    }

    @Override
    public Product createProduct(Product product) {
        FakeStoreProductDTO fakeStoreProductDTO = mapProductToFakeStoreProductDTO(product);

        System.out.println("Here");
        ResponseEntity<FakeStoreProductDTO> fakeStoreProductDTOResponseEntity = restTemplate.postForEntity(
            "https://fakestoreapi.com/products", fakeStoreProductDTO, FakeStoreProductDTO.class
        );

        if(fakeStoreProductDTOResponseEntity.getStatusCode().is2xxSuccessful()) {
            return mapFakeStoreProductDTOToProduct(Objects.requireNonNull(fakeStoreProductDTOResponseEntity.getBody()));
        } else {
            throw new RuntimeException("Error while processing fake store createProduct API");
        }
    }

    @Override
    public Product updateProduct(Product product) {
        FakeStoreProductDTO fakeStoreProductDTO = mapProductToFakeStoreProductDTO(product);
        String finalURI = String.format(fakeStoreUpdateProductURI, product.getId());

        ResponseEntity<FakeStoreProductDTO> fakeStoreProductDTOResponseEntity = restTemplate.exchange(
            finalURI, HttpMethod.PATCH, getHttpEntity(fakeStoreProductDTO), FakeStoreProductDTO.class);

        if(fakeStoreProductDTOResponseEntity.getStatusCode().is2xxSuccessful()) {
            return mapFakeStoreProductDTOToProduct(Objects.requireNonNull(fakeStoreProductDTOResponseEntity.getBody()));
        } else {
            throw new RuntimeException("Error while processing fake store updateProduct API");
        }
    }

    @Override
    public Product replaceProduct(Product product) {
        FakeStoreProductDTO fakeStoreProductDTO = mapProductToFakeStoreProductDTO(product);
        String finalURI = String.format(fakeStoreReplaceProductURI, product.getId());


        ResponseEntity<FakeStoreProductDTO> fakeStoreProductDTOResponseEntity = restTemplate.exchange(
                finalURI, HttpMethod.PUT, getHttpEntity(fakeStoreProductDTO), FakeStoreProductDTO.class);

        if(fakeStoreProductDTOResponseEntity.getStatusCode().is2xxSuccessful()) {
            return mapFakeStoreProductDTOToProduct(Objects.requireNonNull(fakeStoreProductDTOResponseEntity.getBody()));
        } else {
            throw new RuntimeException("Error while processing fake store replaceProduct API");
        }
    }

    @Override
    public Product deleteProduct(Long id) {
        return null;
    }

    public static Product mapFakeStoreProductDTOToProduct(FakeStoreProductDTO fakeStoreProductDTO) {
        Product product = new Product();
        product.setId(fakeStoreProductDTO.getId());
        product.setTitle(fakeStoreProductDTO.getTitle());
        product.setPrice(fakeStoreProductDTO.getPrice());
        product.setDescription(fakeStoreProductDTO.getDescription());

        // Mapping Category
        Category category = new Category();
        category.setTitle(fakeStoreProductDTO.getCategory());
        product.setCategory(category);

        return product;
    }

    // Write a function to convert Product to FakeStoreProductDTO
    public static FakeStoreProductDTO mapProductToFakeStoreProductDTO(Product product) {
        FakeStoreProductDTO fakeStoreProductDTO = new FakeStoreProductDTO();
        fakeStoreProductDTO.setId(product.getId());
        fakeStoreProductDTO.setTitle(product.getTitle());
        fakeStoreProductDTO.setPrice(product.getPrice());
        fakeStoreProductDTO.setDescription(product.getDescription());
        fakeStoreProductDTO.setCategory(product.getCategory().getTitle());

        return fakeStoreProductDTO;
    }

    public <T> HttpEntity<T> getHttpEntity(T body) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(body, headers);
    }
}
