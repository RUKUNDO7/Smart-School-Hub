package com.smartschoolhub.service;

import com.smartschoolhub.domain.InventoryItem;
import com.smartschoolhub.domain.SchoolAsset;
import com.smartschoolhub.domain.StockTransaction;
import com.smartschoolhub.repository.InventoryItemRepository;
import com.smartschoolhub.repository.SchoolAssetRepository;
import com.smartschoolhub.repository.StockTransactionRepository;
import com.smartschoolhub.service.dto.InventoryItemRequest;
import com.smartschoolhub.service.dto.SchoolAssetRequest;
import com.smartschoolhub.service.dto.StockTransactionRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class InventoryService {
    private final InventoryItemRepository itemRepository;
    private final StockTransactionRepository transactionRepository;
    private final SchoolAssetRepository assetRepository;

    public InventoryService(InventoryItemRepository itemRepository,
                            StockTransactionRepository transactionRepository,
                            SchoolAssetRepository assetRepository) {
        this.itemRepository = itemRepository;
        this.transactionRepository = transactionRepository;
        this.assetRepository = assetRepository;
    }

    public List<InventoryItem> getAllItems() {
        return itemRepository.findAll();
    }

    public InventoryItem createItem(InventoryItemRequest request) {
        InventoryItem item = new InventoryItem();
        item.setName(request.getName());
        item.setDescription(request.getDescription());
        item.setUnitPrice(request.getUnitPrice());
        item.setQuantity(0);
        return itemRepository.save(item);
    }

    @Transactional
    public StockTransaction recordTransaction(StockTransactionRequest request) {
        InventoryItem item = itemRepository.findById(request.getItemId())
            .orElseThrow(() -> new ResourceNotFoundException("Item not found: " + request.getItemId()));

        int quantityChange = request.getQuantity();
        if (item.getQuantity() + quantityChange < 0) {
            throw new IllegalArgumentException("Insufficient stock for item: " + item.getName());
        }

        StockTransaction transaction = new StockTransaction();
        transaction.setItem(item);
        transaction.setQuantity(quantityChange);
        transaction.setType(request.getType());
        transaction.setTransactionDate(LocalDateTime.now());
        transaction.setRemarks(request.getRemarks());

        item.setQuantity(item.getQuantity() + quantityChange);
        itemRepository.save(item);

        return transactionRepository.save(transaction);
    }

    public List<SchoolAsset> getAllAssets() {
        return assetRepository.findAll();
    }

    public SchoolAsset createAsset(SchoolAssetRequest request) {
        SchoolAsset asset = new SchoolAsset();
        asset.setAssetTag(request.getAssetTag());
        asset.setName(request.getName());
        asset.setDescription(request.getDescription());
        asset.setPurchaseDate(request.getPurchaseDate());
        asset.setPurchasePrice(request.getPurchasePrice());
        asset.setLocation(request.getLocation());
        asset.setStatus(request.getStatus());
        return assetRepository.save(asset);
    }

    public SchoolAsset updateAssetStatus(Long assetId, String status) {
        SchoolAsset asset = assetRepository.findById(assetId)
            .orElseThrow(() -> new ResourceNotFoundException("Asset not found: " + assetId));
        asset.setStatus(status);
        return assetRepository.save(asset);
    }
}
