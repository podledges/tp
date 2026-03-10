package medistock.inventory;

import java.util.List;

/**
 * Represents an item in the medical inventory with a name, unit and its batch number.
 */
public class InventoryItem {

    private String name;
    private String unit;
    private List<Batch> batches;
}
