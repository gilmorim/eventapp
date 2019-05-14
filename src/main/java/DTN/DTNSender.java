package DTN;

import AlertList.IncomingAlertsBuffer;
import Alerts.Alert;
import Logger.Logger;
import Utils.Vars;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.Random;
import java.util.TimerTask;

public class DTNSender extends TimerTask {
    private MulticastSocket multicastSocket;
    private int port;
    private DTNAddressList addressList;
    private IncomingAlertsBuffer alertBuffer;
    private int delayInMillis;
    private int nextAlertIndex;

    public DTNSender(DTNAddressList addressList, IncomingAlertsBuffer alertBuffer) throws IOException {
        multicastSocket = new MulticastSocket();
        port = Vars.MULTICAST_PORT_SEND;
        this.addressList = addressList;
        this.alertBuffer = alertBuffer;
        delayInMillis = new Random(System.currentTimeMillis()).nextInt(3000);
        nextAlertIndex = 0;
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
            multicastSocket.send(dp);
        }
    }


    public void run() {
        if (alertBuffer.getAlertBuffer().size() > 0) {
            Alert nextAlert = alertBuffer.getAlertBuffer().get(nextAlertIndex);
            if (nextAlert != null) {
                try {
                    sendSingleAlert(nextAlert);
                    // Logger l = Logger.getInstance();
                    // l.logTransmissionSuccess(nextAlert.toString());
                    System.out.println("sent alert " + nextAlert.toString());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                System.out.println("could not retrieve alert with index " + nextAlertIndex + " from list. Skipping...");
            }

            if (nextAlertIndex < alertBuffer.getAlertBuffer().size() - 1) {
                nextAlertIndex++;
            } else {
                nextAlertIndex = 0;
            }
        } /*else {
            System.out.println("alert list empty, waiting for alerts...");
        }*/
    }
}
