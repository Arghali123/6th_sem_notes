import java.net.*;
import java.util.Enumeration;

public class NetworkInterfaceGetters {
    public static void main(String[] args) throws Exception {

        Enumeration<NetworkInterface> interfaces =
                NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {

            NetworkInterface network =
                    interfaces.nextElement();

            System.out.println("Name: "
                    + network.getName());

            System.out.println("Display Name: "
                    + network.getDisplayName());

            System.out.println("Index: "
                    + network.getIndex());

            System.out.println("MAC Address: "
                    + getMacAddress(network));

            System.out.println("IP Addresses:");

            Enumeration<InetAddress> addresses =
                    network.getInetAddresses();

            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();

                System.out.println("  "
                        + address.getHostAddress());
            }

            System.out.println("-------------------------");
        }
    }

    // Method to convert MAC address into readable format
    public static String getMacAddress(NetworkInterface network)
            throws Exception {

        byte[] mac = network.getHardwareAddress();

        if (mac == null) {
            return "Not Available";
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < mac.length; i++) {

            result.append(String.format("%02X", mac[i]));

            if (i < mac.length - 1) {
                result.append("-");
            }
        }

        return result.toString();
    }
}