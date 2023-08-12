package com.example.product.service;

import com.example.product.model.Product;
import com.example.product.model.dto.request.ProductRequest;
import com.example.product.model.dto.response.ProductResponse;
import com.example.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;


    /**
     * Fetches all products from the database.
     *
     * @return A list containing all the products available in the database.
     */
    @Override
    public List<ProductResponse> findAll() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Maps a {@link Product} object to a {@link ProductResponse} object.
     *
     * @param product The {@link Product} object to be converted into the response format.
     * @return A {@link ProductResponse} object representing the mapped product.
     */
    private ProductResponse mapToResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .build();
    }

    /**
     * find product by id
     *
     * @param id The unique identifier of the {@link Product} to find.
     * @return An Optional containing the {@link Product} if found, or an empty Optional if not found.
     */
    @Override
    public Optional<Product> findById(String id) {
        return productRepository.findById(id);
    }

    /**
     * @param {@link ProductRequest} The {@link Product} to be created
     * @return The created product, with an assigned ID in case of a new product creation.
     */
    @Override
    public Product create(ProductRequest productRequest) {
        return productRepository.save(productRequest.convert(productRequest));
    }
}
