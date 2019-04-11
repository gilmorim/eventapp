package DTN;

import java.io.IOException;
import java.net.InetAddress;

public class TestInternet {
    public static void main(String[] args) {
        String mcastaddr = "FF02::1";

        try {
            InetAddress address = InetAddress.getByName(mcastaddr);
            DTNAddressList addressList = new DTNAddressList(address);
            DTNReceiver dtnReceiver = new DTNReceiver(addressList);
            Thread t = new Thread(dtnReceiver);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
