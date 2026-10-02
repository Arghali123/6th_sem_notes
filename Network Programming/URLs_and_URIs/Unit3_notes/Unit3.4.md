# 3.4 — `x-www-form-urlencoded`

## 1. What is `x-www-form-urlencoded`?

When data contains spaces or special characters, it cannot always be placed directly inside a URL.

For example:

```text
name=John Doe
```

The space needs to be encoded.

Using **URL encoding**, it becomes:

```text
name=John+Doe
```

This format is called **`application/x-www-form-urlencoded`**.

### ⭐ Exam definition

> **`application/x-www-form-urlencoded` is a format used to encode form data so that it can safely be transmitted through URLs or HTTP requests.**

---

# 2. Why is URL Encoding Needed?

Suppose we want to send:

```text
name = John Doe
city = New York
```

A URL might look like:

```text
https://example.com/search?name=John Doe&city=New York
```

Spaces and some special characters can cause problems.

After encoding:

```text
https://example.com/search?name=John+Doe&city=New+York
```

So:

```text
Original        Encoded
-------------------------
space     →     +
&         →     %26
=         →     %3D
?         →     %3F
```

---

# 3. `URLEncoder` Class

Java provides:

```java
java.net.URLEncoder
```

It is used to **encode strings into `application/x-www-form-urlencoded` format**.

### Important method

```java
URLEncoder.encode(String s, String enc)
```

For modern Java code, you can also use:

```java
URLEncoder.encode(String s, Charset charset)
```

---

# 4. Simple `URLEncoder` Example

```java
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class URLEncoderExample {
    public static void main(String[] args) {

        String text = "Hello World Java";

        String encoded = URLEncoder.encode(
                text,
                StandardCharsets.UTF_8
        );

        System.out.println("Original : " + text);
        System.out.println("Encoded  : " + encoded);
    }
}
```

### Output

```text
Original : Hello World Java
Encoded  : Hello+World+Java
```

Notice:

```text
Space → +
```

---

# 5. Encoding Special Characters

Let's encode a string containing special characters.

```java
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class EncodeSpecialCharacters {
    public static void main(String[] args) {

        String text = "Java & Networking = Easy?";

        String encoded = URLEncoder.encode(
                text,
                StandardCharsets.UTF_8
        );

        System.out.println("Original : " + text);
        System.out.println("Encoded  : " + encoded);
    }
}
```

Output:

```text
Original : Java & Networking = Easy?
Encoded  : Java+%26+Networking+%3D+Easy%3F
```

### Notice:

```text
& → %26
= → %3D
? → %3F
space → +
```

---

# 6. `URLDecoder` Class

The opposite operation is **decoding**.

Java provides:

```java
java.net.URLDecoder
```

It converts encoded data back into its original form.

### Important method

```java
URLDecoder.decode(String s, String enc)
```

Modern code can also use:

```java
URLDecoder.decode(String s, Charset charset)
```

---

# 7. Simple `URLDecoder` Example

```java
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class URLDecoderExample {
    public static void main(String[] args) {

        String encoded = "Hello+World+Java";

        String decoded = URLDecoder.decode(
                encoded,
                StandardCharsets.UTF_8
        );

        System.out.println("Encoded : " + encoded);
        System.out.println("Decoded : " + decoded);
    }
}
```

Output:

```text
Encoded : Hello+World+Java
Decoded : Hello World Java
```

---

# 8. Encoding and Decoding Together

This is a very useful exam program.

```java
import java.net.URLEncoder;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class EncodeDecodeExample {
    public static void main(String[] args) {

        String original = "Java Network Programming";

        // Encoding
        String encoded = URLEncoder.encode(
                original,
                StandardCharsets.UTF_8
        );

        // Decoding
        String decoded = URLDecoder.decode(
                encoded,
                StandardCharsets.UTF_8
        );

        System.out.println("Original : " + original);
        System.out.println("Encoded  : " + encoded);
        System.out.println("Decoded  : " + decoded);
    }
}
```

Output:

```text
Original : Java Network Programming
Encoded  : Java+Network+Programming
Decoded  : Java Network Programming
```

---

# 9. Using URL Encoding with Form Data

Suppose a web form contains:

```text
Name = Daenish
City = New Butwal
Course = Network Programming
```

We can encode the parameters:

```text
Name=Daenish&City=New+Butwal&Course=Network+Programming
```

### Full Java Example

```java
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class FormDataEncoding {
    public static void main(String[] args) {

        String name = "Daenish";
        String city = "New Butwal";
        String course = "Network Programming";

        String data =
                "name=" + URLEncoder.encode(name, StandardCharsets.UTF_8)
                + "&city=" + URLEncoder.encode(city, StandardCharsets.UTF_8)
                + "&course=" + URLEncoder.encode(course, StandardCharsets.UTF_8);

        System.out.println("Encoded Form Data:");
        System.out.println(data);
    }
}
```

Output:

```text
Encoded Form Data:
name=Daenish&city=New+Butwal&course=Network+Programming
```

This type of encoded data is commonly used when sending form parameters in HTTP requests.

---

# 10. Common Encoding Rules

Remember these for the exam:

| Character | Encoded form |
| --------- | ------------ |
| Space     | `+`          |
| `&`       | `%26`        |
| `=`       | `%3D`        |
| `?`       | `%3F`        |
| `#`       | `%23`        |
| `%`       | `%25`        |
| `+`       | `%2B`        |

For example:

```text
Java + Network
```

becomes:

```text
Java+%2B+Network
```

The original `+` becomes `%2B` so it isn't confused with the `+` representing a space.

---

# 11. `URLEncoder` vs `URLDecoder`

| `URLEncoder`                | `URLDecoder`                      |
| --------------------------- | --------------------------------- |
| Encodes data                | Decodes data                      |
| Converts special characters | Converts encoded characters back  |
| Space → `+`                 | `+` → Space                       |
| `%26` generated for `&`     | `%26` → `&`                       |
| Used before transmission    | Used after receiving encoded data |

### Easy memory trick 🧠

```text
URLEncoder
     ↓
Normal → Encoded

URLDecoder
     ↓
Encoded → Normal
```

---

