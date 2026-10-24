
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpKeepAliveDemo {

    public static void main(String[] args) throws Exception {

        // Create one reusable HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Build the first GET request
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get"))
                .GET()
                .build();

        // Send the first request
        HttpResponse<String> response1 = client.send(
                request1,
                HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("First request status: "
                + response1.statusCode());

        // Build the second GET request
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get"))
                .GET()
                .build();

        // Send the second request using the same client
        HttpResponse<String> response2 = client.send(
                request2,
                HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("Second request status: "
                + response2.statusCode());

        System.out.println("Second response body:");
        System.out.println(response2.body());
    }
}
