
# 2.2 Address Types, Testing Reachability & Object Methods

## 1. Address Types

An IP address can be classified into different types.

For Java's `InetAddress`, the commonly used address-type checking methods are:

| Method                 | Meaning                                       |
| ---------------------- | --------------------------------------------- |
| `isAnyLocalAddress()`  | Checks if address is a wildcard/local address |
| `isLoopbackAddress()`  | Checks if address is a loopback address       |
| `isLinkLocalAddress()` | Checks if address is link-local               |
| `isSiteLocalAddress()` | Checks if address is site-local               |
| `isMulticastAddress()` | Checks if address is multicast                |

### Full Java Program

```java
import java.net.InetAddress;

public class AddressTypes {
    public static void main(String[] args) throws Exception {

        InetAddress address = InetAddress.getByName("127.0.0.1");

        System.out.println("IP Address: " + address.getHostAddress());

        System.out.println("Any Local Address: "
                + address.isAnyLocalAddress());

        System.out.println("Loopback Address: "
                + address.isLoopbackAddress());

        System.out.println("Link Local Address: "
                + address.isLinkLocalAddress());

        System.out.println("Site Local Address: "
                + address.isSiteLocalAddress());

        System.out.println("Multicast Address: "
                + address.isMulticastAddress());
    }
}
```

For `127.0.0.1`, the important result is:

```text
Loopback Address: true
```

### 📝 Remember

**Loopback address:** An address used by a computer to communicate with itself.

Example:

```text
127.0.0.1
```

---

# 2. Testing Reachability

Java provides the `isReachable()` method to check whether a host can be reached.

### Syntax

```java
address.isReachable(timeout);
```

`timeout` is specified in **milliseconds**.

### Full Java Program

```java
import java.net.InetAddress;

public class ReachabilityTest {
    public static void main(String[] args) throws Exception {

        InetAddress address = InetAddress.getByName("google.com");

        boolean reachable = address.isReachable(5000);

        if (reachable) {
            System.out.println("Host is reachable.");
        } else {
            System.out.println("Host is not reachable.");
        }
    }
}
```

Here:

```text
5000 milliseconds = 5 seconds
```

> ⚠️ `isReachable()` does not guarantee that a website/application is actually working. Firewall settings and network configuration can affect the result.

---

# 3. Object Methods

`InetAddress` also inherits some useful methods from the `Object` class.

### Important methods

| Method       | Purpose                       |
| ------------ | ----------------------------- |
| `equals()`   | Compares two addresses        |
| `hashCode()` | Returns hash code of address  |
| `toString()` | Returns string representation |

### Full Java Program

```java
import java.net.InetAddress;

public class ObjectMethods {
    public static void main(String[] args) throws Exception {

        InetAddress address1 = InetAddress.getByName("google.com");
        InetAddress address2 = InetAddress.getByName("google.com");

        // equals()
        System.out.println("Are addresses equal? "
                + address1.equals(address2));

        // hashCode()
        System.out.println("Hash Code: "
                + address1.hashCode());

        // toString()
        System.out.println("Address: "
                + address1.toString());
    }
}
```

### 📝 Exam Revision

**Address types:**

* Loopback → `127.0.0.1`
* Multicast → one-to-many communication
* Site-local → private/local network address
* Link-local → local network link

**Reachability:**

```java
isReachable(timeout)
```

Checks whether an address is reachable within the specified timeout.

**Object methods:**

```text
equals()    → compares addresses
hashCode()  → returns hash value
toString()  → returns address as string
```

