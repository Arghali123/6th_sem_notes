import java.net.Authenticator;
import java.net.InetAddress;
import java.net.PasswordAuthentication;

public class AuthenticationExample {

    public static void main(String[] args) {
        Authenticator authenticator = new Authenticator() {
            
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                String username = "daenish";
                char[] password = "12345".toCharArray();

                return new PasswordAuthentication(username, password);
            }
        };

        // Register default authenticator
        Authenticator.setDefault(authenticator);
        System.out.println("Authenticator has been configured");

        // Use the static method to trigger authentication request
        PasswordAuthentication auth = Authenticator.requestPasswordAuthentication(
                "example.com", // host
                (InetAddress) null, // site address
                80, // port
                "http", // protocol
                "Authentication Required", // prompt
                "basic" // scheme
        );

        if (auth != null) {
            System.out.println("Username: " + auth.getUserName());
            System.out.println("Password: " + new String(auth.getPassword()));
        }
    }
}