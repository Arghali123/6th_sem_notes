import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class SocketWriterClient {
    public static void main(String[] args) {
        String host = "example.com";
        int port = 80;

        try (
                Socket socket = new Socket(host, port);

                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII))) {
            System.out.println("Connected to server!");

            // Send HTTP request
            writer.write("GET / HTTP/1.1\r\n");
            writer.write("Host: example.com\r\n");
            writer.write("Connection: close\r\n");
            writer.write("\r\n");

            // Ensure data is sent
            writer.flush();

            System.out.println("HTTP request sent!");
        } catch (IOException e) {
            System.out.println("error: " + e.getMessage());
        }
    }
}
