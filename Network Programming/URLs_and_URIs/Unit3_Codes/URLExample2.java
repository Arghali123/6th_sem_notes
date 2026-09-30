import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class URLExample2
{
    public static void main(String[] args)
    {
        try{
            URL url=new URL("https://example.com");

            URLConnection conn=url.openConnection();

            
            InputStreamReader isr=new InputStreamReader(conn.getInputStream());
            BufferedReader br=new BufferedReader(isr);
            String line;

            while ((line=br.readLine()) != null) {
                System.out.println(line);
            }

        }catch(Exception e)
        {
         e.printStackTrace();
        }
    }
}