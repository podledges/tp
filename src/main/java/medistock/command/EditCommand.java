package medistock.command;

import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;
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
    public void execute(Inventory inventory, Ui ui, List<String> histories)
            throws MediStockException {
        InventoryItem updatedItem = inventory.editItem(oldName, newName, newUnit, newMinimumThreshold);
        ui.printEdit(oldName, updatedItem);
        histories.add(toHistoryString(updatedItem));
    }

    public String toHistoryString(InventoryItem updatedItem) {
        return "Edited '" + oldName + "' to '" + updatedItem.getName()
                + "' (" + updatedItem.getUnit()
                + ") with minimum threshold of " + updatedItem.getMinimumThreshold() + ".";
    }
}
