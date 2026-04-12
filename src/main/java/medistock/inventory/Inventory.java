package medistock.inventory;

import medistock.exception.MediStockException;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;

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

    /**
     * Constructs an empty Inventory.
     */
    public Inventory() {
        this.items = new LinkedHashMap<>();
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
     * Finds all items whose name contain the specified keyword.
     * The search is case-insensitive.
     *
     * @param keyword The keyword to search for in item names.
     * @return List of items that match the keyword.
     */
    public List<InventoryItem> findItem(String keyword) {
        assert  keyword != null: ASSERT_NAME_NOT_NULL;
        String key = normalizeName(keyword);

        return items.values().stream()
                .filter(item -> item.getName()
                        .toLowerCase()
                        .contains(key))
                .collect(toList());
    }

    public void addBatchToItem(String itemName, Batch batch) throws MediStockException {
        assert itemName != null : ASSERT_NAME_NOT_NULL;
        assert batch != null : "Batch should not be null.";
        InventoryItem item = getItem(itemName);
        item.addBatch(batch);
        item.sortAndMarkExpiredBatches();
        logger.log(Level.INFO, "Loaded/Added batch to existing item: " + item.getName());
    }

    public InventoryItem editItem(String oldName, String newName, String newUnit, Integer newMinimumThreshold)
            throws MediStockException {
        assert oldName != null : ASSERT_NAME_NOT_NULL;

        InventoryItem currentItem = getItem(oldName);
        String updatedName = newName == null ? currentItem.getName() : newName;
        String updatedUnit = newUnit == null ? currentItem.getUnit() : newUnit;
        int updatedMinimumThreshold = newMinimumThreshold == null
                ? currentItem.getMinimumThreshold() : newMinimumThreshold;

        if (currentItem.getName().equals(updatedName)
                && currentItem.getUnit().equals(updatedUnit)
                && currentItem.getMinimumThreshold() == updatedMinimumThreshold) {
            throw new MediStockException("No changes made to product: " + currentItem.getName());
        }

        String oldKey = normalizeName(currentItem.getName());
        String newKey = normalizeName(updatedName);

        if (!oldKey.equals(newKey) && items.containsKey(newKey)) {
            throw new MediStockException("Product already exists: " + updatedName);
        }

        InventoryItem updatedItem = currentItem.copyWithMetadata(updatedName, updatedUnit, updatedMinimumThreshold);
        items.remove(oldKey);
        items.put(newKey, updatedItem);
        return updatedItem;
    }

    /**
     * Removes the item with the given name.
     *
     * @param name The item name.
     * @throws MediStockException If the item does not exist.
     */
    public InventoryItem deleteItem(String name) throws MediStockException {
        if (items.isEmpty()) {
            throw new MediStockException("Unable to delete as inventory is empty!");
        }
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

    /**
     * Removes the item at the specified index in the inventory.
     *
     * @param index The 1-based index of the item to delete.
     * @return The deleted inventory item.
     * @throws MediStockException If the index is out of bounds.
     */
    public InventoryItem deleteItem(int index) throws MediStockException {
        if (items.isEmpty()) {
            throw new MediStockException("Unable to delete as inventory is empty!");
        }
        if (index <= 0 || index > items.size()) {
            throw new MediStockException("Index entered out of bounds! Valid indices: 1 to " + items.size());
        }
        int i = 0;
        InventoryItem deletedItem;
        for (String key : items.keySet()) {
            if (i == index - 1) {
                deletedItem = items.get(key);
                items.remove(key);
                logger.log(Level.INFO, "Deleted item: " + key);
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

    public List<InventoryItem> getActiveBatches() {
        return new ArrayList<>(items.values());
    }

    public int removeAllExpiredBatches() {
        int total = 0;
        for (InventoryItem item : items.values()) {
            total += item.removeExpiredBatches();
        }
        return total;
    }

    public List<InventoryItem> getExpiredBatches() {
        List<InventoryItem> expired = new ArrayList<>();
        for (InventoryItem item : items.values()) {
            if (!item.getExpiredBatches().isEmpty()) {
                expired.add(item);
            }
        }
        return expired;
    }

    /**
     * Normalizes an item name by trimming whitespace and converting to lowercase.
     * Used internally to ensure consistent key lookups.
     *
     * @param name The item name to normalize.
     * @return The normalized name.
     */
    private String normalizeName(String name) {
        assert name != null : ASSERT_NAME_NOT_NULL;
        return name.trim().toLowerCase();
    }
}
