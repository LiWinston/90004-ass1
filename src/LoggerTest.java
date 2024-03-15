import org.junit.jupiter.api.Test;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;

public class LoggerTest {

    @Test
    public void testGetInstance() {
        Logger instance1 = Logger.getInstance();
        Logger instance2 = Logger.getInstance();
        assertEquals(instance1, instance2);
    }

    @Test
    public void testSetConsoleOutputEnabled() {
        Logger logger = Logger.getInstance();
        logger.setConsoleOutputEnabled(true);
        assertTrue(logger.consoleOutputEnabled);
    }

    @Test
    public void testSetFileOutputEnabled() {
        Logger logger = Logger.getInstance();
        logger.setFileOutputEnabled(true);
        assertTrue(logger.fileOutputEnabled);
    }

    @Test
    public void testSetLogFilePath() {
        Logger logger = Logger.getInstance();
        String filePath = "test.log";
        logger.setLogFilePath(filePath);

        // Check if the new log file is created
        File logFile = new File(filePath);
        assertTrue(logFile.exists(), "Log file was not created at the specified path");

        // Write a success declaration to the log file
        try (FileWriter writer = new FileWriter(filePath, true)) {
            writer.write("Success declaration written to log file");
        } catch (IOException e) {
            fail("Error writing to log file: " + e.getMessage());
        }
    }

    @Test
    public void testLog() {
        Logger logger = Logger.getInstance();
        logger.setConsoleOutputEnabled(false); // Disable console output for this test
        logger.setFileOutputEnabled(true); // Enable file output

        String message = "Test message1212121";

        // Log the message
        logger.log(message);

        // Read the log file and check if the message is logged
        try (BufferedReader reader = new BufferedReader(new FileReader(logger.logFilePath))) {
            String line;
            boolean messageLogged = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains(message)) {
                    messageLogged = true;
                    break;
                }
            }
            assertTrue(messageLogged, "Message was not logged in the file");
        } catch (IOException e) {
            fail("Error reading log file: " + e.getMessage());
        }
    }

    @Test
    public void testClose() {
        Logger logger = Logger.getInstance();
        logger.close();

        // Positive test case
        // Check if fileWriter is closed

        // Negative test case
        // Check if fileWriter is not closed when already closed
    }
}