package medistock.parser;

import medistock.command.BatchCommand;
import medistock.command.Command;
import medistock.command.CreateCommand;
import medistock.command.DeleteCommandIndex;
import medistock.command.DeleteCommandName;
import medistock.command.ExitCommand;
import medistock.command.WithdrawCommand;
import medistock.command.ListCommand;
import medistock.exception.MediStockException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {
    public static Command parseCommand(String input) throws MediStockException {
        String text = input.trim();

        if (text.startsWith("create ")) {
            return prepareCreate(text);
        } else if (text.startsWith("batch")) {
            return prepareBatch(text);
        } else if (text.startsWith("withdraw")) {
            return prepareWithdraw(text);
        } else if (text.startsWith("delete")) {
            return prepareDeleteName(text);
        } else if (text.equals("list")) {
            return new ListCommand();
        } else if (text.startsWith("exit") || text.startsWith("quit")) {
            return new ExitCommand();
        } else {
            throw new MediStockException("Unknown command.");
        }

    }

    /**
     * Extracts an argument from the input text based on a starting prefix index.
     * If a second index is provided, it treats it as the start of the next
     * parameter.
     *
     * @param text   User input string.
     * @param index1 The starting index of the current parameter prefix (e.g.,
     *               "n/").
     * @param index2 Optional: The starting index of the next parameter prefix.
     * @return The trimmed string value of the argument.
     */
    private static String getArgument(String text, int index1, int... index2) {
        if (index2.length > 0) {
            return text.substring(index1 + 2, index2[0]).trim();
        } else {
            return text.substring(index1 + 2).trim();
        }
    }

    /**
     * Specifically extracts the value for the "min/" parameter.
     *
     * @param text     The full input string.
     * @param minIndex The starting index of the "min/" prefix.
     * @return The trimmed string value of the minimum threshold.
     */
    private static String getMinimum(String text, int minIndex) {
        return text.substring(minIndex + 4).trim();
    }

    /**
     * Parses the "batch" command input and prepares a BatchCommand for execution.
     *
     * @param text The user input string starting with "batch".
     * @return A BatchCommand.
     * @throws MediStockException If the format is invalid or parameters are out of
     *                            order.
     */
    public static Command prepareBatch(String text) throws MediStockException {
        int nameIndex = text.indexOf("n/");
        int quantIndex = text.indexOf("q/");
        int expiryIndex = text.indexOf("d/");

        if (nameIndex == -1 || quantIndex == -1 || expiryIndex == -1) {
            throw new MediStockException("Invalid batch format. Use: batch n/NAME q/QUANTITY d/EXPIRY_DATE");
        }

        if (!(nameIndex < quantIndex && quantIndex < expiryIndex)) {
            throw new MediStockException("Ensure the arguments are in the correct order:" +
                            " n/NAME q/QUANTITY d/EXPIRY_DATE");
        }

        String name = getArgument(text, nameIndex, quantIndex).trim();

        int quant;
        try {
            quant = Integer.parseInt(getArgument(text, quantIndex, expiryIndex).trim());
        } catch (NumberFormatException e) {
            throw new MediStockException("Invalid quantity. Please enter a positive whole number for the quantity.");
        }

        if (quant <= 0) {
            throw new MediStockException("Invalid quantity. Please enter a positive whole number for the quantity.");
        }

        LocalDate expiryDate;
        try {
            expiryDate = LocalDate.parse(getArgument(text, expiryIndex).trim());
        } catch (DateTimeParseException e) {
            throw new MediStockException("Invalid expiry date. Please use a valid format (e.g., YYYY-MM-DD).");
        }

        return new BatchCommand(name, quant, expiryDate);
    }

    /**
     * Parses the "create" command input and prepares a CreateCommand for execution.
     *
     * @param text The user input string starting with "create".
     * @return A CreateCommand object.
     * @throws MediStockException If parameters are missing, empty, or incorrectly
     *                            formatted.
     */
    private static Command prepareCreate(String text) throws MediStockException {
        int nameIndex = text.indexOf("n/");
        int unitIndex = text.indexOf("u/");
        int minIndex = text.indexOf("min/");

        if (nameIndex == -1 || unitIndex == -1 || minIndex == -1) {
            throw new MediStockException("Invalid create format. Use: create n/NAME u/UNIT min/THRESHOLD");
        }

        if (!(nameIndex < unitIndex && unitIndex < minIndex)) {
            throw new MediStockException("Use create format: create n/NAME u/UNIT min/THRESHOLD");
        }

        String name = getArgument(text, nameIndex, unitIndex);
        String unit = getArgument(text, unitIndex, minIndex);
        String minText = getMinimum(text, minIndex);

        if (name.isEmpty() || unit.isEmpty() || minText.isEmpty()) {
            throw new MediStockException("Name, unit, and minimum threshold must not be empty.");
        }

        int min;
        try {
            min = Integer.parseInt(minText);
        } catch (NumberFormatException e) {
            throw new MediStockException("Minimum threshold must be a valid number.");
        }

        if (min <= 0) {
            throw new MediStockException("Minimum threshold must be greater than 0.");
        }
        return new CreateCommand(name, unit, min);
    }

    /**
     * Parses the "withdraw" command input and prepares a WithdrawCommand for
     * execution.
     *
     * @param text The user input string starting with "withdraw".
     * @return A WithdrawCommand.
     * @throws MediStockException If the format is invalid or parameters are out of
     *                            order.
     */
    private static Command prepareWithdraw(String text) throws MediStockException {
        int nameIndex = text.indexOf("n/");
        int quantIndex = text.indexOf("q/");

        if (nameIndex == -1 || quantIndex == -1) {
            throw new MediStockException("Invalid withdraw format. Use: withdraw n/NAME q/QUANTITY");
        }
        if (!(nameIndex < quantIndex)) {
            throw new MediStockException("Use withdraw format: withdraw n/NAME q/QUANTITY");
        }

        String name = getArgument(text, nameIndex, quantIndex);
        String quantText = getArgument(text, quantIndex);

        if (name.isEmpty() || quantText.isEmpty()) {
            throw new MediStockException("Name and quantity must not be empty.");
        }

        int quant;
        try {
            quant = Integer.parseInt(quantText);
        } catch (NumberFormatException e) {
            throw new MediStockException("Quantity must be a valid number.");
        }

        if (quant <= 0) {
            throw new MediStockException("Quantity must be greater than 0.");
        }

        return new WithdrawCommand(name, quant);
    }

    private static Command prepareDeleteName(String text) throws MediStockException {
        int nameIndex = text.indexOf("n/");

        if (nameIndex == -1) {
            throw new MediStockException("Invalid delete format. Use: delete 'n/NAME' or 'i/INDEX'");
        }

        String name = getArgument(text, nameIndex);

        if (name.isEmpty()) {
            throw new MediStockException("Name must not be empty.");
        }

        return new DeleteCommandName(name);
    }

    private static Command prepareDeleteIndex(String text) throws MediStockException {
        int nameIndex = text.indexOf("i/");

        if (nameIndex == -1) {
            throw new MediStockException("Invalid delete format. Use: delete 'n/NAME' or 'i/INDEX'");
        }

        String indexText = getArgument(text, nameIndex);
        int index;

        try {
            index = Integer.parseInt(indexText);
        } catch (NumberFormatException e) {
            throw new MediStockException("Index must be a valid number.");
        }

        if (index <= 0 ) {
            throw new MediStockException("Invalid index! Please enter a valid index.");
        }

        return new DeleteCommandIndex(index);
    }

}
