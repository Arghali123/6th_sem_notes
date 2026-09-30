### 📘 Unit 2: Internet Addresses — Roadmap

| Part    | Topic                                             | Status |
| ------- | ------------------------------------------------- | ------ |
| **2.1** | `InetAddress` Class                               | ⬜      |
| **2.2** | Address Types, Reachability & Object Methods      | ⬜      |
| **2.3** | `Inet4Address` & `Inet6Address`                   | ⬜      |
| **2.4** | `NetworkInterface` Class                          | ⬜      |
| **2.5** | Useful Programs: `SpamCheck`, Web Server Logfiles | ⬜      |
| 🔄      | **Revision**                                      | ⬜      |

We'll go **one part at a time**, and after finishing the unit, I'll give you a **short revision sheet + important exam questions**.

---

# 2.1 The `InetAddress` Class

`InetAddress` is a Java class used to represent an **IP address** and its corresponding **hostname**.

It belongs to:

```java
import java.net.InetAddress;
```

### 1. Creating a new `InetAddress` Object

There are several factory methods used to obtain an `InetAddress` object.

#### `getByName()`

Gets the IP address of a hostname.

```java
import java.net.*;

public class Example {
    public static void main(String[] args) throws Exception {
        InetAddress address = InetAddress.getByName("google.com");

        System.out.println(address);
    }
}
```

**Output example:**

```text
google.com/142.250.xxx.xxx
```

---

### 2. `getLocalHost()`

Returns the address of the computer on which the program is running.

```java
InetAddress address = InetAddress.getLocalHost();

System.out.println(address);
```

---

### 3. `getAllByName()`

Returns **all IP addresses** associated with a hostname.

```java
InetAddress[] addresses =
    InetAddress.getAllByName("google.com");

for (InetAddress address : addresses) {
    System.out.println(address);
}
```

---

## Getter Methods

Getter methods are used to obtain information from an `InetAddress` object.

### `getHostName()`

Returns the hostname.

```java
System.out.println(address.getHostName());
```

### `getHostAddress()`

Returns the IP address as a string.

```java
System.out.println(address.getHostAddress());
```

### `getCanonicalHostName()`

Returns the canonical hostname.

```java
System.out.println(address.getCanonicalHostName());
```

### Quick Table for Exam 📝

| Method                   | Purpose                          |
| ------------------------ | -------------------------------- |
| `getByName()`            | Gets address of a hostname       |
| `getLocalHost()`         | Gets local machine address       |
| `getAllByName()`         | Gets all addresses of a hostname |
| `getHostName()`          | Returns hostname                 |
| `getHostAddress()`       | Returns IP address               |
| `getCanonicalHostName()` | Returns canonical hostname       |

### ⭐ Remember

**`InetAddress = Hostname + IP Address`**

For example:

```text
google.com → 142.250.xxx.xxx
```

---

