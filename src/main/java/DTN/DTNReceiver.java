package DTN;

import AlertFactory.AlertFactory;
import AlertFactory.GeneratedAlert;
import AlertList.Display;
import AlertList.IncomingAlertsBuffer;
import Alerts.Alert;
import Statistics.Statistics;
import Utils.Vars;
import com.google.errorprone.annotations.Var;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class DTNReceiver implements Runnable{

    private MulticastSocket multicastSocket;
    private int port;
    private DTNAddressList addressList;
    private IncomingAlertsBuffer alertsBuffer;
    private Display display;

    AtomicBoolean running = new AtomicBoolean(false);
    Thread receiver;

    private ReentrantLock lock;

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

    public void start(){
        receiver = new Thread(this);
        receiver.start();
    }

    public void stop(){
        running.set(false);
    }

    public void interrupt(){
        running.set(false);
        receiver.interrupt();
    }

    @Override
    public void run() {
        // System.out.println("listening for incoming packets...");
        running.set(true);

        try {
            multicastSocket.joinGroup(addressList.getGroupAddress());
        } catch (IOException e) {
            System.out.println("failed to join multicast group " + addressList.getGroupAddress());
            e.printStackTrace();
        }

        try {
            byte[] buffer = new byte[8192];

            while (running.get()){
                Thread.sleep(100);
                DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
                try {
                    multicastSocket.receive(dp);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                String s = new String(dp.getData(), 0, dp.getLength());
                String receivedAddress = dp.getAddress().toString();

                if(Vars.DEBUG_ENABLED) System.out.println("RECEIVED DATA FROM ADDRESS: " + receivedAddress);

                Gson gson = new Gson();
                JsonObject jo = gson.fromJson(s, JsonObject.class);

                if(Vars.DEBUG_ENABLED) System.out.println("RECEIVER JSON DATA: " + jo.toString());


                AlertFactory alertFactory = new AlertFactory();
                GeneratedAlert generatedAlert = alertFactory.generateFromJson(jo);
                Alert a = generatedAlert.getAlert();

                if (!alertsBuffer.hasAlert(a) && !display.hasAlert(a) && !a.isSelfGenerated()) {
                    try {
                        Statistics stats = Statistics.getInstance();
                        stats.increaseReceivedAlerts(a.getDescription());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    try {
                        lock.lock();
                        display.addAlert(a);
                        display.appendToDisplayFile(a.toString());

                        if(!a.isRsuGenerated() || !a.isRsuRetransmitted())
                            alertsBuffer.addAlert(a);

                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        lock.unlock();
                    }

                }
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.println("Receiver interrupted");
        }
        System.out.println("terminating receiver...");
    }
}
