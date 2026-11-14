import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class HttpMethodsExample {
    public static void main(String[] args) {
        try {
            // Example 1: GET request
            sendGetRequest();

            //Example 2: POST request
            sendPostRequest();

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

    }

    private static void sendGetRequest() throws IOException {
        URL url = new URL("https://httpbin.org/get");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        try {
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);

            System.out.println("\nGET Response Code: " + connection.getResponseCode());

            readResponse(connection);
        } finally {
            connection.disconnect();
        }
    }

    private static void sendPostRequest() throws IOException {
        URL url = new URL("https://httpbin.org/post");

        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        try {
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);

            connection.setRequestProperty("Content-Type", "application/json");
            connection.setConnectTimeout(5000);
            connection.setReadTimeout(10000);

            String json = """
                    {
                        "name": "Alex",
                        "course": "Java Networking"
                    }
                    """;

            // Send requst body
            try (OutputStream output = connection.getOutputStream()) {
                output.write(json.getBytes(StandardCharsets.UTF_8));
            }
            System.out.println("\nPOST Resposne Code: " + connection.getResponseCode());
            readResponse(connection);
        } finally {
            connection.disconnect();
        }
    }

    private static void readResponse(HttpURLConnection connection) throws IOException {
        int status = connection.getResponseCode();

        InputStream stream;

        if (status >= 200 && status < 400) {
            stream = connection.getInputStream();
        } else {
            stream = connection.getErrorStream();
        }

        if (stream == null) {
            System.out.println("No response body.");
            return;
        }

        try (
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}