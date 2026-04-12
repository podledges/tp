package medistock.command;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;
import medistock.ui.Ui;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Command to find a stock item in the inventory.
 */
public class FindCommand extends Command {
    private static final Logger logger = Logger.getLogger(FindCommand.class.getName());
    private final String keyword;

    /**
     * Creates a FindCommand with the specified keyword.
     *
     * @param keyword The keyword to search for in task descriptions.
     */
    public FindCommand(String keyword) {
        this.keyword = keyword;
    }

    /**
     * Executes the find command by searching for items matching the keyword
     * and displaying the results.
     *
     * @param inventory The inventory to search in.
     * @param ui The UI for displaying the search results.
     * @throws MediStockException If an error occurs during execution.
     */
    @Override
    public void execute(Inventory inventory, Ui ui, List<String> histories) throws MediStockException {
        logger.log(Level.INFO, "Executing find command with keyword: " + keyword);
        
        List<InventoryItem> matchedItems = inventory.findItem(keyword);
        
        logger.log(Level.FINE, "Search returned " + matchedItems.size() + " matching item(s)");
        ui.showFindList(inventory, matchedItems);
        
        logger.log(Level.INFO, "Find command completed successfully");
    }
}
