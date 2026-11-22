
import java.io.*;
import java.net.*;

public class Client {

    public static void main(String[] args) {

        try (Socket socket =
                 new Socket("localhost", 5000)) {

  
            BufferedReader input = new BufferedReader(
                new InputStreamReader(
                    socket.getInputStream()
                )
            );

            System.out.println(input.readLine());

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}
