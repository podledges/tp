package medistock.storage;

import medistock.exception.MediStockException;
import medistock.inventory.Batch;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.nio.file.Files.exists;
import static java.nio.file.Files.readAllLines;

/**
 * Handles loading and saving of inventory data to persistent storage.
 */
public class Storage {
    private final Path filePath;

    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    /**
     * Saves newly created Item to data.txt
     *
     * @param data the data to be saved
     * @throws IOException if an Error occurs while reading the file
     */
    public void saveToFile(Storable data) throws IOException {
        FileWriter fw = new FileWriter(filePath.toFile(), true);
        fw.write(data.toFileFormat() + System.lineSeparator());
        fw.close();
    }

    public void saveToFile(Inventory inventory) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filePath.toFile(), false))) {
            for (InventoryItem item : inventory.getAllItems()) {
                bw.write(item.toFileFormat());
                bw.newLine();
                for (Batch batch : item.getActiveBatches()) {
                    bw.write(batch.toFileFormat());
                    bw.newLine();
                }
                bw.newLine();
            }
        }
    }

    public void readFromFile(Inventory inventory) throws IOException, MediStockException {
        List<String> lines = readAllLines(filePath);
        String currentItemName = "";
        for (String line : lines) {
            if (line.isEmpty() || line.equals("[Batches]")) {
                continue;
            }
            if (line.startsWith("Item:")) {
                InventoryItem newItem = parseInventoryItem(line);
                currentItemName = newItem.getName();
                try {
                    inventory.addItem(newItem);
                } catch (MediStockException e) {
                    throw new MediStockException("Corrupted save file! Duplicate item found: " + currentItemName);
                }
            }
            else {
                if (currentItemName.isEmpty()) {
                    throw new MediStockException("Corrupted save file! Found a batch before any item was declared: "
                                    + line);
                }
                Batch newBatch = parseItemBatch(line);
                inventory.addBatchToItem(currentItemName, newBatch);
            }
        }
    }

    public InventoryItem parseInventoryItem(String line) throws MediStockException {
        String regex = "Item: (.*?) \\((.*?)\\) \\| (\\d+)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line);
        if (!matcher.find()) {
            throw new MediStockException("Corrupted save file! Could not parse item from: " + line);
        }
        String name = matcher.group(1).trim();
        String unit = matcher.group(2).trim();
        int minThreshold = Integer.parseInt(matcher.group(3).trim());
        return new InventoryItem(name, unit, minThreshold);
    }

    public Batch parseItemBatch(String line) throws MediStockException {
        String regex = "^(\\d+) \\| (.*?) \\| (\\d{4}-\\d{2}-\\d{2})$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(line.trim());
        if (!matcher.matches()) {
            throw new MediStockException("Corrupted save file! Batch line does not match format: " + line);
        }
        try {
            int batchNumber = Integer.parseInt(matcher.group(1).trim());
            int quantity = Integer.parseInt(matcher.group(2).trim());
            String expiryDateString = matcher.group(3).trim();
            LocalDate expiryDate = LocalDate.parse(expiryDateString);
            return new Batch(batchNumber, quantity, expiryDate);
        } catch (NumberFormatException e) {
            throw new MediStockException("Corrupted save file! Number formatting error in batch: " + line);
        }
    }

    public void createNewFile() {
        try {
            Path parentDir = filePath.getParent();
            if (parentDir != null) {
                Files.createDirectories(parentDir);
            }
            Files.createFile(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void initializeInventory(Inventory inventory) throws IOException, MediStockException {
        if (exists(filePath)) {
            readFromFile(inventory);
        } else {
            createNewFile();
        }
    }
}
