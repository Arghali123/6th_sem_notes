import java.io.IOException;
import java.net.Socket;

public class SocketInformationExplorer {

    public static void main(String[] args) {
        String host="localhost";
        int port=5000;

        try(Socket socket=new Socket(host,port))
        {
          System.out.println("------SOCKET INFORMATION------");

          //Remote information
          System.out.println("Remote IP: "+socket.getInetAddress());
          System.out.println("Remote Port: "+socket.getPort());
          System.out.println("Remote Endpoint: "+socket.getRemoteSocketAddress());

          //Local information
          System.out.println("Local IP: "+socket.getLocalAddress());
          System.out.println("Local Port: "+socket.getLocalPort());
          System.out.println("Local IP: "+socket.getLocalSocketAddress());

          //Connection states
          System.out.println("\n===SOCKET STATUS=== ");
          System.out.println("Connected: "+socket.isConnected());
          System.out.println("Bound: "+socket.isBound());
          System.out.println("Closes: "+socket.isClosed());
          System.out.println("Input Shutdown: "+socket.isInputShutdown());
          System.out.println("Output Shutdown: "+socket.isOutputShutdown());
        }catch(IOException e)
        {
            System.out.println("Connection error: "+e.getMessage());
        }
    }
}