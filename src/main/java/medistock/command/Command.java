package medistock.command;

import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.ui.Ui;

/**
 * Abstract base class for all commands in the MediStock application.
 * Defines the common interface for command execution.
 */
public abstract class Command {

    public abstract void execute(Inventory inventory, Ui ui,List<String> histories)
                    throws MediStockException;

    public boolean isExit() {
        return false;
    };
}
