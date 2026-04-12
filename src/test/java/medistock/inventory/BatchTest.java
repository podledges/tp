package medistock.inventory;

import medistock.exception.MediStockException;
import medistock.parser.Parser;
import medistock.storage.Storage;
import medistock.ui.Ui;
import org.junit.jupiter.api.Test;

import medistock.command.BatchCommand;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;


public class BatchTest {

    @Test
    void prepareBatch_missingNameTag_throwsException() {
        String input = "batch q/10 d/2025-01-01";

        assertThrows(MediStockException.class,
                () -> Parser.prepareBatch(input));
    }

    @Test
    void prepareBatch_incorrectTagOrder_throwsException() {
        String input = "batch q/10 n/Vyvanse 40mg d/2025-01-01";

        assertThrows(MediStockException.class,
                () -> Parser.prepareBatch(input));
    }

    @Test
    void prepareBatch_quantityNegative_throwsException() {
        String input = "batch n/Aspirin q/-5 d/2025-01-01";

        assertThrows(MediStockException.class,
                () -> Parser.prepareBatch(input));
    }

    @Test
    void prepareBatch_validFormat_doesNotThrow() {
        String input = "batch n/Aspirin q/10 d/2025-01-01";

        assertDoesNotThrow(() -> Parser.prepareBatch(input));
    }

    @Test
    void execute_existingItem_increasesQuantity() throws MediStockException {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        Storage storage = new Storage(Path.of("./data/Inventory.txt"));
        List<String> histories = new ArrayList<>();

        InventoryItem existingItem = new InventoryItem("Aspirin 500mg", "Tablets", 200);
        inventory.addItem(existingItem);

        LocalDate date = LocalDate.parse("2028-06-07");
        BatchCommand command = new BatchCommand("Aspirin 500mg", 50, date);

        command.execute(inventory, ui, histories);

        InventoryItem updatedItem = inventory.getItem("Aspirin 500mg");
        assertEquals(50, updatedItem.getQuantity());
    }

    @Test
    void execute_missingItem_throwsException() {
        Inventory inventory = new Inventory();
        Ui ui = new Ui();
        List<String> histories = new ArrayList<>();
        Storage storage = new Storage(Path.of("./data/Inventory.txt"));

        LocalDate date = LocalDate.parse("2028-06-07");
        BatchCommand command = new BatchCommand("Aspirin 500mg", 50, date);

        assertThrows(MediStockException.class,
                () -> command.execute(inventory, ui, histories));
    }
}
