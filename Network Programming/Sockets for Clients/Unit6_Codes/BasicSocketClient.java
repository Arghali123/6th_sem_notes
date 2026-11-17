import java.net.Socket;
import java.io.IOException;

public class BasicSocketClient
{
    public static void main(String[] args) {
        String host="localhost";
        int port=5000;

        try(
            Socket socket=new Socket(host,port)
        )
        {
            System.out.println("Connection Successful!");
            System.out.println("Server: "+socket.getInetAddress());
            System.out.println("Remote Port: "+socket.getPort());
            System.out.println("Local Port: "+socket.getLocalPort());
        }catch(IOException e)
        {
         System.out.println("Connection failed: "+e.getMessage());
        }
    }
}