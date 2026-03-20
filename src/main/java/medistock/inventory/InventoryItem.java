package medistock.inventory;

import medistock.exception.MediStockException;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents an item in the medical inventory with a name, unit,
 * minimum threshold, total quantity, and its batches.
 */
public class InventoryItem {

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
        if (batches.isEmpty()) {
            return 0;
        } else {
            return batches.size();
        }
    }

    /**
     * Returns the earliest expiry date
     *
     * @return the earliest expiry date
     */
    public LocalDate getEarliestExpiry() throws MediStockException {
        if (batches.isEmpty()) {
            throw new MediStockException("There is no recorded stock for this item");
        }

        LocalDate earliestDate = batches.get(0).getExpiryDate();
        for (Batch batch : batches) {
            if (earliestDate.isBefore(batch.getExpiryDate())) {
                earliestDate = batch.getExpiryDate();
            }
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
            totalQuantity += batch.getQuantity();
        }

        return totalQuantity;
    }

    public String  getStockStatus() {
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
        sortAndRemoveExpiredBatches();

        // Check if enough stock remains after removing expired batches
        if (getQuantity() < quantity) {
            throw new MediStockException("Insufficient stock for " + name
                    + ". Available: " + getQuantity() + ", Requested: " + quantity);
        }

        // Deduct from batches in order (earliest expiry first)
        int remaining = quantity;
        while (remaining > 0) {
            Batch batch = batches.get(0);
            int available = batch.getQuantity();
            if (available <= remaining) {
                remaining -= available;
                batches.remove(0);
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
    private void sortAndRemoveExpiredBatches() {
        // Earliest first
        batches.sort(Comparator.comparing(Batch::getExpiryDate));

        // Remove all expired batches
        int i = 0;
        while (i < batches.size()) {
            if (batches.get(i).isExpired()) {
                System.out.println("Please remove expired batch "
                        + batches.get(i).getBatchNumber() + " (Expired: "
                        + batches.get(i).getExpiryDate() + ")");
            } else {
                i++;
            }
            /// Im sorry but Podles has broken the code, as I think we should not be removing expired while withdrawing
            //Todo: May you pls update the method to handle this.
            /// If you want Podles to update this instead let Podles know
        }
    }

    /**
     * Returns whether the current quantity is below the minimum threshold.
     *
     * @return true if stock is low, false otherwise.
     */
    public boolean isLowStock() {
        return getQuantity() < minimumThreshold;
    }

    public int getThreshold() {
        return minimumThreshold;
    }
}


