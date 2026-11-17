# Lesson 6.3: Constructing and Connecting Sockets
In this lesson, we'll study four important areas:
1. Basic Socket Constructors.
2. The Socket Class.
3. Constructing Without Connecting.
4. Proxy Servers.

**Learning objectives**

By the end of this lesson, you should be able to:
- Create TCP sockets using different constructors.
- Understand how Java establishes a socket connection.
- Create an unconnected socket and connect it later.
- Understand how proxy servers work.
- Write Java programs that connect to remote servers through proxies.

## Part 1: Basic Socket Constructors
### 1. What is a Socket constructor?
A constructor is a special method used to create an object.

In Java, the `Socket` class provides several constructors that allow us to create client sockets in different ways.

For example:
```
Socket socket = new Socket("example.com", 80);
```

This statement does two things:
1. Creates a TCP socket.
2. Establishes a connection to `example.com` on port `80`.

We don't need to call `connect()` separately because this constructor connects automatically.

### 2. Important Socket constructors
Here are the main constructors available in modern Java.
|Constructor|	Purpose|
|-----------|----------|
|`Socket()`	|Creates an unconnected socket.|
|`Socket(String host, int port)`	|Creates and connects to a server using its hostname.|
|`Socket(InetAddress address, int port)`	|Creates and connects using an IP address.|
|`Socket(String host, int port, InetAddress localAddr, int localPort)`	|Connects to a remote server while specifying the local address and port.|
|`Socket(InetAddress address, int port, InetAddress localAddr, int localPort)`	|Same as above, using the remote IP address.|
|`Socket(Proxy proxy)`	|Creates an unconnected socket with a specified proxy configuration.|


**Note:** Java also has older constructors containing a `boolean` stream parameter. They are deprecated for removal. We will not use them in modern Java applications.

### 3. Constructor example: Using hostname
Create a Java file named `BasicSocketClient.java`.
```
import java.net.Socket;
import java.io.IOException;

public class BasicSocketClient {

    public static void main(String[] args) {

        String host = "localhost";
        int port = 5000;

        try (Socket socket = new Socket(host, port)) {

            System.out.println("Connection successful!");

            System.out.println("Server: " +
                    socket.getInetAddress());

            System.out.println("Remote port: " +
                    socket.getPort());

            System.out.println("Local port: " +
                    socket.getLocalPort());

        } catch (IOException e) {
            System.out.println("Connection failed: " +
                    e.getMessage());
        }
    }
}
```
**Explanation:**
- `localhost` refers to your own computer.
- `5000` is the port where your server is listening.
- `new Socket(host, port)` immediately attempts a connection.
- `getInetAddress()` returns the remote IP address.
- `getPort()` returns the remote port.
- `getLocalPort()` returns the local port assigned to your client.
Run your server from Lesson 6.1 before running this client.

### 4. Constructor example: Using InetAddress
Sometimes we already have an IP address instead of a hostname.

Java provides the `InetAddress` class to represent IP addresses.
```
import java.net.*;

public class InetAddressSocketClient {

    public static void main(String[] args) {

        try {

            InetAddress address =
                    InetAddress.getByName("localhost");

            Socket socket =
                    new Socket(address, 5000);

            System.out.println("Connected successfully!");

            socket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
Here:
- `InetAddress.getByName()` resolves the hostname.
- `new Socket(address, 5000)` establishes the TCP connection using the resolved address.

### 5. Constructor example: Specifying the local address and port
Normally, the operating system automatically selects a local port for the client.

However, sometimes an application needs to specify its local network endpoint.
```
For example:
Socket socket = new Socket(
    "localhost",
    5000,
    null,
    0
);
```

**What do the parameters mean?**

|Parameter	|Value	|Meaning|
|-----------|-------|-------|
|Remote host	|`localhost	`|Server hostname|
|Remote port	|`5000`	|Server port|
|Local address|	`null`	|Any local address|
|Local port	|`0`	|OS selects an available port|

This is useful when an application needs to control which local interface or port it uses.

Practical caution: binding to a specific local port can fail if another application already occupies that port.

## Part 2: The Socket Class
### 6. What is the Java Socket class?
The Socket class belongs to:
```
java.net.Socket
```

It represents the client-side endpoint of a TCP connection.

It provides methods to:
- Connect to remote servers.
- Send data.
- Receive data.
- Retrieve connection information.
- Configure socket options.
- Close connections.
The socket class is not itself an HTTP client. It provides lower-level TCP communication, and your application implements the protocol carried over that connection. 

### 7. Important methods of the Socket class
|Method	|Purpose|
|-------|-------|
|`connect()`	|Connects to a remote server.|
|`bind()`	|Binds to a local address.|
|`getInputStream()`	|Receives incoming data.|
|`getOutputStream()`	|Sends outgoing data.|
|`getInetAddress()`	|Gets remote IP address.|
|`getPort()`	|Gets remote port.|
|`getLocalPort()`	|Gets local port.|
|`isConnected()`	|Checks whether connection was successfully established.|
|`isClosed()`	|Checks whether socket is closed.|
|`close()`|	Closes the socket.|

### 8. Practical example: Exploring Socket methods
Create `SocketMethodsDemo.java`.
```
import java.net.*;
import java.io.*;

public class SocketMethodsDemo {

    public static void main(String[] args) {

        try (Socket socket =
                     new Socket("localhost", 5000)) {

            System.out.println(
                    "Connected: " + socket.isConnected());

            System.out.println(
                    "Closed: " + socket.isClosed());

            System.out.println(
                    "Remote address: " +
                    socket.getInetAddress());

            System.out.println(
                    "Remote port: " + socket.getPort());

            System.out.println(
                    "Local address: " +
                    socket.getLocalAddress());

            System.out.println(
                    "Local port: " +
                    socket.getLocalPort());

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

**Expected observation:**
```
Connected: true
Closed: false
Remote address: localhost/127.0.0.1
Remote port: 5000
Local address: /127.0.0.1
Local port: [OS assigned port]
```

Your exact output may differ depending on your network configuration.

One important detail: `isConnected()` indicates whether the socket was ever successfully connected. It does not necessarily mean that the socket is currently open. Check `isClosed()` as well.

## Part 3: Constructing Without Connecting
### 9. What does constructing without connecting mean?
In the previous examples, we used:
```
Socket socket = new Socket("localhost", 5000);
```

This creates the socket and immediately attempts to connect.

But what if we want to:
- Create the socket first.
- Configure it before connecting.
- Select a particular local address.
- Set socket options.
- Choose a connection timeout.
Java allows us to create a socket without establishing a connection.

We use:
```
Socket socket = new Socket();
```

This creates an unconnected socket.

Later, we establish the connection using:
```
socket.connect(endpoint);
```

Or:
```
socket.connect(endpoint, timeout);
```

The second version allows us to specify a connection timeout in milliseconds. A timeout of zero means no timeout limit. 

### 10. Why is this useful?
Consider a real-world application that connects to a remote database server.

You may want to:
1. Create the socket.
2. Configure its options.
3. Bind it to a particular local address.
4. Connect to the database server.
5. Exchange data.
This gives you more control over the connection process.

### 11. Complete Java example: Unconnected Socket
Create `UnconnectedSocketDemo.java`.
```
import java.net.*;
import java.io.*;

public class UnconnectedSocketDemo {

    public static void main(String[] args) {

        try (Socket socket = new Socket()) {

            // STEP 1: Create an unconnected socket
            System.out.println(
                    "Socket created!");

            System.out.println(
                    "Connected: " +
                    socket.isConnected());

            // STEP 2: Create server address
            SocketAddress endpoint =
                    new InetSocketAddress(
                            "localhost", 5000);

            // STEP 3: Connect with a 5-second timeout
            socket.connect(endpoint, 5000);

            // STEP 4: Verify connection
            System.out.println(
                    "Connected successfully!");

            System.out.println(
                    "Connected: " +
                    socket.isConnected());

        } catch (SocketTimeoutException e) {

            System.out.println(
                    "Connection timed out!");

        } catch (IOException e) {

            System.out.println(
                    "Connection error: " +
                    e.getMessage());
        }
    }
}
```


### 12. Understanding the execution
1. Create socket
   
   `new Socket()`
   
   Creates an unconnected socket.
2. Create endpoint
   
   `new InetSocketAddress("localhost", 5000)`
   
   Represents the remote server address.
3. Connect
   
   `socket.connect(endpoint, 5000)`
   
   Attempts to establish a connection with a five-second timeout.
4. Verify
   
   `socket.isConnected()`
   
   Checks whether the socket successfully connected.

### 13. Difference between connected and unconnected constructors
|Connected constructor	|Unconnected constructor|
|-----------------------|-----------------------|
|new Socket(host, port)	|new Socket()|
|Attempts connection immediately|	Does not connect immediately|
|Less configuration flexibility before connection|	Allows configuration before connection|
|Convenient for simple clients|	Useful for advanced networking|

**Teacher's important point:** Constructing an unconnected socket does not mean that it has no underlying socket implementation. It means that the remote connection has not yet been established.

## Part 4: Proxy Servers
### 14. What is a Proxy Server?
A **proxy server** is an intermediary server that forwards network requests between a client and a destination server.

Instead of connecting directly to a destination server, the client sends its connection through the proxy.

Let's understand with an example.

Suppose:
- Client: Your Java application.
- Proxy server: `192.168.1.100:1080`.
- Destination server: `example.com:80`.
Without a proxy:
```
Java Client
     |
     v
Destination Server
```

With a proxy:
```
Java Client
     |
     v
Proxy Server
     |
     v
Destination Server
```

The proxy receives the connection request and helps establish communication with the destination.

### 15. Why are proxy servers needed?
Proxy servers are used for several reasons:
|Purpose	|Explanation|
|---------------|-----------|
|Security	|Organizations can control outbound network access.|
|Privacy|	The destination may see the proxy's network address rather than the client's direct address.|
|Access control|	Restricts access to certain destinations.|
|Monitoring|	Organizations can monitor network traffic according to their policies.|
|Network management|	Provides centralized routing and filtering.|
|Caching|	Some proxies cache content to reduce repeated network requests.|

**Important:** A proxy does not automatically encrypt traffic or guarantee anonymity. Those properties depend on the proxy protocol, encryption and configuration.

### 16. Types of Java proxy configurations
Java provides the `Proxy` class in `java.net`.

The important proxy types are:
|Type	|Purpose|
|-------|-------|
|`Proxy.Type.DIRECT`|	Direct connection without a proxy|
|`Proxy.Type.HTTP`|	Proxy for high-level protocols such as HTTP and FTP|
|`Proxy.Type.SOCKS`|	SOCKS V4 or V5 proxy|

These types are defined in the official Java documentation. 


### 17. Java Proxy example: Creating a SOCKS proxy connection
Let's create a socket that connects through a SOCKS proxy.

For this example, assume you have access to a SOCKS proxy at:
```
127.0.0.1:1080
```

This is an example address, not a proxy server that Java automatically provides.

Create `ProxySocketClient.java`.
```
import java.net.*;
import java.io.*;

public class ProxySocketClient {

    public static void main(String[] args) {

        String destination = "example.com";
        int destinationPort = 80;

        // STEP 1: Define proxy address
        SocketAddress proxyAddress =
                new InetSocketAddress(
                        "127.0.0.1", 1080);

        // STEP 2: Create SOCKS proxy
        Proxy proxy = new Proxy(
                Proxy.Type.SOCKS,
                proxyAddress
        );

        // STEP 3: Create socket using proxy
        try (Socket socket = new Socket(proxy)) {

            // STEP 4: Connect to destination
            socket.connect(
                    new InetSocketAddress(
                            destination,
                            destinationPort),
                    5000
            );

            System.out.println(
                    "Connected through SOCKS proxy!");

            System.out.println(
                    "Remote server: " +
                    socket.getInetAddress());

            System.out.println(
                    "Remote port: " +
                    socket.getPort());

        } catch (IOException e) {

            System.out.println(
                    "Proxy connection failed: " +
                    e.getMessage());
        }
    }
}
```
**Understanding the code:**
1. `InetSocketAddress` represents the proxy's address.
2. `new Proxy(Proxy.Type.SOCKS, proxyAddress)` creates a SOCKS proxy configuration.
3. `new Socket(proxy)` creates an unconnected socket using that proxy configuration.
4. `socket.connect()` connects to the destination through the SOCKS proxy.
The proxy must be running, reachable and configured to allow the destination connection.

### 18. Direct connection without a proxy
Sometimes we want to ignore configured proxy settings.

Java provides:
```
Socket socket = new Socket(Proxy.NO_PROXY);
```

This explicitly requests a direct connection without proxying.

For example:
```
import java.net.*;

public class DirectSocketClient {

    public static void main(String[] args) {

        try (Socket socket =
                     new Socket(Proxy.NO_PROXY)) {

            socket.connect(
                    new InetSocketAddress(
                            "example.com", 80),
                    5000
            );

            System.out.println(
                    "Direct connection successful!");

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
```

### 19. HTTP Proxy vs SOCKS Proxy
This distinction is important.
|HTTP Proxy	|SOCKS Proxy|
|---------------|-----------|
|Designed for high-level protocols such as HTTP and FTP	|Supports general TCP connections|
|Understands application-level proxy requests	|Operates at a lower networking level|
|Commonly used for web traffic|	Can support different TCP-based applications|
|Often used through Java URL/HTTP APIs|	Supported through Socket(Proxy)|

**Java API detail:** For an HTTP proxy, Java's higher-level HTTP client is usually the appropriate choice. The raw `Socket(Proxy)` constructor is demonstrated here with SOCKS proxies, as in Oracle's official example. 


Example of using an HTTP proxy with Java's modern HTTP client:
```
import java.net.*;
import java.net.http.*;

public class HttpProxyClient {

    public static void main(String[] args) {

        try {

            ProxySelector proxySelector =
                    ProxySelector.of(
                            new InetSocketAddress(
                                    "127.0.0.1", 8080));

            HttpClient client =
                    HttpClient.newBuilder()
                            .proxy(proxySelector)
                            .build();

            HttpRequest request =
                    HttpRequest.newBuilder()
                            .uri(URI.create(
                                    "http://example.com"))
                            .GET()
                            .build();

            HttpResponse<String> response =
                    client.send(
                            request,
                            HttpResponse.BodyHandlers.ofString()
                    );

            System.out.println(
                    "Status: " +
                    response.statusCode());

            System.out.println(
                    response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```
Here, the HTTP client uses the configured HTTP proxy at `127.0.0.1:8080`. You must provide a working proxy to execute this example successfully.

## Part 5: Important concepts — Lesson 6.3
Let's revise everything before the quiz.
|Concept	|Remember|
|---------------|--------|
|`Socket()`	|Creates an unconnected socket|
|`Socket(host, port)`|	Creates and connects immediately|
|`InetAddress`|	Represents an IP address|
|`InetSocketAddress`|	Represents an IP address/hostname and port|
|`connect()`|	Establishes a connection|
|`bind()`|	Binds a socket to a local endpoint|
|`Proxy`|	Represents a proxy configuration|
|`Proxy.Type.SOCKS`|	Configures SOCKS proxying|
|`Proxy.NO_PROXY`|	Forces a direct connection|
|`SocketAddress`|	Represents a socket endpoint|
