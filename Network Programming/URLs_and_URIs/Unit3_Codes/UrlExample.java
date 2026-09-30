package URLs_and_URIs;

import java.net.URL;

public class UrlExample {
    public static void main(String[] args) {
         try{
        URL url=new URL("https://admin:password123@example.com/shop/products/item.html?category=electronics&id=9876");
        System.out.println("Host: "+url.getHost());
        System.out.println("Protocal: "+url.getProtocol());
        System.out.println("Url: "+url.getUserInfo());
        System.out.println("Port: "+url.getPort());
        System.out.println("Authoruty: "+url.getAuthority());
        System.out.println("Path: "+url.getPath());
        System.out.println("Query: "+url.getQuery());
        System.out.println("Reference: "+url.getRef());
        System.out.println("File: "+url.getFile());
        System.out.println("External Form: "+url.toExternalForm());
        System.out.println("Default Port: "+url.getDefaultPort());

    }catch(Exception e)
    {
        e.printStackTrace();
    }
    }
   
}
