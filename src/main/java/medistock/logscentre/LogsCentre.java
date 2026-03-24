package medistock.logscentre;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * Manages application logging configuration and file output.
 */
public class LogsCentre {
    private static final Logger logger = Logger.getLogger(LogsCentre.class.getName());

    public static void initLogging() {
        try {
            FileHandler fileHandler = new FileHandler("medistock.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            Logger.getLogger("medistock").addHandler(fileHandler);
        } catch (IOException e) {
            logger.warning("Failed to set up file logging: " + e.getMessage());
        }
    }
}
