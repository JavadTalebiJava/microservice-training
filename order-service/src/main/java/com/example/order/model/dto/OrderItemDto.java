package com.example.order.model.dto;

import com.example.order.model.Order;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long id;

    private LocalDateTime createdDate;

    private String sku;
    private String barcode;
    private float qty;

    private BigDecimal price;

    @JsonIgnore
    private Order order;

}
