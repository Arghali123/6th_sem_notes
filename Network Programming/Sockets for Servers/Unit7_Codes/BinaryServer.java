import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class BinaryServer {
    public static void main(String[] args) {
        try (ServerSocket server = new ServerSocket(5001)) {
            System.out.println("Binary server waiting...");

            try (
                    Socket client = server.accept();
                    DataInputStream input = new DataInputStream(client.getInputStream());
                    DataOutputStream output = new DataOutputStream(client.getOutputStream());) {

                        int length=input.readInt();

                        if(length < 0 || length > 1_000_000)
                        {
                            throw new IOException("Invalid data length");
                        }

                        byte[] receivedData=new byte[length];

                        input.readFully(receivedData);

                        System.out.println("Received "+receivedData.length+" bytes");
                        output.writeInt(receivedData.length);
                        output.write(receivedData);
                        output.flush();

                        System.out.println("Data send back!");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
