package medistock.command;

import medistock.exception.MediStockException;
import medistock.inventory.Batch;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;
import medistock.ui.Ui;

import java.time.LocalDate;
import java.util.List;

/**
 * Command to add a new batch to an existing inventory item.
 */
public class BatchCommand extends Command {
    private final String name;
    private final int quantity;
    private final LocalDate expiryDate;

    public BatchCommand(String name, int quantity, LocalDate expiryDate) {
        this.name = name;
        this.quantity = quantity;
        this.expiryDate = expiryDate;
    }

    @Override
    public void execute(Inventory inventory, Ui ui, List<String> histories) throws MediStockException {
        if (!inventory.hasItem(this.name)) {
            throw new MediStockException("Item '" + this.name + "' does not exist in inventory." +
                    " Please add the item first.");
        }
        InventoryItem item = inventory.getItem(name);
        item.sortAndMarkExpiredBatches();
        if (expiryDate.isBefore(LocalDate.now())) {
            String errorMessage = "This batch is already expired (" + expiryDate + ").";
            if (!ui.wasMessageConfirm(errorMessage)) {
                System.out.println("Batch not added.");
                ui.printAbortCommand();
                return;
            }
        }
        int batchNumber = item.getAndIncrementBatchNumber();
        Batch newBatch = new Batch(batchNumber, quantity, expiryDate);
        item.addBatch(newBatch);
        item.sortAndMarkExpiredBatches();
        ui.printBatch(inventory, item, quantity, expiryDate);
        histories.add(toHistoryString(item.getUnit()));
    }

    public String toHistoryString(String unit) {
        return "Added a batch of " + quantity + " " + unit + " of " + name + " with expiry date "
                + expiryDate + ".";
    }
}

