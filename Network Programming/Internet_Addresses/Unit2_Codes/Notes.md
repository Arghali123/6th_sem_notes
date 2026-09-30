Think of the classes as **layers** that make reading a file more convenient:

```text
File → FileInputStream → InputStreamReader → BufferedReader → your code
```

In your program:

```java
FileInputStream fis = new FileInputStream(fileName);
InputStreamReader isr = new InputStreamReader(fis);
BufferedReader br = new BufferedReader(isr);
```

- **`InputStream`** is the general Java type for reading **bytes** from a source. It’s an abstract parent class; `FileInputStream` is one of its concrete subclasses.
- **`FileInputStream`** reads the file as bytes. It doesn’t interpret those bytes as text.
- **`InputStreamReader`** converts bytes into characters using a character encoding. It acts as a bridge between byte-based input and text.
- **`BufferedReader`** makes text reading convenient and efficient. It buffers characters and provides `readLine()`, which your loop uses to read one line at a time.

So the loop:

```java
while ((line = br.readLine()) != null) {
    // process this line
}
```

keeps reading lines until the end of the file. At the end, `readLine()` returns `null`.

The `try (...)` is **try-with-resources**. It automatically closes the readers and the file stream when the block finishes, even if an error occurs.

A shorter version is also available:

```java
try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
    // read lines
}
```

Your current version makes each layer visible, which is useful for learning.