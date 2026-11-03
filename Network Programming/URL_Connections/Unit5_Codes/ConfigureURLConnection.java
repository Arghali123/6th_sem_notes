import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class ConfigureURLConnection {
    public static void main(String[] args) {
        try{

            //Step 1: Create URL
            URL url=new URL("https://example.com");

            //Step 2: Open Connection
            URLConnection connection=url.openConnection();

            //Step 3: Configure connection BEFORE connecting
            connection.setAllowUserInteraction(false);
            connection.setDoInput(true);
            connection.setDoOutput(false);
            connection.setIfModifiedSince(0);
            connection.setUseCaches(false);

            //Connection timeout: 5 seconds
            connection.setConnectTimeout(5000);

            //Read timeout: 10 seconds
            connection.setReadTimeout(10000);

            //4. Display Configuration
            System.out.println("URL: "+connection.getURL());
            System.out.println("Allow Interaction: "+connection.getAllowUserInteraction());
            System.out.println("Do input: "+connection.getDoInput());
            System.out.println("Do output: "+connection.getDoOutput());
            System.out.println("Use Caches: "+connection.getUseCaches());
            System.out.println("Connect Timeout: "+connection.getConnectTimeout());
            System.out.println("Read Timeout: "+connection.getReadTimeout());

            //Step 5: Establish connection
            connection.connect();
            System.out.println("\nConnection Established");

            //Step 6: Read server response
            try(BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream(),StandardCharsets.UTF_8))){
              String line;

              while((line = reader.readLine()) != null)
              {
                System.out.println(line);
              }
            }


        }catch(IOException e)
        {
            System.out.println("Error: "+e.getMessage());
        }
    }
}
