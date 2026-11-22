## **Lesson 7.4 – Getting Information About Server Socket**


### **1. What does getting information about a ServerSocket mean?**

In our previous lessons, we learned how to:

- Create a `ServerSocket`.
- Bind it to an IP address and port.
- Accept client connections.
- Configure a server before binding.
- Close server sockets.

But imagine you're running a server on a computer with multiple network interfaces.

You might want to know:

- Which IP address is the server listening on?
- Which port is it using?
- Is the socket bound?
- Is the socket closed?
- What is its complete local network address?

Java provides several methods in the `ServerSocket` class to retrieve this information.

**Why is this needed?**

Getting server socket information is useful for debugging, server monitoring, network configuration, and dynamically assigning ports.

For example, if you use port `0`, the operating system assigns an available port. Your application can retrieve the actual assigned port using `getLocalPort()`.

## **2. Important methods for getting ServerSocket information**

Let's learn the most important methods provided by Java's `ServerSocket` class.

### **A.&#x20;**`getInetAddress()`

This method returns the local IP address to which the server socket is bound.

Syntax:

```
InetAddress address = server.getInetAddress();
```

Example:

```
ServerSocket server = new ServerSocket(5000);

System.out.println(server.getInetAddress());
```

If the server is bound to a wildcard address, the output might be:

```
0.0.0.0
```

This means the server is listening on all suitable local IPv4 interfaces.

If you bind it to `127.0.0.1`, the returned address represents the loopback interface.

**Remember:** `getInetAddress()` returns the server's local listening address, not the client's IP address.

### **B.&#x20;**`getLocalPort()`

This method returns the port number on which the server is listening.

Syntax:

```
int port = server.getLocalPort();
```

Example:

```
ServerSocket server = new ServerSocket(5000);

System.out.println(server.getLocalPort());
```

Output:

```
5000
```

What if we use port `0`?

```
ServerSocket server = new ServerSocket(0);

System.out.println(server.getLocalPort());
```

Possible output:

```
54321
```

The exact number is selected by the operating system and can differ each time.

This is useful when you want to avoid hardcoding a port.

### **C.&#x20;**`getLocalSocketAddress()`

This method returns the complete local endpoint to which the server socket is bound.

Syntax:

```
SocketAddress address =
    server.getLocalSocketAddress();
```

Example:

```
ServerSocket server = new ServerSocket(5000);

System.out.println(server.getLocalSocketAddress());
```

Possible output:

```
0.0.0.0/0.0.0.0:5000
```

The exact formatting depends on the environment.

What's the difference between the three methods?

| Method                    | Returns                                  |
| ------------------------- | ---------------------------------------- |
| `getInetAddress()`        | Local IP address                         |
| `getLocalPort()`          | Local port number                        |
| `getLocalSocketAddress()` | Complete local endpoint (address + port) |

### **D.&#x20;**`isBound()`

This method checks whether the server socket has successfully been bound.

Syntax:

```
boolean result = server.isBound();
```

Example:

```
ServerSocket server = new ServerSocket();

System.out.println(server.isBound());

server.bind(new InetSocketAddress(5000));

System.out.println(server.isBound());
```

Output:

```
false
true
```

Remember our Lesson 7.3 practical example?

We used this exact method to check whether the socket was bound.

### **E.&#x20;**`isClosed()`

This method checks whether the server socket has been closed.

Syntax:

```
boolean result = server.isClosed();
```

Example:

```
ServerSocket server = new ServerSocket(5000);

System.out.println(server.isClosed());

server.close();

System.out.println(server.isClosed());
```

Output:

```
false
true
```

This is useful when checking the lifecycle state of a server socket.

### **F.&#x20;**`toString()`

This method returns a string representation of the server socket.

Example:

```
ServerSocket server = new ServerSocket(5000);

System.out.println(server);
```

Possible output:

```
ServerSocket[addr=0.0.0.0,localport=5000]
```

It is useful for quick debugging, but for structured logging, prefer explicitly logging the address and port.

### **G.&#x20;**`getChannel()`

This method returns the `ServerSocketChannel` associated with the server socket, if one exists.

Example:

```
ServerSocket server = new ServerSocket(5000);

System.out.println(server.getChannel());
```

Output:

```
null
```

Why null?

Because this server socket was created using the traditional `ServerSocket` constructor, not through `ServerSocketChannel.open()`.

This method is useful when working with Java NIO (New I/O), which we'll encounter in more advanced network programming.


## **3. Complete practical example – Inspecting a ServerSocket**

Now let's combine these methods into one Java program.

We'll create a server socket on an automatically assigned port and inspect its properties.

Practical example 1

**ServerSocketInformation.java**

```

import java.io.*;
import java.net.*;

public class ServerSocketInformation {

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket()) {

            // Bind to loopback using an automatically selected port
            server.bind(
                new InetSocketAddress(
                    InetAddress.getLoopbackAddress(),
                    0
                )
            );

            System.out.println("SERVER SOCKET INFORMATION");
            System.out.println("-------------------------");

            // 1. Local IP address
            System.out.println(
                "Local IP: " + server.getInetAddress()
            );

            // 2. Local port
            System.out.println(
                "Local Port: " + server.getLocalPort()
            );

            // 3. Complete local endpoint
            System.out.println(
                "Local Address: "
                + server.getLocalSocketAddress()
            );

            // 4. Binding state
            System.out.println(
                "Is Bound: " + server.isBound()
            );

            // 5. Closed state
            System.out.println(
                "Is Closed: " + server.isClosed()
            );

            // 6. String representation
            System.out.println(
                "Server Details: " + server
            );

            // 7. Associated channel
            System.out.println(
                "Server Channel: " + server.getChannel()
            );

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}

```

### **How to execute**

Compile:

```
javac ServerSocketInformation.java
```

Run:

```
java ServerSocketInformation
```

Possible output:

```
SERVER SOCKET INFORMATION
-------------------------
Local IP: localhost/127.0.0.1
Local Port: 54321
Local Address: localhost/127.0.0.1:54321
Is Bound: true
Is Closed: false
Server Details: ServerSocket[addr=localhost/127.0.0.1,localport=54321]
Server Channel: null
```

Your assigned port may be different.

### **Let's understand what happened**

1. `new ServerSocket()` created an unbound socket.
2. `bind()` assigned the loopback address and an automatically selected port.
3. `getInetAddress()` retrieved the local IP address.
4. `getLocalPort()` retrieved the assigned port.
5. `getLocalSocketAddress()` retrieved the complete endpoint.
6. `isBound()` confirmed that the socket was bound.
7. `isClosed()` confirmed that the socket was still open.
8. `getChannel()` returned `null` because we didn't create the socket using Java NIO.

## **4. What happens to ServerSocket information after closing?**

This is an interesting detail that many beginners overlook.

Consider:

```
ServerSocket server = new ServerSocket(5000);

server.close();

System.out.println(server.isBound());
System.out.println(server.isClosed());
System.out.println(server.getLocalPort());
```

Output:

```
true
true
5000
```

Why does `isBound()` still return `true`?

Because Java remembers that the socket was successfully bound, even after it has been closed.

Similarly, the local port and local address remain available after closing a previously bound socket.

Let's understand the states:

| ServerSocket state  | `isBound()` | `isClosed()` |
| ------------------- | ----------- | ------------ |
| Created but unbound | false       | false        |
| Bound and open      | true        | false        |
| Bound and closed    | true        | true         |

This is important:

**Being bound and being open are two different properties.**

A socket can be bound and closed simultaneously.



## **5. ServerSocket information versus client Socket information**

This distinction is especially important for network programming.

Suppose a client connects to your server.

```
Socket client = server.accept();
```

Now we have two different objects:

- `ServerSocket`: Represents the server's listening endpoint.
- `Socket`: Represents the accepted client connection.

The `ServerSocket` tells us where our server is listening.

The accepted `Socket` tells us information about the actual client connection.

For example:

**ServerSocket information**

```
server.getInetAddress();
server.getLocalPort();
server.getLocalSocketAddress();
```

**Accepted Socket information**

```
client.getInetAddress();
client.getPort();
client.getLocalAddress();
client.getLocalPort();
```

Let's compare them:

| Method                    | ServerSocket              | Accepted Socket                   |
| ------------------------- | ------------------------- | --------------------------------- |
| `getInetAddress()`        | Server's local address    | Client's remote address           |
| `getLocalPort()`          | Server listening port     | Local port of this connection     |
| `getLocalSocketAddress()` | Server listening endpoint | Local endpoint of this connection |

Notice that the same method name can have a different meaning depending on the class.

### **Practical example 2 – Getting client connection information**

**Server.java**

```

import java.io.*;
import java.net.*;

public class Server {

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket(5004)) {

            System.out.println(
                "Server listening on: "
                + server.getLocalSocketAddress()
            );

            try (Socket client = server.accept()) {

                System.out.println("\nCLIENT INFORMATION");
                System.out.println("------------------");

                System.out.println(
                    "Client IP: " + client.getInetAddress()
                );

                System.out.println(
                    "Client Port: " + client.getPort()
                );

                System.out.println(
                    "Server Local IP: " + client.getLocalAddress()
                );

                System.out.println(
                    "Server Local Port: " + client.getLocalPort()
                );
            }

        } catch (IOException e) {

            e.printStackTrace();
        }
    }
}

```

**Client.java**

```

import java.net.*;

public class Client {

    public static void main(String[] args) {

        try (Socket socket =
                 new Socket("localhost", 5004)) {

            System.out.println("Connected to server!");

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}

```

Run the server first, then the client.

For a local connection, the server might display:

```
Server listening on: 0.0.0.0/0.0.0.0:5004

CLIENT INFORMATION
------------------
Client IP: /127.0.0.1
Client Port: 54322
Server Local IP: /127.0.0.1
Server Local Port: 5004
```

The client's port is dynamically assigned and may differ.

**Important:** The client port is not the server listening port. Each TCP connection has its own local and remote endpoints.

## **6. Common mistakes to avoid**

| Mistake                                                             | Correct understanding                               |
| ------------------------------------------------------------------- | --------------------------------------------------- |
| Using `getInetAddress()` to get the client's IP from `ServerSocket` | Use the accepted client's `Socket.getInetAddress()` |
| Assuming `getLocalPort()` always returns a valid port               | It returns `-1` if the server socket is unbound     |
| Assuming `isBound()` becomes false after closing                    | It remains true if the socket was previously bound  |
| Assuming `getChannel()` always returns a channel                    | It may return `null`                                |
| Confusing local and remote ports                                    | Understand which endpoint each object represents    |

## **7. Lesson 7.4 – Quick revision**

Here are the important points to remember:

| Method                    | Purpose                                          |
| ------------------------- | ------------------------------------------------ |
| `getInetAddress()`        | Gets the server's local IP address               |
| `getLocalPort()`          | Gets the listening port                          |
| `getLocalSocketAddress()` | Gets the complete local endpoint                 |
| `isBound()`               | Checks whether the socket was successfully bound |
| `isClosed()`              | Checks whether the socket is closed              |
| `toString()`              | Returns a string representation                  |
| `getChannel()`            | Gets an associated NIO channel, if available     |
