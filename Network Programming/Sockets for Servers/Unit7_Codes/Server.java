import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;


public class Server
{
    public static void main(String[] args) {
        try(ServerSocket server=new ServerSocket(5000))
        {
          System.out.println("Server started on port 5000");
          System.out.println("Waiting for client...");

          try(Socket client=server.accept())
          {
            System.out.println("Client connected!");

            BufferedReader input=new BufferedReader(new InputStreamReader(client.getInputStream()));

            PrintWriter output=new PrintWriter(client.getOutputStream(),true);

            String message=input.readLine();
            System.out.println("Client says: "+message);

            System.out.println("Hello! Message received by server.");
          }
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}