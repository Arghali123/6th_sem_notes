# Lesson 6.5 — Setting Socket Options
Learning objectives

By the end of this lesson, you should be able to:
1. Explain why socket options are needed.
2. Configure TCP behavior, timeouts, buffers, and connection settings.
3. Use Java's Socket methods to set and retrieve options.
4. Understand the practical effects and limitations of each option.
5. Build and run a complete Java example in IntelliJ IDEA.

### 1. What are socket options?
A **socket option** is a setting that controls how a socket behaves during network communication.

When you create a Java `Socket`, Java and the operating system use default settings. Those defaults are suitable for many applications, but sometimes you need more control.

For example:
- A chat application may need low-latency delivery of small messages.
- A client downloading large files may benefit from a larger receive buffer.
- A client communicating with a slow server should avoid waiting forever for a response.
- A long-lived connection may need TCP keep-alive probes.

#### Why are socket options needed?
They let you tune communication behavior, improve responsiveness, manage resources, and handle network failures more predictably.

In Java, you configure these options using methods such as `setTcpNoDelay()`, `setSoTimeout()`, and `setKeepAlive()`, then inspect them using corresponding `get...()` methods.

### 2. The nine socket options in this lesson
We'll cover all nine options in your syllabus.

#### A. `TCP_NODELAY` — Disable Nagle's algorithm
Latency

**Purpose:** Controls whether TCP may combine small outgoing writes before sending them.

By default, TCP may use Nagle's algorithm to reduce the number of small packets. This can improve network efficiency, but sometimes adds delay.
```
socket.setTcpNoDelay(true);
```

- `true`: Disable Nagle's algorithm. Small writes can be sent without waiting for the algorithm to combine them.
- `false`: Allow Nagle's algorithm.
Check the current setting:
```
System.out.println(socket.getTcpNoDelay());
```

**When is it useful?**
- Interactive applications.
- Real-time messaging.
- Applications that send frequent, small messages.

Be aware: disabling Nagle's algorithm can increase the number of packets. It does not guarantee that every write becomes a separate network packet.

#### B. `SO_LINGER` — Control socket closing behavior
Connection closing

**Purpose:** Controls how `close()` behaves when unsent data remains.
```
socket.setSoLinger(true, 5);
```

This enables linger with a timeout of 5 seconds.
|Configuration	|Meaning|
|---------------|-------|
|`setSoLinger(false, 0)`|	Disable linger; this is the usual default|
|`setSoLinger(true, 5)`|	Wait up to 5 seconds during close for pending data|
|`setSoLinger(true, 0)`|	Abortive close; can discard pending data and reset the connection|

To inspect it:
```
System.out.println(socket.getSoLinger());
```

A result of `-1` means linger is disabled.

#### When is it useful?
When an application needs to control how a TCP connection closes, especially when pending outgoing data matters.

Be careful: enabling linger can make `close()` block. A zero-second linger can cause data loss.

#### C. `SO_TIMEOUT` — Read timeout
Very important

**Purpose:** Prevents a blocking socket read from waiting indefinitely.
```
socket.setSoTimeout(3000);
```

The timeout is measured in milliseconds.
- `3000` = 3 seconds.
- `5000` = 5 seconds.
- `0` = infinite timeout (disabled).
Example:
```
try {
    int data = socket.getInputStream().read();
    System.out.println("Received: " + data);
} catch (SocketTimeoutException e) {
    System.out.println("The server did not send data in time.");
}
```

**Important:** A read timeout does not automatically close the socket. You can still use the socket after catching `SocketTimeoutException`, provided the connection is otherwise valid.

Also, `SO_TIMEOUT` applies to blocking reads, not to the time taken by `Socket.connect()`. For connection timeouts, use:
```
socket.connect(new InetSocketAddress("localhost", 5000), 5000);
```

#### D. `SO_RCVBUF` — Receive buffer size
**Purpose:** Controls the requested size of the socket's incoming-data buffer.
```
socket.setReceiveBufferSize(64 * 1024);
```

This requests a receive buffer size of 64 KB.

Check the actual reported size:
```
System.out.println(socket.getReceiveBufferSize());
```

When is it useful?
- Large file transfers.
- High-volume network communication.
- Connections with high bandwidth or significant network delay.
Important: the requested size is a hint to the operating system. The actual reported size may differ. For a client that needs a TCP receive window larger than 64 KB, request it **before connecting**.

#### E. `SO_SNDBUF` — Send buffer size
**Purpose:** Controls the requested size of the socket's outgoing-data buffer.
```
socket.setSendBufferSize(64 * 1024);
```

Check it:
```
System.out.println(socket.getSendBufferSize());
```

**When is it useful?**
- Sending large files.
- High-volume data transfer.
- Applications that need to tune outgoing network throughput.
Like `SO_RCVBUF`, this is a request to the operating system, not a guarantee of the exact buffer size.

#### F. `SO_KEEPALIVE` — Keep connection alive
**Purpose:** Enables TCP keep-alive probes to help detect connections that may no longer be reachable.
```
socket.setKeepAlive(true);
```

Check it:
```
System.out.println(socket.getKeepAlive());
```
**When is it useful?**
- Long-lived client-server connections.
- Applications that maintain persistent TCP connections.
- Detecting certain broken or unreachable connections.

Important: TCP keep-alive is not an application-level heartbeat. The operating system controls probe timing, and defaults can be quite long. If your application needs quick detection, implement its own heartbeat protocol.

#### G. `SO_REUSEADDR` — Reuse local address
**Purpose:** Allows an address to be reused under certain operating-system networking rules, including situations involving a previous TCP connection in `TIME_WAIT.`
```
socket.setReuseAddress(true);
```

Check it:
```
System.out.println(socket.getReuseAddress());
```

**When is it useful?**

It is particularly relevant to servers that need to bind to a well-known local port again after restarting.

For a client socket, it is less commonly needed. Configure it **before binding** the socket to a local address. Operating-system behavior can vary.

#### H. `SO_OOBINLINE` — Receive urgent TCP data inline
**Purpose:** Controls whether TCP urgent data is delivered through the normal input stream.
```
socket.setOOBInline(true);
```

Check it:
```
System.out.println(socket.getOOBInline());
```

- `true`: Urgent data is delivered inline with normal data.
- `false`: Urgent data is discarded by default.
Java also provides:
```
socket.sendUrgentData(65);
```
This sends one byte of TCP urgent data.

**When is it useful?**

Mostly in specialized protocols that explicitly use TCP urgent data. Ordinary applications rarely need it.

#### I. `IP_TOS` — IP Type of Service / Traffic Class
**Purpose:** Sets the traffic-class or Type-of-Service value in the IP header for outgoing packets.

In Java:
```
socket.setTrafficClass(0x10);
```

Read the current value:
```
System.out.println(socket.getTrafficClass());
```

The value is an integer representing the traffic-class bit field.

**When is it useful?**

Applications may use traffic-class markings to express network handling preferences, such as low-delay or throughput-oriented traffic.

Important: the operating system, network equipment, or network policy may ignore or modify this value. Setting it does not guarantee priority or better performance.

### 3. Quick reference table
|Option	|Java method	|Main purpose|
|-------|---------------|------------|
|`TCP_NODELAY`|	`setTcpNoDelay(boolean)`|	Reduce delays for small writes|
|`SO_LINGER`|	`setSoLinger(boolean, int)`|	Control closing behavior|
|`SO_TIMEOUT`|	`setSoTimeout(int)`|	Limit blocking read time|
|`SO_RCVBUF`|	`setReceiveBufferSize(int)`|	Configure incoming buffer|
|`SO_SNDBUF`|	`setSendBufferSize(int)`|	Configure outgoing buffer|
|`SO_KEEPALIVE`|	`setKeepAlive(boolean)`|	Enable TCP keep-alive probes|
|`SO_REUSEADDR`|	`setReuseAddress(boolean)`|	Allow local address reuse|
|`SO_OOBINLINE`|	`setOOBInline(boolean)`|	Receive urgent data inline|
|`IP_TOS`	|`setTrafficClass(int)`|	Set IP traffic-class value|

### 4. Complete Java example — SocketOptionsDemo.java
Let's combine the options in one program.

This example connects to the TCP server from our earlier lessons at `localhost:5000`, configures the socket, displays the settings, and attempts to read a response.
```
import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketTimeoutException;

public class SocketOptionsDemo {

    public static void main(String[] args) {

        try (Socket socket = new Socket()) {

            // 1. Configure options before connecting
            socket.setReuseAddress(true);

            // Request a 64 KB receive buffer before connecting
            socket.setReceiveBufferSize(64 * 1024);

            // Request a 64 KB send buffer
            socket.setSendBufferSize(64 * 1024);

            // Disable Nagle's algorithm
            socket.setTcpNoDelay(true);

            // Set a 3-second read timeout
            socket.setSoTimeout(3000);

            // Enable TCP keep-alive
            socket.setKeepAlive(true);

            // Receive urgent TCP data inline
            socket.setOOBInline(true);

            // Set IP traffic class
            socket.setTrafficClass(0x10);

            // Disable SO_LINGER
            socket.setSoLinger(false, 0);

            // 2. Connect with a 5-second connection timeout
            socket.connect(
                new InetSocketAddress("localhost", 5000),
                5000
            );

            System.out.println("Connected successfully!");

            // 3. Display configured options
            System.out.println("\n--- Socket Options ---");

            System.out.println(
                "TCP_NODELAY: " + socket.getTcpNoDelay()
            );

            System.out.println(
                "SO_LINGER: " + socket.getSoLinger()
            );

            System.out.println(
                "SO_TIMEOUT: " + socket.getSoTimeout() + " ms"
            );

            System.out.println(
                "SO_RCVBUF: " + socket.getReceiveBufferSize()
            );

            System.out.println(
                "SO_SNDBUF: " + socket.getSendBufferSize()
            );

            System.out.println(
                "SO_KEEPALIVE: " + socket.getKeepAlive()
            );

            System.out.println(
                "SO_REUSEADDR: " + socket.getReuseAddress()
            );

            System.out.println(
                "SO_OOBINLINE: " + socket.getOOBInline()
            );

            System.out.println(
                "IP_TOS: " + socket.getTrafficClass()
            );

            // 4. Attempt to read one byte from the server
            System.out.println("\nWaiting for server response...");

            try {
                InputStream input = socket.getInputStream();

                int data = input.read();

                if (data == -1) {
                    System.out.println("Server closed the connection.");
                } else {
                    System.out.println(
                        "Received character: " + (char) data
                    );
                }

            } catch (SocketTimeoutException e) {
                System.out.println(
                    "Read timed out: server did not send data in 3 seconds."
                );
            }

        } catch (IOException e) {
            System.out.println("Socket error: " + e.getMessage());
        }
    }
}
```

**How to run it in IntelliJ IDEA**
1. Start your `SimpleServer` from Lesson 6.1 on port `5000`.
2. Create a new Java class named `SocketOptionsDemo`.
3. Paste the code above.
4. Run SocketOptionsDemo.
5. Observe the configured options and the server response.
If the server accepts the connection but does not send data, the client should report a read timeout after approximately three seconds.

**Important observations:**
- The receive buffer is configured before connecting because a larger TCP receive window may need to be requested before connection establishment.
- The connection timeout is set separately using `connect(endpoint, timeout)`.
- The read timeout controls `InputStream.read()`, not the connection attempt.
- The reported buffer sizes are the operating system's values; they might not exactly match the requested sizes.
- `SO_LINGER` is disabled in this example to avoid changing the normal close behavior.

### 5. Modern Java approach: StandardSocketOptions
Java also provides the SocketOption API. The `Socket` class supports generic `setOption()` and `getOption()` methods.

For example:
```
import java.net.Socket;
import java.net.StandardSocketOptions;

public class ModernSocketOptions {

    public static void main(String[] args) throws Exception {

        try (Socket socket = new Socket()) {

            socket.setOption(
                StandardSocketOptions.TCP_NODELAY, true
            );

            socket.setOption(
                StandardSocketOptions.SO_KEEPALIVE, true
            );

            socket.setOption(
                StandardSocketOptions.SO_RCVBUF, 65536
            );

            System.out.println(
                "TCP_NODELAY: " +
                socket.getOption(StandardSocketOptions.TCP_NODELAY)
            );

            System.out.println(
                "SO_KEEPALIVE: " +
                socket.getOption(StandardSocketOptions.SO_KEEPALIVE)
            );
        }
    }
}
```

This API has been available since Java 9, so it works with your Java 17+ environment.

Not every option in your syllabus is exposed through `StandardSocketOptions`. For example, `SO_TIMEOUT` and `SO_OOBINLINE` are configured using the traditional Socket methods.

You can also inspect the options supported by a particular socket:
```
System.out.println(socket.supportedOptions());
```

The returned set tells you which generic socket options that implementation supports.
