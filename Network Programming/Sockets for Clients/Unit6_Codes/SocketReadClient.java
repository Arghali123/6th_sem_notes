import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketReadClient {
    public static void main(String[] args) {
        String host="example.com";
        int port=80;

        try(Socket socket=new Socket(host,port);

        BufferedReader reader=new BufferedReader(new InputStreamReader(socket.getInputStream(),StandardCharsets.US_ASCII))
    ){
         System.out.println("Connected to a server!");
         String line;

         //read the response from the server
         while((line=reader.readLine()) != null)
         {
            System.out.println(line);
         }
        }catch(IOException e)
        {
          System.out.println("Error: "+e.getMessage());
        }
    }
}
