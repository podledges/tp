package medistock.ui;

import medistock.exception.MediStockException;
import medistock.inventory.Batch;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

/**
 * Handles all user interactions including input and output.
 */
public class Ui {

    public static final String BATCH_FORMAT = "Format: batch n/NAME q/QUANTITY d/EXPIRY_DATE(YYYY-MM-DD)";

    public static final String CREATE_FORMAT = "Format: create n/NAME u/UNIT min/THRESHOLD";

    public static final String EDIT_FORMAT =
            "Format: edit o/OLD_NAME [n/NEW_NAME] [u/NEW_UNIT] [min/NEW_THRESHOLD]";

    public static final String FIND_FORMAT = "Format: find <keyword>";

    public static final String DELETE_FORMAT = "Format: delete 'n/NAME' or 'i/INDEX'";

    public static final String WITHDRAW_FORMAT = "Format: withdraw n/NAME q/QUANTITY";

    public static final String REMOVE_EXPIRED_FORMAT =
            "Format: remove-expired or remove-expired n/NAME";

    public static final String ERROR_MISSING_KEYWORD = "Missing name of the item you want to find.";

    private static final String EXIT_MESSAGE = "Inventory saved\nThank you for using MediStock, have a nice day!";

    private static final String WELCOME_MESSAGE =
            "Welcome to medistock\nType <help> to see the list of available commands.";

    private final Scanner scanner;

    /**
     * Creates an Ui instance with a Scanner for reading user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads the next command from the user. Returns the full string entered into
     * the console.
     *
     * @return The user's input line.
     */
    public String getInput() {
        return scanner.nextLine();
    }

    public static void printLine() {
        System.out.println("____________________________________________________________");
    }

    public static void greet() {
        System.out.println(Logo.LOGO);
        printLine();
        System.out.println(WELCOME_MESSAGE);
        printLine();
    }

    /**
     * Prints an error message.
     *
     * @param e the error message printed
     */
    public void printError(String e) {
        printLine();
        System.out.println(e);
        printLine();
    }

    public void printExitMessage() {
        printLine();
        System.out.println(EXIT_MESSAGE);
        printLine();
    }

    public void printNewLine() {
        System.out.println("");
    }

    /**
     * Shows the inventory list to the user.
     *
     * @param inventory The inventory to display.
     */
    public void showInventoryList(Inventory inventory) throws MediStockException {
        if (inventory.getSize() == 0) {
            printEmptyInventoryMessage();
        } else {
            int itemIndex = 1;
            int maxIndex = inventory.getSize();
            printLine();
            System.out.println("Current Active Pharmaceutical Inventory:");
            if (inventory.getActiveBatches().isEmpty()) {
                System.out.println("No active batches found.");
            } else {
                for (InventoryItem item : inventory.getActiveBatches()) {
                    printActiveItemDetails(inventory, item);
                    if (itemIndex < maxIndex) {
                        printNewLine();
                    }
                    itemIndex = itemIndex + 1;
                }
            }
            printLine();
            System.out.println("Current Expired Pharmaceutical Inventory:");
            if (inventory.getExpiredBatches().isEmpty()) {
                System.out.println("No expired batches found.");
            } else {
                for (InventoryItem item : inventory.getExpiredBatches()) {
                    printExpiredItemDetails(inventory, item);
                    if (itemIndex < maxIndex) {
                        printNewLine();
                    }
                    itemIndex = itemIndex + 1;
                }
            }
            printLine();
        }
    }

    /**
     * Displays a list of items that contains the keyword
     *
     * @param matchedItems The items that contain the desired keyword
     */
    public void showFindList(Inventory inventory, List<InventoryItem> matchedItems) throws MediStockException {
        if (matchedItems.isEmpty()) {
            printLine();
            System.out.println("No matches found!");
            printLine();
        } else {
            printLine();
            System.out.println("Here are the matching items in your inventory:");
            for (InventoryItem item : matchedItems) {
                printItemDetails(inventory, item);
            }
            printLine();
        }
    }

    /**
     * Shows the history to the user.
     *
     * @param histories The history to display.
     */
    public void showHistory(List<String> histories) throws MediStockException {
        if (histories.isEmpty()) {
            printEmptyHistoryMessage();
        } else {
            int itemIndex = 1;
            int maxIndex = histories.size();
            printLine();
            System.out.println("History of Stocks:");
            for (java.lang.String commandText : histories) {
                System.out.println(itemIndex + ". " + commandText);
                itemIndex++;
            }
            printLine();
        }
    }

    public int getItemIndex(Inventory inventory, String searchName) {
        int itemCount = 1;
        for (InventoryItem item : inventory.getAllItems()) {
            String foundName = item.getName();
            if (searchName.equals(item.getName())) {
                return itemCount;
            }
            itemCount = itemCount + 1;
        }
        return -1;
    }

    private static void printEmptyInventoryMessage() {
        printLine();
        System.out.println("Your inventory is empty.");
        printLine();
    }

    private static void printEmptyHistoryMessage() {
        printLine();
        System.out.println("Currently no history recorded.");
        printLine();
    }

    public static void printCreate(String name, String unit, int minimumThreshold) {
        printLine();
        System.out.printf("Product created:" + name + " (" + unit + ")\n" + "Minimum threshold: "
                + minimumThreshold + "%n");
        printLine();
    }

    public void printEdit(String oldName, InventoryItem updatedItem) {
        printLine();
        System.out.printf("Product updated: %s -> %s (%s)%n",
                oldName, updatedItem.getName(), updatedItem.getUnit());
        System.out.printf("Minimum threshold: %d%n", updatedItem.getMinimumThreshold());
        printLine();
    }

    public void printDelete(InventoryItem deletedItem) {
        printLine();
        System.out.printf("Product deleted:" + deletedItem.getName() + " (" + deletedItem.getUnit() + ")\n");
        printLine();
    }

    public void printRemoveExpired(int count) {
        printLine();
        if (count == 0) {
            System.out.println("No expired batches found.");
        } else {
            System.out.printf("Removed %d expired batch(es) "
                    + "from all items.%n", count);
        }
        printLine();
    }

    public void printRemoveExpired(String name, int count) {
        printLine();
        if (count == 0) {
            System.out.printf("No expired batches found for "
                    + "%s.%n", name);
        } else {
            System.out.printf("Removed %d expired batch(es) "
                    + "from %s.%n", count, name);
        }
        printLine();
    }

    public void printSpacing() {
        System.out.print("    ");
    }

    public static void printItemDetails(InventoryItem item) {
        String unit = item.getUnit();
        int quantity = item.getQuantity();
        System.out.println(quantity + " " + unit);
    }

    public void printItemDetails(Inventory inventory, InventoryItem item) throws MediStockException {
        String itemName = item.getName();
        String unit = item.getUnit();
        item.sortAndMarkExpiredBatches();

        System.out.printf("%d. %s (Min: %d)%n",
                getItemIndex(inventory, itemName),
                itemName, item.getMinimumThreshold());

        List<Batch> activeBatches = item.getActiveBatches();
        if (!activeBatches.isEmpty()) {
            printSpacing();
            System.out.println("Active Batches:");
            for (Batch batch : activeBatches) {
                printSpacing();
                printSpacing();
                System.out.printf("Batch %d: %d %s, Exp: %tF%n",
                        batch.getBatchNumber(), batch.getQuantity(),
                        unit, batch.getExpiryDate());
            }
        }

        List<Batch> expiredBatches = item.getExpiredBatches();
        if (!expiredBatches.isEmpty()) {
            printSpacing();
            System.out.println("Expired Batches:");
            for (Batch batch : expiredBatches) {
                printSpacing();
                printSpacing();
                System.out.printf("Batch %d: %d %s, Exp: %tF%n",
                        batch.getBatchNumber(), batch.getQuantity(),
                        unit, batch.getExpiryDate());
            }
        }

        printSpacing();
        System.out.printf("Total (active): %d %s%n", item.getQuantity(), unit);
        printSpacing();
        System.out.printf("Status: %s%n", item.getStockStatus());
    }

    public void printActiveItemDetails(Inventory inventory,
            InventoryItem item) throws MediStockException {
        String itemName = item.getName();
        String unit = item.getUnit();

        System.out.printf("%d. %s (Min: %d)%n",
                getItemIndex(inventory, itemName),
                itemName, item.getMinimumThreshold());

        for (Batch batch : item.getActiveBatches()) {
            printSpacing();
            System.out.printf("Batch %d: %d %s, Exp: %tF%n",
                    batch.getBatchNumber(), batch.getQuantity(),
                    unit, batch.getExpiryDate());
        }

        printSpacing();
        System.out.printf("Total: %d %s%n",
                item.getQuantity(), unit);
        printSpacing();
        System.out.printf("Status: %s%n", item.getStockStatus());
    }

    public void printExpiredItemDetails(Inventory inventory,
            InventoryItem item) throws MediStockException {
        String itemName = item.getName();
        String unit = item.getUnit();

        System.out.printf("%d. %s%n",
                getItemIndex(inventory, itemName), itemName);

        for (Batch batch : item.getExpiredBatches()) {
            printSpacing();
            System.out.printf("Batch %d: %d %s, Exp: %tF%n",
                    batch.getBatchNumber(), batch.getQuantity(),
                    unit, batch.getExpiryDate());
        }
    }

    public void printBatch(Inventory inventory, InventoryItem item, int quantity, LocalDate date)
            throws MediStockException {

        String itemName = item.getName();

        System.out.printf("Batch of %d %s, expiring on %3$tF %n has been successfully to" +
                " the inventory!%n", quantity, itemName, date);
        printLine();
        System.out.println(String.format("Stock of %s is now:", itemName));
        printItemDetails(inventory, item);
        printLine();
    }

    public static void printWithdraw(int quantity, InventoryItem item) {
        System.out.printf("Withdrawn %d %s from inventory.%n", quantity, item.getName());
        printLine();
        System.out.printf("Stock of %s is now: ", item.getName());
        printItemDetails(item);
        printLine();
    }

    public void printCommandList() {
        printLine();
        System.out.println("Available commands:");
        System.out.println("1. list");
        System.out.println("2. create " + CREATE_FORMAT);
        System.out.println("3. edit " + EDIT_FORMAT);
        System.out.println("4. delete " + DELETE_FORMAT);
        System.out.println("5. batch " + BATCH_FORMAT);
        System.out.println("6. withdraw " + WITHDRAW_FORMAT);
        System.out.println("7. find " + FIND_FORMAT);
        System.out.println("8. remove-expired " + REMOVE_EXPIRED_FORMAT);
        System.out.println("9. history");
        System.out.println("10. exit");
        printLine();
    }
}
