package com.example.inventory.controller;

import com.example.inventory.model.dto.request.InventoryRequest;
import com.example.inventory.model.dto.response.InventoryResponse;
import com.example.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/inventory")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    //http://localhost:4002/v1/inventory/check/11111,22222
    //http://localhost:4002/v1/inventory/check/skuCode=11111,skuCode=22222
    @PostMapping("/check")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestBody InventoryRequest inventoryRequest) {
        return inventoryService.isAvailable(inventoryRequest.getSkuCodes());
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> fetchAll() {
        return inventoryService.findAll();
    }
}
