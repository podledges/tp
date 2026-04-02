package medistock.logscentre;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Manages application logging configuration and file output.
 */
public class LogsCentre {
    private static final Path LOG_FILE_PATH = Path.of("data", "medistock.log");
    private static final Logger logger = Logger.getLogger(LogsCentre.class.getName());

    public static void initLogging() {
        try {
            Files.createDirectories(LOG_FILE_PATH.getParent());

            FileHandler fileHandler = new FileHandler(LOG_FILE_PATH.toString(), true);
            fileHandler.setFormatter(new SimpleFormatter());
            
            Logger rootLogger = Logger.getLogger("medistock");
            rootLogger.addHandler(fileHandler);
            rootLogger.setUseParentHandlers(false); // Disable console output
        } catch (IOException e) {
            logger.warning("Failed to set up file logging: " + e.getMessage());
        }
    }
}
