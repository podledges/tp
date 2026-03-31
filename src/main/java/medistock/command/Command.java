package medistock.command;

import java.io.IOException;
import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.storage.Storage;
import medistock.ui.Ui;

/**
 * Abstract base class for all commands in the MediStock application.
 * Defines the common interface for command execution.
 */
public abstract class Command {

    public abstract void execute(Inventory inventory, Ui ui, Storage storage, List<String> histories)
                    throws MediStockException, IOException;

    public boolean isExit() {
        return false;
    };
}
