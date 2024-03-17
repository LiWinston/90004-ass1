import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
/**
 * Manages logging operations for the emergency department simulator.
 * <p>
 * The Logger class provides functionality for logging messages to the console
 * and/or a log file. It also generates a header containing system and network
 * information upon initialization.
 * <p>
 * This class follows the Singleton design pattern to ensure only one instance
 * exists throughout the application.
 * <p>
 * Usage:
 * - Obtain the Logger instance using {@link #getInstance()}.
 * - Enable or disable console output using {@link #setConsoleOutputEnabled(boolean)}.
 * - Enable or disable file output using {@link #setFileOutputEnabled(boolean)}.
 * - Set the log file path using {@link #setLogFilePath(String)}.
 * - Log messages using {@link #log(String)} or {@link #log(Patient, String)}.
 * - Close the Logger instance using {@link #close()} when no longer needed.
 * <p>
 * Example:
 * <pre>{@code
 * // Enable console output
 * Logger.getInstance().setConsoleOutputEnabled(true);
 *
 * // Enable file output
 * Logger.getInstance().setFileOutputEnabled(true);
 *
 * // Set the log file path
 * Logger.getInstance().setLogFilePath("path/to/log/file.log");
 *
 * // Log a message
 * Logger.getInstance().log("This is a log message.");
 * }</pre>
 *
 * @author yongchunl@student.unimelb.edu.au
 * @version 1.0
 * @since 18 Mar 2024
 */
public class Logger {
    private static final Object lock = new Object();
    private static Logger instance;
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

    /**
     * Gets the singleton instance of the Logger class.
     *
     * @return the Logger instance
     */
    public static Logger getInstance() {
        if (instance == null) synchronized (lock) {
            if (instance == null) {
                instance = new Logger();
                instance.writeHeader(); // Write header when initializing
            }
        }
        return instance;
    }

    /**
     * Enables or disables console output.
     *
     * @param enabled true to enable console output, false to disable
     */
    public void setConsoleOutputEnabled(boolean enabled) {
        consoleOutputEnabled = enabled;
    }

    /**
     * Enables or disables file output.
     *
     * @param enabled true to enable file output, false to disable
     */
    public void setFileOutputEnabled(boolean enabled) {
        fileOutputEnabled = enabled;
    }

    /**
     * Sets the log file path.
     *
     * @param filePath the path of the log file
     */
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

    /**
     * Logs a message.
     *
     * @param message the message to be logged
     */
    public void log(String message) {
        if (consoleOutputEnabled) {
            System.out.println(message);
        }
        if (fileOutputEnabled) {
            fileWriter.println(message);
            fileWriter.flush(); // Flush the buffer to ensure the message is written immediately
        }
    }

    /**
     * Logs a message associated with a patient, indicating the patient's severity.
     *
     * @param patient the patient associated with the message
     * @param message the message to be logged
     */
    public void log(Patient patient, String message) {
        if (patient.Severe()) {
            log("Patient " + patient.getId() + " (S)" + message);
        } else {
            log("Patient " + patient.getId() + message);
        }
    }

    /**
     * Closes the Logger instance.
     */
    public void close() {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }

    // Helper method to generate header containing system and network information
    private void writeHeader() {
        StringBuilder header = new StringBuilder();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Format for date and time
        header.append("════════════════════════════════════════════════════════════════════════════════════\n");
        header.append("Simu Start Time:   ").append(dateFormat.format(new Date())).append("\n");

        // Start a new thread to get network interface information
        Thread networkThread = new Thread(() -> {
            try {
                InetAddress localhost = InetAddress.getLocalHost();
                header.append("Computer Name:     ").append(localhost.getHostName()).append("\n");
                header.append("IP Address:        ").append(localhost.getHostAddress()).append("\n");
                Enumeration<NetworkInterface> networkInterfaces = NetworkInterface.getNetworkInterfaces();
                while (networkInterfaces.hasMoreElements()) {
                    NetworkInterface networkInterface = networkInterfaces.nextElement();
                    byte[] mac = networkInterface.getHardwareAddress();
                    if (mac != null) {
                        StringBuilder macAddress = new StringBuilder();
                        for (int i = 0; i < mac.length; i++) {
                            macAddress.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
                        }
                        header.append("MAC Address:       ").append(macAddress.toString()).append("\n");
                        break; // Only get the MAC address of the first interface
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        networkThread.start(); // Start the network thread
        try {
            networkThread.join(); // Wait for the network thread to finish
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        header.append("Operating System:  ").append(System.getProperty("os.name")).append(" ").append(System.getProperty("os.version")).append("\n");
        header.append("Java Version:      ").append(System.getProperty("java.version")).append("\n");
        header.append("════════════════════════════════════════════════════════════════════════════════════\n");

        // Print the header to the console
        System.out.print(header.toString());

        fileWriter.println(header.toString());
        fileWriter.flush(); // Flush the buffer to ensure the header is written immediately
    }

}
