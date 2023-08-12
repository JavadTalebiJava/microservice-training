package com.example.order.service;

import com.example.order.model.Order;
import com.example.order.model.OrderItem;
import com.example.order.model.dto.OrderItemDto;
import com.example.order.model.dto.request.OrderRequest;
import com.example.order.model.dto.response.OrderResponse;
import com.example.order.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional //spring will automatically commit the transactions
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    /**
     * place order
     *
     * @param orderRequest
     */
    @Override
    public void placeOrder(OrderRequest orderRequest) {
        orderRepository.save(orderRequest.convert());
    }

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
