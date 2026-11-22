# Lesson 6.4: Getting Information about a Socket
### 1. Introduction
In the previous lessons, we learned how to:
- Create sockets.
- Connect clients to servers.
- Send and receive data.
- Configure proxy connections.
But there's another important question:

**How can we obtain information about an established socket connection?**

For example, suppose your Java application connects to a remote server.

You might want to know:
- What is the server's IP address?
- Which port is the server using?
- What is the client's local IP address?
- Which local port was assigned?
- Is the socket connected?
- Is the socket closed?
- Is the connection using TCP?

Java provides several methods in the `Socket` class to retrieve this information.

### 2. Why do we need socket information?
Imagine you're developing a chat application.

When a user connects to your server, you may want to:
1. Identify the remote client's IP address.
2. Identify the remote port.
3. Record connection details for debugging.
4. Check whether the connection was established.
5. Check whether the socket has been closed.
6. Display connection information in an administration dashboard.

These details are useful for network monitoring, troubleshooting, debugging and connection management.

### 3. Important Socket information methods
Let's examine the most useful methods individually.
#### A. `getInetAddress()`
Returns the IP address of the remote server to which the socket is connected.
```
System.out.println(socket.getInetAddress());
```

Example output:
```
localhost/127.0.0.1
```
Use it when you need to identify the remote server's IP address.

#### B. `getPort()`
Returns the remote server's port number.
```
System.out.println(socket.getPort());
```
Example:
```
5000
```
If the socket is not connected, this method returns `0`.

#### C. `getLocalAddress()`
Returns the local IP address to which the socket is bound.
```
System.out.println(socket.getLocalAddress());
```

This identifies the local network interface used by the connection.

#### D. `getLocalPort()`
Returns the local port number assigned to the client socket.
```
System.out.println(socket.getLocalPort());
```

For example:
```
Local Port: 52134
```
The operating system usually assigns an available temporary (ephemeral) port automatically.

### E. getRemoteSocketAddress()
Returns the complete remote endpoint, including the remote IP address and port.
```
System.out.println(socket.getRemoteSocketAddress());
```

Example:
```
localhost/127.0.0.1:5000
```

#### F. getLocalSocketAddress()
Returns the complete local endpoint.
```
System.out.println(socket.getLocalSocketAddress());
```

Example:
```
/127.0.0.1:52134
```

### 4. Checking the socket connection state
Java also provides methods to check the state of a socket.
|Method	|Meaning|
|-------|-------|
|`isConnected()`|	Whether the socket has successfully connected|
|`isBound()`|	Whether the socket has been bound to a local address|
|`isClosed()`|	Whether the socket has been closed|
|`isInputShutdown()`|	Whether its input side has been shut down|
|`isOutputShutdown()`|	Whether its output side has been shut down|

**Very important exam concept:**

`isConnected()` does not necessarily mean that the socket is currently open.

For example:
```
socket.close();

System.out.println(socket.isConnected()); // true
System.out.println(socket.isClosed());    // true
```

Why?

Because `isConnected()` remembers whether the socket successfully connected previously. Closing it does not reset that historical state. 

### 5. Complete Java practical: Socket Information Explorer
Now let's combine everything into one program.

Create a Java file named `SocketInformationExplorer.java`.
```
import java.io.*;
import java.net.*;

public class SocketInformationExplorer {

    public static void main(String[] args) {

        String host = "localhost";
        int port = 5000;

        try (Socket socket = new Socket(host, port)) {

            System.out.println("===== SOCKET INFORMATION =====");

            // Remote information
            System.out.println("Remote IP: " +
                    socket.getInetAddress());

            System.out.println("Remote Port: " +
                    socket.getPort());

            System.out.println("Remote Endpoint: " +
                    socket.getRemoteSocketAddress());

            // Local information
            System.out.println("Local IP: " +
                    socket.getLocalAddress());

            System.out.println("Local Port: " +
                    socket.getLocalPort());

            System.out.println("Local Endpoint: " +
                    socket.getLocalSocketAddress());

            // Connection states
            System.out.println("\n===== SOCKET STATUS =====");

            System.out.println("Connected: " +
                    socket.isConnected());

            System.out.println("Bound: " +
                    socket.isBound());

            System.out.println("Closed: " +
                    socket.isClosed());

            System.out.println("Input Shutdown: " +
                    socket.isInputShutdown());

            System.out.println("Output Shutdown: " +
                    socket.isOutputShutdown());

        } catch (IOException e) {

            System.out.println(
                    "Connection error: " + e.getMessage());
        }
    }
}
```

### 6. How to execute the program
1. Open IntelliJ IDEA.
2. Create `SocketInformationExplorer.java`.
3. Start your `SimpleServer.java` from Lesson 6.1.
4. Run `SocketInformationExplorer.java`.
5. Observe the information printed in the console.

**Expected output (example)**
```
===== SOCKET INFORMATION =====
Remote IP: localhost/127.0.0.1
Remote Port: 5000
Remote Endpoint: localhost/127.0.0.1:5000
Local IP: /127.0.0.1
Local Port: 52134
Local Endpoint: /127.0.0.1:52134

===== SOCKET STATUS =====
Connected: true
Bound: true
Closed: false
Input Shutdown: false
Output Shutdown: false
```

The local port and displayed addresses may differ on your computer.

### 7. Bonus practical: Observe socket information before and after connecting
This example helps you understand how socket information changes during the connection lifecycle.
```
import java.net.*;

public class SocketStateDemo {

    public static void main(String[] args) {

        Socket socket = new Socket();

        try {

            System.out.println("BEFORE CONNECTION");

            System.out.println(
                    "Connected: " + socket.isConnected());

            System.out.println(
                    "Remote IP: " + socket.getInetAddress());

            System.out.println(
                    "Remote Port: " + socket.getPort());

            System.out.println(
                    "Local Port: " + socket.getLocalPort());

            // Establish connection
            socket.connect(
                    new InetSocketAddress("localhost", 5000),
                    5000
            );

            System.out.println("\nAFTER CONNECTION");

            System.out.println(
                    "Connected: " + socket.isConnected());

            System.out.println(
                    "Remote IP: " + socket.getInetAddress());

            System.out.println(
                    "Remote Port: " + socket.getPort());

            System.out.println(
                    "Local Port: " + socket.getLocalPort());

            socket.close();

            System.out.println("\nAFTER CLOSING");

            System.out.println(
                    "Connected: " + socket.isConnected());

            System.out.println(
                    "Closed: " + socket.isClosed());

        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            try {
                socket.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
```
This experiment demonstrates three important states:
- Before connection: `isConnected()` is false.
- After connection: `isConnected()` is true.
- After closing: `isConnected()` remains true, but `isClosed() `becomes true.

### 8. Lesson 6.4 — Quick Revision
Remember these important distinctions:
|Information|	Java method|
|-----------|--------------|
|Remote IP address|	`getInetAddress()`|
|Remote port|	`getPort()`|
|Local IP address|	`getLocalAddress()`|
|Local port|	`getLocalPort()`|
|Remote endpoint|	`getRemoteSocketAddress()`|
|Local endpoint|	`getLocalSocketAddress()`|
|Connection state|	`isConnected()`|
|Binding state|	`isBound()`|
|Closed state|	`isClosed()`|
|Input shutdown|	`isInputShutdown()`|
|Output shutdown|	`isOutputShutdown()`|
