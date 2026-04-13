package medistock.inventory;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

import medistock.exception.MediStockException;

public class InventoryItemTest {

    @Test
    void addBatch_exceedIntMax_throwsException() throws MediStockException {
        InventoryItem item = new InventoryItem("Aspirin 500mg", "Tablet", 200);

        item.addBatch(new Batch(1, Integer.MAX_VALUE - 10, LocalDate.now().plusDays(30)));

        MediStockException exception = assertThrows(MediStockException.class,
                () -> item.addBatch(new Batch(2, 11, LocalDate.now().plusDays(31))));

        assertEquals("Cannot add batch: Total quantity would exceed maximum allowed limit.",
                exception.getMessage());
    }

    @Test
    void withdraw_singleBatch_reducesQuantity() throws MediStockException {
        InventoryItem item = new InventoryItem("Aspirin 500mg", "Tablet", 10);
        item.addBatch(new Batch(1, 20, LocalDate.now().plusDays(30)));

        int withdrawnQuantity = item.withdraw(7);

        assertEquals(7, withdrawnQuantity);
        assertEquals(13, item.getQuantity());
        assertEquals(1, item.getBatchQuantity());
    }

    @Test
    void withdraw_exactTotalQuantity_reachesZeroAndClearsBatches() throws MediStockException {
        InventoryItem item = new InventoryItem("Aspirin 500mg", "Tablet", 10);
        item.addBatch(new Batch(1, 8, LocalDate.now().plusDays(30)));

        int withdrawnQuantity = item.withdraw(8);

        assertEquals(8, withdrawnQuantity);
        assertEquals(0, item.getQuantity());
        assertEquals(0, item.getBatchQuantity());
    }

    @Test
    void withdraw_insufficientStock_withdrawsAvailableStock() throws MediStockException {
        InventoryItem item = new InventoryItem("Aspirin 500mg", "Tablet", 10);
        item.addBatch(new Batch(1, 5, LocalDate.now().plusDays(30)));

        int withdrawnQuantity = item.withdraw(6);

        assertEquals(5, withdrawnQuantity);
        assertEquals(0, item.getQuantity());
        assertEquals(0, item.getBatchQuantity());
    }

    @Test
    void withdraw_multipleBatches_usesEarliestExpiryFirst() throws MediStockException {
        InventoryItem item = new InventoryItem("Aspirin 500mg", "Tablet", 10);
        item.addBatch(new Batch(1, 5, LocalDate.now().plusDays(5)));
        item.addBatch(new Batch(2, 10, LocalDate.now().plusDays(20)));

        int withdrawnQuantity = item.withdraw(7);

        assertEquals(7, withdrawnQuantity);
        assertEquals(8, item.getQuantity());
        assertEquals(1, item.getBatchQuantity());
    }

    @Test
    void withdraw_insufficientValidStock_withdrawsAvailableValidStock() throws MediStockException {
        InventoryItem item = new InventoryItem("Aspirin 500mg", "Tablet", 10);
        item.addBatch(new Batch(1, 10, LocalDate.now().minusDays(1)));
        item.addBatch(new Batch(2, 5, LocalDate.now().plusDays(10)));

        int withdrawnQuantity = item.withdraw(6);

        assertEquals(5, withdrawnQuantity);
        assertEquals(0, item.getQuantity());
        assertEquals(0, item.getBatchQuantity());
        assertEquals(1, item.getTotalBatchQuantity());
        assertEquals(1, item.getExpiredBatches().size());
    }

    @Test
    void withdraw_allStockExpired_doesNotWithdrawFromExpiredBatch() throws MediStockException {
        InventoryItem item = new InventoryItem("Paracetamol 500mg", "Tablets", 10);
        item.addBatch(new Batch(1, 10, LocalDate.now().minusDays(1)));

        int withdrawnQuantity = item.withdraw(5);

        assertEquals(0, withdrawnQuantity);
        assertEquals(0, item.getQuantity());
        assertEquals(0, item.getBatchQuantity());
        assertEquals(1, item.getTotalBatchQuantity());
        assertEquals(1, item.getExpiredBatches().size());
    }
}
