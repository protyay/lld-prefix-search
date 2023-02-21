package org.code.lld.service;

import org.code.lld.dao.InventoryDao;
import org.code.lld.model.Inventory;

public class InventoryManagementService {
    private InventoryDao inventoryDao;

    public InventoryManagementService(InventoryDao dao){
        this.inventoryDao = dao;
    }
    public Integer addInventory(Inventory inventory){
        return this.inventoryDao.addInventory(inventory);
    }
    public Integer updateInventory(Inventory inventory){
        return this.inventoryDao.updateInventory(inventory);
    }
}
