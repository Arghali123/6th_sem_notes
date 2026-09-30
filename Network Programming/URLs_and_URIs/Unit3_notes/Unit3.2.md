# 3.2 — The `URL` Class

The Java `URL` class is found in:

```java
java.net.URL
```

It represents a **Uniform Resource Locator** and provides methods to create, inspect, compare, and access resources through URLs.

---

# 1. Creating New URLs

There are several ways to create a `URL` object.

## A. Creating URL from a String

The most common constructor is:

```java
URL(String spec)
```

### Example

```java
import java.net.URL;

public class CreateURL {
    public static void main(String[] args) throws Exception {

        URL url = new URL("https://www.example.com/index.html");

        System.out.println("URL: " + url);
    }
}
```

### Output

```text
URL: https://www.example.com/index.html
```

---

## B. Creating URL from components

We can create a URL by separately specifying the protocol, host, port, and file.

```java
URL(String protocol, String host, int port, String file)
```

### Example

```java
import java.net.URL;

public class CreateURLComponents {
    public static void main(String[] args) throws Exception {

        URL url = new URL(
            "https",
            "www.example.com",
            443,
            "/index.html"
        );

        System.out.println(url);
    }
}
```

Output:

```text
https://www.example.com:443/index.html
```

---

## C. Creating URL relative to another URL

We can create a URL using a **base URL and relative URL**.

```java
URL(URL context, String spec)
```

### Example

```java
import java.net.URL;

public class RelativeURL {
    public static void main(String[] args) throws Exception {

        URL base = new URL("https://example.com/products/index.html");

        URL url = new URL(base, "images/mobile.jpg");

        System.out.println("Base URL : " + base);
        System.out.println("New URL  : " + url);
    }
}
```

Output:

```text
Base URL : https://example.com/products/index.html
New URL  : https://example.com/products/images/mobile.jpg
```

### ⭐ Exam point

A relative URL is resolved using the **base/context URL**.

---

# 2. Retrieving Data from a URL

The `URL` class provides methods for accessing the resource represented by a URL.

The most important methods are:

| Method             | Purpose                              |
| ------------------ | ------------------------------------ |
| `openStream()`     | Opens an input stream to read data   |
| `openConnection()` | Creates a connection to the resource |
| `getContent()`     | Gets the content of the resource     |

---

## A. `openStream()`

```java
InputStream openStream()
```

It opens a connection and returns an `InputStream`.

We can use it to read data from a URL.

### Example

```java
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

public class RetrieveData {
    public static void main(String[] args) throws Exception {

        URL url = new URL("https://example.com");

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

### How it works

```text
URL
 ↓
openStream()
 ↓
InputStream
 ↓
InputStreamReader
 ↓
BufferedReader
 ↓
readLine()
 ↓
Web page data
```

### Important

```java
url.openStream()
```

is basically a convenient way to open a connection and obtain an input stream for reading the resource.

---

# 3. Using `openConnection()`

Another important method is:

```java
URLConnection openConnection()
```

It establishes a connection to the resource.

### Example

```java
import java.net.URL;
import java.net.URLConnection;

public class URLConnectionExample {
    public static void main(String[] args) throws Exception {

        URL url = new URL("https://example.com");

        URLConnection connection = url.openConnection();

        System.out.println("Content Type: "
                + connection.getContentType());

        System.out.println("Content Length: "
                + connection.getContentLengthLong());
    }
}
```

### Why use `openConnection()`?

It gives us more control over the connection.

For example, we can obtain:

* Content type
* Content length
* Input stream
* Output stream
* Header information

---

# 4. Splitting a URL into Pieces

A URL consists of several components.

For example:

```text
https://www.example.com:8080/products/item.html?id=10#details
```

The `URL` class provides getter methods to retrieve these individual components.

### Important methods

| Method             | Returns            |
| ------------------ | ------------------ |
| `getProtocol()`    | Protocol           |
| `getHost()`        | Host name          |
| `getPort()`        | Port number        |
| `getDefaultPort()` | Default port       |
| `getFile()`        | File/path + query  |
| `getPath()`        | Path               |
| `getQuery()`       | Query              |
| `getRef()`         | Fragment/reference |
| `getAuthority()`   | Authority          |
| `getUserInfo()`    | User information   |

Let's see them in action.

---

## Full Java Example

```java
import java.net.URL;

public class SplitURL {
    public static void main(String[] args) throws Exception {

        URL url = new URL(
            "https://user@example.com:8080/products/item.html?id=10#details"
        );

        System.out.println("Complete URL : " + url);
        System.out.println("Protocol     : " + url.getProtocol());
        System.out.println("Host         : " + url.getHost());
        System.out.println("Port         : " + url.getPort());
        System.out.println("Default Port : " + url.getDefaultPort());
        System.out.println("Authority    : " + url.getAuthority());
        System.out.println("Path         : " + url.getPath());
        System.out.println("File         : " + url.getFile());
        System.out.println("Query        : " + url.getQuery());
        System.out.println("Reference    : " + url.getRef());
        System.out.println("User Info    : " + url.getUserInfo());
    }
}
```

### Approximate output

```text
Complete URL : https://user@example.com:8080/products/item.html?id=10#details
Protocol     : https
Host         : example.com
Port         : 8080
Default Port : 443
Authority    : user@example.com:8080
Path         : /products/item.html
File         : /products/item.html?id=10
Query        : id=10
Reference    : details
User Info    : user
```

### 🧠 Easy memory trick

Remember:

```text
URL
 │
 ├── Protocol → https
 ├── Host     → example.com
 ├── Port     → 8080
 ├── Path     → /products/item.html
 ├── Query    → id=10
 └── Fragment → details
```

---

# 5. Equality of URLs

The `URL` class provides:

```java
boolean equals(Object obj)
```

It checks whether two URLs refer to the **same resource**.

There is also:

```java
boolean sameFile(URL other)
```

which checks whether two URLs refer to the same file/resource while ignoring the fragment/reference.

---

## Example

```java
import java.net.URL;

public class URLEquality {
    public static void main(String[] args) throws Exception {

        URL url1 = new URL("https://example.com/index.html");

        URL url2 = new URL("https://example.com/index.html");

        URL url3 = new URL("https://example.com/index.html#top");

        System.out.println("url1 equals url2: "
                + url1.equals(url2));

        System.out.println("url1 sameFile url3: "
                + url1.sameFile(url3));
    }
}
```

Output:

```text
url1 equals url2: true
url1 sameFile url3: true
```

### Difference

```text
equals()
    ↓
Checks complete URL, including reference

sameFile()
    ↓
Checks whether they identify the same resource
and ignores the fragment/reference.
```

---

# 6. Comparing URLs

The `URL` class implements comparison through:

```java
equals()
```

and:

```java
sameFile()
```

There is also:

```java
hashCode()
```

which returns a hash code for the URL.

### Important methods

```java
url1.equals(url2);
url1.sameFile(url2);
url1.hashCode();
```

### ⚠️ Important exam point

`URL.equals()` may perform **network-related hostname resolution** when comparing host names. Therefore, URL equality can potentially involve network access.

This is one reason the `URI` class is often preferred for purely syntactic URI comparison.

---

# 7. Conversion of URL

The `URL` class provides conversion methods.

## A. `toString()`

Converts the URL into a string.

```java
String toString()
```

Example:

```java
import java.net.URL;

public class URLConversion {
    public static void main(String[] args) throws Exception {

        URL url = new URL("https://example.com/index.html");

        String str = url.toString();

        System.out.println("URL object : " + url);
        System.out.println("String     : " + str);
    }
}
```

Output:

```text
URL object : https://example.com/index.html
String     : https://example.com/index.html
```

---

## B. `toExternalForm()`

```java
String toExternalForm()
```

Returns the URL as a string in external form.

Example:

```java
String urlString = url.toExternalForm();
```

In normal cases:

```java
url.toString()
```

and

```java
url.toExternalForm()
```

produce the same textual URL.

---

## C. URL to URI

A `URL` can also be converted into a `URI` using:

```java
url.toURI()
```

Example:

```java
import java.net.URL;
import java.net.URI;

public class URLToURI {
    public static void main(String[] args) throws Exception {

        URL url = new URL("https://example.com/index.html");

        URI uri = url.toURI();

        System.out.println("URL : " + url);
        System.out.println("URI : " + uri);
    }
}
```

Output:

```text
URL : https://example.com/index.html
URI : https://example.com/index.html
```

We'll study `URI` properly in **3.3**.

---

# 📌 Important `URL` Class Methods for Exam

This is the list I'd memorize:

| Method             | Use                      |
| ------------------ | ------------------------ |
| `openStream()`     | Retrieve/read data       |
| `openConnection()` | Open connection          |
| `getProtocol()`    | Get protocol             |
| `getHost()`        | Get host                 |
| `getPort()`        | Get port                 |
| `getDefaultPort()` | Get default port         |
| `getPath()`        | Get path                 |
| `getFile()`        | Get file/path + query    |
| `getQuery()`       | Get query                |
| `getRef()`         | Get fragment             |
| `getAuthority()`   | Get authority            |
| `getUserInfo()`    | Get user information     |
| `equals()`         | Compare URLs             |
| `sameFile()`       | Check same resource      |
| `hashCode()`       | Get hash code            |
| `toString()`       | Convert URL to String    |
| `toExternalForm()` | Get external string form |
| `toURI()`          | Convert URL to URI       |

---

# 📝 Exam-Ready Answer

### What is the URL class?

> The `URL` class in Java, available in the `java.net` package, represents a Uniform Resource Locator. It is used to create URLs, access resources, retrieve URL components, compare URLs, and convert URLs into other representations.

### Common constructors

```java
URL(String spec)

URL(URL context, String spec)

URL(String protocol, String host, int port, String file)
```

### Important methods

```java
openStream()
openConnection()

getProtocol()
getHost()
getPort()
getPath()
getQuery()
getRef()

equals()
sameFile()

toString()
toExternalForm()
toURI()
```

---


