package com.example.inventory.controller;

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

    //http://localhost:4002/v1/inventory/11111,22222
    //http://localhost:4002/v1/inventory/sku=11111,sku=22222
    @GetMapping("/{barcode}")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam("sku") List<String> skuCodes) {
        return inventoryService.isAvailable(skuCodes);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> fetchAll() {
        return inventoryService.findAll();
    }
}
