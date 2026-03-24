package medistock.command;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;
import medistock.ui.Ui;

/**
 * Command to delete an inventory item by its index in the list.
 */
public class DeleteCommandIndex extends Command {
    private final int index;

    public DeleteCommandIndex(int index) {
        this.index = index;
    }

    @Override
    public void execute(Inventory inventory, Ui ui) throws MediStockException {
        InventoryItem deletedItem = inventory.deleteItem(index);
        ui.printDelete(deletedItem);
    }
}
