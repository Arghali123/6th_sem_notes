# 5.8 — Guessing MIME Media Types
### 1. TEACH — What is a MIME media type?
A **MIME media type** identifies the format or type of data being transferred over the internet.

MIME stands for *Multipurpose Internet Mail Extensions*.

It tells an application how to interpret received data.

For example:
|MIME type|	Meaning|
|---------|--------|
|`text/html`|	HTML document|
|`text/plain`|	Plain text|
|`application/json`|	JSON data|
|`application/pdf`	|PDF document|
|`image/png`|	PNG image|
|`image/jpeg`	|JPEG image|
|`application/xml`	|XML document|

MIME types generally follow this structure:
```
type/subtype
```

Examples:
```
text/html
image/png
application/json
```

### 2. WHY — Why do we need MIME types?
Imagine your Java application downloads a file from a server.

The response contains:
```
Content-Type: application/pdf
```

This tells your application that the response body contains PDF data.

Another response might contain:
```
Content-Type: application/json
```

Your application can then process it as JSON.

**Important:** MIME type identifies the format, but it does not guarantee that the content is valid or safe. Always validate untrusted content before processing it.

### 3. The three important Java approaches
Java's `URLConnection` provides two static methods for guessing MIME types, plus a method for reading the server's declared content type.

#### A. `guessContentTypeFromName(String fname)`
This method guesses the MIME type based on a filename or file extension.

**Syntax:**
```
URLConnection.guessContentTypeFromName("document.pdf");
```

Possible result:
```
application/pdf
```

More examples:
```
URLConnection.guessContentTypeFromName("photo.png");
URLConnection.guessContentTypeFromName("index.html");
URLConnection.guessContentTypeFromName("data.json");
```

The method returns `null` if it cannot determine the type.

#### B. `guessContentTypeFromStream(InputStream is)`
This method examines the beginning of an input stream to guess its MIME type.

Why is this useful?

Imagine a file is named:
```
photo.txt
```

But its actual contents are PNG image data.

Guessing from the filename may suggest `text/plain`, while examining the initial bytes may identify `image/png`.

Syntax:
```
URLConnection.guessContentTypeFromStream(inputStream);
```

Important points:
- It examines the initial bytes of the stream.
- The input stream must support marking (`markSupported()` must return `true`).
- It may return `null` if the type cannot be identified.
- It can throw `IOException`.

#### C. `getContentType()`
This method retrieves the `Content-Type` response header provided by the server.
```
URLConnection connection = url.openConnection();

String contentType = connection.getContentType();
```

Example server response:
```
Content-Type: application/json
```

Then:
```
application/json
```

**Important difference:**
|Method|	How it determines MIME type|
|------|-------------------------------|
|`guessContentTypeFromName()`|	Filename or extension|
|`guessContentTypeFromStream()`|	Initial bytes of data|
|`getContentType()`|	Server's declared response header|

The server's declared type may be incorrect, so it should not be treated as proof of the actual file format. 

### 4. `FileNameMap` interface
Java also provides the `FileNameMap` interface in the `java.net package`.

Its main purpose is to map filenames to MIME types.

It contains this method:
```
String getContentTypeFor(String fileName);
```

Example:
```
FileNameMap map = URLConnection.getFileNameMap();

String type = map.getContentTypeFor("report.pdf");

System.out.println(type);
```

Possible output:
```
application/pdf
```

Useful methods:
|Method	|Purpose|
|-------|-------|
|`URLConnection.getFileNameMap()`|	Retrieves the current filename-to-MIME mapping|
|`URLConnection.setFileNameMap(map)`|	Sets a custom filename-to-MIME mapping|
|`FileNameMap.getContentTypeFor(name)`|	Finds a MIME type for a filename|

`FileNameMap` is useful when you want to work directly with filename-based MIME mappings. 

### 5. CODE — Complete runnable Java example
Create a class named `GuessMimeType.java`.

This program demonstrates filename guessing, stream guessing, and `FileNameMap`.
```
import java.net.URLConnection;
import java.net.FileNameMap;
import java.io.ByteArrayInputStream;
import java.io.IOException;

public class GuessMimeType {

    public static void main(String[] args) {

        // 1. Guess MIME type from filename
        System.out.println("Guessing from filenames:");

        String[] files = {
            "photo.png",
            "document.pdf",
            "index.html",
            "data.json",
            "unknown.xyz"
        };

        for (String file : files) {

            String mimeType =
                    URLConnection.guessContentTypeFromName(file);

            System.out.println(file + " -> " + mimeType);
        }

        // 2. Guess MIME type from stream bytes
        System.out.println("\nGuessing from stream:");

        // PNG file signature (first 8 bytes)
        byte[] pngSignature = {
            (byte) 0x89,
            0x50, 0x4E, 0x47,
            0x0D, 0x0A, 0x1A, 0x0A
        };

        try (ByteArrayInputStream input =
                     new ByteArrayInputStream(pngSignature)) {

            if (input.markSupported()) {

                String mimeType =
                        URLConnection.guessContentTypeFromStream(input);

                System.out.println("Stream MIME type: " + mimeType);
            }

        } catch (IOException e) {
            System.out.println("Error: " + e.getMessage());
        }

        // 3. Using FileNameMap
        System.out.println("\nUsing FileNameMap:");

        FileNameMap map = URLConnection.getFileNameMap();

        String type = map.getContentTypeFor("report.pdf");

        System.out.println("report.pdf -> " + type);
    }
}
```


Note: MIME mappings can vary by JDK installation and configuration. An unknown extension may return null. The stream example uses a PNG signature to demonstrate byte-based detection.

Understand the execution
1. Filename detection
   
   Java checks the filename mapping and guesses the MIME type from the extension.
2. Stream detection
   
   Java examines the initial bytes of the stream to identify a recognizable format.
3. FileNameMap
   
   Java retrieves the configured filename mapping and uses it to look up the MIME type.

   