import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.PrintWriter;

public class UnboundServer {
    public static void main(String[] args) {
        try(ServerSocket server=new ServerSocket())
        {

            //Step 1: Check initial state
            System.out.println("Before binding: "+server.isBound());

            //Step 2: Bind the socket
            server.bind(new InetSocketAddress("localhost",5003));

            //Step 3: Check new state
            System.out.println("After binding: "+server.isBound());

            System.out.println("Listining port: "+server.getLocalPort());

            //Step 4: Accept client
            try(Socket client=server.accept())
            {
                System.out.println("Client connected!");

                PrintWriter output=new PrintWriter(client.getOutputStream(),true);

                output.println("Welcome to the Java server!");
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
