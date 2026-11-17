import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.SocketAddress;

public class ProxtSocketClient {
    public static void main(String[] args) {
        String destination="example.com";
        int destinationPort=80;

        //STEP 1: Define proxy address
        SocketAddress proxyAddress=new InetSocketAddress("127.0.0.1",1080);

        //STEP 2: Create SOCKS proxy
        Proxy proxy=new Proxy(Proxy.Type
            .SOCKS, proxyAddress);

        //STEP 3: Create a socket using proxy
        try(Socket socket=new Socket(proxy))
        {
            //STEP 4: Connect to destination
                        socket.connect(
                    new InetSocketAddress(
                            destination,
                            destinationPort),
                    5000
            );

            System.out.println("Connected through SOCKS proxy!");
            System.out.println("Remote Server: "+socket.getInetAddress());
            System.out.println("Remote Port: "+socket.getPort());
        }catch(IOException  e)
        {
            System.out.println("Proxy connection failed: "+e.getMessage());
        }
    }
}
