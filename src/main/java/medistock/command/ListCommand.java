package medistock.command;

import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.ui.Ui;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command to list all items in the inventory.
 */
public class ListCommand extends Command {
    private static final Logger logger = Logger.getLogger(ListCommand.class.getName());

    /**
     * Executes the list command by displaying all inventory items.
     *
     * @param inventory The inventory to display.
     * @param ui The UI for displaying the inventory.
     * @param histories The History of stock activities.
     * @throws MediStockException If an error occurs during execution.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, List<String> histories)
                    throws MediStockException {
        logger.log(Level.INFO, "Executing list command");
        ui.showInventoryList(inventory);
    }
}
