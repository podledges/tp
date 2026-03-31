package medistock.inventory;

import medistock.storage.Storable;

import java.time.LocalDate;

/**
 * Represents a batch of medical inventory items with a unique batch number,
 * quantity, and expiry date. Batches are immutable once created except for
 * quantity.
 */
public class Batch implements Storable {
    private final int batchNumber;
    private int quantity;
    private final LocalDate expiryDate;
    private boolean isExpired = false;

    /**
     * Creates a batch with the specified batch number, quantity and expiry date.
     * The batch number and expiry date are immutable once set.
     *
     * @param batchNumber The unique identifier for this batch.
     * @param quantity    The initial stock count of the batch.
     * @param expiryDate  The expiry date of the batch.
     */
    public Batch(int batchNumber, int quantity, LocalDate expiryDate) {
        this.batchNumber = batchNumber;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    public int getBatchNumber() {
        return batchNumber;
    }

    public int getQuantity() {
        return quantity;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void markExpired() {
        this.isExpired = true;
    }

    public void reduceQuantity(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Quantity to reduce cannot be negative");
        }
        if (quantity > this.quantity) {
            throw new IllegalArgumentException("Quantity to reduce cannot exceed current stock");
        }
        this.quantity -= quantity;
    }

    @Override
    public String toFileFormat() {
        return String.format("%d | %s | %tF", this.batchNumber, this.quantity, this.expiryDate);
    }



}
