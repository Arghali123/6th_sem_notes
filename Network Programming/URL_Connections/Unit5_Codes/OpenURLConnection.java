import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.io.IOException;

public class OpenURLConnection {

    public static void main(String[] args) throws MalformedURLException{

        try {
            // Step 1: Create URL object
            URL url = new URL("https://example.com");

            // Step 2: Open connection
            URLConnection connection = url.openConnection();

            System.out.println("Connection object created.");

            // Step 3: Establish actual connection
            connection.connect();

            System.out.println("Connection established!");

            // Step 4: Display content type
            System.out.println(
                "Content Type: " + connection.getContentType()
            );

            // Step 5: Display content length
            System.out.println(
                "Content Length: " + connection.getContentLength()
            );

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}