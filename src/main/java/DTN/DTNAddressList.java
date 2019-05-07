package DTN;

import Utils.Vars;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class DTNAddressList {

    // TODO: RETHINK IMPLEMENTATION OF IGNORED INTERFACES AND ADDRESSES
    private InetAddress groupAddress;
    private List<InetAddress> addressList;
    private List<NetworkInterface> ignoredInterfaces;

    public DTNAddressList(InetAddress groupAddress){
        this.groupAddress = groupAddress;
        addressList = new ArrayList<>();
        ignoredInterfaces = new ArrayList<>();
    }

    public InetAddress getGroupAddress() {
        return groupAddress;
    }

    public void setGroupAddress(InetAddress groupAddress) {
        this.groupAddress = groupAddress;
    }

    public List<InetAddress> getAddressList() {
        return addressList;
    }

    public void setAddressList(List<InetAddress> addressList) {
        this.addressList = addressList;
    }

    public List<NetworkInterface> getIgnoredAddresses() {
        return ignoredInterfaces;
    }

    public void setIgnoredAddresses(List<NetworkInterface> ignoredInterfaces) {
        this.ignoredInterfaces = ignoredInterfaces;
    }

    public int initialize(){

        // see if address is not null and is a valid multicast address
        if(groupAddress == null || !groupAddress.isMulticastAddress())
            return Vars.INVALID_MC_ADDRESS;

        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()){
                NetworkInterface networkInterface = interfaces.nextElement();

                if(networkInterface.isLoopback() || !networkInterface.isUp() || !networkInterface.supportsMulticast()){
                   ignoredInterfaces.add(networkInterface);
                   continue;
                }

                Enumeration<InetAddress> addressesEnum = networkInterface.getInetAddresses();

                while(addressesEnum.hasMoreElements()){
                    InetAddress address = addressesEnum.nextElement();

                    if(groupAddress.getClass() != address.getClass()) continue; // ask teacher about these
                    if((groupAddress.isMCLinkLocal() && address.isLinkLocalAddress())
                            || !groupAddress.isMCLinkLocal() && !address.isLinkLocalAddress()){
                        addressList.add(address);
                    }
                }
            }
        } catch (SocketException e) {
            System.out.println("socket exception " + e.getMessage());
            e.printStackTrace();
        }

        return 0;
    }
}
