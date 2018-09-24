package netprog.logger;

import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FileLogger {
    private FileHandler fileTxt;
    private String className;
    private String logLevel;
    private Logger logger;

    public FileLogger(String className, String logLevel) {
        this.className = className;
        this.logLevel = logLevel;
    }

    public void setup() throws IOException {
        // AnP: get the global logger to configure it
        logger = Logger.getLogger(this.className);
        logger.setLevel(toLevel(this.logLevel));

        fileTxt = new FileHandler("logs/serverlog.log", true);
        fileTxt.setFormatter(new SimpleFormatter());
        logger.addHandler(fileTxt);
    }

    private static Level toLevel(String logLevel) {
        switch (logLevel.toLowerCase()) {
            case "severe":
                return Level.SEVERE;
            case "warning":
                return Level.WARNING;
            case "debug":
            case "error":
                return Level.FINEST;
            default:
                return Level.FINE;
        }
    }

    public Logger logger() {
        return logger;
    }
}
