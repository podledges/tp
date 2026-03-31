package medistock;

import medistock.command.Command;
import medistock.exception.MediStockException;
import medistock.historystorage.HistoryStorage;
import medistock.inventory.Inventory;
import medistock.logscentre.LogsCentre;
import medistock.parser.Parser;
import medistock.storage.Storage;
import medistock.ui.Ui;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.ArrayList;

/**
 * Main class for the MediStock application.
 * Manages the initialization and execution of the inventory management system.
 */
public class Medistock {        // I think we need to change name of class and file to MediStock

    private Inventory inventory;
    private Ui ui;
    private Storage storage;
    private final HistoryStorage historyStorage;
    private List<String> histories;


    public Medistock(Path filepath, String historyFilepath) {
        this.ui = new Ui();
        this.storage = new Storage(filepath);
        this.historyStorage = new HistoryStorage(historyFilepath);
        this.inventory = new Inventory();


        try {
            histories = historyStorage.load(new ArrayList<>());
        } catch (Exception e) {
            histories = new ArrayList<>();
        }
    }

    /**
     * Starts the main application loop. Continuously reads user input, parses commands, and executes them
     * Does so, until an exit command is given.
     */
    public void boot() throws IOException, MediStockException {
        ui.greet();
        storage.initializeInventory(inventory);

        boolean isRunning = true;

        while (isRunning) {
            try {
                String input = ui.getInput();
                Command command = Parser.parseCommand(input);
                command.execute(inventory, ui, storage, histories);
                if (input.equals("exit") || input.equals("quit")) {
                    isRunning = false;
                }
            } catch (MediStockException e) {
                ui.printError(e.getMessage());
            }
            try {
                historyStorage.save(histories);
            } catch (IOException e) {
                System.out.println("Error saving history.");
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
    public static void main(String[] args) throws IOException, MediStockException {
        LogsCentre.initLogging();

        Medistock mediStock = new Medistock(Path.of("./data/Inventory.txt"), "./data/History.txt");
        mediStock.boot();
    }
}

