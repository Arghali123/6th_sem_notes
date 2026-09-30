package Internet_Addresses;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class GetterMethod {
    public static void main(String[] args) {

        try{

            InetAddress address=InetAddress.getByName("google.com");
    
            System.out.println("Host name: "+address.getHostName());
            System.out.println("Host address: "+address.getHostAddress());
            System.out.println("Canonical Host Name: "+address.getCanonicalHostName());
        }catch(UnknownHostException ex)
        {

        }

    }
}
