package medistock.command;

import java.util.List;

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
    public void execute(Inventory inventory, Ui ui, List<String> histories) throws MediStockException {
        InventoryItem item = new InventoryItem(name, unit, minimumThreshold);
        inventory.addItem(item);
        Ui.printCreate(name, unit, minimumThreshold);
        histories.add(toHistoryString());
    }

    public String toHistoryString() {
        return "Created '" + name + "' of '" + unit + "' unit with minimum threshold of " + minimumThreshold + ".";
    }
}
