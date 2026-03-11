package medistock.inventory;

import medistock.exception.MediStockException;

import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private final Map<String, InventoryItem> items = new HashMap<>();

    public void addItem(InventoryItem item) throws MediStockException {
        String key = normalizeName(item.getName());
        if (items.containsKey(key)) {
            throw new MediStockException("Product already exists: " + item.getName());
        }
        items.put(key, item);
    }

    private String normalizeName(String name) {
        return name.toLowerCase();
    }
}
