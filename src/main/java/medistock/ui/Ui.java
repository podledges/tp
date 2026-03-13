package medistock.ui;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * Handles all user interactions including input and output.
 */
public class Ui {

    private static final String EXIT_MESSAGE =
            "Inventory saved\nThank you for using MediStock, have a nice day!";
    private static final String WELCOME_MESSAGE = "Welcome to medistock";
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

    /**
     * Reads a command from the user.
     *
     * @return The user's input as a trimmed string.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
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
        System.out.println("Invalid Input: " + e);
        printLine();
    }

    public void printEarliestExpiry(InventoryItem item) {

    }

    public void printExitMessage() {
        printLine();
        System.out.println(EXIT_MESSAGE);
        printLine();
    }

    /**
     * Shows the inventory list to the user.
     *
     * @param inventory The inventory to display.
     */
    public void showInventoryList(Inventory inventory) {
        if (inventory.getAllItems().isEmpty()) {
            printEmptyInventoryMessage();
        } else {
            printLine();
            System.out.println("Here are the medicines in your inventory:");
            int itemCount = 1;
            for (InventoryItem item : inventory.getAllItems()) {
                System.out.println(itemCount + ". " + item.getName() + " (" + item.getUnit() + ") " +
                        "| Qty: " + item.getQuantity());
                itemCount++;
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

    public static void printCreate(String name, String unit, int minimumThreshold) {
        printLine();
        System.out.printf("Product created:" + name + " (" + unit + ")\n" + "Minimum threshold: "
                + minimumThreshold + "%n");
        printLine();
    }

    public void printDelete(InventoryItem deletedItem) {
        printLine();
        System.out.printf("Product deleted:" + deletedItem.getName() + " (" + deletedItem.getUnit() + ")\n"
                + "Minimum threshold: "
                + deletedItem.getMinimumThreshold() + "%n");
        printLine();
    }

    public static void printInventory(Inventory inventory) {
        System.out.println("Current Pharmaceutical Inventory:");
        printLine();

        printLine();
    }

    public void printSpacing(){
        System.out.print("    ");
    }

    public static void printItemDetails(InventoryItem item) {
        String unit = item.getUnit();
        int quantity = item.getQuantity();
        System.out.println(quantity + " " + unit);
    }

    public void printItemDetails(Inventory inventory, InventoryItem item) throws MediStockException {

        String itemName = item.getName();

        System.out.println(String.format("Stock of %s is now:", itemName));
        printSpacing();
        System.out.print(String.format("%d. %s (%d) %n", getItemIndex(inventory, itemName),
                        itemName,item.getMinimumThreshold()));
        printSpacing();
        System.out.println(String.format("Total: %d %s", item.getQuantity(), item.getUnit()));
        printSpacing();
        System.out.println(String.format("Earliest Exp: %tF %n    Status: %s", item.getEarliestExpiry(),
                        item.getStockStatus()));
    }


    public void printBatch(Inventory inventory, InventoryItem item, int quantity, LocalDate date)
                    throws MediStockException {

        String itemName = item.getName();

        System.out.printf("Batch of %d %s, expiring on %3$tF %n has been successfully to" +
                " the inventory!%n", quantity, itemName, date);
        printLine();
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

}
