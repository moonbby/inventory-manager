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
 *
 * @author lifeo
 */
public class LogManagerTest {

    private LogManager logManager;
    private static InventoryTableSeeder seeder;

    public LogManagerTest() {
    }

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

    @AfterClass
    public static void tearDownClass() {
        seeder.closeConnection();
    }

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
     * Test of log method, of class LogManager.
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
     * Test of logRaw method, of class LogManager.
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
     * Test of getLogs method, of class LogManager.
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

    /**
     * Test of resetLogs method, of class LogManager.
     */
    @Test
    public void testResetLogs() {
        logManager.logRaw("Dummy log 1");
        logManager.logRaw("Dummy log 2");

        logManager.resetLogs();
        List<Object[]> logsAfterReset = logManager.getLogs();

        List<String> logsStr = new ArrayList<>();
        for (Object[] o : logsAfterReset) {
            logsStr.add(Arrays.toString(o));
        }

        boolean dummy1Exists = logsStr.stream().anyMatch(log -> log.contains("Dummy log 1"));
        boolean dummy2Exists = logsStr.stream().anyMatch(log -> log.contains("Dummy log 2"));

        assertFalse("Dummy log 1 should be cleared", dummy1Exists);
        assertFalse("Dummy log 2 should be cleared", dummy2Exists);
    }
}
