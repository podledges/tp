package medistock.command;

import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;
import medistock.ui.Ui;

/**
 * Command to remove expired batches from the inventory.
 * Can remove expired batches from all items or from a specific item by name.
 */
public class RemoveExpiredCommand extends Command {
    private final String name;

    public RemoveExpiredCommand() {
        this.name = null;
    }

    public RemoveExpiredCommand(String name) {
        this.name = name;
    }

    /**
     * Executes the remove expired command by removing expired batches from inventory.
     * If no name is specified, removes expired batches from all items.
     * If a name is specified, removes expired batches only from that item.
     *
     * @param inventory The inventory to remove expired batches from.
     * @param ui The UI for displaying the result.
     * @param histories The command history list.
     * @throws MediStockException If the specified item is not found.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, List<String> histories) throws MediStockException {
        if (name == null) {
            int count = inventory.removeAllExpiredBatches();
            ui.printRemoveExpired(count);
            histories.add(toHistoryStringAll(count));
        } else {
            if (!inventory.hasItem(name)) {
                throw new MediStockException(
                        "Product not found: " + name);
            }
            InventoryItem item = inventory.getItem(name);
            int count = item.removeExpiredBatches();
            ui.printRemoveExpired(name, count);
            histories.add(toHistoryString(count));
        }
    }

    /**
     * Generates a history string for removing expired batches from all items.
     *
     * @param count The number of expired batches removed.
     * @return A formatted history string.
     */
    public String toHistoryStringAll(int count) {
        return "Removed " + count + " expired batch(es) from inventory.";
    }

    /**
     * Generates a history string for removing expired batches from a specific item.
     *
     * @param count The number of expired batches removed.
     * @return A formatted history string.
     */
    public String toHistoryString(int count) {
        return "Removed " + count + " expired batch(es) from '" + name + "'.";
    }
}
