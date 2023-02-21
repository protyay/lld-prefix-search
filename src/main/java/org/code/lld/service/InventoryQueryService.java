package org.code.lld.service;

import org.code.lld.dao.InventoryDao;
import org.code.lld.model.Inventory;

import java.util.List;

public class InventoryQueryService {
    private InventoryDao inventoryDao;

    public InventoryQueryService(InventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    public List<Inventory> findById(List<Integer> ids) {
        return this.inventoryDao.findById(ids);
    }

    public List<Inventory> findByPrefix(String prefix) {
        return this.inventoryDao.findByPrefix(prefix);
    }
}
