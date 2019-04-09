package DTN;

import Alerts.Alert;
import Utils.Vars;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class DTNSender {
    private MulticastSocket multicastSocket;
    private int port;
    private DTNAddressList addressList;

    public DTNSender(DTNAddressList addressList) throws IOException {
        multicastSocket = new MulticastSocket();
        port = Vars.MULTICAST_PORT_SEND;
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

    public void sendSingleAlert(Alert a) throws IOException {
        String message = a.toJson();
        DatagramPacket dp = new DatagramPacket(message.getBytes(), message.length(),
                addressList.getGroupAddress(), port);

        for(InetAddress address : addressList.getAddressList()){
            multicastSocket.setInterface(address);
            multicastSocket.send(dp);
        }
    }
}
