package medistock.command;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import medistock.storage.Storage;
import org.junit.jupiter.api.Test;

import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.ui.Ui;

/**
 * Tests the HistoryCommand class.
 * Uses a Ui stub so that the command can be tested in isolation from the real Ui.
 */
class HistoryCommandTest {
    /**
     * Stub Ui used for testing.
     * Records whether showHistory was called and stores the histories passed in.
     */
    private static class UiStub extends Ui {
        private boolean showHistoryCalled;
        private List<String> receivedHistories;

        /**
         * Records the histories passed in by the command.
         *
         * @param histories Histories supplied to the Ui.
         */
        @Override
        public void showHistory(List<String> histories) {
            showHistoryCalled = true;
            receivedHistories = histories;
        }
    }

    /**
     * Tests if execute passes a non-empty history list to the Ui.
     */
    @Test
    void execute_nonEmptyHistories_historiesPassedToUi() throws MediStockException {
        HistoryCommand command = new HistoryCommand();
        Inventory inventory = new Inventory();
        Storage storage = new Storage(Path.of("./data/Inventory.txt"));
        List<String> histories = List.of(
                "create n/Panadol u/tablet min/10",
                "withdraw n/Panadol q/2");
        UiStub uiStub = new UiStub();

        command.execute(inventory, uiStub, storage, histories);

        assertTrue(uiStub.showHistoryCalled);
        assertSame(histories, uiStub.receivedHistories);
    }

    /**
     * Tests if execute passes an empty history list to the Ui.
     */
    @Test
    void execute_emptyHistories_emptyHistoryPassedToUi() throws MediStockException {
        HistoryCommand command = new HistoryCommand();
        Inventory inventory = new Inventory();
        List<String> histories = new ArrayList<>();
        Storage storage = new Storage(Path.of("./data/Inventory.txt"));
        UiStub uiStub = new UiStub();

        command.execute(inventory, uiStub, storage, histories);

        assertTrue(uiStub.showHistoryCalled);
        assertSame(histories, uiStub.receivedHistories);
    }
}
