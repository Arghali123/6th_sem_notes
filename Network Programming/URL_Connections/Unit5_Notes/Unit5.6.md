# 5.6 — Configuring the Client Request HTTP Header

### 1. TEACH — What is an HTTP Request Header?
When a Java client sends an HTTP request to a server, it can include additional information called **request headers.**

These headers tell the server things about the request, such as:
- What type of response the client accepts.
- Which language the client prefers.
- What application is making the request.
- Whether the client supports compression.
Example HTTP request:
```
GET / HTTP/1.1
Host: example.com
User-Agent: Java-Client
Accept: text/html
Accept-Language: en-US
```

Here:
- `User-Agent` identifies the client application.
- `Accept` tells the server which response formats are acceptable.
- `Accept-Language` indicates the preferred language.
- `Host` identifies the target host.

### 2. WHY — Why configure request headers?
Imagine your Java application requests data from an API.

The server might support JSON and XML. You want JSON.

You can send:
```
Accept: application/json
```

This tells the server that your client prefers JSON responses.

Without appropriate headers, the server may return a default representation, reject the request, or behave differently depending on its API rules.


### 3. CODE — Java methods for configuring request headers
The `URLConnection` class provides three important methods:

|Method	|Purpose|
|-------|-------|
|setRequestProperty(String key, String value)|	Sets a request header or replaces its existing value.|
|addRequestProperty(String key, String value)|	Adds another value for a request header.|
|getRequestProperty(String key)|	Retrieves a configured request-header value.|

There is also:
```
getRequestProperties()
```

It retrieves all configured request headers as a `Map<String, List<String>>.`

**Important:** Configure request headers before connecting. Once the connection has been established, changing request properties is not allowed.


### 4. Full runnable example
Create `ConfigureRequestHeaders.java`:
```
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class ConfigureRequestHeaders {

    public static void main(String[] args) {

        try {
            URL url = new URL("https://httpbin.org/anything");

            URLConnection connection = url.openConnection();

            // Configure request headers
            connection.setRequestProperty(
                    "User-Agent", "Java-Network-Client"
            );

            connection.setRequestProperty(
                    "Accept", "application/json"
            );

            connection.setRequestProperty(
                    "Accept-Language", "en-US"
            );

            // Add another value to a header
            connection.addRequestProperty(
                    "Accept-Language", "en"
            );

            // Display a specific request header
            System.out.println(
                    "User-Agent: "
                    + connection.getRequestProperty("User-Agent")
            );

            // Display all configured request headers
            System.out.println("\nAll Request Headers:");

            Map<String, List<String>> headers =
                    connection.getRequestProperties();

            headers.forEach((key, values) ->
                    System.out.println(key + ": " + values)
            );

            // Send request and read response
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            connection.getInputStream(),
                            StandardCharsets.UTF_8
                    ))) {

                System.out.println("\nServer Response:");

                String line;

                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

Note: This example uses httpbin.org to echo the request headers. It requires an internet connection.

### 5. Understand the difference: `setRequestProperty()` vs `addRequestProperty()`
Suppose you execute:
```
connection.setRequestProperty("Accept-Language", "en-US");

connection.addRequestProperty("Accept-Language", "en");
```

The resulting header can contain multiple values:
```
Accept-Language: en-US
Accept-Language: en
```

But if you execute:
```
connection.setRequestProperty("Accept-Language", "fr");
```

The previous configured values are replaced by `fr`.

**Remember:**
- `setRequestProperty()` → Set or replace.
- `addRequestProperty()` → Add another value.
- `getRequestProperty()` → Retrieve one header value.
- `getRequestProperties()` → Retrieve all configured request headers.
