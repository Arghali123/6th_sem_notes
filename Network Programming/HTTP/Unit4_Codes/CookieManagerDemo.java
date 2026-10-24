import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.HttpCookie;

public class CookieManagerDemo {
    public static void main(String[] args) throws Exception {
        //1. Create a cookie manager
        CookieManager cookieManager=new CookieManager(
            null,
            CookiePolicy.ACCEPT_ALL
        );

        //2. Create a HTTP client that uses the manager
        HttpClient client=HttpClient.newBuilder()
        .cookieHandler(cookieManager)
        .build();

        //3. Request that a test server set a cookie
        HttpRequest setCookiRequest=HttpRequest.newBuilder()
        .uri(URI.create("https://httpbin.org/cookies/set?student=Ram"))
        .GET()
        .build();

        HttpResponse<String> firstResponse=client.send(setCookiRequest,
             HttpResponse.BodyHandlers.ofString());


        System.out.println("First response status: "+firstResponse.statusCode());

        //4. Access the cookie store
        CookieStore store=cookieManager.getCookieStore();

        System.out.println("\nStored cookies: ");

        for(HttpCookie cookie: store.getCookies())
        {
            System.out.println(cookie.getName()+" = "+cookie.getValue());
        }

        //5. Make another request using the same client
        HttpRequest secondRequest=HttpRequest.newBuilder()
        .uri(URI.create("https://httpbin.org/cookies"))
        .GET()
        .build();

        HttpResponse<String> secondResponse=client.send(secondRequest, 
            HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("\nSecond response status: "+secondResponse.statusCode());
        System.out.println(secondResponse.body());
    }
}
