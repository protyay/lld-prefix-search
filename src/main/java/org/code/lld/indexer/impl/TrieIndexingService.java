package org.code.lld.indexer.impl;

import org.code.lld.indexer.IndexingService;
import org.code.lld.model.Inventory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrieIndexingService implements IndexingService {
    private static TrieNode rootNode = null;

    public TrieIndexingService() {
        if (rootNode == null)
            rootNode = new TrieNode();
    }

    @Override
    public void addToIndex(Inventory inventory) {
        if (inventory == null || inventory.desc() == null || inventory.desc().isBlank()) return;
        TrieNode tempRoot = rootNode;
        // We will inventory the inventory description. For simplicity we can also inventory the first 10 chars
        // from the inventory product to enable search use-cases. In Real world, we would also have
        // description sanitization to avoid indexing garbled values
        for (char current : inventory.desc().toLowerCase(Locale.ROOT).toCharArray()) {
            if(!Character.isAlphabetic(current)) continue;
            if (tempRoot.children[current - 'a'] == null)
                tempRoot.children[current - 'a'] = new TrieNode();
            tempRoot = tempRoot.children[current - 'a'];
        }
        tempRoot.inventoryIdsAtNode.add(inventory.id());
    }

    /**
     * Find algorithm works by searching all the valid
     * child nodes of the given prefix node ,
     * where at-least one inventory item is present
     *
     * @param searchPrefix
     * @return all the list of product ids that can be
     */
    @Override
    public List<Integer> findByPrefix(String searchPrefix) {
        if (searchPrefix == null || searchPrefix.isBlank()) return List.of();
        TrieNode tempRootNode = rootNode;
        for (char c : searchPrefix.toLowerCase(Locale.ROOT).toCharArray()) {
            if(!Character.isAlphabetic(c)) continue;
            if (tempRootNode.children[c - 'a'] != null) {
                tempRootNode = tempRootNode.children[c - 'a'];
            } else
                return List.of(); // If the prefix entered doesn't exist, we immediately return empty list
        }
        List<Integer> tempList = new ArrayList<>();
        this.findChildren(tempRootNode, tempList);
        return tempList;
    }

    private void findChildren(TrieNode rootNode, List<Integer> tempList) {
        // Try to search all 26 nodes from this prefix and gather the ids
        for (TrieNode node : rootNode.children) {
            // For each node, we would traverse till the leaf node or we encounter no children, and gather all id
            if (node == null) continue;
            if (!node.inventoryIdsAtNode.isEmpty()) tempList.addAll(node.inventoryIdsAtNode);
            findChildren(node, tempList);
        }
    }

    // We would need to have a transactional mechanism here
    public void updateIndex(Inventory inventory) {
        this.deleteFromIndex(inventory);
        this.addToIndex(inventory);
    }

    public void deleteFromIndex(Inventory inventory) {
        // TODO: Impelement update job
    }

    private static class TrieNode {
        List<Integer> inventoryIdsAtNode;
        TrieNode[] children;

        TrieNode() {
            this.inventoryIdsAtNode = new ArrayList<>();
            this.children = new TrieNode[26];
        }

    }
}

