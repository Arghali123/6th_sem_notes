import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    public static void main(String[] args) {

        ExecutorService pool=Executors.newFixedThreadPool(10);

        try(ServerSocket server=new ServerSocket(5002))
        {
            System.out.println("Multithreaded server started!");

            while(true)
            {
                Socket client=server.accept();

                System.out.println("Client connected: "+client.getInetAddress());

                pool.execute(()->handleCleint(client));
            }
        }catch(IOException e)
        {
            e.printStackTrace();
        }finally
        {
            pool.shutdown();
        }
    }

    private static void handleCleint(Socket client)
    {
        try(client;
            BufferedReader input=new BufferedReader(new InputStreamReader(client.getInputStream()));
            PrintWriter output=new PrintWriter(client.getOutputStream(),true)
        )
        {
         String message=input.readLine();

         System.out.println(Thread.currentThread().getName()+" handling: "+message);

         output.println("Hello! Your request was processed");
        }catch(IOException e)
        {
            System.err.println("Client handling error: "+e.getMessage());
        }
    }
}
