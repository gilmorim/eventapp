package Runnables;

import AlertList.Display;

import java.text.ParseException;
import java.util.TimerTask;

public class AlertRemover extends TimerTask {
    Display alertList;
    public AlertRemover(Display alertList){
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
