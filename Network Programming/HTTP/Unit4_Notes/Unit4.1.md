# Unit 4 — HTTP

## HTTP: Hypertext Transfer Protocol

Java Network Programming · Unit 4

HTTP is an application-layer protocol used by clients and servers to exchange resources and data over a network.

- **4.1 — The protocol: Keep-Alive**
- [**4.2 — HTTP methods**](Unit4.2.md)
- [**4.3 — The request body**](Unit4.3.md)
- [**4.4 — Cookies: CookieManager and CookieStore**](Unit4.4.md)


## Topic 4.1 — The HTTP Protocol: Keep-Alive

### 1. TEACH — Understand the concept

First, let's understand what happens when you visit a website.

Client (Browser or Java program)

Requests a webpage

HTTP request and HTTP response

Web Server

Processes the request and sends a response

Now imagine that a webpage contains:

- One HTML file
- Five images
- Three CSS files
- Two JavaScript files

Without a persistent connection, the client might need to establish multiple TCP connections to fetch these resources. With HTTP Keep-Alive, the client can reuse a TCP connection for multiple HTTP exchanges instead of closing it after every exchange.

Definition for exams: HTTP Keep-Alive is a mechanism that allows multiple HTTP requests and responses to use the same persistent TCP connection, reducing the overhead of repeatedly establishing connections.

### 2. WHY — Why does Keep-Alive exist?

Establishing a TCP connection requires connection setup, which adds latency and consumes resources.

Keep-Alive helps by:

- Reducing repeated TCP connection setup.
- Improving performance when fetching multiple resources.
- Reducing connection-management overhead.
- Making better use of network resources.

### Without Keep-Alive vs. With Keep-Alive

Without reuse

Request 1 → Connect → Response → Close

Request 2 → Connect → Response → Close

With reuse

Connect once

Request 1 → Response

Request 2 → Response

Reuse the connection

One important detail: HTTP/1.1 uses persistent connections by default, unless the connection is closed or another condition prevents reuse. HTTP/1.0 generally used non-persistent connections by default.

Modern HTTP/2 and HTTP/3 also support efficient reuse, but their connection behavior differs from HTTP/1.1.

### 3. CODE — Complete runnable Java example

Java's `HttpClient` supports HTTP requests and connection reuse. The following program sends two GET requests to the same server using the same `HttpClient` instance.

Create a file named `HttpKeepAliveDemo.java`.

```

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpKeepAliveDemo {

    public static void main(String[] args) throws Exception {

        // Create one reusable HTTP client
        HttpClient client = HttpClient.newHttpClient();

        // Build the first GET request
        HttpRequest request1 = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get"))
                .GET()
                .build();

        // Send the first request
        HttpResponse<String> response1 = client.send(
                request1,
                HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("First request status: "
                + response1.statusCode());

        // Build the second GET request
        HttpRequest request2 = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get"))
                .GET()
                .build();

        // Send the second request using the same client
        HttpResponse<String> response2 = client.send(
                request2,
                HttpResponse.BodyHandlers.ofString()
        );

        System.out.println("Second request status: "
                + response2.statusCode());

        System.out.println("Second response body:");
        System.out.println(response2.body());
    }
}

```

How to run it

1. Save the file as `HttpKeepAliveDemo.java`.
2. Ensure JDK 11 or later is installed.
3. Run `javac HttpKeepAliveDemo.java`.
4. Run `java HttpKeepAliveDemo`.

An internet connection is required for this example, and the external test server must be available.

Expected output (assuming both requests succeed):

```
First request status: 200
Second request status: 200
Second response body:
{
  ...
}
```

Important: this demonstrates reusing the same client, which can reuse connections. It does not prove that both requests used the exact same TCP connection; the client and server control connection reuse.
