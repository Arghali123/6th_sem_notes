import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;

public class BinaryClient {
    public static void main(String[] args) {
        byte[] originalData={
            10,20,30,50,50
        };

        try(Socket socket=new Socket("localhost",5001);         
           DataOutputStream output=new DataOutputStream(socket.getOutputStream());
          DataInputStream input=new DataInputStream(socket.getInputStream()))
        {
          //send data length first
          output.writeInt(originalData.length);

          //send actual binary data
          output.write(originalData);
          output.flush();

          //receive response
          int length=input.readInt();

          if(length < 0 || length > 1_000_000)
          {
            throw new IOException("Invalid response length");
          }
          byte[] response=new byte[length];

          input.readFully(response);

          System.out.println("Received: "+Arrays.toString(response));
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
}
