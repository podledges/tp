package medistock.inventory;

import medistock.exception.MediStockException;
import medistock.storage.Storable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents an item in the medical inventory with a name, unit,
 * minimum threshold, total quantity, and its batches.
 */
public class InventoryItem implements Storable {

    private final String name;
    private final String unit;
    private final int minimumThreshold;
    private int batchQuantity;
    private final List<Batch> batches;

    /**
     * Creates an inventory item with the specified name, unit, and minimum
     * threshold.
     * The item starts with zero quantity and an empty list of batches.
     *
     * @param name             The name of the medical item.
     * @param unit             The unit of measurement (e.g., "tablets", "ml",
     *                         "mg").
     * @param minimumThreshold The minimum stock level before item is considered low
     *                         stock.
     */
    public InventoryItem(String name, String unit, int minimumThreshold) {
        this.name = name;
        this.unit = unit;
        this.minimumThreshold = minimumThreshold;
        this.batchQuantity = 0;
        this.batches = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    /**
     * Returns the minimum stock threshold of the item.
     *
     * @return The minimum threshold.
     */
    public int getMinimumThreshold() {
        return minimumThreshold;
    }

    /**
     * Returns the amount of batches insides the item's stock
     *
     * @return the quanity of batches
     */
    public int getBatchQuantity() {
        int count = 0;
        for (Batch batch : batches) {
            if (!batch.isExpired()) {
                count++;
            }
        }
        return count;
    }

    public int getTotalBatchQuantity() {
        return batches.size();
    }

    /**
     * Returns the earliest expiry date
     *
     * @return the earliest expiry date
     */
    public LocalDate getEarliestExpiry() throws MediStockException {
        LocalDate earliestDate = null;
        for (Batch batch : batches) {
            if (!batch.isExpired()) {
                if (earliestDate == null || batch.getExpiryDate().isBefore(earliestDate)) {
                    earliestDate = batch.getExpiryDate();
                }
            }
        }
        if (earliestDate == null) {
            throw new MediStockException("There is no recorded stock for this item");
        }
        return earliestDate;
    }

    /**
     * Returns the total quantity of this item across all batches.
     *
     * @return The total quantity.
     */
    public int getQuantity() {
        int totalQuantity = 0;
        for (Batch batch : batches) {
            if (!batch.isExpired()) {
                totalQuantity += batch.getQuantity();
            }
        }
        return totalQuantity;
    }

    public String getStockStatus() {
        int quantity = getQuantity();
        if (isLowStock()) {
            return "Critical";
        } else {
            return "Healthy";
        }
    }

    /**
     * Stores the newly added batch and updates the total quantity.
     * The batch is added to the internal list and its quantity is added
     * to the item's total quantity.
     *
     * @param batch The batch to add to this inventory item.
     */
    public void addBatch(Batch batch) {
        batches.add(batch);
    }

    public InventoryItem copyWithMetadata(String name, String unit, int minimumThreshold) {
        InventoryItem updatedItem = new InventoryItem(name, unit, minimumThreshold);
        for (Batch batch : batches) {
            updatedItem.addBatch(batch);
        }
        return updatedItem;
    }

    /**
     * Withdraws the specified quantity from this item's batches.
     * Batches are processed in order of earliest expiry date first.
     * Expired batches (expiry date before today) are automatically removed.
     * If the quantity spans multiple batches, earlier batches are fully depleted
     * before moving to the next. Batches that reach zero quantity are removed.
     *
     * @param quantity The amount to withdraw.
     * @throws MediStockException If the total available (non-expired) quantity
     *                            is less than the requested amount.
     */
    public void withdraw(int quantity) throws MediStockException {
        sortAndMarkExpiredBatches();

        // Check if enough stock remains after marking expired batches
        if (getQuantity() < quantity) {
            throw new MediStockException("Insufficient stock for " + name
                    + ". Available: " + getQuantity() + ", Requested: " + quantity);
        }

        // Deduct from non-expired batches in order (earliest expiry first)
        int remaining = quantity;
        int i = 0;
        while (remaining > 0 && i < batches.size()) {
            Batch batch = batches.get(i);
            if (batch.isExpired()) {
                i++;
                continue;
            }
            int available = batch.getQuantity();
            if (available <= remaining) {
                remaining -= available;
                batches.remove(i);
            } else {
                batch.reduceQuantity(remaining);
                remaining = 0;
            }
        }
    }

    /**
     * Sorts batches by expiry date (earliest first) and removes any expired
     * batches.
     * A batch is considered expired if its expiry date is not after today.
     */
    public void sortAndMarkExpiredBatches() {
        LocalDate today = LocalDate.now();

        // Earliest first
        batches.sort(Comparator.comparing(Batch::getExpiryDate));

        // Mark and warn about all expired batches
        for (Batch batch : batches) {
            if (batch.getExpiryDate().isBefore(today)) {
                if (!batch.isExpired()) {
                    batch.markExpired();
                }
                System.out.println("Please remove expired batch "
                        + batch.getBatchNumber() + " (expired: "
                        + batch.getExpiryDate() + ")");
            }
        }
    }

    @Override
    public String toFileFormat() {
        String descriptionLine = String.format("Item: %s (%s) | %d", this.name, this.unit, this.minimumThreshold);
        return descriptionLine + System.lineSeparator() + "[Batches]";
    }

    /**
     * Returns whether the current quantity is below the minimum threshold.
     *
     * @return true if stock is low, false otherwise.
     */
    public boolean isLowStock() {
        return getQuantity() < minimumThreshold;
    }

    public int removeExpiredBatches() {
        sortAndMarkExpiredBatches();
        List<Batch> expired = getExpiredBatches();
        int count = expired.size();
        batches.removeAll(expired);
        return count;
    }

    public List<Batch> getExpiredBatches() {
        List<Batch> expired = new ArrayList<>();
        for (Batch batch : batches) {
            if (batch.isExpired()) {
                expired.add(batch);
            }
        }
        return expired;
    }

    public List<Batch> getActiveBatches() {
        List<Batch> active = new ArrayList<>();
        for (Batch batch : batches) {
            if (!batch.isExpired()) {
                active.add(batch);
            }
        }
        return active;
    }
}
