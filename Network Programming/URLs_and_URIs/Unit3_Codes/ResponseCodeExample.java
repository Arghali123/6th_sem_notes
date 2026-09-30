package URLs_and_URIs;

import java.net.HttpURLConnection;
import java.net.URL;

public class ResponseCodeExample {
    public static void main(String[] args) {
        try{
         URL url=new URL("https://google.com");
         
         HttpURLConnection connection=(HttpURLConnection)url.openConnection();

         connection.setRequestMethod("GET");

         int responseCode=connection.getResponseCode();

         System.out.println("HTTP Response Code: "+responseCode);

         if(responseCode == HttpURLConnection.HTTP_OK)
         {
            System.out.println("The web page exist");
         }else
         {
            System.out.println("Failed! Server returned code: "+responseCode);
         }
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
