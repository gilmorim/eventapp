package Runnables;

import AlertList.AlertList;

import java.text.ParseException;
import java.util.TimerTask;

public class AlertRemover extends TimerTask {
    AlertList alertList;
    public AlertRemover(AlertList alertList){
        this.alertList = alertList;
    }

    public void run(){
        int removedAlertsCount = 0;
        try {
            System.out.print("cleaning expired events... ");
            removedAlertsCount = alertList.cleanExpired();
            System.out.println("removed " + removedAlertsCount + " alerts");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
