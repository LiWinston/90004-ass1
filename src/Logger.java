import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;

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
        if (instance == null) synchronized (lock) {
            if (instance == null) {
                instance = new Logger();
                instance.writeHeader(); // Write header when initializing
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

    public void log(Patient patient, String message) {
        if (patient.Severe()) {
            log("Patient " + patient.getId() + " (S)" + message);
        } else {
            log("Patient " + patient.getId() + message);
        }
    }



    public void close() {
        if (fileWriter != null) {
            fileWriter.close();
        }
    }

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
