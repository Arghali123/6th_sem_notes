import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.List;

public class ReadHttpHeaders {
    public static void main(String[] args) {
        try {

            // Step 1: Create URL
            URL url = new URL("https://example.com");

            // Step2 : Open Connection
            URLConnection connection = url.openConnection();

            // Step 3: Establish Connection
            connection.connect();

            // =================================
            // PART 1: SPECIFIC HEADER FIELDS
            // =================================

            System.out.println("=== SPECIFIC HEADERS ===");

            System.out.println(
                    "Content Type: " +
                            connection.getContentType());

            System.out.println(
                    "Content Length: " +
                            connection.getContentLengthLong());

            System.out.println(
                    "Content Encoding: " +
                            connection.getContentEncoding());

            System.out.println(
                    "Date: " + connection.getDate());

            System.out.println(
                    "Expiration: " + connection.getExpiration());

            System.out.println(
                    "Last Modified: " +
                            connection.getLastModified());

            // =================================
            // PART 2: ARBITRARY HEADER FIELDS
            // =================================

            System.out.println("\n=== ARBITRARY HEADERS ===");

            System.out.println("Server: " + connection.getHeaderField("Server"));
            System.out.println("Cache-Control: " + connection.getHeaderField("Cache-Control"));

            // Retrieve header by index
            System.out.println("Header at index 0: " + connection.getHeaderField(0));

            // Retrieve header name by index
            System.out.println("Header key at index 0: " + connection.getHeaderFieldKey(0));

            // =================================
            // PART 3: ALL HEADER FIELDS
            // =================================

            System.out.println("\n=== ALL RESPONSE HEADERS ===");
            Map<String, List<String>> headers = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
                System.out.println(entry.getKey() + " : " + entry.getValue());
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}