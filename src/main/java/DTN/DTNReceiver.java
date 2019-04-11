package DTN;

import Utils.Vars;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

public class DTNReceiver implements Runnable{

    private MulticastSocket multicastSocket;
    private int port;
    private DTNAddressList addressList;

    public DTNReceiver(DTNAddressList addressList) throws IOException{
        multicastSocket = new MulticastSocket();
        port = Vars.MULTICAST_PORT_RECEIVE;
        this.addressList = addressList;
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

    @Override
    public void run() {
        try {
            multicastSocket.joinGroup(addressList.getGroupAddress());
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[8192];

        while (true){
            DatagramPacket dp = new DatagramPacket(buffer, buffer.length);
            try {
                System.out.println("listening for incoming packets...");
                multicastSocket.receive(dp);
            } catch (IOException e) {
                e.printStackTrace();
            }
            String s = new String(dp.getData(), 0, dp.getLength());
            String receivedAddress = dp.getAddress().toString();
            System.out.println("Receive message " + s + " from " + receivedAddress);
        }
    }
}
