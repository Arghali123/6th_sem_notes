
import java.io.*;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;

public class SingleFileServer {

    private static final int PORT = 8080;
    private static final Path ROOT =
            Paths.get("www").toAbsolutePath().normalize();

    public static void main(String[] args) throws IOException {

        Files.createDirectories(ROOT);

        try (ServerSocket server = new ServerSocket(PORT)) {

            System.out.println("HTTP server running at:");
            System.out.println("http://localhost:" + PORT);

            while (true) {
                try (Socket client = server.accept()) {
                    handleRequest(client);
                } catch (IOException e) {
                    System.out.println("Request error: " + e.getMessage());
                }
            }
        }
    }

    private static void handleRequest(Socket client)
            throws IOException {

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        client.getInputStream(),
                        StandardCharsets.ISO_8859_1
                )
        );

        OutputStream output = client.getOutputStream();

        // Read the HTTP request line
        String requestLine = reader.readLine();

        if (requestLine == null || requestLine.isBlank()) {
            return;
        }

        System.out.println("Request: " + requestLine);

        String[] parts = requestLine.split(" ");

        if (parts.length != 3) {
            sendResponse(output, 400, "Bad Request",
                    "Invalid HTTP request".getBytes(
                            StandardCharsets.UTF_8),
                    "text/plain; charset=UTF-8");
            return;
        }

        String method = parts[0];
        String target = parts[1];

        // Read and discard request headers
        String header;
        while ((header = reader.readLine()) != null
                && !header.isEmpty()) {
            // Headers are not needed for this simple example
        }

        if (!method.equals("GET")) {
            sendResponse(output, 405, "Method Not Allowed",
                    "Only GET is supported".getBytes(
                            StandardCharsets.UTF_8),
                    "text/plain; charset=UTF-8");
            return;
        }

        try {
            String path = URI.create(target).getPath();

            if (path == null || path.equals("/")) {
                path = "/index.html";
            }

            // Resolve requested path safely inside ROOT
            Path file = ROOT.resolve(
                    path.substring(1)
            ).normalize();

            if (!file.startsWith(ROOT)) {
                sendResponse(output, 403, "Forbidden",
                        "Access denied".getBytes(
                                StandardCharsets.UTF_8),
                        "text/plain; charset=UTF-8");
                return;
            }

            if (!Files.isRegularFile(file)) {
                sendResponse(output, 404, "Not Found",
                        "File not found".getBytes(
                                StandardCharsets.UTF_8),
                        "text/plain; charset=UTF-8");
                return;
            }

            byte[] body = Files.readAllBytes(file);

            String contentType = Files.probeContentType(file);
            if (contentType == null) {
                contentType = "application/octet-stream";
            }

            sendResponse(output, 200, "OK",
                    body, contentType);

        } catch (IllegalArgumentException e) {
            sendResponse(output, 400, "Bad Request",
                    "Invalid request target".getBytes(
                            StandardCharsets.UTF_8),
                    "text/plain; charset=UTF-8");
        }
    }

    private static void sendResponse(
            OutputStream output,
            int status,
            String reason,
            byte[] body,
            String contentType
    ) throws IOException {

        String headers =
                "HTTP/1.1 " + status + " " + reason + "\r\n" +
                "Content-Type: " + contentType + "\r\n" +
                "Content-Length: " + body.length + "\r\n" +
                "Connection: close\r\n" +
                "\r\n";

        output.write(headers.getBytes(
                StandardCharsets.ISO_8859_1));

        output.write(body);
        output.flush();
    }
}
