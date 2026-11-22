import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

public class FullHttpServer {
    public static void main(String[] args) throws IOException {
        // Create a HTTP server on port 8082

        HttpServer server = HttpServer.create(new InetSocketAddress("127.0.0.1", 8082), 0);

        // Route 1: Home Page
        server.createContext("/", exchange -> {

            String path = exchange.getRequestURI().getPath();

            if (!path.equals("/")) {
                sendText(exchange, 404, "Page not found");
                return;
            }

            if (!exchange.getRequestMethod().equals("GET")) {
                exchange.getResponseHeaders().set("Allow", "GET");

                sendText(exchange, 405, "Method not allowed");

                return;
            }

            String html = """
                    <!DOCTYPE html>
                    <html>
                    <head>
                        <title>Java HTTP Server</title>
                    </head>
                    <body>
                        <h1>Welcome!</h1>
                        <p>My Java HTTP server is working.</p>
                        <a href="/api/hello">Hello API</a>
                    </body>
                    </html>
                    """;
            exchange.getResponseHeaders().set("Context-Type", "text/html; charset=UTF-8");
            sendText(exchange, 200, html);
        });

        // Route 2: JSON API
        server.createContext("/api/hello", exchange -> {
            if (!exchange.getRequestMethod().equals("GET")) {
                exchange.getResponseHeaders().set("Allow", "GET");

                sendText(exchange, 405, "Method not allowed");

                return;
            }
            String json = """
                    {
                        "message": "Hello from Java HTTP Server",
                        "language": "Java",
                        "status": "success"
                    }
                    """;
            exchange.getResponseHeaders().set("Context-Type", "application/json; charset=UTF-8");

            sendText(exchange, 200, json);
        });

        // Route 3: Current server time
        server.createContext("/api/time", exchange -> {
            if (!exchange.getRequestMethod().equals("GET")) {
                exchange.getResponseHeaders().set("Allow", "GET");

                sendText(exchange, 405, "Method not alloed");

                return;
            }

            String time = LocalDateTime.now().toString();
            String json = "{\"serverTime\":\"" + time + "\"}";

            exchange.getResponseHeaders().set("Context-Type", "application/json; charset=UTF-8");

            sendText(exchange, 200, json);
        });

        // Handle requests using a pool of worker threads
        server.setExecutor(Executors.newFixedThreadPool(10));

        // Start the server
        server.start();

        System.out.println("Server running at http://localhost:8082");
        System.out.println("Try /api/hello and /api/time");
    }

    private static void sendText(HttpExchange exchange, int statusCode, String response) throws IOException {
        byte[] bytes = response.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(statusCode, bytes.length);

        try (OutputStream output = exchange.getResponseBody()) {
            output.write(bytes);
        }
    }
}
