package com.example.product.service;

import com.example.product.model.Product;
import com.example.product.model.dto.request.ProductRequest;
import com.example.product.model.dto.response.ProductResponse;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    /**
     * Fetches all products from the database.
     *
     * @return A list containing all the products available in the database.
     */
    List<ProductResponse> findAll();

    /**
     * find product by id
     *
     * @param id The unique identifier of the {@link Product} to find.
     * @return An Optional containing the {@link Product} if found, or an empty Optional if not found.
     */
    Optional<Product> findById(String id);

    /**
     * @param {@link ProductRequest} The {@link Product} to be created
     * @return The created product, with an assigned ID in case of a new product creation.
     */
    Product create(ProductRequest productRequest);


}
