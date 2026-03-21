package medistock.inventory;

import medistock.exception.MediStockException;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Represents the full inventory of tracked medical items.
 * Items are stored by normalized name (trimmed and lowercase) to prevent
 * duplicates and ensure consistent lookups.
 */
public class Inventory {
    public static final String ASSERT_NAME_NOT_NULL =
            "Name should not be null.";
    private static final Logger logger = Logger.getLogger(Inventory.class.getName());
    
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
        assert item != null: "Item must not be null.";
        assert item.getName() != null: ASSERT_NAME_NOT_NULL;
        
        String key = normalizeName(item.getName());
        if (items.containsKey(key)) {
            logger.log(Level.WARNING, "Attempted to add duplicate item: " + item.getName());
            throw new MediStockException("Product already exists: " + item.getName());
        }
        items.put(key, item);
        logger.log(Level.INFO, "Added item: " + item.getName() + " (" + item.getUnit() + ")");
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
        assert name != null: ASSERT_NAME_NOT_NULL;
        
        String key = normalizeName(name);
        if (!items.containsKey(key)) {
            logger.log(Level.WARNING, "Attempted to get non-existent item: " + name);
            throw new MediStockException("Product not found: " + name);
        }
        logger.log(Level.FINE, "Retrieved item: " + name);
        return items.get(key);
    }

    /**
     * Removes the item with the given name.
     *
     * @param name The item name.
     * @throws MediStockException If the item does not exist.
     */
    public InventoryItem deleteItem(String name) throws MediStockException {
        String key = normalizeName(name);
        if (!items.containsKey(key)) {
            logger.log(Level.WARNING, "Attempted to delete non-existent item: " + name);
            throw new MediStockException("Product not found: " + name);
        }
        InventoryItem deletedItem = items.get(key);
        items.remove(key);
        logger.log(Level.INFO, "Deleted item: " + name);
        return deletedItem;
    }

    public InventoryItem deleteItem(int index) throws MediStockException {
        if (index <= 0 || index > items.size()) {
            throw new MediStockException("Index entered out of bounds! Valid indices: 1 to " + items.size());
        }
        int i = 0;
        InventoryItem deletedItem;
        for (String key : items.keySet()) {
            if (i == index - 1) {
                deletedItem = items.get(key);
                items.remove(key);
                return deletedItem;
            }
            i++;
        }
        throw new MediStockException("Error with index!");
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
        assert name != null : ASSERT_NAME_NOT_NULL;
        return name.trim().toLowerCase();
    }
}
