# 5.7 — Security Considerations for URLConnections
### 1. TEACH — What are the security considerations?
When a Java application communicates with a remote server using `URLConnection`, it exchanges data over a network.

That creates security risks, including:
1. **Unencrypted communication** — sensitive information can be intercepted.
2. **Untrusted server responses** — received data may contain malicious content.
3. **Unauthorized access** — private resources may be accessed without proper authentication.
4. **Information leakage** — sensitive information may accidentally be exposed through request headers or logs.
5. **Untrusted URLs** — attackers may manipulate URLs to make your application contact internal or unauthorized systems.
Let's understand the important precautions.

### 2. WHY — Why does security matter?
Imagine your application sends login credentials to a server:
```
Username: student
Password: mySecret123
```

If the application uses plain HTTP:
```
http://example.com/login
```

The communication is not encrypted. Someone who can observe the network traffic may be able to read the credentials.

Instead, use HTTPS:
```
https://example.com/login
```

HTTPS uses TLS to encrypt communication and authenticate the server (when certificate validation succeeds).


**Important:** HTTPS protects data in transit, but it doesn't automatically make every application operation secure.

### 3. Important security precautions
#### A. Use HTTPS instead of HTTP
- HTTP sends data without transport encryption.
- HTTPS protects communication using TLS.
- HTTPS also provides server authentication through certificate validation.
Use:
```
https://api.example.com
```

Avoid sending passwords, API keys, or personal information over plain HTTP.

#### B. Validate server certificates
When using `HttpsURLConnection`, Java normally validates the server's certificate using its configured trust infrastructure.

Never disable certificate or hostname verification just to make a connection work.

For example, avoid custom code that blindly trusts every certificate. That defeats an important security protection.

#### C. Validate URLs before connecting
Suppose your application accepts a URL from a user.

An attacker might provide:
```
http://localhost:8080/admin
```

Or a URL pointing to an internal server.

This can cause **Server-Side Request Forgery (SSRF)**: your application is tricked into making requests to systems it shouldn't access.

Precautions:
- Allow only expected URL schemes, usually HTTPS.
- Use an allowlist of permitted hostnames.
- Avoid blindly connecting to user-provided URLs.
- Apply network-level restrictions where appropriate.

#### D. Set connection and read timeouts
An untrusted or unavailable server might never respond.

Use:
```
connection.setConnectTimeout(5000);
connection.setReadTimeout(10000);
```

This limits how long your application waits to establish a connection and receive data.

#### E. Protect sensitive information
Never expose sensitive information unnecessarily.

Examples:
- Passwords
- API keys
- Authentication tokens
- Personal information
Avoid printing these values in application logs. Send credentials only over HTTPS and use secure secret management.

#### F. Treat server responses as untrusted
A server response is not automatically safe just because it came from a server.

For example:
- Validate received data.
- Check the expected content type.
- Limit the amount of data your application reads.
- Don't execute downloaded content blindly.
- Apply appropriate validation before storing or displaying data.

### 4. CODE — A safer HTTPS connection
Let's put several of these precautions together.

Create `SecureURLConnectionExample.java`:
```
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.net.URI;
import java.net.URLConnection;
import java.util.Set;
import java.nio.charset.StandardCharsets;
import javax.net.ssl.HttpsURLConnection;

public class SecureURLConnectionExample {

    private static final Set<String> ALLOWED_HOSTS =
            Set.of("example.com");

    public static void main(String[] args) {

        String address = "https://example.com";

        try {
            URI uri = URI.create(address);

            // 1. Validate the protocol
            if (!"https".equalsIgnoreCase(uri.getScheme())) {
                throw new SecurityException(
                        "Only HTTPS connections are allowed"
                );
            }

            // 2. Validate the hostname
            String host = uri.getHost();

            if (host == null || !ALLOWED_HOSTS.contains(
                    host.toLowerCase()
            )) {
                throw new SecurityException(
                        "Host is not allowed"
                );
            }

            // 3. Open a secure HTTPS connection
            URLConnection connection =
                    uri.toURL().openConnection();

            if (!(connection instanceof HttpsURLConnection)) {
                throw new SecurityException(
                        "HTTPS connection required"
                );
            }

            HttpsURLConnection https =
                    (HttpsURLConnection) connection;

            // 4. Configure timeouts
            https.setConnectTimeout(5000);
            https.setReadTimeout(10000);

            // 5. Configure request headers
            https.setRequestProperty(
                    "Accept", "text/html"
            );

            // 6. Connect and read response
            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(
                            https.getInputStream(),
                            StandardCharsets.UTF_8
                    ))) {

                String line;

                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }

            // 7. Release the connection
            finally {
                https.disconnect();
            }

        } catch (IOException | SecurityException e) {
            System.out.println("Request failed: " + e.getMessage());
        }
    }
}
```

**What this example demonstrates:**
1. Accepts only HTTPS URLs.
2. Allows only a predefined hostname.
3. Uses HttpsURLConnection for encrypted communication.
4. Sets connection and read timeouts.
5. Uses Java's default TLS certificate and hostname verification.
6. Closes the reader and disconnects the HTTP connection.

**Important limitation:** A hostname allowlist is a useful first layer, not a complete SSRF defense. Production applications should also consider DNS resolution, IP address restrictions, redirects, and network-level controls.

