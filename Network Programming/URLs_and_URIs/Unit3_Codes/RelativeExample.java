// package relatives_and_URIs;

import java.net.URI;
// import java.net.relative;

public class RelativeExample {
    public static void main(String[] args) {
        try {
            /*
             * relative relative=new relative(
             * "https://admin:password123@example.com/shop/products/item.html?category=electronics&id=9876"
             * );
             * System.out.println("Host: "+relative.getHost());
             * System.out.println("Protocal: "+relative.getProtocol());
             * System.out.println("relative: "+relative.getUserInfo());
             * System.out.println("Port: "+relative.getPort());
             * System.out.println("Authoruty: "+relative.getAuthority());
             * System.out.println("Path: "+relative.getPath());
             * System.out.println("Query: "+relative.getQuery());
             * System.out.println("Reference: "+relative.getRef());
             * System.out.println("File: "+relative.getFile());
             * System.out.println("External Form: "+relative.toExternalForm());
             * System.out.println("Default Port: "+relative.getDefaultPort());
             */

            URI base = new URI("https://example.com/");
            URI relative = new URI("photos/logo.png");
            URI resolved = base.resolve(relative);

            System.out.println(resolved);
            System.out.println("Host: " + relative.getHost());
            System.out.println("relative: " + relative.getUserInfo());
            System.out.println("Port: " + relative.getPort());
            System.out.println("Authoruty: " + relative.getAuthority());
            System.out.println("Path: " + relative.getPath());
            System.out.println("Query: " + relative.getQuery());

            URI uri1 = new URI("https://example.com/index1.html");
            URI uri2 = new URI("https://example.com/index2.html");

            System.out.println("Are they equal? "
                    + uri1.equals(uri2));

            System.out.println("Are they equal? "
                    + base.equals(relative));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
