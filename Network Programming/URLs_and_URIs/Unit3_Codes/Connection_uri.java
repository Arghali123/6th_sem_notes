import java.net.URI;

public class Connection_uri {

    public static void main(String[] args) {
        try {
            URI urlstr = new URI(
                    "https://example.com/site/shop/products/item.html?categories=electronics&id=9876null//#region%20abcd");
            System.out.println(urlstr.getHost());
            System.out.println(urlstr.getPath());
            System.out.println(urlstr.getPort());
            System.out.println(urlstr.getQuery());
            System.out.println(urlstr.getUserInfo());
            System.out.println(urlstr.getScheme());
            System.out.println(urlstr.getSchemeSpecificPart());
            System.out.println(urlstr.getFragment());
            System.out.println(urlstr.getRawFragment());

        } catch (Exception e) {
            e.getMessage();
        }
    }
}