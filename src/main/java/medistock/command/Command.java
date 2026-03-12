package medistock.command;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.ui.Ui;

public abstract class Command {

    public abstract void execute(Inventory inventory, Ui ui) throws MediStockException;

    public boolean isExit() {
        return false;
    };
}
