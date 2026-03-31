package medistock.command;

import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.storage.Storage;
import medistock.ui.Ui;

/**
 * Command to display the list of available commands and their usage.
 */
public class HelpCommand extends Command {

    @Override
    public void execute(Inventory inventory, Ui ui, Storage storage, List<String> histories) throws MediStockException {
        ui.printCommandList();
    }
}
