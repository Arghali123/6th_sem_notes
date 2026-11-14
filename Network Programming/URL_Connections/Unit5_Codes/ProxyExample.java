import java.net.*;

public class ProxyExample {
    public static void main(String[] args) throws Exception{
        URL url=new URL("https://example.com");

        Proxy proxy=new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1",8080));

        HttpURLConnection connection=(HttpURLConnection)url.openConnection();
        connection.setConnectTimeout(5000);
        connection.setReadTimeout(10000);

        System.out.println("Response Code: "+connection.getResponseCode());

        connection.disconnect();
    }
}
