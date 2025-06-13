/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package managers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Unit tests for the LogManager class.
 *
 * Validates log recording, retrieval, and raw entry insertion.
 */
public class LogManagerTest {

    private LogManager logManager;
    private static InventoryTableSeeder seeder;

    public LogManagerTest() {
    }

    /**
     * Establishes database connection and initialises tables once before all tests.
     */
    @BeforeClass
    public static void setUpClass() {
        System.setProperty("derby.system.home", "database");
        boolean connected = DatabaseManager.getInstance().establishConnection();
        if (!connected) {
            fail("Could not establish DB connection");
        }

        seeder = new InventoryTableSeeder();
        seeder.initialiseAllTables();
    }

    /**
     * Closes database connection after all tests complete.
     */
    @AfterClass
    public static void tearDownClass() {
        seeder.closeConnection();
    }

    /**
     * Resets all tables before each test to maintain isolation and consistency.
     */
    @Before
    public void setUp() {
        seeder.resetProductsTable();
        seeder.resetLogsTable();
        seeder.resetBackupTable();

        this.logManager = new LogManager();
    }

    @After
    public void tearDown() {
    }

    /**
     * Verifies that a successful action is logged correctly.
     */
    @Test
    public void testLog_Successful() {
        logManager.log("Tested", "logging function", true, null);

        List<Object[]> logs = logManager.getLogs();
        assertFalse(logs.isEmpty());

        List<String> logsStr = new ArrayList<>();
        for (Object[] o : logs) {
            logsStr.add(Arrays.toString(o));;
        }
        
        boolean found = logsStr.stream().anyMatch(log -> log.contains("Successfully tested logging function"));
        assertTrue("Expected log message not found in logs", found);
    }

    /**
     * Verifies that a failed action is logged with appropriate error message.
     */
    @Test
    public void testLog_Unsuccessful() {
        logManager.log("Remove", "test product", false, "Product not found");

        List<Object[]> logs = logManager.getLogs();
        assertFalse(logs.isEmpty());

        List<String> logsStr = new ArrayList<>();
        for (Object[] o : logs) {
            logsStr.add(Arrays.toString(o));
        }

        boolean found_1 = logsStr.stream().anyMatch(log -> log.contains("Failed to remove test product"));
        boolean found_2 = logsStr.stream().anyMatch(log -> log.contains("Product not found"));

        assertTrue("Expected log message not found in logs", found_1);
        assertTrue("Expected error details not found in logs", found_2);
    }

    /**
     * Verifies raw log entries are saved correctly.
     */
    @Test
    public void testLogRaw() {
        logManager.logRaw("This is a test");

        List<Object[]> logs = logManager.getLogs();
        assertFalse(logs.isEmpty());

        List<String> logsStr = new ArrayList<>();
        for (Object[] o : logs) {
            logsStr.add(Arrays.toString(o));
        }
        
        boolean found = logsStr.stream().anyMatch(log -> log.contains("This is a test"));
        assertTrue("Expected log message not found in logs", found);
    }

    /**
     * Verifies that logs are correctly retrieved from the database.
     */
    @Test
    public void testGetLogs() {
        String testMessage = "This is a test.";
        logManager.logRaw(testMessage);

        List<Object[]> logs = logManager.getLogs();
        assertFalse(logs.isEmpty());

        List<String> logsStr = new ArrayList<>();
        for (Object[] o : logs) {
            logsStr.add(Arrays.toString(o));
        }
        
        boolean found = logsStr.stream().anyMatch(log -> log.contains(testMessage));
        assertTrue("Expected log message not found in logs", found);
    }
}
