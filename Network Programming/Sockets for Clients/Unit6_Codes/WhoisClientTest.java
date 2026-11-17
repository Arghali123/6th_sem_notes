public class WhoisClientTest {
    public static void main(String[] args) {
        
        WhoisClient client=new WhoisClient();

        try{
            String result=client.lookup("whois.iana.org", "example.com");
            System.out.println("WHOIS RESULT: ");
            System.out.println(result);
        }catch(Exception e)
        {
            System.out.println("WHOIS lookup failed: "+e.getMessage());
        }
        
    }
}
