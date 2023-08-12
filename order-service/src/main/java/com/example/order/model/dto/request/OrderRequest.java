package com.example.order.model.dto.request;

import com.example.order.model.Order;
import com.example.order.model.OrderItem;
import com.example.order.model.dto.OrderItemDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {



    private List<OrderItemDto> orderItems;

    public Order convert() {
        Order order = Order.builder()
                .createdDate(LocalDateTime.now())
                .build();

        order.setOrderItems(orderItems.stream()
                .map(orderItem -> {
                    OrderItem dto = mapToDto(orderItem);
                    dto.setOrder(order); // Set the Order ID
                    return dto;
                })
                .collect(Collectors.toList()));

        return order;
    }

    private OrderItem mapToDto(OrderItemDto orderItemDto) {
        return OrderItem.builder()
                .id(orderItemDto.getId())
                .price(orderItemDto.getPrice())
                .barcode(orderItemDto.getBarcode())
                .sku(orderItemDto.getSku())
                .qty(orderItemDto.getQty())
                .createdDate(LocalDateTime.now())
                .build();
    }
}
