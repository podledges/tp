package medistock;

import medistock.command.Command;
import medistock.exception.MediStockException;
import medistock.inventory.Inventory;
import medistock.logscentre.LogsCentre;
import medistock.parser.Parser;
import medistock.storage.Storage;
import medistock.ui.Ui;

import java.nio.file.Path;


public class Medistock {        // I think we need to change name of class and file to MediStock

    private Inventory inventory;
    private Ui ui;
    private Storage storage;


    public Medistock(Path filepath) {
        this.ui = new Ui();
        this.storage = new Storage(filepath);
        this.inventory = new Inventory();
    }

    /**
     * Starts the main application loop. Continuously reads user input, parses commands, and executes them
     * Does so, until an exit command is given.
     */
    public void boot() {
        ui.greet();
        boolean isRunning = true;

        while (isRunning) {
            try {
                String input = ui.getInput();
                Command command = Parser.parseCommand(input);
                command.execute(inventory, ui);
                if (input.equals("exit")) {
                    isRunning = false;
                }
            } catch (MediStockException e) {
                ui.printError(e.getMessage());
            }
        }
        exit();
    }

    /**
     * Terminates the application safely. Forces the Java Virtual Machine to shut down.
     */
    private void exit(){
        System.exit(0);
    }

    /**
     * Main entry-point for the java.medistick.Medistock application.
     */
    public static void main(String[] args) {
        LogsCentre.initLogging();

        Medistock mediStock = new Medistock(Path.of("./data/Inventory.txt"));
        mediStock.boot();
    }
}
