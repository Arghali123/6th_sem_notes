## 📚 Unit 3 — URLs and URIs


# 🚀 Part 1 — 3.1 URLs: URLs and Relative URLs

Let's start with the foundation.

## 1. What is a URL?

**URL (Uniform Resource Locator)** is an address used to identify and locate a resource on the Internet.

For example:

```text
https://www.example.com/index.html
```

It tells us **where a resource is located** and **how to access it**.

### Simple example

Think of a URL like a **home address**.

```text
https://www.example.com/index.html
│       │                │
│       │                └── Resource/file
│       └─────────────────── Website/server
└─────────────────────────── Protocol
```

---

## 2. General structure of a URL

A URL can contain several parts:

```text
scheme://host:port/path?query#fragment
```

Example:

```text
https://www.example.com:8080/products/item.html?id=10#details
```

| Part     | Example               | Meaning                   |
| -------- | --------------------- | ------------------------- |
| Scheme   | `https`               | Protocol used             |
| Host     | `www.example.com`     | Server/domain             |
| Port     | `8080`                | Network port              |
| Path     | `/products/item.html` | Resource location         |
| Query    | `id=10`               | Additional information    |
| Fragment | `#details`            | Specific section/resource |

### ⭐ Exam definition

> **URL (Uniform Resource Locator) is a standardized address that specifies the location and access method of a resource on the Internet.**

---

# 3. Absolute URL

An **absolute URL** contains the complete address of a resource.

Example:

```text
https://www.example.com/images/logo.png
```

It can be used independently because it contains all necessary information.

### Example

Suppose the current webpage is:

```text
https://example.com/products/index.html
```

An absolute URL could be:

```text
https://example.com/images/mobile.jpg
```

---

# 4. Relative URL

A **relative URL** specifies the location of a resource **relative to the current URL**.

Example:

Current page:

```text
https://example.com/products/index.html
```

Relative URL:

```text
images/mobile.jpg
```

The browser can resolve it as:

```text
https://example.com/products/images/mobile.jpg
```

### Another example

Current URL:

```text
https://example.com/products/index.html
```

Relative URL:

```text
../images/logo.png
```

Here `..` means **go up one directory**.

Result:

```text
https://example.com/images/logo.png
```

---

# 5. Absolute vs Relative URL

| Absolute URL                          | Relative URL                         |
| ------------------------------------- | ------------------------------------ |
| Complete address                      | Partial address                      |
| Can be used independently             | Depends on base/current URL          |
| Contains scheme and host              | Usually does not contain scheme/host |
| Example: `https://example.com/a.html` | Example: `a.html`                    |

### 🧠 Easy way to remember

**Absolute = Full address**

**Relative = Address relative to where you currently are**

---

# 6. Java Example

Here's a simple program showing an absolute URL and a relative URL.

```java
import java.net.URL;

public class URLExample {
    public static void main(String[] args) throws Exception {

        // Absolute URL
        URL baseURL = new URL("https://example.com/products/index.html");

        // Relative URL
        URL relativeURL = new URL(baseURL, "images/mobile.jpg");

        System.out.println("Base URL     : " + baseURL);
        System.out.println("Relative URL : " + relativeURL);
    }
}
```

### Output

```text
Base URL     : https://example.com/products/index.html
Relative URL : https://example.com/products/images/mobile.jpg
```

### Important line

```java
new URL(baseURL, "images/mobile.jpg");
```

This means:

> Create a new URL by resolving the relative URL against the base URL.

We'll study this much more deeply when we reach the **URL Class**.

---

# 📝 Exam-ready answer

### What is URL?

> URL stands for Uniform Resource Locator. It is an address used to identify and locate a resource on the Internet. A URL specifies the protocol and location of a resource.

Example:

```text
https://www.example.com/index.html
```

### What is Relative URL?

> A relative URL specifies the location of a resource relative to a base or current URL. It does not normally contain the complete address.

Example:

```text
images/logo.png
```

If the base URL is:

```text
https://example.com/products/index.html
```

the resulting URL is:

```text
https://example.com/products/images/logo.png
```

---


