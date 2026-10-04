# 3.6 — Communicating with Server-Side Programs through GET

## 1. What is GET?

**GET** is an HTTP method used by a client to request data from a server.

The client sends information to the server by putting parameters in the **URL's query string**.

For example:

```text
https://example.com/search?name=John&city=Kathmandu
```

Here:

```text
https://example.com/search
```

is the server-side resource, and:

```text
?name=John&city=Kathmandu
```

is the **query string**.

---

# 2. How GET Communication Works

The basic process is:

```text
Java Client
    │
    │ GET request
    ↓
https://example.com/search?name=John&city=Kathmandu
    │
    ↓
Web Server
    │
    │ Processes parameters
    ↓
Response
    │
    ↓
Java Client
```

### Example

Suppose a server-side program expects:

```text
name
city
```

The client can send:

```text
https://example.com/search?name=John&city=Kathmandu
```

The server receives:

```text
name = John
city = Kathmandu
```

and processes them.

---

# 3. Structure of a GET URL

A GET request commonly looks like:

```text
scheme://host/path?parameter1=value1&parameter2=value2
```

Example:

```text
https://example.com/search?name=John&age=20
```

Breakdown:

| Part          | Meaning                   |
| ------------- | ------------------------- |
| `https`       | Protocol                  |
| `example.com` | Server                    |
| `/search`     | Server-side resource      |
| `?`           | Beginning of query string |
| `name=John`   | First parameter           |
| `&`           | Separates parameters      |
| `age=20`      | Second parameter          |

---

# 4. Why URL Encoding Is Important

Suppose the user enters:

```text
name = John Doe
```

We cannot simply put the raw value into the URL:

```text
?name=John Doe
```

Instead, we encode it:

```text
?name=John+Doe
```

That's why **3.4 — `URLEncoder`** is directly related to GET communication.

For example:

```java
String name = URLEncoder.encode(
    "John Doe",
    StandardCharsets.UTF_8
);
```

Result:

```text
John+Doe
```

---

# 5. Sending GET Request Using Java

One simple way is to construct the URL containing the query parameters and use `openStream()`.

### Full Runnable Java Program

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GetRequestExample {

    public static void main(String[] args) throws Exception {

        String name = "John Doe";
        String city = "Kathmandu";

        // Encode parameters
        String encodedName =
                URLEncoder.encode(name, StandardCharsets.UTF_8);

        String encodedCity =
                URLEncoder.encode(city, StandardCharsets.UTF_8);

        // Build GET URL
        String urlString =
                "https://example.com/search"
                + "?name=" + encodedName
                + "&city=" + encodedCity;

        System.out.println("GET URL:");
        System.out.println(urlString);

        // Create URL object
        URL url = new URL(urlString);

        // Send GET request and read response
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(url.openStream())
        );

        String line;

        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        reader.close();
    }
}
```

### What happens?

```text
1. User data
      ↓
2. URL encode data
      ↓
3. Create GET URL
      ↓
4. Create URL object
      ↓
5. openStream()
      ↓
6. Server receives GET request
      ↓
7. Server sends response
      ↓
8. BufferedReader reads response
```

---

# 6. Important Point About the Example

`https://example.com/search` is only an **illustrative URL**. It is not necessarily a server-side program that accepts `name` and `city`.

For an actual GET application, you need a server endpoint that expects those parameters.

For example, suppose your server has:

```text
https://myserver.com/search
```

Then your Java program could send:

```text
https://myserver.com/search?name=John+Doe&city=Kathmandu
```

---

# 7. Using `URLConnection`

Instead of directly using:

```java
url.openStream();
```

we can explicitly open a connection.

```java
URLConnection connection = url.openConnection();
```

For an HTTP URL, this gives us an `HttpURLConnection`.

---

## Full Example Using `HttpURLConnection`

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GetUsingHttpURLConnection {

    public static void main(String[] args) throws Exception {

        String name = "John Doe";
        String city = "Kathmandu";

        // Encode parameters
        String encodedName =
                URLEncoder.encode(name, StandardCharsets.UTF_8);

        String encodedCity =
                URLEncoder.encode(city, StandardCharsets.UTF_8);

        // Create GET URL
        String urlString =
                "https://example.com/search"
                + "?name=" + encodedName
                + "&city=" + encodedCity;

        URL url = new URL(urlString);

        // Open HTTP connection
        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        // Specify request method
        connection.setRequestMethod("GET");

        // Get response code
        int responseCode =
                connection.getResponseCode();

        System.out.println("Response Code: " + responseCode);

        // Read server response
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()
                )
        );

        String line;

        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        reader.close();
        connection.disconnect();
    }
}
```

---

# 8. Important `HttpURLConnection` Methods

For exam purposes, remember these:

| Method                    | Purpose                       |
| ------------------------- | ----------------------------- |
| `setRequestMethod("GET")` | Specifies GET request         |
| `getResponseCode()`       | Gets HTTP response code       |
| `getInputStream()`        | Reads successful response     |
| `getErrorStream()`        | Reads error response          |
| `disconnect()`            | Closes/disconnects connection |

---

# 9. GET Parameters

Suppose we want to send:

```text
name = Daenish
course = Network Programming
semester = 6
```

The query string becomes:

```text
?name=Daenish&course=Network+Programming&semester=6
```

Complete URL:

```text
https://example.com/search?name=Daenish&course=Network+Programming&semester=6
```

### Important structure

```text
?parameter=value
       │
       └── parameters separated using &
```

---

# 10. GET and URL Encoding Together

This is a very important connection between **3.4 and 3.6**.

Suppose:

```text
course = Network Programming
```

First:

```java
URLEncoder.encode(
    "Network Programming",
    StandardCharsets.UTF_8
);
```

Result:

```text
Network+Programming
```

Then:

```text
?course=Network+Programming
```

So:

```text
Original data
     ↓
URLEncoder
     ↓
Encoded data
     ↓
Query string
     ↓
GET request
     ↓
Server
```

---

# 11. GET vs POST

This is a common exam question.

| GET                                    | POST                                                |
| -------------------------------------- | --------------------------------------------------- |
| Data is sent in URL                    | Data is sent in request body                        |
| Parameters visible in URL              | Parameters not normally shown in URL                |
| Suitable for retrieving/searching data | Commonly used for submitting data                   |
| Can be bookmarked                      | Request body isn't represented by a normal bookmark |
| URL length can limit amount of data    | Better suited to larger request bodies              |

### Example GET

```text
https://example.com/search?name=John
```

### Example POST

```text
POST /search HTTP/1.1

name=John
```

---

# 12. Advantages of GET

### 1. Simple

Parameters are directly visible in the URL.

### 2. Bookmarkable

A GET URL can generally be bookmarked.

Example:

```text
https://example.com/search?query=java
```

### 3. Good for retrieving data

GET is commonly used for searches and retrieving resources.

---

# 13. Limitations of GET

### 1. Data appears in URL

For example:

```text
?username=john
```

So GET should **not be used for sensitive information such as passwords**.

### 2. URL length limitations

Browsers and servers may impose limits on URL length.

### 3. Special characters must be encoded

For example:

```text
John Doe
```

should be encoded before being placed in the query.

---

# 📝 Exam-Ready Answer

### What is GET?

> GET is an HTTP request method used by a client to request information from a server. Parameters are commonly passed through the query string of the URL in the form `parameter=value`.

Example:

```text
https://example.com/search?name=John&city=Kathmandu
```

### Steps for communicating with a server using GET

1. Collect the parameter values.
2. Encode the parameter values using `URLEncoder`.
3. Construct the URL with the query string.
4. Create a `URL` object.
5. Open an HTTP connection.
6. Set the request method to `GET`.
7. Send the request.
8. Read the server response using an input stream.
9. Close the connection.

---

# ⭐ Exam Program

If asked:

> **Write a Java program to communicate with a server-side program using GET.**

A good answer is:

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GetCommunication {

    public static void main(String[] args) throws Exception {

        String name = "John Doe";

        String encodedName =
                URLEncoder.encode(name, StandardCharsets.UTF_8);

        String urlString =
                "https://example.com/search"
                + "?name=" + encodedName;

        URL url = new URL(urlString);

        HttpURLConnection connection =
                (HttpURLConnection) url.openConnection();

        connection.setRequestMethod("GET");

        System.out.println(
                "Response Code: "
                + connection.getResponseCode()
        );

        BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()
                )
        );

        String line;

        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        reader.close();
        connection.disconnect();
    }
}
```

---

# ⚡ Quick Revision

Remember this flow:

```text
             GET COMMUNICATION

User Data
    ↓
URLEncoder
    ↓
Query String
    ↓
URL
    ↓
HttpURLConnection
    ↓
GET Request
    ↓
Server
    ↓
Response
    ↓
InputStream
    ↓
Java Program
```

### Most important code:

```java
String encoded =
    URLEncoder.encode(data, StandardCharsets.UTF_8);

URL url = new URL(urlString);

HttpURLConnection connection =
    (HttpURLConnection) url.openConnection();

connection.setRequestMethod("GET");

int code = connection.getResponseCode();

BufferedReader reader =
    new BufferedReader(
        new InputStreamReader(
            connection.getInputStream()
        )
    );
```

---


