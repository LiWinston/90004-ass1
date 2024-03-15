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
    private static Logger instance;
    private static final Object lock = new Object();

    boolean consoleOutputEnabled;
    boolean fileOutputEnabled;
    String logFilePath;
    private PrintWriter fileWriter;

    private Logger() {
        // Default settings
        consoleOutputEnabled = true;
        fileOutputEnabled = true;
        logFilePath = Params.LOG_FILE;
        try {
            fileWriter = new PrintWriter(new FileWriter(logFilePath, true));
        } catch (IOException e) {
            e.printStackTrace(); // Handle file initialization error
        }
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
        if (fileWriter != null) {
            fileWriter.close(); // Close the old log file writer
        }
        logFilePath = filePath;
        try {
            fileWriter = new PrintWriter(new FileWriter(logFilePath, true));
        } catch (IOException e) {
            e.printStackTrace(); // Handle file initialization error
        }
    }


    public void log(String message) {
        if (consoleOutputEnabled) {
            System.out.println(message);
        }
        if (fileOutputEnabled) {
            fileWriter.println(message);
            fileWriter.flush(); // Flush the buffer to ensure the message is written immediately
        }
    }

    public void close() {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }
}
