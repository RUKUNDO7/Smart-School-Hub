package com.smartschoolhub.controller;

import com.smartschoolhub.domain.InventoryItem;
import com.smartschoolhub.domain.SchoolAsset;
import com.smartschoolhub.domain.StockTransaction;
import com.smartschoolhub.service.InventoryService;
import com.smartschoolhub.service.dto.InventoryItemRequest;
import com.smartschoolhub.service.dto.SchoolAssetRequest;
import com.smartschoolhub.service.dto.StockTransactionRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;

    public InventoryController(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    @GetMapping("/items")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<InventoryItem> getAllItems() {
        return inventoryService.getAllItems();
    }

    @PostMapping("/items")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<InventoryItem> createItem(@Valid @RequestBody InventoryItemRequest request) {
        return new ResponseEntity<>(inventoryService.createItem(request), HttpStatus.CREATED);
    }

    @PostMapping("/transactions")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<StockTransaction> recordTransaction(@Valid @RequestBody StockTransactionRequest request) {
        return new ResponseEntity<>(inventoryService.recordTransaction(request), HttpStatus.CREATED);
    }

    @GetMapping("/assets")
    @PreAuthorize("hasAnyRole('ADMIN','TEACHER')")
    public List<SchoolAsset> getAllAssets() {
        return inventoryService.getAllAssets();
    }

    @PostMapping("/assets")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<SchoolAsset> createAsset(@Valid @RequestBody SchoolAssetRequest request) {
        return new ResponseEntity<>(inventoryService.createAsset(request), HttpStatus.CREATED);
    }

    @PatchMapping("/assets/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public SchoolAsset updateAssetStatus(@PathVariable Long id, @RequestParam String status) {
        return inventoryService.updateAssetStatus(id, status);
    }
}
