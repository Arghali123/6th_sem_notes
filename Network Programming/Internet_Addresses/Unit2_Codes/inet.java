package Internet_Addresses;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class inet {
    public static void main(String[] args) {
        try{
            InetAddress address=InetAddress.getByName("www.w3schools.com");
            System.out.println("Get by name"+address.getHostName());
            System.out.println("Get CanonicalHostname: "+address.getCanonicalHostName());
            
            //canonical: प्रामाणिक

            if(address.isLoopbackAddress())
            {
                System.out.println(address+": is loop back address");
            }
        }catch(UnknownHostException e)
        {
            System.out.println("Error:"+e);
        }

        //lab 1:Implementing inet address class and its methods
        //lab 2: Program to check address



        ///
    }
}
