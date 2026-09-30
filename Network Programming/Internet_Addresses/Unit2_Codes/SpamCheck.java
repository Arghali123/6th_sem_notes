package Internet_Addresses;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class SpamCheck {
    public static void main(String[] args) {
        String address="192.168.10.1";
        if(isSpam(address))
        {
            System.out.println(address+" is spam address");
        }else
        {
            System.out.println(address+" is valid address");
        }
        
    }

    private static boolean isSpam(String address)
    {
     try{
        InetAddress addr=InetAddress.getByName(address);
        byte[] quadAddresses=addr.getAddress();
        String DNSBL="spamhaus.org/sbl";

        for(byte octet:quadAddresses)
        {
            int unsignedByte=octet < 0 ? octet+256 : octet;
            DNSBL=unsignedByte+"."+DNSBL;
            System.out.println(DNSBL);
        }

        InetAddress.getByName(DNSBL);
        return true;
    
     }catch(UnknownHostException ex)
     {
        return false;
     }
    }
}
