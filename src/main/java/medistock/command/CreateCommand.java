package medistock.command;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;
import medistock.ui.Ui;

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
    public void execute(Inventory inventory, Ui ui) throws MediStockException {
        InventoryItem item = new InventoryItem(name, unit, minimumThreshold);
        inventory.addItem(item);
        ui.printCreate(name,unit,minimumThreshold);
    }
}
