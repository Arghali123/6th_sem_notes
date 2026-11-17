import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.net.SocketTimeoutException;

public class UnconnectedSocketDemo {
    public static void main(String[] args) {
        try(Socket socket=new Socket())
        {
          //STEP 1: Create an unconnected socket
          System.out.println("Socket created!");

          System.out.println("Connected: "+socket.isConnected());

          //STEP 2: create a server address
          SocketAddress endpoint=new InetSocketAddress("localhost",5000);

          //STEP 3: connect with 5-second timeout
          socket.connect(endpoint, 5000);

          //STEP 4: verify connection
          System.out.println("Connected successfully!");
          System.out.println("Connected: "+socket.isConnected());
        }catch(SocketTimeoutException e)
        {
          System.out.println("Connection timeout!");
        }
        catch(IOException e)
        {
            System.out.println("Connection error: "+e.getMessage());
        }
    }
}
