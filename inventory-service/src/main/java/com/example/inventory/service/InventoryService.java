package com.example.inventory.service;


import com.example.inventory.model.dto.request.InventoryRequest;
import com.example.inventory.model.dto.response.InventoryResponse;

import java.util.List;

public interface InventoryService {

    /**
     * create inventory record
     *
     * @param inventoryRequest
     */
    void save(InventoryRequest inventoryRequest);

    /**
     * Fetches all Inventories from the database.
     *
     * @return A list containing all the Inventories available in the database.
     */
    List<InventoryResponse> findAll();

    /**
     * checks the inventory for the stock availability of a stock by barcode
     *
     * @param barcode
     * @return boolean
     */
    boolean isAvailable(String barcode);
}
