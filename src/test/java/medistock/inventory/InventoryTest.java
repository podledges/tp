package medistock.inventory;

import medistock.exception.MediStockException;
import org.junit.jupiter.api.Test;

import java.util.List;

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
    void deleteItem_existingItem_removesItem() throws MediStockException {
        Inventory inventory = new Inventory();

        String name = "Aspirin 500mg";
        InventoryItem existingItem = new InventoryItem(name, "Tablet", 200);
        inventory.addItem(existingItem);
        inventory.deleteItem(name);
        assertEquals(0, inventory.getSize());
    }

    @Test
    void deleteItem_nonExistingItem_throwsException() {
        Inventory inventory = new Inventory();

        assertThrows(MediStockException.class,
                () -> inventory.deleteItem("Panadol"));
    }

    @Test
    void findItem_matchingItem_findsItem() throws MediStockException {
        Inventory inventory = new Inventory();

        String name = "Aspirin 500mg";
        InventoryItem matchingItem = new InventoryItem(name, "Tablet", 200);
        inventory.addItem(matchingItem);
        
        List<InventoryItem> result = inventory.findItem("Aspirin");
        
        assertEquals(1, result.size());
        assertEquals(matchingItem, result.get(0));
    }

    @Test
    void findItem_noMatches_returnsEmptyList() throws MediStockException {
        Inventory inventory = new Inventory();

        InventoryItem item = new InventoryItem("Aspirin 500mg", "Tablet", 200);
        inventory.addItem(item);
        
        List<InventoryItem> result = inventory.findItem("Panadol");
        
        assertEquals(0, result.size());
    }

    @Test
    void findItem_multipleMatches_findsAllItems() throws MediStockException {
        Inventory inventory = new Inventory();

        InventoryItem item1 = new InventoryItem("Aspirin 500mg", "Tablet", 200);
        InventoryItem item2 = new InventoryItem("Aspirin 100mg", "Tablet", 150);
        InventoryItem item3 = new InventoryItem("Panadol 500mg", "Tablet", 300);
        
        inventory.addItem(item1);
        inventory.addItem(item2);
        inventory.addItem(item3);
        
        List<InventoryItem> result = inventory.findItem("Aspirin");
        
        assertEquals(2, result.size());
    }

    @Test
    void findItem_caseInsensitive_findsItem() throws MediStockException {
        Inventory inventory = new Inventory();

        InventoryItem item = new InventoryItem("Aspirin 500mg", "Tablet", 200);
        inventory.addItem(item);
        
        List<InventoryItem> result = inventory.findItem("ASPIRIN");
        
        assertEquals(1, result.size());
        assertEquals(item, result.get(0));
    }

    @Test
    void findItem_partialMatch_findsItem() throws MediStockException {
        Inventory inventory = new Inventory();

        InventoryItem item = new InventoryItem("Aspirin 500mg", "Tablet", 200);
        inventory.addItem(item);
        
        List<InventoryItem> result = inventory.findItem("rin");
        
        assertEquals(1, result.size());
        assertEquals(item, result.get(0));
    }

    @Test
    void find_partialKeyword_returnsMatchingItems() throws MediStockException {
        Inventory inventory = new Inventory();

        InventoryItem paracetamol = new InventoryItem("Paracetamol 500mg", "Tablet", 200);
        InventoryItem vyvanse = new InventoryItem("Vyvanse 70mg", "Tablet", 50);
        inventory.addItem(paracetamol);
        inventory.addItem(vyvanse);

        List<InventoryItem> result = inventory.findItem("ceta");

        assertEquals(1, result.size());
        assertEquals(paracetamol, result.get(0));
    }

    @Test
    void delete_invalidIndex_throwsException() throws MediStockException {
        Inventory inventory = new Inventory();
        inventory.addItem(new InventoryItem("Paracetamol 500mg", "Tablet", 200));

        MediStockException exception = assertThrows(MediStockException.class,
                () -> inventory.deleteItem(2));

        assertEquals("Index entered out of bounds! Valid indices: 1 to 1",
                exception.getMessage());
    }
}
