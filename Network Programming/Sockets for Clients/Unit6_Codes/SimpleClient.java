import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class SimpleClient {
    public static void main(String[] args) {
        try(Socket socket=new Socket("localhost",5000);

        PrintWriter output=new PrintWriter(socket.getOutputStream(),true);

        BufferedReader input=new BufferedReader(new InputStreamReader(socket.getInputStream()))
    )
    {
      System.out.println("Connected to server!");

      //send message
      output.println("Hello server!");

      //receive response
      String response=input.readLine();

      System.out.println("Server says: "+response);
    }catch(IOException e)
    {
        e.printStackTrace();
    }
    }
}
