
import java.net.*;
import java.io.*;

public class SocketMethodsDemo {

    public static void main(String[] args) {

        try (Socket socket =
                     new Socket("localhost", 5000)) {

            System.out.println(
                    "Connected: " + socket.isConnected());

            System.out.println(
                    "Closed: " + socket.isClosed());

            System.out.println(
                    "Remote address: " +
                    socket.getInetAddress());

            System.out.println(
                    "Remote port: " + socket.getPort());

            System.out.println(
                    "Local address: " +
                    socket.getLocalAddress());

            System.out.println(
                    "Local port: " +
                    socket.getLocalPort());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
