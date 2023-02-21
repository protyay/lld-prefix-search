package org.code.lld;

import org.code.lld.dao.InventoryDao;
import org.code.lld.model.Inventory;
import org.code.lld.service.InventoryQueryService;
import org.code.lld.indexer.impl.TrieIndexingService;

import java.util.List;


public class LldApplication {

	public static void main(String[] args) {
		TrieIndexingService trieIndexingService = new TrieIndexingService();
		InventoryQueryService queryService = new InventoryQueryService(new InventoryDao(trieIndexingService));
		final List<Inventory> searchResult = queryService.findByPrefix("Red p");
		System.out.println("searchResult = " + searchResult);

	}

}
