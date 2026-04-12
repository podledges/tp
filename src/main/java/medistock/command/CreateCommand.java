package medistock.command;

import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;
import medistock.storage.Storage;
import medistock.ui.Ui;

import java.io.IOException;

/**
 * Command to create a new inventory item with specified name, unit, and minimum threshold.
 */
public class CreateCommand extends Command {
    private final String name;
    private final String unit;
    private final int minimumThreshold;

    public CreateCommand(String name, String unit, int minimumThreshold) {
        this.name = name;
        this.unit = unit;
        this.minimumThreshold = minimumThreshold;
    }

    @Override
    public void execute(Inventory inventory, Ui ui, Storage storage, List<String> histories) throws MediStockException {
        try {
            InventoryItem item = new InventoryItem(name, unit, minimumThreshold);
            inventory.addItem(item);
            storage.saveToFile(inventory);
            ui.printCreate(name, unit, minimumThreshold);
            histories.add(toHistoryString());
        } catch (IOException e) {
            throw new MediStockException("Failed to save to file: " + e.getMessage());
        }
    }

    public String toHistoryString() {
        return "Created '" + name + "' of '" + unit + "' unit with minimum threshold of " + minimumThreshold + ".";
    }
}
