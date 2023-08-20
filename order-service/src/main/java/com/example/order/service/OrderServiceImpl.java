package com.example.order.service;

import com.example.order.model.Order;
import com.example.order.model.OrderItem;
import com.example.order.model.dto.OrderItemDto;
import com.example.order.model.dto.request.InventoryRequest;
import com.example.order.model.dto.request.OrderRequest;
import com.example.order.model.dto.response.InventoryResponse;
import com.example.order.model.dto.response.OrderResponse;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
/*import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;*/

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@Transactional //spring will automatically commit the transactions
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    //private final WebClient.Builder webClientBuilder;
    private final RestTemplate restTemplate;
    //private final String inventoryServiceUrl = "http://vitelco_user6.local:inventory-service/v1/inventory/check"; //"http://localhost:4002/v1/inventory/check";


    /**
     * place order
     *
     * @param orderRequest
     */
    @Override
    public void placeOrder(OrderRequest orderRequest) throws URISyntaxException {
         List<String> skuCodes = orderRequest.getOrderItems().stream().map(OrderItemDto::getSku).toList();

        //call Inventory Service and Place Order if all Products are in stock
        boolean allProductsInStock = fetchStockInventoryAvailability(InventoryRequest.builder().skuCodes(skuCodes).build());

        if (allProductsInStock){
            Order order = orderRequest.convert();
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock!");
        }

    }

    private boolean fetchStockInventoryAvailability(InventoryRequest inventoryRequest) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        URI uri;
        try {
            uri = new URI("http://localhost:4002/v1/inventory/check");
        } catch (URISyntaxException e) {
            // Handle the exception appropriately (e.g., log an error)
            e.printStackTrace();
            return false;
        }

        HttpEntity<InventoryRequest> httpEntity = new HttpEntity<>(inventoryRequest, headers);

        /**
         * uses a ParameterizedTypeReference to capture the generic type information for the response body when making a REST API call
         *
         * ParameterizedTypeReference is a class provided by Spring Framework (used in your code through RestTemplate) that allows you to capture the generic type information.
         * By using ParameterizedTypeReference, you can overcome type erasure and let Spring correctly deserialize the JSON response into a list of InventoryResponse objects.
         *
         * This code creates an anonymous subclass of ParameterizedTypeReference with the specified type parameter List<InventoryResponse>.
         * The curly braces {} define the body of this anonymous subclass, but in this case, it's empty since you're not adding any additional behavior or methods.
         * The purpose of this construction is solely to capture the generic type information.
         *
         * When you pass this ParameterizedTypeReference to the exchange method, Spring will use it
         * to properly deserialize the response body into a List<InventoryResponse>, allowing you to work with the response data as you expect.
         * */
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<List<InventoryResponse>> responseEntity = restTemplate.exchange(
                uri,
                HttpMethod.POST,
                httpEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        List<InventoryResponse> inventoryResponses = responseEntity.getBody();
        if (inventoryResponses != null && inventoryResponses.size()>0) {
            // Process inventoryResponses as needed
            log.info(inventoryResponses.toString());
            return inventoryResponses.stream()
                    .allMatch(InventoryResponse::isInStock);
        } else {
            return false;
        }
    }


   /* @LoadBalanced
    public boolean fetchStockInventoryAvialability(InventoryRequest inventoryRequest) {
        Flux<List<InventoryResponse>> data = webClientBuilder.build().post()
                .uri(inventoryServiceUrl)
                .body(BodyInserters.fromValue(inventoryRequest))
                .retrieve()
                .bodyToFlux(InventoryResponse.class)
                .collectList()
                .flux();
        List<InventoryResponse> responseArray = data.blockLast();
        log.info(responseArray.toString());
        return responseArray.stream()
                .allMatch(InventoryResponse::isInStock);
    }*/
    /**
     * Fetches all orders from the database.
     *
     * @return A list containing all the orders available in the database.
     */
    @Override
    public List<OrderResponse> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    /**
     * Maps a {@link Order} object to a {@link OrderResponse} object.
     *
     * @param order The {@link Order} object to be converted into the response format.
     * @return A {@link OrderResponse} object representing the mapped order.
     */
    private OrderResponse mapToResponse(Order order) {
        return OrderResponse.builder()
                .id(order.getId())
                .orderItems(order.getOrderItems().stream()
                        .map(this::mapToOrderItemDto)
                        .collect(Collectors.toList()))
                .createdDate(order.getCreatedDate())
                .build();

    }

    /**
     * Maps a {@link OrderItem} object to a {@link OrderItemDto} object.
     *
     * @param orderItem The {@link OrderItem} object to be converted into the response format.
     * @return A {@link OrderItemDto} object representing the mapped orderItemDto.
     */
    private OrderItemDto mapToOrderItemDto(OrderItem orderItem) {
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .price(orderItem.getPrice())
                .barcode(orderItem.getBarcode())
                .sku(orderItem.getSku())
                .qty(orderItem.getQty())
                .createdDate(orderItem.getCreatedDate())
                .build();
    }
}
