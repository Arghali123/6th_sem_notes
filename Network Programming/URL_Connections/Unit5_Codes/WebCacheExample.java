import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class WebCacheExample
{
    public static void main(String[] args) {
        try
        {
            //Step 1: Create URL
            URL url=new URL("https://example.com");

            //Step 2: Open Connection
            URLConnection connection=url.openConnection();

            //Step 3: Enable caching
            connection.setUseCaches(true);

            //Step 4: Check caching configuration
            System.out.println("Caching enabled: "+connection.getUseCaches());

            //Step 5: Retrieve server response
            try(InputStream input=connection.getInputStream())
            {
             byte[] buffer=new byte[1024];

             int bytesRead;

             while((bytesRead = input.read(buffer)) != -1)
             {
               System.out.println("Received bytes: "+bytesRead);
             }
            }
        }catch(IOException e)
        {
            System.out.println("Error: "+e.getMessage());
        }
    }
}