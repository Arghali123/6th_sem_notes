# 🚀 1.10 — Streaming Responses

This is the **last major concept before your Phase 1 mini-project**.

So far, you've used:

```text
prompt → call() → wait → complete response
```

With streaming, we change this to:

```text
prompt → stream() → chunks arrive continuously
```

---

## 1. What is a Streaming Response?

Normally, when you call:

```java
.call()
```

Spring AI waits for the model to finish generating the response.

For example:

```text
User
 ↓
"Explain Spring Boot"
 ↓
Gemini generates entire answer
 ↓
Spring AI receives entire answer
 ↓
User sees answer
```

This can feel slow for long responses.

With **streaming**:

```text
User
 ↓
"Explain Spring Boot"
 ↓
Gemini
 ↓
"Spring"
 ↓
" Boot"
 ↓
" is"
 ↓
" a"
 ↓
" framework..."
 ↓
User sees it continuously
```

This gives the application a much more interactive experience.

---

# 2. `call()` vs `stream()`

This is the main thing to remember.

### Normal response

```java
String response = chatClient
        .prompt("Explain Spring AI")
        .call()
        .content();
```

Conceptually:

```text
Request
   ↓
[Wait]
   ↓
Complete response
```

### Streaming response

```java
Flux<String> response = chatClient
        .prompt("Explain Spring AI")
        .stream()
        .content();
```

Conceptually:

```text
Request
   ↓
Chunk 1
   ↓
Chunk 2
   ↓
Chunk 3
   ↓
Chunk 4
   ↓
...
```

The important difference:

> `.call()` gives you the completed response, while `.stream()` lets you consume the response progressively.

---

# 3. What is `Flux<String>`?

You'll see this frequently when working with Spring AI streaming.

`Flux` comes from **Project Reactor**, which is used heavily in Spring's reactive programming model.

You don't need to go deep into Reactor yet.

For now, remember:

```text
Flux<String>
    ↓
A stream of String values
```

For example:

```text
Flux<String>

"Spring"
" AI"
" is"
" useful"
" for"
" Java"
...
```

So:

```java
Flux<String> response = chatClient
        .prompt("Explain Spring AI")
        .stream()
        .content();
```

means:

> "Give me the generated text as a stream of chunks."

---

# 4. Simple Streaming REST API

Here's a complete example:

```java
package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/stream")
    public Flux<String> stream(@RequestParam String message) {

        return chatClient
                .prompt()
                .system("You are a helpful Java teacher.")
                .user(message)
                .stream()
                .content();
    }
}
```

Now request:

```text
/ai/stream?message=Explain%20Spring%20Boot
```

Instead of returning one large `String`, the endpoint returns a stream.

---

# 5. Why Streaming is Useful

Imagine asking:

> "Explain Spring Boot in detail."

Without streaming:

```text
User → request
       ↓
     wait...
       ↓
     wait...
       ↓
     wait...
       ↓
Complete answer appears
```

With streaming:

```text
User → request
       ↓
"Spring Boot..."
       ↓
" is a framework..."
       ↓
" built on Spring..."
       ↓
" that simplifies..."
       ↓
...
```

The user starts seeing the answer immediately.

### Benefits

* ⚡ Better perceived performance
* 👀 User gets immediate feedback
* 💬 ChatGPT-like experience
* 🧑‍💻 Better interactive UI
* 📱 Useful for long AI responses

---

# 6. Streaming in Your React Frontend

This becomes especially useful for your project.

Your architecture could eventually be:

```text
React
  │
  │ HTTP streaming
  ↓
Spring Boot
  │
  ↓
Spring AI ChatClient
  │
  ↓
Gemini
```

Gemini generates:

```text
Chunk 1 → Spring Boot
Chunk 2 → Spring Boot
Chunk 3 → Spring Boot
Chunk 4 → Spring Boot
```

Spring Boot forwards those chunks to React.

React can display them immediately:

```text
Career Roadmap
────────────────────────

Step 1: Learn Java
Step 2: Learn Spring Boot
Step 3: Build REST APIs
...
```

instead of waiting for the entire roadmap.

---

# 7. `stream()` vs `call()`

| Feature                            | `call()`                  | `stream()`  |
| ---------------------------------- | ------------------------- | ----------- |
| Response                           | Complete                  | Progressive |
| Return type                        | Usually String / response | `Flux`      |
| User waits for complete generation | Yes                       | No          |
| Good for chat UI                   | 👍                        | ⭐⭐⭐         |
| Good for simple REST APIs          | ⭐⭐⭐                       | 👍          |
| Long responses                     | Less interactive          | Better      |

### Simple memory trick

```text
call()   → Complete
stream() → Continuous
```

---

# 8. Streaming Does NOT Mean Multiple AI Requests

This is a common confusion.

Suppose the AI generates:

```text
Spring AI is a framework for building AI applications.
```

Streaming doesn't mean:

```text
Request 1 → "Spring"
Request 2 → "AI"
Request 3 → "is"
Request 4 → "a"
```

Instead, **one AI generation request produces a sequence of chunks**.

```text
ONE REQUEST
     ↓
AI generation
     ↓
chunk → chunk → chunk → chunk
```

That's an important distinction.

---

# 9. Streaming and Error Handling

Because streaming is asynchronous, error handling is slightly different from a simple `.call()`.

You can use Reactor operators such as:

```java
.onErrorResume(...)
```

For example:

```java
return chatClient
        .prompt()
        .user(message)
        .stream()
        .content()
        .onErrorResume(error ->
                Flux.just("Unable to generate AI response."));
```

Conceptually:

```text
AI Stream
    ↓
Success → send chunks
    ↓
Error → fallback message
```

For a production application, you'd generally log the actual exception and return an appropriate error signal rather than exposing internal details.

---

# 10. Streaming vs Server-Sent Events

You'll often hear **SSE (Server-Sent Events)** when building streaming AI applications.

The basic idea is:

```text
Server
   ↓
   ↓ continuous events
   ↓
Browser
```

The browser maintains a connection and receives updates from the server.

Spring applications can expose streaming responses using reactive types such as `Flux`.

Conceptually:

```text
React Browser
     ↑
     │ SSE / streaming HTTP
     │
Spring Boot
     ↑
     │ Flux<String>
     │
Spring AI
     ↑
     │
Gemini
```

You don't need to master SSE right now. We'll focus on the Spring AI side first.

---

# 11. Streaming in Your Career Platform 🎯

Imagine the user asks:

> "Create a roadmap to become a backend developer."

Normal:

```text
[Loading.................]
        ↓
Complete roadmap
```

Streaming:

```text
Career Roadmap

Step 1: Master Java
        ↓
Step 2: Learn Spring Boot
        ↓
Step 3: Learn REST APIs
        ↓
Step 4: Learn PostgreSQL
        ↓
Step 5: Build projects
        ↓
...
```

This will make your application feel **much more like a modern AI product**.

---


