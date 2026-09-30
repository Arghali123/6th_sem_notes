# 2.5 Some Useful Programs

## 1. SpamCheck

### What is SpamCheck?

`SpamCheck` is a simple program that checks whether an **email address belongs to a known spam domain**.

For example, we can maintain a list of blocked domains:

```text
spam.com
badmail.com
example.net
```

The program extracts the domain from an email address and checks whether it is present in the spam list.

### Full Java Program

```java
import java.util.Scanner;

public class SpamCheck {

    public static void main(String[] args) {

        Scanner input = new Scanner(System.in);

        // List of known spam domains
        String[] spamDomains = {
            "spam.com",
            "badmail.com",
            "example.net"
        };

        System.out.print("Enter email address: ");
        String email = input.nextLine();

        // Find @ symbol
        int atPosition = email.indexOf('@');

        if (atPosition == -1) {
            System.out.println("Invalid email address.");
            input.close();
            return;
        }

        // Extract domain
        String domain = email.substring(atPosition + 1);

        boolean isSpam = false;

        // Check domain
        for (String spamDomain : spamDomains) {
            if (domain.equalsIgnoreCase(spamDomain)) {
                isSpam = true;
                break;
            }
        }

        if (isSpam) {
            System.out.println("This email is from a spam domain.");
        } else {
            System.out.println("This email is not from a known spam domain.");
        }

        input.close();
    }
}
```

### Example

```text
Enter email address: user@spam.com
This email is from a spam domain.
```

Another example:

```text
Enter email address: user@gmail.com
This email is not from a known spam domain.
```

### 📝 Exam Logic

```text
Email
  ↓
Find @
  ↓
Extract domain
  ↓
Compare with spam domains
  ↓
Match? → Spam
No match? → Not known spam
```

> **Important:** This is a simple educational spam-domain checker, not a real-world spam filter.

---

# 2. Processing Web Server Logfiles

A **web server log file** stores information about requests made to a web server.

Example `log.txt`:

```text
192.168.10.1 GET index.html HTTPS 200
192.168.10.2 GET home.html HTTPS 404
192.168.10.3 POST login.html HTTPS 401
192.168.10.1 GET about.html HTTPS 200
```

Here:

```text
192.168.10.1 → Client IP
GET           → HTTP method
index.html    → Requested resource
HTTPS         → Protocol
200           → Status code
```

A Java program can read the file and process this information.

---

## Full Java Program: Process Web Server Log

This program reads the logfile and counts **any status code automatically**.

```java
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class WebServerLogProcessor {

    public static void main(String[] args) {

        String fileName = "log.txt";

        // Stores status code and its count
        Map<Integer, Integer> statusCount = new HashMap<>();

        try (
            FileInputStream fis = new FileInputStream(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr)
        ) {

            String line;

            while ((line = br.readLine()) != null) {

                // Split the log line into parts
                String[] parts = line.trim().split("\\s+");

                // Make sure the line has enough fields
                if (parts.length >= 5) {

                    // Status code is the last field
                    int statusCode =
                            Integer.parseInt(parts[parts.length - 1]);

                    // Count the status code
                    statusCount.put(
                            statusCode,
                            statusCount.getOrDefault(statusCode, 0) + 1
                    );
                }
            }

            System.out.println("Status Code Counts:");

            for (Map.Entry<Integer, Integer> entry
                    : statusCount.entrySet()) {

                System.out.println(
                        entry.getKey() + " : " + entry.getValue()
                );
            }

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
```

### Example `log.txt`

```text
192.168.10.1 GET index.html HTTPS 200
192.168.10.1 GET index.html HTTPS 404
192.168.10.1 GET index.html HTTPS 401
192.168.10.2 GET home.html HTTPS 200
192.168.10.3 GET login.html HTTPS 404
192.168.10.4 POST login.html HTTPS 500
```

### Output

```text
Status Code Counts:
200 : 2
401 : 1
404 : 2
500 : 1
```

### 🧠 Important Logic

The key part is:

```java
statusCount.put(
    statusCode,
    statusCount.getOrDefault(statusCode, 0) + 1
);
```

It means:

```text
First time status code appears → count = 1
Next time → count = 2
Next time → count = 3
...
```

So you **don't need separate variables** like:

```java
int count200;
int count404;
int count401;
```

It automatically handles **any status code**.

---




