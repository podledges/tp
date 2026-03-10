package medistock.exception;

/**
 * Represents exceptions specific to the MediStock application.
 * Used to signal errors during command parsing and execution.
 */
public class MediStockException extends Exception {

    /**
     * Creates a MediStockException with the specified error message.
     *
     * @param message The error message describing what went wrong.
     */
    public MediStockException(String message) {
        super(message);
    }
}
