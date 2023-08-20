package com.example.order.service;

import com.example.order.model.dto.request.OrderRequest;
import com.example.order.model.dto.response.OrderResponse;

import java.net.URISyntaxException;
import java.util.List;

public interface OrderService {

    /**
     * place order
     *
     * @param orderRequest
     */
    void placeOrder(OrderRequest orderRequest) throws URISyntaxException;

    /**
     * Fetches all orders from the database.
     *
     * @return A list containing all the orders available in the database.
     */
    List<OrderResponse> findAll();
}
