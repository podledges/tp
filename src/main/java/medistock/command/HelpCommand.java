package medistock.command;

import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.ui.Ui;

/**
 * Command to display the list of available commands and their usage.
 */
public class HelpCommand extends Command {

    @Override
    public void execute(Inventory inventory, Ui ui, List<String> histories) throws MediStockException {
        ui.printCommandList();
    }
}
