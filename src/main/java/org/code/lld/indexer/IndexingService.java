package org.code.lld.indexer;

import org.code.lld.model.Inventory;

import java.util.List;

public interface IndexingService {
    void addToIndex(Inventory inventory);
    List<Integer> findByPrefix(String prefix);
}
