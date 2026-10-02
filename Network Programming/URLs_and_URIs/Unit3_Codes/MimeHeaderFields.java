import java.net.URL;
import java.net.URLConnection;
import java.util.Scanner;

/** Reads MIME-related and arbitrary HTTP header fields from a URL. */
public class MimeHeaderFields {
    public static void main(String[] args) {
        String address = args.length > 0 ? args[0] : "https://example.com";

        try {
            URL url = new URL(address);
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            // MIME-related fields are available through convenience methods.
            System.out.println("MIME type (Content-Type): " + connection.getContentType());
            System.out.println("Content length: " + connection.getContentLengthLong());
            System.out.println("Content encoding: " + connection.getContentEncoding());

            // Ask for any header by name; HTTP header names are case-insensitive.
            Scanner input = new Scanner(System.in);
            System.out.print("Enter an arbitrary header name (e.g., Cache-Control): ");
            String headerName = input.nextLine().trim();
            String value = connection.getHeaderField(headerName);
            System.out.println(headerName + ": " + (value == null ? "not provided by server" : value));

            // Also show every response header, including the status line.
            System.out.println("\nAll response headers:");
            for (int i = 0; ; i++) {
                String name = connection.getHeaderFieldKey(i);
                String headerValue = connection.getHeaderField(i);
                if (name == null && headerValue == null) {
                    break;
                }
                System.out.println(name == null ? headerValue : name + ": " + headerValue);
            }
        } catch (Exception e) {
            System.err.println("Could not read headers from " + address + ": " + e.getMessage());
        }
    }
}
