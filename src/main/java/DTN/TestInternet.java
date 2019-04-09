package DTN;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

public class TestInternet {
    public static void main(String[] args){
        String mcastaddr = "FF02::1";
        int port = 9999;

        try{
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            while (interfaces.hasMoreElements()){
                NetworkInterface ni = interfaces.nextElement();

                if(ni.isLoopback() || !ni.isUp() || !ni.supportsMulticast()) {
                    System.out.println("ignoring " + ni.getDisplayName());
                }

                Enumeration<InetAddress> addrs = ni.getInetAddresses();

            }
        } catch (SocketException e){
            System.out.println("socket error: " + e.getMessage());
        }

    }
}
