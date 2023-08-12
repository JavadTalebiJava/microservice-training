package com.example.inventory.controller;

import com.example.inventory.model.dto.response.InventoryResponse;
import com.example.inventory.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/inventories")
@RequiredArgsConstructor
@Slf4j
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping("/{barcode}")
    @ResponseStatus(HttpStatus.OK)
    public boolean isInStock(@PathVariable String barcode) {
        return inventoryService.isAvailable(barcode);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> fetchAll() {
        return inventoryService.findAll();
    }
}
