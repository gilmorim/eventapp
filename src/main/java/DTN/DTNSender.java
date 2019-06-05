package DTN;

import AlertList.IncomingAlertsBuffer;
import Alerts.Alert;
import Statistics.Statistics;
import Utils.Vars;
import Vehicle.Vehicle;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.ParseException;
import java.util.Random;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

public class DTNSender extends TimerTask {
    private MulticastSocket multicastSocket;
    private int port;
    private DTNAddressList addressList;
    private IncomingAlertsBuffer alertBuffer;
    private int delayInMillis;
    private int nextAlertIndex;

    private ReentrantLock lock;

    public DTNSender(DTNAddressList addressList, IncomingAlertsBuffer alertBuffer, ReentrantLock lock) throws IOException {
        multicastSocket = new MulticastSocket();
        port = Vars.MULTICAST_PORT;
        this.addressList = addressList;
        this.alertBuffer = alertBuffer;
        delayInMillis = new Random(System.currentTimeMillis()).nextInt(3000);
        nextAlertIndex = 0;
        this.lock = lock;
    }

    public MulticastSocket getMulticastSocket() {
        return multicastSocket;
    }

    public void setMulticastSocket(MulticastSocket multicastSocket) {
        this.multicastSocket = multicastSocket;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public DTNAddressList getAddressList() {
        return addressList;
    }

    public void setAddressList(DTNAddressList addressList) {
        this.addressList = addressList;
    }

    public IncomingAlertsBuffer getAlertBuffer() {
        return alertBuffer;
    }

    public void setAlertBuffer(IncomingAlertsBuffer alertList) {
        this.alertBuffer = alertList;
    }

    public int getDelayInMillis() {
        return delayInMillis;
    }

    public void setDelayInMillis(int delayInMillis) {
        this.delayInMillis = delayInMillis;
    }

    public int getNextAlertIndex() {
        return nextAlertIndex;
    }

    public void setNextAlertIndex(int nextAlertIndex) {
        this.nextAlertIndex = nextAlertIndex;
    }

    public void sendSingleAlert(Alert a) throws IOException {
        String message = a.toJson();
        DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(),
                addressList.getGroupAddress(), port);

        for (InetAddress address : addressList.getAddressList()) {
            multicastSocket.setInterface(address);

            if(Vars.DEBUG_ENABLED) System.out.println("sending through interface of address: " + address.toString());

            multicastSocket.send(dp);
        }
    }


    public void run() {
        Alert nextAlert;
        Vehicle v = Vehicle.getInstance();
        // check for empty buffer, if so do not even attempt to transmit
        if (alertBuffer.getAlertBuffer().size() > 0) {
            try {
                lock.lock();
                nextAlert = alertBuffer.getAlertBuffer().get(nextAlertIndex);
            } finally {
                lock.unlock();
            }

            if(Vars.DEBUG_ENABLED) System.out.println("NEXT ALERT INDEX: " + nextAlertIndex);

            // check if next alert in queue is not null
            if (nextAlert != null) {
                try{
                    // if next alert in transmission queue is expired, remove it from buffer and do not transmit
                    if(nextAlert.isExpired() || !nextAlert.isTransmittable()) {

                        if(Vars.DEBUG_ENABLED) System.out.println("expired alert, not transmitting...");

                        try {
                            lock.lock();
                            alertBuffer.getAlertBuffer().remove(nextAlert);
                        } finally {
                            lock.unlock();
                        }
                    } else {
                        try {
                            // send alert
                            nextAlert.setLastRetransmitter(v.getVin());
                            sendSingleAlert(nextAlert);
                            // System.out.println("sent: " + nextAlert.toJson());
                            try {
                                lock.lock();
                                alertBuffer.getAlertBuffer().get(nextAlertIndex).decreaseRemainingTransmissions();
                            } finally {
                                lock.unlock();
                            }
                            Statistics stats = Statistics.getInstance();
                            stats.increaseTransmittedAlerts(nextAlert.getDescription());

                            if(Vars.DEBUG_ENABLED) System.out.println("SENT ALERT: " +  nextAlert.toJson());

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }

                } catch (ParseException p) {
                    System.out.println("invalid date format");
                }

            } else {
                System.out.println("could not retrieve alert with index " + nextAlertIndex + " from list. Skipping...");
            }

            try {
                lock.lock();
                if (nextAlertIndex < alertBuffer.getAlertBuffer().size() - 1) {
                    nextAlertIndex++;
                } else {
                    nextAlertIndex = 0;
                }
            } finally {
                lock.unlock();
            }

        } /*else {
            System.out.println("alert list empty, waiting for alerts...");
        }*/
    }
}
