package AlertList;

import Alerts.Alert;

import java.text.ParseException;
import java.util.ArrayList;

public class AlertList {
    private ArrayList<Alert> alertList;
    private int cleanupInterval;

    public AlertList(){
        alertList = new ArrayList<Alert>();
        cleanupInterval = 0;
    }

    public AlertList(AlertList al){
        alertList = al.getAlertList();
        cleanupInterval = al.getCleanupInterval();
    }

    public ArrayList<Alert> getAlertList() {
        return alertList;
    }

    public void setAlertList(ArrayList<Alert> alertList) {
        this.alertList = alertList;
    }

    public int getCleanupInterval() {
        return cleanupInterval;
    }

    public void setCleanupInterval(int cleanupInterval) {
        this.cleanupInterval = cleanupInterval;
    }

    public synchronized void addAlert(Alert a){
        alertList.add(a);
    }

    public synchronized int cleanExpired() throws ParseException {
        int countRemovedAlerts = 0;
        for(Alert a : alertList){
            if(a.isExpired()){
                alertList.remove(a);
                countRemovedAlerts++;
            }
        }
        return countRemovedAlerts;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Alert a : alertList){
            sb.append(a.toString());
        }
        return sb.toString();
    }
}
