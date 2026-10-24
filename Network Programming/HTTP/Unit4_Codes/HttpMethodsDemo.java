import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpMethodsDemo {
    private static final String URL = "https://httpbin.org/anything/students/10";

    public static void main(String[] args) throws Exception {
        HttpClient client = HttpClient.newHttpClient();

        // 1. GET: Retrieve data
        sendRequest(client, "GET", null);

        // 2. POST: Submit data
        sendRequest(client, "POST",
                "{\"name\":\"Ram\",\"course\":\"Java\"}");

        // 3. PUT: Replace a resource representation
        sendRequest(client, "PUT",
                "{\"name\":\"Ram\",\"course\":\"Spring Boot\"}");

        // 4. PATCH: Partially update a resource
        sendRequest(client, "PATCH",
                "{\"course\":\"Python\"}");

        // 5. DELETE: Request resource deletion
        sendRequest(client, "DELETE", null);

        // 6. HEAD: Retrieve headers without response content
        sendRequest(client, "HEAD", null);

    }

    private static void sendRequest(
            HttpClient client,
            String method,
            String body) throws Exception {
        HttpRequest.BodyPublisher bodyPublisher;

        if (body == null) {
            bodyPublisher = HttpRequest.BodyPublishers.noBody();
        } else {
            bodyPublisher = HttpRequest.BodyPublishers.ofString(body);
            System.out.println("Hello: "+bodyPublisher);
        }

        HttpRequest.Builder builder = HttpRequest.newBuilder()
                .uri(URI.create(URL))
                .header("Accept", "application/json");

        if (body != null) {
            builder.header("Content-Type", "application/json");
        }

        HttpRequest request = builder
                .method(method, bodyPublisher)
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        System.out.println("\nMethod: " + method);
        System.out.println("Status: " + response.statusCode());
        System.out.println("Allow header: "
                + response.headers()
                        .firstValue("Allow")
                        .orElse("Not provided"));

        if (!response.body().isEmpty()) {
            System.out.println("Response body:");
            System.out.println(response.body());
        } else {
            System.out.println("No response body.");
        }
    }
}
