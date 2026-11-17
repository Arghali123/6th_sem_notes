
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;

public class SocketHttpClient {

    public static void main(String[] args) {

        String host = "example.com";
        int port = 80;

        try (Socket socket = new Socket(host, port);

             BufferedWriter writer =
                 new BufferedWriter(
                     new OutputStreamWriter(
                         socket.getOutputStream(),
                         StandardCharsets.US_ASCII));

             BufferedReader reader =
                 new BufferedReader(
                     new InputStreamReader(
                         socket.getInputStream(),
                         StandardCharsets.ISO_8859_1))) {

            System.out.println("Connected to " + host);

            // STEP 1: Write request to server
            writer.write("GET / HTTP/1.1\r\n");
            writer.write("Host: example.com\r\n");
            writer.write("Connection: close\r\n");
            writer.write("\r\n");

            writer.flush();

            System.out.println("Request sent!");
            System.out.println("\nServer Response:");

            // STEP 2: Read response from server
            String response;

            while ((response = reader.readLine()) != null) {
                System.out.println(response);
            }

            System.out.println("\nConnection completed.");

        } catch (IOException e) {
            System.out.println("Network error: " + e.getMessage());
        }
    }
}
