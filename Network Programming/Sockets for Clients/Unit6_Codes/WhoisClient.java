import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class WhoisClient {
    private static final int WHOIS_PORT = 43;
    private static final int CONNECT_TIMEOUT = 5000;
    private static final int READ_TIMEOUT = 8000;

    public String lookup(String server, String query) throws IOException {
        if (server == null || server.isBlank()) {
            throw new IllegalArgumentException("WHOIS server cannot be empty");
        }

        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("WHOIS query cannot be empty");
        }

        try (Socket socket = new Socket()) {
            // Connect to WHOIS server
            socket.connect(new InetSocketAddress(server, WHOIS_PORT), CONNECT_TIMEOUT);

            // prevent indefinite blocking while reading
            socket.setSoTimeout(READ_TIMEOUT);

            // Send query using ASCII and CRLF
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.US_ASCII));
            writer.write(query);
            writer.write("\r\n");
            writer.flush();

            // Read response until server closes connection
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = reader.readLine()) != null) {
                response.append(line).append('\n');
            }
            return response.toString();
        }

    }
}
