package medistock.command;

import java.io.IOException;
import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;
import medistock.storage.Storage;
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
    public void execute(Inventory inventory, Ui ui, Storage storage, List<String> histories)
                    throws MediStockException, IOException {
        InventoryItem deletedItem = inventory.deleteItem(name);
        ui.printDelete(deletedItem);
        histories.add(toHistoryString());
        storage.saveToFile(inventory);
    }

    public String toHistoryString() {
        return "Deleted '" + name + "'.";
    }
}
