import java.net.HttpURLConnection;
import java.net.URL;

public class HttpHeader {
    public static void main(String[] args) {
        try{
            URL url=new URL("https://www.google.com");
            HttpURLConnection connection=(HttpURLConnection)url.openConnection();

            connection.setRequestMethod("GET");
            connection.connect();

            System.out.println(connection.getHeaderField(1));
            connection.getHeaderFields().forEach((key,value)->{
                System.out.println(key+": "+value);
            });

        }catch(Exception e)
        {
            e.printStackTrace();    
        }
    }
}
