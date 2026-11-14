import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.List;

public class ConfigureRequestHeaders
{
    public static void main(String[] args) {
        try {
            URL url=new URL("https://httpbin.org/anything");

            URLConnection connection=url.openConnection();

            //Configure request headers
            connection.setRequestProperty("User-Agent", "Java-Network-Client");
            connection.setRequestProperty("Accept", "application/json");
            connection.setRequestProperty("Accept-Language", "en-US");

            //Add another value to a header
            connection.setRequestProperty("Accept-Language", "en");

            //Display specific request header
            System.out.println("User-Agent: "+connection.getRequestProperty("User-Agent"));

            //Displat all configured request headers
            System.out.println("\nAll request headers:");
            Map<String,List<String>> headers=connection.getRequestProperties();

            headers.forEach((key,values)->
            System.out.println(key+": "+values)
        );

        //Send request and read response
        try(
            BufferedReader reader=new BufferedReader(new InputStreamReader(connection.getInputStream(),StandardCharsets.UTF_8))
        )
        {
            System.out.println("\nServer Response: ");
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }


        } catch (IOException e) {
            System.out.println("Error: "+e.getMessage());
        }
    }
}