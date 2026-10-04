import java.net.*;
public class ProxyExample {
    public static void main(String[] args) throws Exception {
        // 1. Define the HTTP proxy address and port
        SocketAddress proxyAddress = new InetSocketAddress("192.168.1.100", 8080);
        Proxy proxy = new Proxy(Proxy.Type.HTTP, proxyAddress);

        // 2. Pass the proxy object into openConnection()
        URL url = new URL("https://example.com");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection(proxy);

        // Optional: If the proxy requires a username and password
        String encodedAuth = java.util.Base64.getEncoder()
            .encodeToString(("username:password").getBytes());
        connection.setRequestProperty("Proxy-Authorization", "Basic " + encodedAuth);

        // 3. Proceed with your code to read response headers...
        System.out.println("Response Code: " + connection.getResponseCode());
    }
}
