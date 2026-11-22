
# Lesson 7.5 – Socket Options
## 1. What are socket options?
A *socket option* is a setting that controls how a socket behaves during network communication.

Think of a socket as a communication endpoint. Socket options let us configure details such as:

- How long a socket waits for incoming data.
- Whether a local port can be reused.
- How much data the operating system should buffer when receiving.
- The type of service or priority associated with network traffic.

### Why do we need socket options?
Without configuration, sockets use operating-system and Java defaults. Those defaults are often fine, but a server may need more control—for example, to avoid waiting forever for a client or to restart quickly using the same port.

In Java, the `Socket` and `ServerSocket` classes provide methods to configure and inspect these options.

## 2. The four socket options in this lesson
### A. `SO_TIMEOUT` – Set a waiting time limit
**Purpose:** Prevent a server or client from waiting forever during certain blocking operations.

Java provides:
```
socket.setSoTimeout(5000);
```

The value is in milliseconds, so `5000` means 5 seconds.

It affects:
- `ServerSocket.accept()` – waiting for a client connection.
- `Socket` input operations such as `read()` – waiting for incoming data.

If the timeout expires, Java throws `SocketTimeoutException`.

Important:
- 0 means no timeout (wait indefinitely).
- The timeout must be set before the blocking operation.
- A timeout does **not** automatically close the socket. 
  
Example:
```
ServerSocket server = new ServerSocket(5000);

server.setSoTimeout(10000); // Wait up to 10 seconds

try {
    Socket client = server.accept();
    System.out.println("Client connected!");
} catch (SocketTimeoutException e) {
    System.out.println("No client connected within 10 seconds.");
}
```

### B. `SO_REUSEADDR` – Reuse a local address
**Purpose:** Help a server bind to its intended local address and port even when a previous TCP connection involving that address is still in the `TIME_WAIT` state.

Example:
```
ServerSocket server = new ServerSocket();

server.setReuseAddress(true); // Set before binding

server.bind(new InetSocketAddress(5000));
```

#### Why is this useful?

Imagine:
1. Your server starts on port `5000`.
2. It accepts a client connection.
3. The server stops.
4. You immediately restart the server.
5. The operating system may still have a previous connection in `TIME_WAIT`.

`SO_REUSEADDR` can help the server bind again without waiting for that previous connection to leave its timeout state.

**Important:** Set this option before binding. Its exact behavior is platform-dependent, and it does not mean that any other process can freely take over your port. 


### C. `SO_RCVBUF` – Receive buffer size
**Purpose:** Configure a suggested size for the operating system's incoming network-data buffer.

Think of the receive buffer as a temporary holding area for incoming data before your application reads it.

Java example:
```
socket.setReceiveBufferSize(65536);

System.out.println(
    "Receive buffer: " + socket.getReceiveBufferSize()
);
```

Here:
- `65536` bytes = 64 KB.
- `setReceiveBufferSize()` requests a buffer size.
- `getReceiveBufferSize()` reports the size actually used by the platform.

#### Why is it needed?
For applications receiving large amounts of data, a larger receive buffer can help improve network throughput. However, it does not guarantee faster communication.

A special server-side detail: ServerSocket.`setReceiveBufferSize()` sets the proposed receive-buffer size for sockets accepted from that server. If you need a TCP receive window larger than 64 KB, configure it before binding the server socket. 


### D. Class of Service – Traffic class (`IP_TOS`)
**Purpose:** Allow an application to provide a traffic-class hint in the IP header for packets sent through a socket.

Java provides:
```
socket.setTrafficClass(0x10);
```

The value must be between `0` and `255`.

For example:
```
socket.setTrafficClass(0x10);

System.out.println(socket.getTrafficClass());
```

The value `0x10` is a traffic-class value historically associated with low delay in the IPv4 Type of Service field.

**Important:** This is only a hint. The operating system, network hardware, routers, or network policies may ignore or modify it. It does not guarantee that your packets receive priority. 


## 3. Quick comparison
|Option	|Purpose	|Java method|
|-------|-----------|-----------|
|SO_TIMEOUT|	Limit waiting time	|setSoTimeout()|
|SO_REUSEADDR|	Allow address reuse in supported circumstances|	setReuseAddress()|
|SO_RCVBUF|	Suggest incoming buffer size|	setReceiveBufferSize()|
|IP_TOS|	Set traffic-class hint|	setTrafficClass()|

One important distinction: `SO_TIMEOUT` controls waiting time; it does not control how long a complete connection is allowed to remain open.

## 4. Complete practical example – Configuring a Java server
Let's build a small TCP server that uses three of the options:
- `SO_REUSEADDR`
- `SO_RCVBUF`
- `SO_TIMEOUT`

Then we will connect to it using a Java client that sets the traffic class.

**Project structure**
```
SocketOptionsDemo/
├── SocketOptionsServer.java
└── SocketOptionsClient.java
```

**File 1:** `SocketOptionsServer.java`
```
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SocketOptionsServer {

    public static void main(String[] args) {

        try (ServerSocket server = new ServerSocket()) {

            // 1. Enable SO_REUSEADDR before binding
            server.setReuseAddress(true);

            // 2. Configure the proposed receive buffer
            server.setReceiveBufferSize(65536);

            // 3. Bind the server to port 5000
            server.bind(new InetSocketAddress("127.0.0.1", 5000));

            // 4. Set a 15-second timeout for accept()
            server.setSoTimeout(15000);

            System.out.println("Server started on port 5000");
            System.out.println("Reuse address: " +
                    server.getReuseAddress());
            System.out.println("Receive buffer: " +
                    server.getReceiveBufferSize());

            System.out.println("Waiting for client...");

            try (Socket client = server.accept()) {

                // Set a 5-second timeout for reading client data
                client.setSoTimeout(5000);

                System.out.println("Client connected!");
                System.out.println("Client IP: " +
                        client.getInetAddress());

                BufferedReader input = new BufferedReader(
                        new InputStreamReader(
                                client.getInputStream()
                        )
                );

                String message = input.readLine();

                System.out.println("Received: " + message);

            } catch (SocketTimeoutException e) {
                System.out.println(
                        "Client did not connect or send data in time."
                );
            }

        } catch (SocketTimeoutException e) {
            System.out.println("Server accept timed out.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```


**File 2:** `SocketOptionsClient.java`

```
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketOptionsClient {

    public static void main(String[] args) {

        try (Socket socket = new Socket()) {

            // Set traffic-class hint before connecting
            socket.setTrafficClass(0x10);

            // Connect to the server
            socket.connect(
                    new InetSocketAddress("127.0.0.1", 5000)
            );

            System.out.println("Connected to server!");
            System.out.println("Traffic class: " +
                    socket.getTrafficClass());

            PrintWriter output = new PrintWriter(
                    socket.getOutputStream(), true
            );

            output.println("Hello from Java client!");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```


## 5. Run the example in IntelliJ IDEA
1. Create a Java project using Java 17 or newer.
2. Create both Java classes.
3. Run `SocketOptionsServer` first.
4. Run `SocketOptionsClient` in a second run window.
5. Observe the server output.

Expected server output (the actual buffer size and client address may vary):
```
Server started on port 5000
Reuse address: true
Receive buffer: 65536
Waiting for client...
Client connected!
Client IP: /127.0.0.1
Received: Hello from Java client!
```

If no client connects within 15 seconds, the server's `accept()` operation times out. If a client connects but sends no line within 5 seconds, the read operation times out.

**Notice:** The server's accept() timeout and the accepted client's read timeout are separate settings.

#### Small correction to the server example
The inner `catch (SocketTimeoutException)` already handles timeouts from both `accept()` and `readLine()`. Therefore, remove the separate outer `catch (SocketTimeoutException)` from the server code. Keep the outer `catch (Exception e)`.

The corrected ending of the server's `main()` method is:

```
            } catch (SocketTimeoutException e) {
                System.out.println(
                        "Accept or read operation timed out."
                );
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

```

## 6. Important points to remember
1. Configure `SO_REUSEADDR` before binding the `ServerSocket`.
2. Configure the server's receive buffer before binding when requesting a TCP receive window larger than 64 KB.
3. `SO_TIMEOUT` uses milliseconds, and 0 means infinite waiting.
4. `SO_RCVBUF` is a suggested buffer size; the operating system determines the actual size.
5. `setTrafficClass()` is a hint, not a guarantee of network priority.

