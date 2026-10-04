import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FilteringProxyServer {
    private static final int PORT = 8888;
    // Thread pool to handle multiple browser connections simultaneously
    private static final ExecutorService threadPool = Executors.newCachedThreadPool();

    public static void main(String[] args) {
        System.out.println("Starting Filtering Proxy Server on port " + PORT + "...");
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            while (true) {
                Socket clientSocket = serverSocket.accept();
                // Handle each browser request in a separate thread
                threadPool.submit(() -> handleClient(clientSocket));
            }
        } catch (IOException e) {
            System.err.println("Server error: " + e.getMessage());
        }
    }

    private static void handleClient(Socket clientSocket) {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            // Read the very first line of the HTTP Request (The Request Line)
            // Example: "GET http://google.com HTTP/1.1" or "CONNECT facebook.com:443 HTTP/1.1"
            String requestLine = reader.readLine();
            if (requestLine == null || requestLine.isEmpty()) {
                clientSocket.close();
                return;
            }

            System.out.println("\n[Proxy Received Request]: " + requestLine);

            // Parse the destination host from the request line
            String targetHost = extractHost(requestLine);
            System.out.println("[Target Host Evaluated]: " + targetHost);

            // EVALUATION LOGIC (The Gatekeeper)
            if (targetHost.contains("facebook.com") || targetHost.contains("youtube.com")) {
                System.out.println("[ACCESS DENIED]: Blocking request to " + targetHost);
                sendForbiddenResponse(clientSocket, targetHost);
                return;
            }

            // If allowed, forward traffic (This basic snippet showcases an HTTP forwarding example)
            System.out.println("[ACCESS ALLOWED]: Forwarding request to " + targetHost);
            forwardTraffic(clientSocket, requestLine, reader, targetHost);

        } catch (Exception e) {
            System.err.println("Error handling client: " + e.getMessage());
        } finally {
            try { clientSocket.close(); } catch (IOException ignored) {}
        }
    }

    // Helper to pull the domain name out of raw HTTP strings
    private static String extractHost(String requestLine) {
        String[] tokens = requestLine.split(" ");
        if (tokens.length < 2) return "";
        String url = tokens[1];
        
        // Clean prefixes if present
        if (url.startsWith("http://")) url = url.substring(7);
        if (url.startsWith("https://")) url = url.substring(8);
        
        // Remove trailing paths or port numbers
        int slashIdx = url.indexOf("/");
        if (slashIdx != -1) url = url.substring(0, slashIdx);
        int colonIdx = url.indexOf(":");
        if (colonIdx != -1) url = url.substring(0, colonIdx);
        
        return url.toLowerCase();
    }

    // Sends a standard HTTP 403 response to the browser when a site is blocked
    private static void sendForbiddenResponse(Socket clientSocket, String blockedHost) throws IOException {
        OutputStream out = clientSocket.getOutputStream();
        String htmlBody = "<html><body><h1>403 Access Denied</h1><p>The proxy has blocked access to: <b>" 
                          + blockedHost + "</b></p></body></html>";
        
        String httpResponse = "HTTP/1.1 403 Forbidden\r\n" +
                              "Content-Type: text/html; charset=UTF-8\r\n" +
                              "Content-Length: " + htmlBody.length() + "\r\n" +
                              "Connection: close\r\n\r\n" + 
                              htmlBody;
        
        out.write(httpResponse.getBytes());
        out.flush();
    }

    // Simple educational forwarding mechanism for standard unencrypted HTTP web nodes
    private static void forwardTraffic(Socket clientSocket, String requestLine, BufferedReader clientReader, String host) {
        try (Socket targetSocket = new Socket(host, 80);
             OutputStream targetOut = targetSocket.getOutputStream();
             InputStream targetIn = targetSocket.getInputStream()) {
            
            // Re-transmit request line to actual server
            targetOut.write((requestLine + "\r\n").getBytes());
            
            // Re-transmit remaining client headers
            String headerLine;
            while ((headerLine = clientReader.readLine()) != null && !headerLine.isEmpty()) {
                targetOut.write((headerLine + "\r\n").getBytes());
            }
            targetOut.write("\r\n".getBytes());
            targetOut.flush();

            // Pass response directly back to client browser
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = targetIn.read(buffer)) != -1) {
                clientSocket.getOutputStream().write(buffer, 0, bytesRead);
            }
            clientSocket.getOutputStream().flush();
        } catch (IOException e) {
            System.err.println("Forwarding failed: " + e.getMessage());
        }
    }
}
