import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class Practice {
    public static void main(String[] args) {
        HttpClient client=HttpClient.newHttpClient();

        HttpRequest request1=HttpRequest.newBuilder()
        .uri(URI.create("https://httpbin.org/get"))
        .GET()
        .build();

       HttpResponse<String> response=client.send(,, null)
    }
}
