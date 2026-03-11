package medistock.parser;

import medistock.command.Command;
import medistock.command.CreateCommand;
import medistock.exception.MediStockException;

public class Parser {
    public static Command parse(String input) throws MediStockException {
        String text = input.trim();

        if (!text.startsWith("create ")) {
            throw new MediStockException("Unknown command.");
        }

        int nameIndex = text.indexOf("n/");
        int unitIndex = text.indexOf("u/");
        int minIndex = text.indexOf("min/");

        if (nameIndex == -1 || unitIndex == -1 || minIndex == -1) {
            throw new MediStockException("Invalid create format. Use: create n/NAME u/UNIT min/THRESHOLD");
        }

        if (!(nameIndex < unitIndex && unitIndex < minIndex)) {
            throw new MediStockException("Use create format: create n/NAME u/UNIT min/THRESHOLD");
        }

        String name = text.substring(nameIndex + 2, unitIndex).trim();
        String unit = text.substring(unitIndex + 2, minIndex).trim();
        String minText = text.substring(minIndex + 4).trim();

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
}
