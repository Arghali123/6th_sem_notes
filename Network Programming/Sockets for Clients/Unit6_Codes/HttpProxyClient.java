import java.net.InetSocketAddress;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpProxyClient {
    public static void main(String[] args) {
        try{
            ProxySelector proxySelector=ProxySelector.of(new InetSocketAddress("127.0.0.1",8080));

            HttpClient client=HttpClient.newBuilder().proxy(proxySelector).build();

            HttpRequest request=HttpRequest.newBuilder().uri(URI.create("http://example.com")).GET().build();

            HttpResponse<String> response=client.send(request,HttpResponse.BodyHandlers.ofString());

            System.out.println("Status: "+response.statusCode());
            System.out.println(response.body());
        }catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}
