package medistock.command;

import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;
import medistock.ui.Ui;

/**
 * Command to delete an inventory item by its name.
 */
public class DeleteCommandName extends Command {
    private final String name;

    public DeleteCommandName(String name) {
        this.name = name;
    }

    @Override
    public void execute(Inventory inventory, Ui ui, List<String> histories) throws MediStockException {
        InventoryItem deletedItem = inventory.deleteItem(name);
        ui.printDelete(deletedItem);
        histories.add(toHistoryString());
    }

    public String toHistoryString() {
        return "Deleted '" + name + "'.";
    }
}
