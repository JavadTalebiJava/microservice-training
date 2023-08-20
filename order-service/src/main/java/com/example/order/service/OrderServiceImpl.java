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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
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
        boolean allProductsInStock = fetchStockInventoryAvialability(InventoryRequest.builder().skuCodes(skuCodes).build());

        if (allProductsInStock){
            Order order = orderRequest.convert();
            orderRepository.save(order);
        } else {
            throw new IllegalArgumentException("Product is not in stock!");
        }

    }

    private boolean fetchStockInventoryAvialability(InventoryRequest inventoryRequest) throws URISyntaxException {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        URI uri = new URI("http://localhost:4002/v1/inventory/check");


        HttpEntity<InventoryRequest> httpEntity = new HttpEntity<>(inventoryRequest, headers);

        RestTemplate restTemplate = new RestTemplate();
        String result = restTemplate.postForObject(uri, httpEntity, String.class);
        log.info(result);
        return true;
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
