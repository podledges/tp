package medistock.command;

import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.storage.Storage;
import medistock.ui.Ui;

public class EditCommand extends Command {
    private final String oldName;
    private final String newName;
    private final String newUnit;
    private final Integer newMinimumThreshold;

    public EditCommand(String oldName, String newName, String newUnit, Integer newMinimumThreshold) {
        this.oldName = oldName;
        this.newName = newName;
        this.newUnit = newUnit;
        this.newMinimumThreshold = newMinimumThreshold;
    }

    @Override
    public void execute(Inventory inventory, Ui ui, Storage storage, List<String> histories)
            throws MediStockException {
        throw new MediStockException("Edit command is not implemented yet.");
    }
}
