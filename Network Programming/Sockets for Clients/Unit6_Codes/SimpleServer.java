
import java.io.*;
import java.net.*;

public class SimpleServer {

    public static void main(String[] args) {

        try (ServerSocket serverSocket = new ServerSocket(5000)) {

            System.out.println("Server is waiting...");

            try (Socket clientSocket = serverSocket.accept();
                    BufferedReader input = new BufferedReader(
                            new InputStreamReader(
                                    clientSocket.getInputStream()));
                    PrintWriter output = new PrintWriter(
                            clientSocket.getOutputStream(), true)) {

                System.out.println("Client connected!");

                String message = input.readLine();

                System.out.println("Client says: " + message);

                output.println("Hello Client! Message received.");

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
