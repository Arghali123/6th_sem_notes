# 🟢 1.5 — `ChatClient`

## 1. What is `ChatClient`?

`ChatClient` is a Spring AI API that provides a convenient way for your Spring Boot application to communicate with a chat model.

In simple terms:

> **`ChatClient` is the main interface we use in our application to send prompts to an AI model and receive responses.**

The basic flow is:

```text
Your Java Code
      ↓
  ChatClient
      ↓
  Chat Model
      ↓
    Gemini
      ↓
   Response
```

---

# 2. Creating a `ChatClient`

In our previous project, we used:

```java
private final ChatClient chatClient;

public ChatController(ChatClient.Builder chatClientBuilder) {
    this.chatClient = chatClientBuilder.build();
}
```

Spring AI provides `ChatClient.Builder`, which we use to create a `ChatClient`.

A common pattern is:

```java
ChatClient chatClient = chatClientBuilder.build();
```

### Why use `Builder`?

Because it allows us to configure the client before building it.

For example, later we can configure:

```text
Default system instructions
Default user instructions
Advisors
Tools
Memory
```

So the builder becomes very useful as our applications become more advanced.

---

# 3. Simplest `ChatClient` Call

The simplest request is:

```java
String response = chatClient
        .prompt("What is Spring Boot?")
        .call()
        .content();
```

Let's break it down.

### `.prompt()`

Starts creating an AI request.

```java
.prompt("What is Spring Boot?")
```

We're giving the AI our prompt.

---

### `.call()`

Actually executes the request.

```java
.call()
```

Conceptually:

```text
Prompt
  ↓
ChatClient
  ↓
AI Model
```

---

### `.content()`

Extracts the generated text from the response.

```java
.content()
```

So:

```java
chatClient
    .prompt("What is Spring Boot?")
    .call()
    .content();
```

means:

> Send this prompt to the configured model and give me the generated text.

---

# 4. Complete Example

Let's modify our previous controller.

```java
package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/chat")
    public String chat(@RequestParam String message) {

        return chatClient
                .prompt(message)
                .call()
                .content();
    }
}
```

Request:

```text
http://localhost:8080/ai/chat?message=Explain%20polymorphism
```

Flow:

```text
HTTP Request
     ↓
Controller
     ↓
ChatClient
     ↓
Gemini
     ↓
Response
     ↓
String
```

---

# 5. `prompt()` Can Be Used in Different Ways

You aren't limited to:

```java
.prompt("...")
```

You can build a more structured request.

For example:

```java
chatClient
    .prompt()
    .user("Explain Spring Boot")
    .call()
    .content();
```

Here:

```java
.prompt()
```

starts the prompt builder.

Then:

```java
.user(...)
```

specifies the user message.

---

# 6. System Message vs User Message

This is **very important**.

AI conversations commonly contain different message roles.

### System message

Defines the AI's behavior/instructions.

Example:

```text
You are an experienced Java teacher.
Explain concepts simply.
```

### User message

Contains the user's actual request.

```text
Explain interfaces in Java.
```

Together:

```text
System:
You are an experienced Java teacher.

User:
Explain interfaces in Java.
```

---

# 7. Using `.system()` and `.user()`

Example:

```java
String response = chatClient
        .prompt()
        .system("You are an experienced Java teacher.")
        .user("Explain interfaces in Java.")
        .call()
        .content();
```

Conceptually:

```text
       ChatClient
           │
     ┌─────┴─────┐
     ↓           ↓
  System       User
 instruction   message
     │           │
     └─────┬─────┘
           ↓
       AI Model
           ↓
        Response
```

This distinction will become extremely important when we study **Prompt Engineering**.

---

# 8. `ChatClient` Request Flow

Think of `ChatClient` as a pipeline:

```text
.prompt()
    ↓
.system()
    ↓
.user()
    ↓
.call()
    ↓
.content()
```

Not every request needs every method.

For example:

### Simple

```java
chatClient
    .prompt("Explain Java")
    .call()
    .content();
```

### Structured

```java
chatClient
    .prompt()
    .system("You are a Java teacher.")
    .user("Explain Java interfaces.")
    .call()
    .content();
```

---

# 9. Getting the Full Response

So far we've used:

```java
.content()
```

which gives us the generated text.

But Spring AI can provide more information about the response.

Conceptually:

```text
ChatResponse
 ├── Result
 │    ├── Assistant message
 │    └── Metadata
 │
 └── Metadata
```

You can retrieve a response object instead of immediately extracting the text.

For example:

```java
var response = chatClient
        .prompt("Explain Spring AI")
        .call()
        .chatResponse();
```

Then you can inspect information available in the response.

This becomes useful when you care about things like:

* Model information
* Response metadata
* Usage/token information
* Other provider/model metadata

---

# 10. Why `ChatClient` Is So Important

At first it looks simple:

```java
chatClient
    .prompt(...)
    .call()
    .content();
```

But later, Spring AI allows us to add much more around this request.

For example:

```text
                 ChatClient
                     │
       ┌─────────────┼─────────────┐
       ↓             ↓             ↓
    Prompts       Advisors       Tools
       ↓             ↓             ↓
       └─────────────┼─────────────┘
                     ↓
                 Chat Model
                     ↓
                    LLM
```

And eventually:

```text
User
 ↓
ChatClient
 ↓
Memory
 ↓
RAG
 ↓
Tools
 ↓
Model
 ↓
Response
```

That's why mastering `ChatClient` early is important.

---

# 11. `ChatClient.Builder` vs `ChatClient`

Don't confuse these two.

### `ChatClient.Builder`

Used to **create/configure** a `ChatClient`.

```java
ChatClient chatClient = chatClientBuilder.build();
```

### `ChatClient`

Used to **send requests**.

```java
chatClient
    .prompt("Explain Java")
    .call()
    .content();
```

Think:

```text
Builder
   ↓
creates
   ↓
ChatClient
   ↓
sends prompts
   ↓
AI Model
```

---

# 12. A Better Controller Example

Let's make our endpoint slightly more useful.

```java
package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/chat")
    public String chat(@RequestParam String message) {

        return chatClient
                .prompt()
                .system("You are a helpful Java and Spring Boot teacher.")
                .user(message)
                .call()
                .content();
    }
}
```

Now try:

```text
http://localhost:8080/ai/chat?message=Explain%20dependency%20injection
```

The model receives the system instruction plus the user's question.

---

# 13. Important Methods to Remember

For now, focus on these:

| Method           | Purpose                              |
| ---------------- | ------------------------------------ |
| `prompt()`       | Starts building a prompt             |
| `prompt(String)` | Creates a prompt with a user message |
| `system()`       | Adds system instructions             |
| `user()`         | Adds a user message                  |
| `call()`         | Executes the request                 |
| `content()`      | Gets generated text                  |
| `chatResponse()` | Gets the response object             |

You don't need to memorize every `ChatClient` method yet. We'll encounter the important ones naturally as we progress.

---

# 🧠 The Most Important Mental Model

Remember this:

```text
ChatClient
   │
   ├── prompt
   │     ├── system instruction
   │     └── user message
   │
   ├── call
   │
   └── response
          └── content
```

And the complete architecture:

```text
┌──────────────────────┐
│    Spring Boot App   │
└──────────┬───────────┘
           ↓
┌──────────────────────┐
│      ChatClient      │
└──────────┬───────────┘
           ↓
┌──────────────────────┐
│      Chat Model      │
└──────────┬───────────┘
           ↓
┌──────────────────────┐
│       Gemini         │
└──────────┬───────────┘
           ↓
       AI Response
```

---


## Difference between System Prompt and User Prompt
[Click here to view the content](https://dev.to/sungwoo_lee_e0f26be4a29fd/system-prompt-vs-user-prompt-whats-the-difference-9po)

# 🧪 Practice Task

Modify your `/ai/chat` endpoint so that the AI behaves like a **Spring Boot teacher**.

For example:

```java
.system("You are an expert Spring Boot teacher. Explain concepts simply with examples.")
```

Then test:

```text
Explain dependency injection
```

```text
What is @RestController?
```

```text
What is Spring Bean?
```

Observe how the **system instruction affects the responses**.

Don't worry about prompt engineering yet—that comes later.

---


