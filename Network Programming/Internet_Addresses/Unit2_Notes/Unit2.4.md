# 2.4 `NetworkInterface` Class

## What is `NetworkInterface`?

`NetworkInterface` is a Java class used to get information about the **network interfaces** of a computer.

A network interface can be:

* Wi-Fi
* Ethernet
* Virtual network adapter
* Other network adapters

It belongs to:

```java
import java.net.NetworkInterface;
```

---

# 1. Factory Methods

Factory methods are used to **create/get a `NetworkInterface` object**.

### Important methods

| Method                   | Purpose                                      |
| ------------------------ | -------------------------------------------- |
| `getByName()`            | Gets interface using its name                |
| `getByIndex()`           | Gets interface using its index               |
| `getByInetAddress()`     | Gets interface associated with an IP address |
| `getNetworkInterfaces()` | Gets all network interfaces                  |

---

## `getByName()`

Gets a network interface by its name.

### Full Program

```java
import java.net.NetworkInterface;

public class NetworkInterfaceExample {
    public static void main(String[] args) throws Exception {

        NetworkInterface network =
                NetworkInterface.getByName("Wi-Fi");

        if (network != null) {
            System.out.println("Interface Name: "
                    + network.getName());
        } else {
            System.out.println("Network interface not found.");
        }
    }
}
```

> The interface name may be `Wi-Fi`, `Ethernet`, etc., depending on your computer.

---

# 2. Getter Methods

Once we have a `NetworkInterface` object, we can use getter methods to obtain information about it.

### Important Getter Methods

| Method                    | Returns                       |
| ------------------------- | ----------------------------- |
| `getName()`               | Interface name                |
| `getDisplayName()`        | Human-readable name           |
| `getIndex()`              | Interface index               |
| `getInetAddresses()`      | IP addresses of interface     |
| `getInterfaceAddresses()` | Interface address information |
| `getHardwareAddress()`    | MAC address                   |

---

## Full Program: Getter Methods

This program displays information about **all network interfaces**.

```java
import java.net.*;
import java.util.Enumeration;

public class NetworkInterfaceGetters {
    public static void main(String[] args) throws Exception {

        Enumeration<NetworkInterface> interfaces =
                NetworkInterface.getNetworkInterfaces();

        while (interfaces.hasMoreElements()) {

            NetworkInterface network =
                    interfaces.nextElement();

            System.out.println("Name: "
                    + network.getName());

            System.out.println("Display Name: "
                    + network.getDisplayName());

            System.out.println("Index: "
                    + network.getIndex());

            System.out.println("MAC Address: "
                    + getMacAddress(network));

            System.out.println("IP Addresses:");

            Enumeration<InetAddress> addresses =
                    network.getInetAddresses();

            while (addresses.hasMoreElements()) {
                InetAddress address = addresses.nextElement();

                System.out.println("  "
                        + address.getHostAddress());
            }

            System.out.println("-------------------------");
        }
    }

    // Method to convert MAC address into readable format
    public static String getMacAddress(NetworkInterface network)
            throws Exception {

        byte[] mac = network.getHardwareAddress();

        if (mac == null) {
            return "Not Available";
        }

        StringBuilder result = new StringBuilder();

        for (int i = 0; i < mac.length; i++) {

            result.append(String.format("%02X", mac[i]));

            if (i < mac.length - 1) {
                result.append("-");
            }
        }

        return result.toString();
    }
}
```

### Example Output

Your output will depend on your computer:

```text
Name: Ethernet
Display Name: Realtek PCIe...
Index: 5
MAC Address: A1-B2-C3-D4-E5-F6
IP Addresses:
  192.168.1.10
-------------------------
Name: Wi-Fi
Display Name: Intel Wireless...
Index: 7
MAC Address: 11-22-33-44-55-66
IP Addresses:
  192.168.1.5
-------------------------
```

---

# ⭐ Important Factory Method Program

For exam purposes, remember this simple program:

```java
import java.net.NetworkInterface;
import java.net.InetAddress;

public class NetworkFactoryMethods {
    public static void main(String[] args) throws Exception {

        // Get interface by name
        NetworkInterface n1 =
                NetworkInterface.getByName("Wi-Fi");

        if (n1 != null) {
            System.out.println("By Name: "
                    + n1.getName());
        }

        // Get interface by index
        if (n1 != null) {
            NetworkInterface n2 =
                    NetworkInterface.getByIndex(n1.getIndex());

            System.out.println("By Index: "
                    + n2.getName());
        }

        // Get interface by IP address
        InetAddress address =
                InetAddress.getLocalHost();

        NetworkInterface n3 =
                NetworkInterface.getByInetAddress(address);

        if (n3 != null) {
            System.out.println("By IP Address: "
                    + n3.getName());
        }
    }
}
```

---


Let's break it down with a very simple example.

## What is this method doing?

```java
public static String getMacAddress(NetworkInterface network)
        throws Exception
```

It takes a `NetworkInterface` and returns its MAC address as a readable string like:

```text
A1-B2-C3-D4-E5-F6
```

---

### Step 1: Get the MAC address

```java
byte[] mac = network.getHardwareAddress();
```

Suppose your computer's MAC address is:

```text
A1-B2-C3-D4-E5-F6
```

Java gets it as **bytes**:

```text
[A1, B2, C3, D4, E5, F6]
```

So `mac` is an array containing 6 values.

---

### Step 2: Check if MAC exists

```java
if (mac == null) {
    return "Not Available";
}
```

Some network interfaces don't have a MAC address.

For example:

```text
mac = null
```

Then the method simply returns:

```text
Not Available
```

---

### Step 3: Create an empty string

```java
StringBuilder result = new StringBuilder();
```

Initially:

```text
result = ""
```

We will build the MAC address inside it.

---

### Step 4: Loop through every byte

```java
for (int i = 0; i < mac.length; i++) {
```

If:

```text
mac = [A1, B2, C3, D4, E5, F6]
```

the loop processes them one by one:

```text
i = 0 → A1
i = 1 → B2
i = 2 → C3
i = 3 → D4
i = 4 → E5
i = 5 → F6
```

---

### Step 5: Convert and add each byte

```java
result.append(String.format("%02X", mac[i]));
```

The important part is:

```text
%02X
```

It means:

* `X` → hexadecimal
* `2` → use 2 characters
* `0` → add zero if necessary

For example:

```text
A1 → A1
B2 → B2
05 → 05
0F → 0F
```

---

### Step 6: Add `-`

```java
if (i < mac.length - 1) {
    result.append("-");
}
```

This adds `-` **between** the values.

So:

```text
A1
```

becomes:

```text
A1-
```

Then:

```text
B2
```

becomes:

```text
A1-B2-
```

Eventually:

```text
A1-B2-C3-D4-E5-F6
```

Notice that it doesn't add `-` after `F6`.

That's why we check:

```java
i < mac.length - 1
```

---

### Step 7: Return the final MAC address

```java
return result.toString();
```

The final result is:

```text
A1-B2-C3-D4-E5-F6
```

---
