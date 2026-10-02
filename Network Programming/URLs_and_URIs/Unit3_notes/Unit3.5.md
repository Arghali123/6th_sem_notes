# 3.5 — Proxies

## 1. What is a Proxy?

A **proxy server** is an intermediate server between a client and the destination server.

Normally:

```text
Client ───────────────→ Web Server
```

With a proxy:

```text
Client ──→ Proxy Server ──→ Web Server
```

The client sends the request to the proxy, and the proxy forwards it to the destination server.

### ⭐ Exam definition

> **A proxy server is an intermediary server that receives requests from a client and forwards them to the destination server. Java provides several mechanisms for working with proxies, including System Properties, the `Proxy` class, and the `ProxySelector` class.**

---

# 2. Why are Proxies Used?

Common uses include:

* Controlling Internet access
* Network security
* Caching frequently requested resources
* Hiding the client's direct network connection
* Accessing resources through a specific network gateway

---

# 3. Proxy Configuration in Java

Java provides **three important approaches**:

```text
                    PROXIES
                       │
       ┌───────────────┼────────────────┐
       ↓               ↓                ↓
System Properties    Proxy          ProxySelector
```

Let's study them one by one.

---

# 3.5.1 System Properties

Java allows proxy settings to be configured using **system properties**.

For example:

```java
System.setProperty("http.proxyHost", "proxy.example.com");
System.setProperty("http.proxyPort", "8080");
```

This tells Java to use the specified proxy for HTTP connections.

### Important properties

| Property             | Purpose                        |
| -------------------- | ------------------------------ |
| `http.proxyHost`     | HTTP proxy host                |
| `http.proxyPort`     | HTTP proxy port                |
| `https.proxyHost`    | HTTPS proxy host               |
| `https.proxyPort`    | HTTPS proxy port               |
| `http.nonProxyHosts` | Hosts that should bypass proxy |

---

## Example

```java
import java.net.URL;
import java.net.URLConnection;

public class SystemProxyExample {
    public static void main(String[] args) throws Exception {

        // Configure HTTP proxy
        System.setProperty("http.proxyHost", "proxy.example.com");
        System.setProperty("http.proxyPort", "8080");

        URL url = new URL("http://example.com");

        URLConnection connection = url.openConnection();

        System.out.println("Connection created successfully.");
        System.out.println("URL: " + url);
    }
}
```

### ⚠️ Important

`proxy.example.com` is only an example. A real proxy host and port must be supplied for an actual connection.

---

# 4. Reading System Proxy Properties

We can also retrieve the configured properties.

```java
import java.net.URL;

public class ReadProxyProperties {
    public static void main(String[] args) {

        System.setProperty("http.proxyHost", "proxy.example.com");
        System.setProperty("http.proxyPort", "8080");

        System.out.println(
                "Proxy Host: "
                + System.getProperty("http.proxyHost")
        );

        System.out.println(
                "Proxy Port: "
                + System.getProperty("http.proxyPort")
        );
    }
}
```

Output:

```text
Proxy Host: proxy.example.com
Proxy Port: 8080
```

### ⭐ Exam point

System properties provide a **global/default way** to configure proxy behavior.

---

# 3.5.2 The `Proxy` Class

Java provides:

```java
java.net.Proxy
```

The `Proxy` class represents a proxy configuration for a particular connection.

Unlike system properties, we can specify a proxy **directly for an individual connection**.

---

# 5. Creating a Proxy

The constructor is:

```java
Proxy(Proxy.Type type, SocketAddress sa)
```

The main proxy types are:

```java
Proxy.Type.DIRECT
Proxy.Type.HTTP
Proxy.Type.SOCKS
```

### Meaning

| Type     | Meaning     |
| -------- | ----------- |
| `DIRECT` | No proxy    |
| `HTTP`   | HTTP proxy  |
| `SOCKS`  | SOCKS proxy |

---

# 6. Creating an HTTP Proxy

We first create a `SocketAddress`.

```java
InetSocketAddress address =
    new InetSocketAddress("proxy.example.com", 8080);
```

Then create a proxy:

```java
Proxy proxy =
    new Proxy(Proxy.Type.HTTP, address);
```

---

# 7. Full `Proxy` Example

```java
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class ProxyExample {
    public static void main(String[] args) throws Exception {

        // Create proxy address
        InetSocketAddress address =
                new InetSocketAddress("proxy.example.com", 8080);

        // Create HTTP proxy
        Proxy proxy =
                new Proxy(Proxy.Type.HTTP, address);

        // Create URL
        URL url = new URL("http://example.com");

        // Open connection through proxy
        URLConnection connection =
                url.openConnection(proxy);

        System.out.println("Proxy Type: " + proxy.type());
        System.out.println("Proxy Address: " + proxy.address());
        System.out.println("Connection created.");
    }
}
```

### Important line

```java
url.openConnection(proxy);
```

This means:

> Open the URL connection using the specified proxy.

---

# 8. `Proxy` Class Important Methods

| Method       | Purpose                       |
| ------------ | ----------------------------- |
| `type()`     | Returns proxy type            |
| `address()`  | Returns proxy address         |
| `toString()` | Returns string representation |
| `equals()`   | Compares proxies              |
| `hashCode()` | Returns hash code             |

---

# 9. `Proxy.NO_PROXY`

Java provides:

```java
Proxy.NO_PROXY
```

It represents a **direct connection without a proxy**.

Example:

```java
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

public class DirectConnection {
    public static void main(String[] args) throws Exception {

        URL url = new URL("https://example.com");

        URLConnection connection =
                url.openConnection(Proxy.NO_PROXY);

        System.out.println("Direct connection created.");
    }
}
```

### Remember

```text
Proxy.NO_PROXY
       ↓
No proxy
       ↓
Client ─────────→ Server
```

---

# 10. System Properties vs `Proxy` Class

This is a good exam comparison.

| System Properties                     | `Proxy` Class                              |
| ------------------------------------- | ------------------------------------------ |
| Configures proxy globally/default     | Configures proxy for a specific connection |
| Uses properties like `http.proxyHost` | Uses `Proxy` object                        |
| Simple configuration                  | More control                               |
| Example: `System.setProperty()`       | Example: `url.openConnection(proxy)`       |

---

# 3.5.3 The `ProxySelector` Class

Now comes the more flexible approach.

Java provides:

```java
java.net.ProxySelector
```

A **ProxySelector** automatically determines which proxy should be used for a particular URI.

### Simple idea

Suppose your application accesses:

```text
https://example.com
http://example.org
ftp://example.net
```

You might want different proxy settings for different destinations.

`ProxySelector` can select the appropriate proxy.

---

# 11. How `ProxySelector` Works

The basic flow is:

```text
Application
     ↓
   URI
     ↓
ProxySelector
     ↓
Select appropriate proxy
     ↓
Connection
```

---

# 12. Getting the Default ProxySelector

We can obtain the system's default `ProxySelector` using:

```java
ProxySelector.getDefault()
```

### Example

```java
import java.net.ProxySelector;

public class DefaultProxySelector {
    public static void main(String[] args) {

        ProxySelector selector =
                ProxySelector.getDefault();

        System.out.println(
                "Default ProxySelector: " + selector
        );
    }
}
```

---

# 13. Selecting Proxies

The important method is:

```java
select(URI uri)
```

It returns a list of possible proxies.

### Example

```java
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.URI;
import java.util.List;

public class SelectProxy {
    public static void main(String[] args) throws Exception {

        URI uri = new URI("https://example.com");

        ProxySelector selector =
                ProxySelector.getDefault();

        List<Proxy> proxies =
                selector.select(uri);

        System.out.println("URI: " + uri);

        for (Proxy proxy : proxies) {
            System.out.println("Proxy: " + proxy);
        }
    }
}
```

### Possible output

The exact output depends on the computer's network configuration. It may show:

```text
URI: https://example.com
Proxy: DIRECT
```

`DIRECT` means no proxy is being used.

---

# 14. `connectFailed()`

`ProxySelector` also provides:

```java
connectFailed(
    URI uri,
    SocketAddress sa,
    IOException ioe
)
```

It is called when a connection through the selected proxy fails.

It allows the application to react to proxy connection failures.

---

# 15. Creating a Custom ProxySelector

We can create our own class by extending `ProxySelector`.

```java
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.ProxySelector;
import java.net.SocketAddress;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class CustomProxySelector extends ProxySelector {

    @Override
    public List<Proxy> select(URI uri) {

        List<Proxy> proxies = new ArrayList<>();

        Proxy proxy = new Proxy(
                Proxy.Type.HTTP,
                new InetSocketAddress(
                        "proxy.example.com",
                        8080
                )
        );

        proxies.add(proxy);

        return proxies;
    }

    @Override
    public void connectFailed(
            URI uri,
            SocketAddress address,
            IOException exception) {

        System.out.println(
                "Proxy connection failed: " + uri
        );
    }

    public static void main(String[] args) throws Exception {

        ProxySelector.setDefault(
                new CustomProxySelector()
        );

        URI uri =
                new URI("http://example.com");

        List<Proxy> proxies =
                ProxySelector.getDefault()
                        .select(uri);

        for (Proxy proxy : proxies) {
            System.out.println(proxy);
        }
    }
}
```

### ⚠️ Exam note

You don't necessarily need to memorize the entire custom implementation. Understand these important methods:

```java
select(URI uri)
connectFailed(...)
```

---

# 16. Important `ProxySelector` Methods

| Method            | Purpose                          |
| ----------------- | -------------------------------- |
| `getDefault()`    | Gets default ProxySelector       |
| `setDefault()`    | Sets default ProxySelector       |
| `select(URI)`     | Selects proxies for a URI        |
| `connectFailed()` | Handles proxy connection failure |

---

# 🔥 System Properties vs Proxy vs ProxySelector

This is probably the **most important comparison** in this topic.

| Feature                | System Properties                  | `Proxy`               | `ProxySelector`           |
| ---------------------- | ---------------------------------- | --------------------- | ------------------------- |
| Purpose                | Global/default proxy configuration | Specific connection   | Automatic proxy selection |
| Control                | Low                                | Medium                | High                      |
| Scope                  | General JVM configuration          | Individual connection | Based on URI              |
| Main API               | `System.setProperty()`             | `Proxy`               | `ProxySelector`           |
| Selection based on URI | ❌                                  | ❌                     | ✅                         |
| Custom selection logic | ❌                                  | ❌                     | ✅                         |

### 🧠 Easy memory trick

```text
System Properties
       ↓
"Use this proxy generally."

Proxy
       ↓
"Use this proxy for THIS connection."

ProxySelector
       ↓
"Choose the appropriate proxy for THIS URI."
```

---




