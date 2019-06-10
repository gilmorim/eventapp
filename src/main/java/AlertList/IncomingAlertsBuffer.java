package AlertList;

import Alerts.Alert;

import java.util.ArrayList;

public class IncomingAlertsBuffer {
    private ArrayList<Alert> alertBuffer;

    public IncomingAlertsBuffer(){
        alertBuffer = new ArrayList<Alert>();
    }

    public IncomingAlertsBuffer(ArrayList<Alert> alertBuffer) {
        this.alertBuffer = alertBuffer;
    }

    public ArrayList<Alert> getAlertBuffer() {
        return alertBuffer;
    }

    public void setAlertBuffer(ArrayList<Alert> alertBuffer) {
        this.alertBuffer = alertBuffer;
    }

    public void addAlert(Alert a){
        alertBuffer.add(a);
    }

    public boolean hasAlert(Alert alert){
        boolean exists = false;
        for(Alert a : alertBuffer){
            if(a.equals(alert)){
                exists = true;
                break;
            }
        }
        return exists;
    }

    public String toString(){
        StringBuilder sb = new StringBuilder();
        for(Alert a : alertBuffer){
            sb.append(a.toString() + "\n");
        }
        return sb.toString();
    }

    public void wipe(){
        alertBuffer.clear();
    }
}
