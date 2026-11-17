import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SocketOptionsDemo {
    public static void main(String[] args) {
        try (Socket socket = new Socket()) {

            // 1. Configure options before connecting
            socket.setReuseAddress(true);

            // Request a 64 KB receive buffer before connecting
            socket.setReceiveBufferSize(64 * 1024);

            // Request a 64 KB send buffer
            socket.setSendBufferSize(64 * 1024);

            // Disable Nagle's algorithm
            socket.setTcpNoDelay(true);

            // Set a 3-second read timeout
            socket.setSoTimeout(3000);

            // Enable TCP keep alive
            socket.setKeepAlive(true);

            // Receive urgent TCP data inline
            socket.setOOBInline(true);

            // Set IP traffic class

            socket.setTrafficClass(0x10);

            // Disable SO_LINGET
            socket.setSoLinger(false, 0);

            // 2. Connect with a 5-seconds connection timeout
            socket.connect(new InetSocketAddress("localhost", 5000), 5000);

            System.out.println("Connected Successfully!");

            // 3. Display configured options
            System.out.println("\n--- Socket Options ---");

            System.out.println(
                    "TCP_NODELAY: " + socket.getTcpNoDelay());

            System.out.println(
                    "SO_LINGER: " + socket.getSoLinger());

            System.out.println(
                    "SO_TIMEOUT: " + socket.getSoTimeout() + " ms");

            System.out.println(
                    "SO_RCVBUF: " + socket.getReceiveBufferSize());

            System.out.println(
                    "SO_SNDBUF: " + socket.getSendBufferSize());

            System.out.println(
                    "SO_KEEPALIVE: " + socket.getKeepAlive());

            System.out.println(
                    "SO_REUSEADDR: " + socket.getReuseAddress());

            System.out.println(
                    "SO_OOBINLINE: " + socket.getOOBInline());

            System.out.println(
                    "IP_TOS: " + socket.getTrafficClass());

            // 4. Attempt to read one byte from the server
            System.out.println("\nWaiting for server response...");
            try {
                InputStream input = socket.getInputStream();
                int data = input.read();

                if (data == -1) {
                    System.out.println("Server closed the connection");
                } else {
                    System.out.println("Received character: " + (char) data);
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Read timeout: server didnot send data in 3 seconds.");
            }
        } catch (IOException e) {
            System.out.println("Socket Error: " + e.getMessage());
        }
    }
}