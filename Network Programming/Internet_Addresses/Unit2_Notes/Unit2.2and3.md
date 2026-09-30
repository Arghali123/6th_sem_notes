# 📘 Unit 2 Revision — 2.1 to 2.3

## 🔹 2.1 `InetAddress` Class

### Definition

`InetAddress` is a Java class used to represent an **IP address and hostname**.

```java
import java.net.InetAddress;
```

### Important Factory Methods

| Method           | Use                              |
| ---------------- | -------------------------------- |
| `getByName()`    | Gets address of a hostname       |
| `getAllByName()` | Gets all addresses of a hostname |
| `getLocalHost()` | Gets address of local computer   |

### Getter Methods

| Method                   | Use                        |
| ------------------------ | -------------------------- |
| `getHostName()`          | Returns hostname           |
| `getHostAddress()`       | Returns IP address         |
| `getCanonicalHostName()` | Returns canonical hostname |

### ⭐ Easy memory trick

**Create → Get**

```text
getByName()
getAllByName()
getLocalHost()

       ↓

getHostName()
getHostAddress()
getCanonicalHostName()
```

---

# 🔹 2.2 Address Types

Important checking methods:

| Method                 | Checks                     |
| ---------------------- | -------------------------- |
| `isAnyLocalAddress()`  | Any local/wildcard address |
| `isLoopbackAddress()`  | Loopback address           |
| `isLinkLocalAddress()` | Link-local address         |
| `isSiteLocalAddress()` | Site-local address         |
| `isMulticastAddress()` | Multicast address          |

### Important Example

```text
127.0.0.1 → Loopback address
```

Loopback means the computer communicates with **itself**.

---

## 🔹 Testing Reachability

Used to check whether a host is reachable.

### Syntax

```java
address.isReachable(timeout);
```

Example:

```java
boolean result = address.isReachable(5000);
```

`5000` = **5 seconds**.

---

# 🔹 Object Methods

`InetAddress` provides inherited methods from `Object`.

| Method       | Purpose                     |
| ------------ | --------------------------- |
| `equals()`   | Compares two addresses      |
| `hashCode()` | Returns hash value          |
| `toString()` | Returns address as a string |

### ⭐ Remember

```text
equals()    → Compare
hashCode()  → Hash value
toString()  → String
```

---

# 🔹 2.3 `Inet4Address` and `Inet6Address`

### `Inet4Address`

Represents an **IPv4 address**.

Example:

```text
192.168.1.10
```

IPv4 uses **32 bits**.

### `Inet6Address`

Represents an **IPv6 address**.

Example:

```text
2001:db8::1
```

IPv6 uses **128 bits**.

### Difference

| Feature      | IPv4           | IPv6           |
| ------------ | -------------- | -------------- |
| Class        | `Inet4Address` | `Inet6Address` |
| Address size | 32-bit         | 128-bit        |
| Example      | `192.168.1.1`  | `2001:db8::1`  |
| Notation     | Decimal        | Hexadecimal    |

---

# 🧠 SUPER QUICK REVISION

If this comes in the exam:

**What is `InetAddress`?**

> `InetAddress` is a Java class in `java.net` package used to represent IP addresses and hostnames.

**Important methods:**

```text
getByName()
getAllByName()
getLocalHost()

getHostName()
getHostAddress()
getCanonicalHostName()

isReachable()
isLoopbackAddress()
isMulticastAddress()

equals()
hashCode()
toString()
```

**IPv4:**

```text
Inet4Address → 32-bit
```

**IPv6:**

```text
Inet6Address → 128-bit
```

---

