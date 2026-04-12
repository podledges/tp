package medistock.command;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import medistock.exception.MediStockException;
import medistock.inventory.Batch;
import medistock.inventory.Inventory;
import medistock.inventory.InventoryItem;
import medistock.storage.Storage;
import medistock.ui.Ui;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class EditCommandTest {

    @TempDir
    Path tempDir;

    @Test
    void execute_editUnitOnly_updatesUnit() throws MediStockException {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        Storage storage = new Storage(tempDir.resolve("Inventory.txt"));
        List<String> histories = new ArrayList<>();
        InventoryItem item = new InventoryItem("Aspirin", "Tablets", 10);
        inventory.addItem(item);

        EditCommand command = new EditCommand("Aspirin", null, "Capsules", null);
        command.execute(inventory, ui, histories);

        InventoryItem updatedItem = inventory.getItem("Aspirin");
        assertEquals("Capsules", updatedItem.getUnit());
        assertEquals(10, updatedItem.getMinimumThreshold());
        assertEquals(1, histories.size());
    }

    @Test
    void execute_editMinOnly_updatesMinimumThreshold() throws MediStockException {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        Storage storage = new Storage(tempDir.resolve("Inventory.txt"));
        List<String> histories = new ArrayList<>();
        InventoryItem item = new InventoryItem("Aspirin", "Tablets", 10);
        inventory.addItem(item);

        EditCommand command = new EditCommand("Aspirin", null, null, 20);
        command.execute(inventory, ui, histories);

        InventoryItem updatedItem = inventory.getItem("Aspirin");
        assertEquals("Tablets", updatedItem.getUnit());
        assertEquals(20, updatedItem.getMinimumThreshold());
        assertEquals(1, histories.size());
    }

    @Test
    void execute_editNameOnly_updatesInventoryKey() throws MediStockException {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        Storage storage = new Storage(tempDir.resolve("Inventory.txt"));
        List<String> histories = new ArrayList<>();
        InventoryItem item = new InventoryItem("Aspirin", "Tablets", 10);
        inventory.addItem(item);

        EditCommand command = new EditCommand("Aspirin", "Aspirin 500mg", null, null);
        command.execute(inventory, ui, histories);

        assertFalse(inventory.hasItem("Aspirin"));
        assertTrue(inventory.hasItem("Aspirin 500mg"));
        assertEquals(1, histories.size());
    }

    @Test
    void execute_editNameOnly_preservesBatchesQuantityAndStorage() throws MediStockException, IOException {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        Path filePath = tempDir.resolve("Inventory.txt");
        Storage storage = new Storage(filePath);
        List<String> histories = new ArrayList<>();
        InventoryItem item = new InventoryItem("Aspirin", "Tablets", 10);
        item.addBatch(new Batch(1, 20, LocalDate.now().plusDays(30)));
        inventory.addItem(item);

        EditCommand command = new EditCommand("Aspirin", "Aspirin 500mg", null, null);
        command.execute(inventory, ui, histories);

        InventoryItem updatedItem = inventory.getItem("Aspirin 500mg");
        assertEquals(20, updatedItem.getQuantity());
        assertEquals(1, updatedItem.getBatchQuantity());

        String fileContents = Files.readString(filePath);
        assertTrue(fileContents.contains("Item: Aspirin 500mg (Tablets) | 10"));
        assertTrue(fileContents.contains("1 | 20 | "));
    }

    @Test
    void execute_renameToExistingItem_throwsException() throws MediStockException {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        Storage storage = new Storage(tempDir.resolve("Inventory.txt"));
        List<String> histories = new ArrayList<>();
        inventory.addItem(new InventoryItem("Aspirin", "Tablets", 10));
        inventory.addItem(new InventoryItem("Panadol", "Capsules", 20));

        EditCommand command = new EditCommand("Aspirin", "Panadol", null, null);

        assertThrows(MediStockException.class,
                () -> command.execute(inventory, ui, histories));
        assertTrue(inventory.hasItem("Aspirin"));
        assertTrue(inventory.hasItem("Panadol"));
        assertEquals(0, histories.size());
    }

    @Test
    void execute_renameToSameNormalizedName_succeeds() throws MediStockException {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        Storage storage = new Storage(tempDir.resolve("Inventory.txt"));
        List<String> histories = new ArrayList<>();
        InventoryItem item = new InventoryItem("Aspirin", "Tablets", 10);
        item.addBatch(new Batch(1, 15, LocalDate.now().plusDays(30)));
        inventory.addItem(item);

        EditCommand command = new EditCommand("Aspirin", "  ASPIRIN  ", null, null);
        command.execute(inventory, ui, histories);

        InventoryItem updatedItem = inventory.getItem("aspirin");
        assertEquals("  ASPIRIN  ", updatedItem.getName());
        assertEquals(15, updatedItem.getQuantity());
        assertEquals(1, histories.size());
    }
}
