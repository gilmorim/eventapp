package DTN;

import AlertFactory.AlertFactory;
import AlertFactory.GeneratedAlert;
import AlertList.Display;
import AlertList.IncomingAlertsBuffer;
import Alerts.Alert;
import Statistics.Statistics;
import Utils.Vars;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.locks.ReentrantLock;

public class DTNReceiver implements Runnable{

    private MulticastSocket multicastSocket;
    private int port;
    private DTNAddressList addressList;
    IncomingAlertsBuffer alertsBuffer;
    Display display;

    ReentrantLock lock;

    public DTNReceiver(DTNAddressList addressList, IncomingAlertsBuffer alertsBuffer, Display display, ReentrantLock lock) throws IOException{
        port = Vars.MULTICAST_PORT;
        multicastSocket = new MulticastSocket(port);
        this.addressList = addressList;
        this.alertsBuffer = alertsBuffer;
        this.display = display;
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

    public IncomingAlertsBuffer getAlertsBuffer() {
        return alertsBuffer;
    }

    public void setAlertsBuffer(IncomingAlertsBuffer alertsBuffer) {
        this.alertsBuffer = alertsBuffer;
    }

    @Override
    public void run() {
        System.out.println("listening for incoming packets...");
        try {
            multicastSocket.joinGroup(addressList.getGroupAddress());
            System.out.println("joined multicast group " + addressList.getGroupAddress());
        } catch (IOException e) {
            System.out.println("failed to join multicast group " + addressList.getGroupAddress());
            e.printStackTrace();
        }
        byte[] buffer = new byte[8192];

        while (true){
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            try {
                multicastSocket.receive(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String s = new String(dp.getData(), 0, dp.getLength());
            String receivedAddress = dp.getAddress().toString();
            //System.out.println("Receive message " + s + " from " + receivedAddress);

            Gson gson = new Gson();
            JsonObject jo = gson.fromJson(s, JsonObject.class);


            AlertFactory alertFactory = new AlertFactory();
            GeneratedAlert generatedAlert = alertFactory.generateFromJson(jo);
            Alert a = generatedAlert.getAlert();;

            if(alertsBuffer.hasAlert(a) || display.hasAlert(a) || a.isSelfGenerated()){
                System.out.println("redundant alert, ignoring...");
            } else {
                try {
                    Statistics stats = Statistics.getInstance();
                    stats.increaseReceivedAlerts(a.getDescription());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    lock.lock();
                    display.addAlert(a);
                    alertsBuffer.addAlert(a);
                } finally {
                    lock.unlock();
                }
            }
        }
    }
}
