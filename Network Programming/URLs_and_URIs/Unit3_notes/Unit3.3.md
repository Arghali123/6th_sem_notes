# 3.3 — The `URI` Class

Java provides the `URI` class in the `java.net` package:

```java
import java.net.URI;
```

**URI** stands for **Uniform Resource Identifier**.

A URI identifies a resource, while a URL is a type of URI that also provides information about **how/where to access** that resource.

---

# 1. What is a URI?

A **URI (Uniform Resource Identifier)** is a string that identifies a resource using a standardized format.

Example:

```text
https://www.example.com/products/item.html
```

Other examples include:

```text
mailto:user@example.com
```

```text
urn:isbn:9780131103627
```

### ⭐ Exam definition

> **URI is a Uniform Resource Identifier used to identify a resource on the Internet or within a system. Java provides the `URI` class in the `java.net` package for creating, manipulating, and comparing URIs.**

---

# 2. URI vs URL

This is an important exam question.

| URI                                                     | URL                                   |
| ------------------------------------------------------- | ------------------------------------- |
| Identifies a resource                                   | Identifies and locates a resource     |
| More general                                            | More specific                         |
| Does not necessarily specify how to access the resource | Usually specifies an access protocol  |
| Example: `urn:isbn:123456`                              | Example: `https://example.com/a.html` |
| Java class: `URI`                                       | Java class: `URL`                     |

### Easy way to remember

```text
URI = Identifier
URL = Locator
```

A URL is generally considered a type of URI.

---

# 3. Constructing a URI

There are several ways to construct a `URI`.

## A. Constructing from a String

The simplest constructor is:

```java
URI(String str)
```

### Example

```java
import java.net.URI;

public class CreateURI {
    public static void main(String[] args) throws Exception {

        URI uri = new URI("https://www.example.com/index.html");

        System.out.println("URI: " + uri);
    }
}
```

### Output

```text
URI: https://www.example.com/index.html
```

---

# 4. Constructing URI from Components

We can construct a URI by providing individual components.

A commonly used constructor is:

```java
URI(String scheme,
    String host,
    String path,
    String fragment)
```

Example:

```java
import java.net.URI;

public class URIComponents {
    public static void main(String[] args) throws Exception {

        URI uri = new URI(
            "https",
            "www.example.com",
            "/products/item.html",
            "details"
        );

        System.out.println(uri);
    }
}
```

Output:

```text
https://www.example.com/products/item.html#details
```

---

# 5. Important Parts of a URI

Consider this URI:

```text
https://user@example.com:8080/products/item.html?id=10#details
```

It can be divided into:

```text
https://user@example.com:8080/products/item.html?id=10#details
│       │    │           │    │                    │       │
│       │    │           │    │                    │       └─ Fragment
│       │    │           │    │                    └───────── Query
│       │    │           │    └────────────────────────────── Path
│       │    │           └─────────────────────────────────── Port
│       │    └─────────────────────────────────────────────── Host
│       └──────────────────────────────────────────────────── Authority
└──────────────────────────────────────────────────────────── Scheme
```

The important URI components are:

1. Scheme
2. Authority
3. User information
4. Host
5. Port
6. Path
7. Query
8. Fragment

---

# 6. URI Getter Methods

The `URI` class provides methods for retrieving these components.

| Method                    | Purpose                   |
| ------------------------- | ------------------------- |
| `getScheme()`             | Gets scheme               |
| `getAuthority()`          | Gets authority            |
| `getUserInfo()`           | Gets user information     |
| `getHost()`               | Gets host                 |
| `getPort()`               | Gets port                 |
| `getPath()`               | Gets path                 |
| `getQuery()`              | Gets query                |
| `getFragment()`           | Gets fragment             |
| `getSchemeSpecificPart()` | Gets scheme-specific part |
| `getRawAuthority()`       | Gets raw authority        |
| `getRawPath()`            | Gets raw path             |
| `getRawQuery()`           | Gets raw query            |
| `getRawFragment()`        | Gets raw fragment         |

---

# 7. Example: Getting URI Parts

Here's a useful **exam-ready program**.

```java
import java.net.URI;

public class URIParts {
    public static void main(String[] args) throws Exception {

        URI uri = new URI(
            "https://user@example.com:8080/products/item.html?id=10#details"
        );

        System.out.println("URI                 : " + uri);
        System.out.println("Scheme              : " + uri.getScheme());
        System.out.println("Authority           : " + uri.getAuthority());
        System.out.println("User Info           : " + uri.getUserInfo());
        System.out.println("Host                : " + uri.getHost());
        System.out.println("Port                : " + uri.getPort());
        System.out.println("Path                : " + uri.getPath());
        System.out.println("Query               : " + uri.getQuery());
        System.out.println("Fragment            : " + uri.getFragment());
        System.out.println("Scheme Specific Part: "
                + uri.getSchemeSpecificPart());
    }
}
```

### Output

```text
URI                 : https://user@example.com:8080/products/item.html?id=10#details
Scheme              : https
Authority           : user@example.com:8080
User Info           : user
Host                : example.com
Port                : 8080
Path                : /products/item.html
Query               : id=10
Fragment            : details
Scheme Specific Part: //user@example.com:8080/products/item.html?id=10
```

---

# 8. Resolving Relative URIs

This is one of the most important parts of the `URI` class.

Suppose we have:

### Base URI

```text
https://example.com/products/index.html
```

### Relative URI

```text
images/mobile.jpg
```

We want to combine them:

```text
https://example.com/products/images/mobile.jpg
```

The `URI` class provides:

```java
resolve()
```

### Syntax

```java
URI resolve(String relative)
```

or:

```java
URI resolve(URI relative)
```

---

## Example

```java
import java.net.URI;

public class ResolveURI {
    public static void main(String[] args) throws Exception {

        URI base = new URI(
            "https://example.com/products/index.html"
        );

        URI relative = new URI("images/mobile.jpg");

        URI result = base.resolve(relative);

        System.out.println("Base URI     : " + base);
        System.out.println("Relative URI : " + relative);
        System.out.println("Resolved URI : " + result);
    }
}
```

Output:

```text
Base URI     : https://example.com/products/index.html
Relative URI : images/mobile.jpg
Resolved URI : https://example.com/products/images/mobile.jpg
```

---

# 9. `../` in Relative URI

Just like URLs, `URI` can resolve `..`.

Example:

```text
Base:
https://example.com/products/item/index.html
```

Relative URI:

```text
../images/logo.png
```

The result is:

```text
https://example.com/products/images/logo.png
```

### Java

```java
import java.net.URI;

public class ResolveParentURI {
    public static void main(String[] args) throws Exception {

        URI base = new URI(
            "https://example.com/products/item/index.html"
        );

        URI relative = new URI("../images/logo.png");

        URI result = base.resolve(relative);

        System.out.println("Resolved URI: " + result);
    }
}
```

Output:

```text
Resolved URI: https://example.com/products/images/logo.png
```

---

# 10. Relativizing URIs

The `URI` class also provides:

```java
relativize()
```

It performs almost the opposite operation of `resolve()`.

Suppose:

```text
Base:
https://example.com/products/

Target:
https://example.com/products/images/logo.png
```

We can obtain:

```text
images/logo.png
```

### Example

```java
import java.net.URI;

public class RelativizeURI {
    public static void main(String[] args) throws Exception {

        URI base = new URI(
            "https://example.com/products/"
        );

        URI target = new URI(
            "https://example.com/products/images/logo.png"
        );

        URI relative = base.relativize(target);

        System.out.println("Base URI     : " + base);
        System.out.println("Target URI   : " + target);
        System.out.println("Relative URI : " + relative);
    }
}
```

Output:

```text
Base URI     : https://example.com/products/
Target URI   : https://example.com/products/images/logo.png
Relative URI : images/logo.png
```

### 🧠 Remember

```text
resolve()     → Relative → Absolute
relativize()  → Absolute → Relative
```

---

# 11. Equality of URIs

The `URI` class provides:

```java
equals()
```

to compare two URI objects.

### Example

```java
import java.net.URI;

public class URIEquality {
    public static void main(String[] args) throws Exception {

        URI uri1 = new URI("https://example.com/index.html");
        URI uri2 = new URI("https://example.com/index.html");

        System.out.println("Are they equal? "
                + uri1.equals(uri2));
    }
}
```

Output:

```text
Are they equal? true
```

---

# 12. Comparing URIs

The `URI` class implements the `Comparable<URI>` interface.

Therefore, we can use:

```java
compareTo()
```

### Syntax

```java
uri1.compareTo(uri2)
```

It returns:

```text
0       → URIs are equal
negative → uri1 comes before uri2
positive → uri1 comes after uri2
```

### Example

```java
import java.net.URI;

public class URIComparison {
    public static void main(String[] args) throws Exception {

        URI uri1 = new URI("https://example.com/a.html");
        URI uri2 = new URI("https://example.com/b.html");

        int result = uri1.compareTo(uri2);

        if (result == 0) {
            System.out.println("URIs are equal");
        } else if (result < 0) {
            System.out.println("URI1 comes before URI2");
        } else {
            System.out.println("URI1 comes after URI2");
        }
    }
}
```

Output:

```text
URI1 comes before URI2
```

### ⭐ Exam point

`compareTo()` provides **lexicographical comparison** of URIs.

---

# 13. String Representation

The URI class provides several methods for representing a URI as a string.

## A. `toString()`

Returns the URI as a string.

```java
String toString()
```

Example:

```java
import java.net.URI;

public class URIString {
    public static void main(String[] args) throws Exception {

        URI uri = new URI(
            "https://example.com/index.html"
        );

        String str = uri.toString();

        System.out.println(str);
    }
}
```

Output:

```text
https://example.com/index.html
```

---

# 14. `toASCIIString()`

Another useful method is:

```java
toASCIIString()
```

It returns an ASCII representation of the URI.

This is particularly useful when the URI contains characters that need encoding.

### Example

```java
import java.net.URI;

public class URIASCII {
    public static void main(String[] args) throws Exception {

        URI uri = new URI(
            "https://example.com/hello%20world"
        );

        System.out.println("URI        : " + uri);
        System.out.println("ASCII form : " + uri.toASCIIString());
    }
}
```

Output:

```text
URI        : https://example.com/hello%20world
ASCII form : https://example.com/hello%20world
```

---

# 15. `toURL()`

A URI can also be converted into a URL.

```java
toURL()
```

### Example

```java
import java.net.URI;
import java.net.URL;

public class URIToURL {
    public static void main(String[] args) throws Exception {

        URI uri = new URI(
            "https://example.com/index.html"
        );

        URL url = uri.toURL();

        System.out.println("URI: " + uri);
        System.out.println("URL: " + url);
    }
}
```

Output:

```text
URI: https://example.com/index.html
URL: https://example.com/index.html
```

---

# 16. Important URI Methods for Exam

This is the list I'd memorize. 👇

| Method                    | Purpose                   |
| ------------------------- | ------------------------- |
| `getScheme()`             | Gets scheme               |
| `getAuthority()`          | Gets authority            |
| `getUserInfo()`           | Gets user information     |
| `getHost()`               | Gets host                 |
| `getPort()`               | Gets port                 |
| `getPath()`               | Gets path                 |
| `getQuery()`              | Gets query                |
| `getFragment()`           | Gets fragment             |
| `getSchemeSpecificPart()` | Gets scheme-specific part |
| `resolve()`               | Resolves relative URI     |
| `relativize()`            | Creates relative URI      |
| `equals()`                | Checks equality           |
| `compareTo()`             | Compares URIs             |
| `toString()`              | Converts URI to String    |
| `toASCIIString()`         | ASCII representation      |
| `toURL()`                 | Converts URI to URL       |

---

# ⭐ URL vs URI — Important Exam Difference

| Feature             | URL                      | URI                         |
| ------------------- | ------------------------ | --------------------------- |
| Full name           | Uniform Resource Locator | Uniform Resource Identifier |
| Main purpose        | Locate/access resource   | Identify resource           |
| Package             | `java.net`               | `java.net`                  |
| Java class          | `URL`                    | `URI`                       |
| Can open connection | ✅ Yes                    | ❌ No                        |
| `openStream()`      | ✅ Yes                    | ❌ No                        |
| `resolve()`         | ❌                        | ✅                           |
| `relativize()`      | ❌                        | ✅                           |
| `compareTo()`       | ❌                        | ✅                           |
| `toURL()`           | ❌                        | ✅                           |
| Can represent URNs  | Generally no             | ✅ Yes                       |

### 🔥 Key difference to remember

```text
URL → Can be used to access a resource.

URI → Mainly identifies/describes a resource.
```

For example:

```java
URL url = new URL("https://example.com");
url.openStream();       // Can access resource
```

Whereas:

```java
URI uri = new URI("https://example.com");
```

creates an identifier, but it doesn't itself open the network connection.

---

