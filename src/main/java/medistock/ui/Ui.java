package medistock.ui;

import java.util.Scanner;

/**
 * Handles all user interactions including input and output.
 */
public class Ui {

    public static final String EXIT_MESSAGE = "Thank you for using medistock!";
    public static final String WELCOME_MESSAGE = "Welcome to medistock";
    private final Scanner scanner;

    /**
     * Creates an Ui instance with a Scanner for reading user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Reads a command from the user.
     *
     * @return The user's input as a trimmed string.
     */
    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public static void printLine() {
        System.out.println("____________________________________________________________");
    }

    public static void greet() {
        printLine();
        System.out.println(WELCOME_MESSAGE);
        printLine();
    }

    public static void exit() {
        printLine();
        System.out.println(EXIT_MESSAGE);
        printLine();
    }


}
