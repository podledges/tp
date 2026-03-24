package medistock.command;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.ui.Ui;

/**
 * Command to display the list of available commands and their usage.
 */
public class HelpCommand extends Command {

    @Override
    public void execute(Inventory inventory, Ui ui) throws MediStockException {
        ui.printCommandList();
    }
}
