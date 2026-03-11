package medistock.inventory;

import medistock.exception.MediStockException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Represents the full inventory of tracked medical items.
 * Items are stored by normalized name (trimmed and lowercase) to prevent
 * duplicates and ensure consistent lookups.
 */
public class Inventory {
    private final Map<String, InventoryItem> items;

    public Inventory() {
        this.items = new HashMap<>();
    }

    /**
     * Adds a new inventory item.
     *
     * @param item The item to add.
     * @throws MediStockException If an item with the same name already exists.
     */
    public void addItem(InventoryItem item) throws MediStockException {
        String key = normalizeName(item.getName());
        if (items.containsKey(key)) {
            throw new MediStockException("Product already exists: " + item.getName());
        }
        items.put(key, item);
    }

    /**
     * Returns whether an item with the given name exists.
     *
     * @param name The item name.
     * @return true if the item exists, false otherwise.
     */
    public boolean hasItem(String name) {
        return items.containsKey(normalizeName(name));
    }

    /**
     * Returns the item with the given name.
     *
     * @param name The item name.
     * @return The matching inventory item.
     * @throws MediStockException If the item does not exist.
     */
    public InventoryItem getItem(String name) throws MediStockException {
        String key = normalizeName(name);
        if (!items.containsKey(key)) {
            throw new MediStockException("Product not found: " + name);
        }
        return items.get(key);
    }

    /**
     * Removes the item with the given name.
     *
     * @param name The item name.
     * @throws MediStockException If the item does not exist.
     */
    public void removeItem(String name) throws MediStockException {
        String key = normalizeName(name);
        if (!items.containsKey(key)) {
            throw new MediStockException("Product not found: " + name);
        }
        items.remove(key);
    }

    /**
     * Returns all items in the inventory.
     *
     * @return A collection of inventory items.
     */
    public Collection<InventoryItem> getAllItems() {
        return items.values();
    }

    /**
     * Returns the number of tracked inventory items.
     *
     * @return Number of items.
     */
    public int getSize() {
        return items.size();
    }

    private String normalizeName(String name) {
        return name.trim().toLowerCase();
    }
}
