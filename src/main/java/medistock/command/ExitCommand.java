package medistock.command;

import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.ui.Ui;

/**
 * Command to exit the MediStock application.
 */
public class ExitCommand extends Command {

    public void execute(Inventory inventory, Ui ui, List<String> histories) throws MediStockException {
        ui.printExitMessage();
    }

    @Override
    public boolean isExit() {
        return true;
    }
}
