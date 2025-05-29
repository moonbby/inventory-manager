/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package cli;

import interfaces.IFileManager;
import interfaces.IInputHelper;
import interfaces.IInventoryManager;
import managers.FileManager;
import managers.InventoryManager;
import managers.LogManager;
import utils.FilePaths;
import utils.InputHelper;

/**
 * Entry point of the Inventory Management System. Initialises the system by
 * loading inventory data, setting up core components, and launching the
 * interactive CLI.
 *
 * Users can navigate the system by entering menu options and may type 'exit'
 * during any prompt to return or cancel an action.
 */
public class InventorySystem {

    private static final IInventoryManager inventoryManager = new InventoryManager();
    private static final IFileManager fileManager = new FileManager(FilePaths.INVENTORY_FILE_PATH);
    private static final IInputHelper inputHelper = new InputHelper(inventoryManager);
    private static final UtilityMenuController utilityMenuController = new UtilityMenuController(inventoryManager, fileManager, inputHelper);
    private static final ProductMenuController productMenuController = new ProductMenuController(inventoryManager, inputHelper);
    private static final MainMenuController mainMenuController = new MainMenuController(inventoryManager, fileManager, inputHelper, utilityMenuController, productMenuController);

    /**
     * Initialises core system components and launches the application.
     *
     * Loads inventory data from file, resets the activity log for a fresh
     * session, displays welcome instructions, and begins the interactive
     * command-line interface loop.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        LogManager.initialize();
        fileManager.readProducts(inventoryManager);
        InventorySystem app = new InventorySystem();
        System.out.println("Welcome to the inventory management system!");
        System.out.println("How can I help you? Type a number.");
        System.out.println("INFO: You can type 'exit' anytime during input to cancel and return to the previous menu.");
        System.out.println("INFO: To quit, choose 'Save and Exit' from the main menu.");
        app.run();
    }

    /**
     * Runs the main program loop.
     *
     * Continuously runs the main menu loop, accepting user input until the
     * programme is explicitly terminated via the 'Save and Exit' option.
     */
    public void run() {
        while (true) {
            mainMenuController.printMenu();
            mainMenuController.handleUserInput();
        }
    }
}
