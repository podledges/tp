package medistock.parser;

import medistock.command.BatchCommand;
import medistock.command.Command;
import medistock.command.CreateCommand;
import medistock.command.DeleteCommandIndex;
import medistock.command.DeleteCommandName;
import medistock.command.EditCommand;
import medistock.command.ExitCommand;
import medistock.command.FindCommand;
import medistock.command.HelpCommand;
import medistock.command.HistoryCommand;
import medistock.command.ListCommand;
import medistock.command.RemoveExpiredCommand;
import medistock.command.WithdrawCommand;
import medistock.exception.MediStockException;
import medistock.ui.Ui;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 * Parses user input and converts it into executable commands.
 */
public class Parser {
    private static final String DOSAGE_UNITS = "mcg|mg|g|kg|ml|l|iu|units?|tablets?|capsules?";
    private static final String DOSAGE_PATTERN = "\\b\\d+(?:\\.\\d+)?\\s*(?:" + DOSAGE_UNITS + ")\\b";
    private static final String NEGATIVE_DOSAGE_PATTERN =
            "(^|\\s)-\\d+(?:\\.\\d+)?\\s*(?:" + DOSAGE_UNITS + ")\\b";
    private static final String DOSAGE_UNIT_PATTERN = "\\b(?:" + DOSAGE_UNITS + ")\\b";

    public static Command parseCommand(String input) throws MediStockException {
        String text = input.trim();

        if (text.isEmpty()) {
            throw new MediStockException("Command cannot be empty");
        }
        
        // Extract command word (first word or phrase) and make it lowercase for case-insensitive matching
        String commandWord = text.split("\\s+", 2)[0].toLowerCase();
        
        if (commandWord.equals("create")) {
            return prepareCreate(text);
        } else if (commandWord.equals("edit")) {
            return prepareEdit(text);
        } else if (commandWord.equals("batch")) {
            return prepareBatch(text);
        } else if (commandWord.equals("withdraw")) {
            return prepareWithdraw(text);
        } else if (commandWord.equals("delete")) {
            return prepareDelete(text);
        } else if (commandWord.equals("list")) {
            return new ListCommand();
        } else if (commandWord.equals("history")) {
            return new HistoryCommand();
        } else if (commandWord.equals("exit") || commandWord.equals("quit")) {
            return new ExitCommand();
        } else if (commandWord.equals("find")) {
            String keyword = parseFind(text);
            return new FindCommand(keyword);
        } else if (commandWord.equals("help")) {
            return new HelpCommand();
        } else if (text.toLowerCase().equals("remove-expired")) {
            return new RemoveExpiredCommand();
        } else if (text.toLowerCase().startsWith("remove-expired n/")) {
            return prepareRemoveExpired(text);
        } else {
            throw new MediStockException("Invalid command: '" + input.split(" ")[0] + "'.\n"
                    + "  - Type <help> to see all available command formats."
                    + "  - Type <list> to view the current inventory state.");
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
     * Returns the value of the "min/" parameter from the input string.
     *
     * @param text The input string containing parameters.
     * @param minIndex The index where the "min/" prefix starts.
     * @return The trimmed minimum threshold value.
     */
    private static String getMinimum(String text, int minIndex) {
        return text.substring(minIndex + 4).trim();
    }

    private static boolean hasDrugName(String name) {
        String textWithoutDosage = name.toLowerCase()
                .replaceAll(DOSAGE_PATTERN, " ")
                .replaceAll(DOSAGE_UNIT_PATTERN, " ");

        return textWithoutDosage.matches(".*[a-z].*");
    }

    private static boolean hasNegativeDosage(String name) {
        return name.toLowerCase().matches(".*" + NEGATIVE_DOSAGE_PATTERN + ".*");
    }

    private static int getNextIndex(String text, int currentIndex, int... indexes) {
        int nextIndex = text.length();
        for (int index : indexes) {
            if (index > currentIndex && index < nextIndex) {
                nextIndex = index;
            }
        }
        return nextIndex;
    }

    private static int getPrefixedIndex(String text, String prefix) {
        int index = text.indexOf(" " + prefix);
        if (index == -1) {
            return -1;
        }
        return index + 1;
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
            throw new MediStockException("Invalid batch format. " + Ui.BATCH_FORMAT);
        }

        if (!(nameIndex < quantIndex && quantIndex < expiryIndex)) {
            throw new MediStockException("Ensure the arguments are in the correct order:" +
                            Ui.BATCH_FORMAT);
        }

        String name = getArgument(text, nameIndex, quantIndex).trim();

        if (name == null || name.trim().isEmpty()) {
            throw new MediStockException("Missing item name.\n"
                    + "Format: batch n/NAME q/QUANTITY d/EXPIRY_DATE(YYYY-MM-DD)");
        }

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
            throw new MediStockException("Invalid create format. " + Ui.CREATE_FORMAT);
        }

        if (!(nameIndex < unitIndex && unitIndex < minIndex)) {
            throw new MediStockException("Use create format: " + Ui.CREATE_FORMAT);
        }

        String name = getArgument(text, nameIndex, unitIndex);
        String unit = getArgument(text, unitIndex, minIndex);
        String minText = getMinimum(text, minIndex);

        if (name.isEmpty() || unit.isEmpty() || minText.isEmpty()) {
            throw new MediStockException("Name, unit, and minimum threshold must not be empty.");
        }

        if (!hasDrugName(name)) {
            throw new MediStockException("Medication name must include a drug name.");
        }

        if (hasNegativeDosage(name)) {
            throw new MediStockException("Medication must not include a negative dosage.");
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

    private static Command prepareEdit(String text) throws MediStockException {
        int oldNameIndex = getPrefixedIndex(text, "o/");
        int nameIndex = getPrefixedIndex(text, "n/");
        int unitIndex = getPrefixedIndex(text, "u/");
        int minIndex = getPrefixedIndex(text, "min/");

        if (oldNameIndex == -1) {
            throw new MediStockException("Invalid edit format. " + Ui.EDIT_FORMAT);
        }

        if (nameIndex == -1 && unitIndex == -1 && minIndex == -1) {
            throw new MediStockException("Edit command requires at least one field to update.");
        }

        if ((nameIndex != -1 && oldNameIndex > nameIndex)
                || (unitIndex != -1 && oldNameIndex > unitIndex)
                || (minIndex != -1 && oldNameIndex > minIndex)) {
            throw new MediStockException("Use edit format: " + Ui.EDIT_FORMAT);
        }

        if (nameIndex != -1 && unitIndex != -1 && nameIndex > unitIndex) {
            throw new MediStockException("Use edit format: " + Ui.EDIT_FORMAT);
        }

        if (nameIndex != -1 && minIndex != -1 && nameIndex > minIndex) {
            throw new MediStockException("Use edit format: " + Ui.EDIT_FORMAT);
        }

        if (unitIndex != -1 && minIndex != -1 && unitIndex > minIndex) {
            throw new MediStockException("Use edit format: " + Ui.EDIT_FORMAT);
        }

        String oldName = text.substring(oldNameIndex + 2,
                getNextIndex(text, oldNameIndex, nameIndex, unitIndex, minIndex)).trim();
        if (oldName.isEmpty()) {
            throw new MediStockException("Old item name must not be empty.");
        }

        String newName = null;
        String newUnit = null;
        Integer newMinimumThreshold = null;

        if (nameIndex != -1) {
            newName = text.substring(nameIndex + 2,
                    getNextIndex(text, nameIndex, unitIndex, minIndex)).trim();
            if (newName.isEmpty()) {
                throw new MediStockException("New item name must not be empty.");
            }
        }

        if (unitIndex != -1) {
            newUnit = text.substring(unitIndex + 2,
                    getNextIndex(text, unitIndex, minIndex)).trim();
            if (newUnit.isEmpty()) {
                throw new MediStockException("New unit must not be empty.");
            }
        }

        if (minIndex != -1) {
            String minText = getMinimum(text, minIndex);
            if (minText.isEmpty()) {
                throw new MediStockException("New minimum threshold must not be empty.");
            }
            try {
                newMinimumThreshold = Integer.parseInt(minText);
            } catch (NumberFormatException e) {
                throw new MediStockException("New minimum threshold must be a valid number.");
            }

            if (newMinimumThreshold <= 0) {
                throw new MediStockException("New minimum threshold must be greater than 0.");
            }
        }

        return new EditCommand(oldName, newName, newUnit, newMinimumThreshold);
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
        int quantityIndex = text.indexOf("q/");

        if (nameIndex == -1 || quantityIndex == -1) {
            throw new MediStockException("Invalid withdraw format. " + Ui.WITHDRAW_FORMAT);
        }
        if (!(nameIndex < quantityIndex)) {
            throw new MediStockException("Use correct format: " + Ui.WITHDRAW_FORMAT);
        }

        String name = getArgument(text, nameIndex, quantityIndex);
        String quantityText = getArgument(text, quantityIndex);

        if (name.isEmpty() || quantityText.isEmpty()) {
            throw new MediStockException("Name and quantity must not be empty.");
        }

        int quantity;
        try {
            quantity = Integer.parseInt(quantityText);
        } catch (NumberFormatException e) {
            throw new MediStockException("Quantity must be a valid number.");
        }

        if (quantity <= 0) {
            throw new MediStockException("Quantity must be greater than 0.");
        }

        return new WithdrawCommand(name, quantity);
    }

    private static Command prepareDelete(String text) throws MediStockException {
        if (text.startsWith("delete n/")) {
            return prepareDeleteName(text);
        } else if (text.startsWith("delete i/")) {
            return prepareDeleteIndex(text);
        }
        throw new MediStockException("Invalid delete format. " + Ui.DELETE_FORMAT);
    }

    private static Command prepareDeleteName(String text) throws MediStockException {
        int nameIndex = text.indexOf("n/");

        if (nameIndex == -1) {
            throw new MediStockException("Invalid delete format. " + Ui.DELETE_FORMAT);
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
            throw new MediStockException("Invalid delete format. " + Ui.DELETE_FORMAT);
        }

        String indexText = getArgument(text, nameIndex);
        int index;

        try {
            index = Integer.parseInt(indexText);
        } catch (NumberFormatException e) {
            throw new MediStockException("Index must be a valid number.");
        }

        return new DeleteCommandIndex(index);
    }

    /**
     * Parses a find command and extracts the keyword.
     *
     * @param text The full find command string.
     * @return The keyword string.
     * @throws MediStockException If the keyword is missing.
     */
    private static String parseFind(String text) throws MediStockException {
        String[] parts = text.split(" ", 2);
        String keyword =parts.length < 2 ? "" : parts[1].trim();

        if (keyword.isEmpty()) {
            throw new MediStockException(Ui.ERROR_MISSING_KEYWORD);
        }
        return keyword;
    }

    private static Command prepareRemoveExpired(String text)
            throws MediStockException {
        int nameIndex = text.indexOf("n/");
        if (nameIndex == -1) {
            throw new MediStockException(
                    "Invalid format. "
                    + Ui.REMOVE_EXPIRED_FORMAT);
        }
        String name = getArgument(text, nameIndex);
        if (name.isEmpty()) {
            throw new MediStockException(
                    "Name must not be empty.");
        }
        return new RemoveExpiredCommand(name);
    }
}
