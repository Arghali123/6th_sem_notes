import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Path;

public class HttpFileBodyDemo {
    public static void main(String[] args) throws Exception{
        HttpClient client=HttpClient.newHttpClient();

        Path file=Path.of("message.txt");


        HttpRequest request=HttpRequest.newBuilder()
        .uri(URI.create("https://httpbin.org/post"))
        .header("Content-Type", "text/plain")
        .POST(HttpRequest.BodyPublishers.ofFile(file))
        .build();


        HttpResponse response=client.send(request, 
            HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("Status: "+response.statusCode());
        System.out.println(response.body());
    }
}
