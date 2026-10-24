import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpRequestBodyDemo {
    public static void main(String[] args) throws Exception{
        HttpClient client=HttpClient.newHttpClient();

        String json="""
        {
          "name": "Daenish",
          "cource": "Java",
          "marks": 90
        }
        """;

        HttpRequest request=HttpRequest.newBuilder()
        .uri(URI.create("https://httpbin.org/post"))
        .header("Content-Type", "application/json")
        .header("Accept", "application/json")
        .POST(HttpRequest.BodyPublishers.ofString(json))
        .build();

        //Send the request and receive the request
        HttpResponse<String> response=client.send(
            request, 
            HttpResponse.BodyHandlers.ofString());

            System.out.println("Status: "+response.statusCode());
            System.out.println("Response: ");
            System.out.println(response.body());
    }
}
