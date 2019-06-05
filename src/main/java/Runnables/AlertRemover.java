package Runnables;

import AlertList.Display;

import java.text.ParseException;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

public class AlertRemover extends TimerTask {
    Display alertList;
    ReentrantLock lock;
    public AlertRemover(Display alertList, ReentrantLock lock){
        this.alertList = alertList;
        this.lock = lock;
    }

    public void run(){
        int removedAlertsCount = 0;
        try {
            // System.out.print("cleaning expired events... ");
            try {
                lock.lock();
                removedAlertsCount = alertList.cleanExpired();
            } finally {
                lock.unlock();
            }
            System.out.println("removed " + removedAlertsCount + " alerts");
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
