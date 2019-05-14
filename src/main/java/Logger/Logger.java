package Logger;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;

public class Logger {
    private File transmissionLogs;
    private File recepetionLogs;
    private File generatedAlertLogs;

    private PrintWriter transmissionWriter;
    private PrintWriter receptionWriter;
    private PrintWriter generatedWriter;

    private static Logger logger = null;

    public Logger() throws IOException {
        transmissionLogs = new File("./src/main/resources/logs/transmission.log");
        recepetionLogs = new File("./src/main/resources/logs/reception.log");
        generatedAlertLogs = new File("./src/main/resources/logs/generation.log");

        if(!transmissionLogs.exists())  transmissionLogs.createNewFile();
        if(!recepetionLogs.exists())  recepetionLogs.createNewFile();
        if(!generatedAlertLogs.exists()) generatedAlertLogs.createNewFile();

        transmissionWriter = new PrintWriter(transmissionLogs);
        receptionWriter = new PrintWriter(recepetionLogs);
        generatedWriter = new PrintWriter(generatedAlertLogs);
    }

    public File getTransmissionLogs() {
        return transmissionLogs;
    }

    public void setTransmissionLogs(File transmissionLogs) {
        this.transmissionLogs = transmissionLogs;
    }

    public File getRecepetionLogs() {
        return recepetionLogs;
    }

    public void setRecepetionLogs(File recepetionLogs) {
        this.recepetionLogs = recepetionLogs;
    }

    public File getGeneratedAlertLogs() {
        return generatedAlertLogs;
    }

    public void setGeneratedAlertLogs(File generatedAlertLogs) {
        this.generatedAlertLogs = generatedAlertLogs;
    }


    public static Logger getInstance() throws IOException {
        if(logger == null)
            logger = new Logger();

        return logger;
    }
    public void logTransmissionSuccess(String message) {
        transmissionWriter.println("SUCCESS " + message);
    }

    public void logTransmissionFail(String message) {
        transmissionWriter.println("FAILED " + message);
    }

    public void logReceptionSuccess(String message) {
        receptionWriter.println("SUCCESS " + message);
    }

    public void logReceptionFail(String message) {
        receptionWriter.println("FAILED " + message);
    }

    public void logGenerationSuccess(String message) {
        generatedWriter.println("SUCCESS " + message);
    }

    public void logGenerationFail(String message) {
        generatedWriter.println("FAIL " + message);
    }
}