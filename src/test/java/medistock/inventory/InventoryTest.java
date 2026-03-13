package medistock.inventory;

import medistock.exception.MediStockException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InventoryTest {

    @Test
    void addItem_duplicateName_throwsException() throws MediStockException {
        Inventory inventory = new Inventory();

        InventoryItem firstItem = new InventoryItem("Aspirin 500mg", "Tablet", 200);
        InventoryItem duplicateItem = new InventoryItem("Aspirin 500mg", "Tablet", 200);

        inventory.addItem(firstItem);
        assertThrows(MediStockException.class,
                () -> inventory.addItem(duplicateItem));
    }

    @Test
    void addItem_validItem_sizeIncreases() throws MediStockException {
        Inventory inventory = new Inventory();
        int initialInventorySize = inventory.getSize();

        InventoryItem validItem = new InventoryItem("Aspirin 500mg", "Tablet", 200);
        inventory.addItem(validItem);
        assertEquals(initialInventorySize + 1, inventory.getSize());
    }

    @Test
    void hasItem() {
    }

    @Test
    void getItem() {
    }

    @Test
    void removeItem() {
    }

    @Test
    void getSize() {
    }

    @Test
    void deleteItem_validItem_success() {
    }
}
