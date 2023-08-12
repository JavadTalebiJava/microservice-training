package com.example.product;

import com.example.product.model.dto.request.ProductRequest;
import com.example.product.repository.ProductRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.math.BigDecimal;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Testcontainers
@AutoConfigureMockMvc
@Slf4j
class ProductServiceApplicationTests {

    @Container
    private static MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:latest");

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @DynamicPropertySource
    static void setDynamicProps(DynamicPropertyRegistry dynamicPropertyRegistry) {
        dynamicPropertyRegistry.add("spring.data.mongodb.url", mongoDBContainer::getReplicaSetUrl);

        dynamicPropertyRegistry.add("spring.data.mongodb.database", TestConfig::testDbName);
    }

    @Before
     void setup() {
        log.info(mongoDBContainer.getContainerName());

    }

    @Test
    void shouldCreateProduct() throws Exception {
        productRepository.deleteAll();
        ProductRequest productRequest = ProductRequest.builder()
                .name("test Macbook")
                .description("test Macbook")
                .price(BigDecimal.valueOf(34000))
                .build();
        String productRequestString = objectMapper.writeValueAsString(productRequest);
        mockMvc.perform(MockMvcRequestBuilders.post("/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(productRequestString))
                .andExpect(status().isCreated());
        Assertions.assertEquals(1, productRepository.findAll().size());
    }

}
