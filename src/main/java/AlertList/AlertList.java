package AlertList;

import Alerts.Alert;

import java.text.ParseException;
import java.util.ArrayList;

public class AlertList {
    private ArrayList<Alert> alertList;

    public AlertList(){
        alertList = new ArrayList<Alert>();
    }

    public AlertList(AlertList al){
        alertList = al.getAlertList();
    }

    public ArrayList<Alert> getAlertList() {
        return alertList;
    }

    public void setAlertList(ArrayList<Alert> alertList) {
        this.alertList = alertList;
    }

    public synchronized void addAlert(Alert a){
        alertList.add(a);
    }

    public synchronized void  cleanExpired() throws ParseException {
        for(Alert a : alertList){
            if(a.isExpired()){
                alertList.remove(a);
            }
        }
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Alert a : alertList){
            sb.append(a.toString());
        }
        return sb.toString();
    }
}
