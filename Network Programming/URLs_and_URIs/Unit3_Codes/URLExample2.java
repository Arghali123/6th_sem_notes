import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

public class URLExample2
{
    public static void main(String[] args)
    {
        try{
            URL url=new URL("https://example.com");

            URLConnection conn=url.openConnection();

            conn.connect();

            Map<String, List<String>> headers = conn.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {

            String headerName = entry.getKey();
            List<String> headerValues = entry.getValue();

            // Note: The HTTP status line (e.g., HTTP/1.1 200 OK) has a null key
            if (headerName == null) {
                System.out.println("Status Line: " + headerValues);
            } else {
                System.out.println(headerName + ": " + String.join(", ", headerValues));
            }
        }

        

            //connecton's method
            // System.out.println("Content Type: "+conn.getContentType());
            // System.out.println("Content length: "+conn.getContentLength());
            // System.out.println("Content Encoding: "+conn.getContentEncoding());
            // System.out.println("Date: "+conn.getDate());
            // System.out.println("Expiration: "+conn.getExpiration());
            // System.out.println("Last modified: "+conn.getLastModified());
            
            // InputStreamReader isr=new InputStreamReader(conn.getInputStream());
            // BufferedReader br=new BufferedReader(isr);
            // String line;

            // while ((line=br.readLine()) != null) {
            //     System.out.println(line);
            // }

            // Object content=url.getContent();
            // System.out.println("Content: "+content.getClass().getName());

        }catch(Exception e)
        {
         e.printStackTrace();
        }
    }
}