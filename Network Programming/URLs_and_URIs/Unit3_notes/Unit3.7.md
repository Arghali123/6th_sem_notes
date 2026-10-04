# 3.7 Accessing Password-Protected Sites

This is the **final topic of Unit 3**. 🎯
We’ll cover exactly these three classes:

1. `Authenticator`
2. `PasswordAuthentication`
3. `JPasswordField`

---

## 1. TEACH → What is a Password-Protected Site?

A **password-protected site** requires a username and password before allowing access to a resource.

For example:

```text
Java Program
     |
     | Request protected resource
     ↓
Web Server
     |
     | "Authentication required"
     ↓
Authenticator
     |
     | Username + Password
     ↓
Web Server
     |
     ↓
Protected Resource
```

In Java networking, the `java.net.Authenticator` class can provide credentials when a server requests authentication.

---

# 2. WHY → Why are these classes used?

### `Authenticator`

Used to respond to an authentication request from a server.

### `PasswordAuthentication`

Stores the:

* Username
* Password

that will be supplied to the server.

### `JPasswordField`

A Swing GUI component used to allow the user to **enter a password securely without displaying the actual characters**.

So their roles are:

| Class                    | Purpose                                   |
| ------------------------ | ----------------------------------------- |
| `Authenticator`          | Handles authentication requests           |
| `PasswordAuthentication` | Stores username and password              |
| `JPasswordField`         | Allows user to enter password through GUI |

---

# 3. `Authenticator` Class

Package:

```java
java.net.Authenticator
```

It is an **abstract class** used by Java networking applications to provide authentication credentials.

You normally create a subclass and override:

```java
getPasswordAuthentication()
```

### Important method

```java
protected PasswordAuthentication getPasswordAuthentication()
```

This method is called when Java needs authentication information.

### Setting the Authenticator

```java
Authenticator.setDefault(authenticator);
```

This installs an authenticator as the default authenticator for the application.

---

# 4. `PasswordAuthentication` Class

Package:

```java
java.net.PasswordAuthentication
```

It represents a username and password.

### Constructor

```java
PasswordAuthentication(String username, char[] password)
```

### Important methods

```java
getUserName()
```

Returns the username.

```java
getPassword()
```

Returns the password as a `char[]`.

### Why `char[]` instead of `String`?

Passwords are commonly represented as `char[]` because a character array can be cleared after use:

```java
password = null;
```

or:

```java
Arrays.fill(password, '\0');
```

---

# 5. CODE → Basic `Authenticator` Example

Here is a **complete runnable Java program** showing how `Authenticator` and `PasswordAuthentication` work.

```java
import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class AuthenticationExample {

    public static void main(String[] args) {

        Authenticator authenticator = new Authenticator() {

            @Override
            protected PasswordAuthentication getPasswordAuthentication() {

                String username = "admin";
                char[] password = "12345".toCharArray();

                return new PasswordAuthentication(username, password);
            }
        };

        Authenticator.setDefault(authenticator);

        System.out.println("Authenticator has been configured.");

        // Demonstration
        PasswordAuthentication auth =
                authenticator.requestPasswordAuthenticationInstance(
                        "example.com",
                        null,
                        80,
                        "http",
                        "Authentication Required",
                        "basic"
                );

        if (auth != null) {
            System.out.println("Username: " + auth.getUserName());
            System.out.println("Password: "
                    + new String(auth.getPassword()));
        }
    }
}
```

### Output

```text
Authenticator has been configured.
Username: admin
Password: 12345
```

> This demonstrates the authentication mechanism. A real server must actually request compatible authentication, such as HTTP Basic Authentication, for credentials to be used in a network request.

---

# 6. `JPasswordField`

Now suppose we don't want to hard-code:

```java
String password = "12345";
```

Instead, we can ask the user to enter the password.

Java Swing provides:

```java
javax.swing.JPasswordField
```

It is a GUI component specifically designed for password input.

Example:

```text
Username: [ admin       ]

Password: [ *********** ]

          [   Login   ]
```

### Important methods

```java
getPassword()
```

Returns the entered password as:

```java
char[]
```

You can also use:

```java
setEchoChar('*');
```

to specify the character displayed instead of the actual password.

---

# 7. CODE → `JPasswordField` Example

Complete runnable program:

```java
import javax.swing.*;

public class PasswordFieldExample {

    public static void main(String[] args) {

        JFrame frame = new JFrame("Login");

        JLabel label = new JLabel("Password:");
        JPasswordField passwordField = new JPasswordField(15);
        JButton button = new JButton("Login");

        JPanel panel = new JPanel();

        panel.add(label);
        panel.add(passwordField);
        panel.add(button);

        frame.add(panel);

        button.addActionListener(e -> {

            char[] password = passwordField.getPassword();

            System.out.println("Password entered: "
                    + new String(password));

            // Clear password after use
            java.util.Arrays.fill(password, '\0');

            passwordField.setText("");
        });

        frame.setSize(350, 100);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }
}
```

When the user types:

```text
mypassword
```

the field displays something like:

```text
**********
```

instead of:

```text
mypassword
```

---

# 8. Putting Them Together

In an actual application, the flow can be:

```text
             User
              |
              ↓
       JPasswordField
              |
              | username + password
              ↓
    PasswordAuthentication
              |
              ↓
        Authenticator
              |
              ↓
         Web Server
              |
        Authentication
              |
        ┌─────┴─────┐
        ↓           ↓
     Success      Failure
        |           |
        ↓           ↓
 Protected       Access
 Resource        Denied
```

### Simple explanation

1. `JPasswordField` gets the password from the user.
2. `PasswordAuthentication` stores username and password.
3. `Authenticator` provides these credentials when authentication is requested.
4. The server verifies them.
5. If correct, the protected resource is returned.

---

# 9. Important Exam Difference

| `Authenticator`                         | `PasswordAuthentication`                 | `JPasswordField`       |
| --------------------------------------- | ---------------------------------------- | ---------------------- |
| Handles authentication                  | Stores credentials                       | Gets password from GUI |
| `java.net`                              | `java.net`                               | `javax.swing`          |
| Abstract class                          | Normal class                             | Swing component        |
| Overrides `getPasswordAuthentication()` | Uses `getUserName()` and `getPassword()` | Uses `getPassword()`   |
| Works with network authentication       | Represents username/password             | Used for user input    |

---

# 10. PRACTICE → Exam Questions

Try these yourself:

### Q1. What is the purpose of the `Authenticator` class?

### Q2. What is `PasswordAuthentication`? List its important methods.

### Q3. What is `JPasswordField`? Why is it preferred over a normal `JTextField` for passwords?

### Q4. Explain the relationship between:

```text
Authenticator
PasswordAuthentication
JPasswordField
```

### Q5. Write a Java program that creates an `Authenticator` and returns a `PasswordAuthentication` object.

---

# 11. REVISION → 3.7 in 30 seconds

Remember:

```text
JPasswordField
      ↓
Gets password from user
      ↓
PasswordAuthentication
      ↓
Stores username + password
      ↓
Authenticator
      ↓
Provides credentials to network authentication
      ↓
Server
```

### Most important methods

```java
Authenticator.setDefault()
Authenticator.getPasswordAuthentication()

PasswordAuthentication.getUserName()
PasswordAuthentication.getPassword()

JPasswordField.getPassword()
```

---
