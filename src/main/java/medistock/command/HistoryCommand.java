package medistock.command;

import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.storage.Storage;
import medistock.ui.Ui;

public class HistoryCommand extends Command {

    /**
     * Executes the list command by displaying all inventory items.
     *
     * @param inventory The inventory to display.
     * @param ui The UI for displaying the inventory.
     * @throws MediStockException If an error occurs during execution.
     */

    @Override
    public void execute(Inventory inventory, Ui ui, Storage storage, List<String> histories)
                    throws MediStockException {
        ui.showHistory(histories);
    }
}
