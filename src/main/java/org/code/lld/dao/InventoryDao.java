package org.code.lld.dao;

import org.code.lld.indexer.IndexingService;
import org.code.lld.model.Inventory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class InventoryDao {
    Map<Integer, Inventory> inventoryDB;
    private AtomicInteger integer;
    private IndexingService indexingService;
    private Logger logger = LoggerFactory.getLogger(InventoryDao.class);

    public InventoryDao(IndexingService indexingService) {
        this.indexingService = indexingService;
        this.inventoryDB = new HashMap<>();
        this.integer = new AtomicInteger(0);
        final Inventory redmi_note = new Inventory(1, "Redmi Note", BigDecimal.TEN);
        this.addInventory(redmi_note);
        final Inventory red_mobile_cases = new Inventory(2, "Red Phone Cases", BigDecimal.valueOf(100));
        this.addInventory(red_mobile_cases);
        this.inventoryDB.put(1, redmi_note);
        this.inventoryDB.put(2, red_mobile_cases);
    }

    public List<Inventory> findByPrefix(String searchPrefix) {
        final List<Integer> byPrefix = this.indexingService.findByPrefix(searchPrefix);
        return this.findById(byPrefix);
    }

    public Integer addInventory(Inventory inventory) {
        int nextId = this.integer.incrementAndGet();
        this.inventoryDB.put(nextId, inventory);

        this.indexingService.addToIndex(inventory);
        return nextId;
    }

    public Integer updateInventory(Inventory inventory) {
        if (!this.inventoryDB.containsKey(inventory.id())) {
            throw new IllegalArgumentException("Invalid Inventory ID provided");
        }
        //this.indexingService.updateIndex(inventory);
        return inventory.id();
    }

    public List<Inventory> findById(List<Integer> ids) {
        if (ids == null || ids.isEmpty()) return List.of();
        List<Inventory> inventories = new ArrayList<>();
        for (Integer id : ids) {
            if (id == null) {
                logger.atInfo().log("Skipping null id");
                continue;
            }
            if (!this.inventoryDB.containsKey(id)) {
                logger.atDebug().log("Invalid ID for inventory. Skipping");
                continue;
            }
            inventories.add(this.inventoryDB.get(id));
        }
        return inventories;
    }

}
