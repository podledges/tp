package medistock.inventory;

/**
 * Represents an item in the medical inventory with a name, unit and minimum threshold.
 */
public class InventoryItem {

    private final String name;
    private final String unit;
    private final int minimumThreshold;

    public InventoryItem(String name, String unit, int minimumThreshold) {
        this.name = name;
        this.unit = unit;
        this.minimumThreshold = minimumThreshold;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public int getMinimumThreshold() {
        return minimumThreshold;
    }
}
