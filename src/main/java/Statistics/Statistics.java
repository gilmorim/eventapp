package Statistics;

import org.apache.commons.lang.mutable.MutableInt;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;

public class Statistics {
    private int alertsTransmitted;
    private int alertsReceived;
    private int alertsGenerated;

    private HashMap<String, MutableInt> transmittedAlertsTable;
    private HashMap<String, MutableInt> receivedAlertsTable;
    private HashMap<String, MutableInt> generatedAlertsTable;

    private static Statistics statistics;

    public Statistics(){
        alertsTransmitted = 0;
        alertsReceived = 0;
        alertsGenerated = 0;

        transmittedAlertsTable = new HashMap<String, MutableInt>();
        receivedAlertsTable = new HashMap<String, MutableInt>();
        generatedAlertsTable = new HashMap<String, MutableInt>();
    }

    public int getAlertsTransmitted() {
        return alertsTransmitted;
    }

    public void setAlertsTransmitted(int alertsTransmitted) {
        this.alertsTransmitted = alertsTransmitted;
    }

    public int getAlertsReceived() {
        return alertsReceived;
    }

    public void setAlertsReceived(int alertsReceived) {
        this.alertsReceived = alertsReceived;
    }

    public int getAlertsGenerated() {
        return alertsGenerated;
    }

    public void setAlertsGenerated(int alertsGenerated) {
        this.alertsGenerated = alertsGenerated;
    }

    public HashMap<String, MutableInt> getTransmittedAlertsTable() {
        return transmittedAlertsTable;
    }

    public void setTransmittedAlertsTable(HashMap<String, MutableInt> transmittedAlertsTable) {
        this.transmittedAlertsTable = transmittedAlertsTable;
    }

    public HashMap<String, MutableInt> getReceivedAlertsTable() {
        return receivedAlertsTable;
    }

    public void setReceivedAlertsTable(HashMap<String, MutableInt> receivedAlertsTable) {
        this.receivedAlertsTable = receivedAlertsTable;
    }

    public HashMap<String, MutableInt> getGeneratedAlertsTable() {
        return generatedAlertsTable;
    }

    public void setGeneratedAlertsTable(HashMap<String, MutableInt> generatedAlertsTable) {
        this.generatedAlertsTable = generatedAlertsTable;
    }

    public static Statistics getInstance() throws IOException {
        if(statistics == null)
            statistics = new Statistics();

        return statistics;
    }

    public void increaseTransmittedAlerts(String alert) {
        alertsTransmitted++;
        if(transmittedAlertsTable.containsKey(alert)){
            transmittedAlertsTable.get(alert).increment();
        } else {
            transmittedAlertsTable.put(alert, new MutableInt(1));
        }
    }

    public void increaseGeneratedAlerts(String alert) {
        alertsGenerated++;
        if(generatedAlertsTable.containsKey(alert)){
            generatedAlertsTable.get(alert).increment();
        } else {
            generatedAlertsTable.put(alert, new MutableInt(1));
        }
    }

    public void increaseReceivedAlerts(String alert) {
        alertsReceived++;
        if(receivedAlertsTable.containsKey(alert)){
            receivedAlertsTable.get(alert).increment();
        } else {
            receivedAlertsTable.put(alert, new MutableInt(1));
        }
    }

    public String transmittedIndividual(){
        StringBuilder sb = new StringBuilder();
        for(String s : transmittedAlertsTable.keySet()){
            sb.append(s).append("\t").append(transmittedAlertsTable.get(s).intValue()).append("\n");
        }
        return sb.toString();
    }

    public String generatedIndividual(){
        StringBuilder sb = new StringBuilder();
        for(String s : generatedAlertsTable.keySet()){
            sb.append(s).append("\t").append(generatedAlertsTable.get(s)).append("\n");
        }
        return sb.toString();
    }

    public String receivedIndividual(){
        StringBuilder sb = new StringBuilder();
        for(String s : receivedAlertsTable.keySet()){
            sb.append(s).append("\t").append(receivedAlertsTable.get(s)).append("\n");
        }
        return sb.toString();
    }

    public String transmittedPercentages(){
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        for(String s : transmittedAlertsTable.keySet()){
            double percentage = (transmittedAlertsTable.get(s).doubleValue() / alertsTransmitted) * 100;
            sb.append(s).append("\t").append(df.format(percentage)).append("\n");
        }
        return sb.toString();
    }

    public String generatedPercentages(){
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        for(String s : generatedAlertsTable.keySet()){
            double percentage = (generatedAlertsTable.get(s).doubleValue() / alertsGenerated) * 100;
            sb.append(s).append("\t").append(df.format(percentage)).append("\n");
        }
        return sb.toString();
    }

    public String receivedPercentages(){
        StringBuilder sb = new StringBuilder();
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);

        for(String s : receivedAlertsTable.keySet()){
            double percentage = (receivedAlertsTable.get(s).doubleValue() / alertsReceived) / 100;
            sb.append(s).append("\t").append(df.format(percentage)).append("\n");
        }
        return sb.toString();
    }
}