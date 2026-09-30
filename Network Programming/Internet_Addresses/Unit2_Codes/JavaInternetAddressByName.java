package Internet_Addresses;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class JavaInternetAddressByName {
    public static void main(String[] args) {
        try{
            // InetAddress address=InetAddress.getByName("google.com");
            // System.out.println(address);

            // InetAddress address=InetAddress.getLocalHost();
            // System.out.println(address);

            InetAddress[] addresses=InetAddress.getAllByName("google.com");
            for(InetAddress address: addresses)
            {
                System.out.println(address);
            }
        }catch(UnknownHostException ex)
        {
            System.out.println("Couldnot find www.javatpoint.com");
        }
    }
}
