package medistock.storage;

import java.nio.file.Path;

/**
 * Handles loading and saving of inventory data to persistent storage.
 */
public class Storage {
    private final Path filePath;

    public Storage(Path filePath) {
        this.filePath = filePath;
    }
}
