/*
Summary: This Logger class provides a singleton logger instance that can log messages to both the console and a
specified log file.
@author: Yongchun Li
@date: 15 Mar 2024
*/

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    private static volatile Logger instance;
    private static final Object lock = new Object();
    private boolean consoleOutputEnabled;
    private boolean fileOutputEnabled;
    private String logFilePath;

    private Logger() {
        // Default settings
        consoleOutputEnabled = true;
        fileOutputEnabled = true;
        logFilePath = "default.log";
    }

    public static Logger getInstance() {
        if (instance == null) {
            synchronized (lock) {
                if (instance == null) {
                    instance = new Logger();
                }
            }
        }
        return instance;
    }

    public void setConsoleOutputEnabled(boolean enabled) {
        consoleOutputEnabled = enabled;
    }

    public void setFileOutputEnabled(boolean enabled) {
        fileOutputEnabled = enabled;
    }

    public void setLogFilePath(String filePath) {
        logFilePath = filePath;
    }

    public void log(String message) {
        if (consoleOutputEnabled) {
            System.out.println(message);
        }
        if (fileOutputEnabled) {
            try (PrintWriter writer = new PrintWriter(new FileWriter(logFilePath, true))) {
                writer.println(message);
            } catch (IOException e) {
                e.printStackTrace(); // Handle file writing error
            }
        }
    }
}