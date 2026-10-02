```
System.out.println("Content length: "+conn.getContentLength());
System.out.println("Expiration: "+conn.getExpiration());
```
## Why the content length is showing -1 and expiration 0 as the output in the code above?
`getContentLength()` returns **-1** and `getExpiration()` returns **0** because `url.openConnection()` **does not execute an HTTP request on its own**.

In Java, `url.openConnection()` simply initializes a connection object in memory—it does not send any network packets or fetch response headers until you explicitly trigger the connection or request data from it.

When you call `conn.getContentLength()` or `conn.getExpiration()` on an unopened connection:

1. The connection attempts to read header fields, but no response headers have been received from the server yet.
2. Java falls back to default sentinel values:
* **`-1`** for missing or unread integer/long headers (like `Content-Length`).
* **`0`** for missing or unread date headers (like `Expires`), representing January 1, 1970 UTC (Epoch time).



Additionally, even after connecting, a server might not send a `Content-Length` header (e.g., if using `Transfer-Encoding: chunked`) or an `Expires` header, which would also result in `-1` and `0`.

---

### How to Fix It

To force the connection to execute the HTTP request and load the headers before querying them, call `conn.connect()` (or read from the stream):

```java
import java.net.URL;
import java.net.URLConnection;

public class Main {
    public static void main(String[] args) throws Exception {
        URL url = new URL("https://example.com");
        URLConnection conn = url.openConnection();

        // Explicitly open the network connection and fetch response headers
        conn.connect();

        System.out.println("Content length: " + conn.getContentLength());
        System.out.println("Expiration: " + conn.getExpiration());
    }
}

```

## Day 2
I added [MimeHeaderFields.java](/C:/Users/user/OneDrive%20-%20Nepathya%20college/Desktop/6th_sem_notes/Network%20Programming/URLs_and_URIs/Unit3_Codes/MimeHeaderFields.java).

It opens a URL connection and reads three MIME-related values: the content type, content length, and content encoding. It then prompts for an arbitrary header name, looks up its value, and prints all response headers. If a header is missing, Java returns `null`; the program displays “not provided by server.” The connection timeouts keep it from waiting indefinitely for a slow server.

To run it from this folder:

```text
javac MimeHeaderFields.java
java MimeHeaderFields
```

It uses `https://example.com` by default. You can supply a different URL as an argument:

```text
java MimeHeaderFields https://www.google.com
```

When prompted, enter a header name such as `Cache-Control` or `Server`. `URLConnection` sends the request when the program first reads a response header; you don’t need to call `connect()` explicitly.