package medistock.command;

import java.io.IOException;
import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;
import medistock.storage.Storage;
import medistock.ui.Ui;

/**
 * Command to withdraw a specified quantity of an inventory item.
 */
public class WithdrawCommand extends Command {
    private final String name;
    private final int quantity;

    public WithdrawCommand(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    @Override
    public void execute(Inventory inventory, Ui ui, Storage storage,  List<String> histories)
            throws MediStockException, IOException {
        InventoryItem item = inventory.getItem(name);
        item.withdraw(quantity);
        ui.printWithdraw(quantity, item);
        histories.add(toHistoryString(item.getUnit()));
        storage.saveToFile(inventory);
    }

    public String toHistoryString(String unit) {
        return "Withdrawn " + quantity + " " + unit + " of '" + name + "'.";
    }
}
