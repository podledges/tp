package medistock.command;

import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;
import medistock.ui.Ui;

public class RemoveExpiredCommand extends Command {
    private final String name;

    public RemoveExpiredCommand() {
        this.name = null;
    }

    public RemoveExpiredCommand(String name) {
        this.name = name;
    }

    @Override
    public void execute(Inventory inventory, Ui ui, List<String> histories) throws MediStockException {
        if (name == null) {
            int count = inventory.removeAllExpiredBatches();
            ui.printRemoveExpired(count);
        } else {
            if (!inventory.hasItem(name)) {
                throw new MediStockException("Product not found: " + name);
            }
            InventoryItem item = inventory.getItem(name);
            int count = item.removeExpiredBatches();
            ui.printRemoveExpired(name, count);
        }
    }
}
