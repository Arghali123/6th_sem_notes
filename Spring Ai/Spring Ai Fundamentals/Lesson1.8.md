# 🚀 1.8 — Chat Model Configuration

Now we're moving from **“how to send prompts”** to **“how to control how the AI generates responses.”**

The key idea is:

> **Chat Model Configuration = controlling the behavior and generation settings of the AI model.**

---

## 1. What is Chat Model Configuration?

When you call Gemini through Spring AI, you can configure things such as:

* 🤖 Which model to use
* 🌡️ Temperature
* 📏 Maximum output tokens
* 🎯 Other model-specific generation parameters

Conceptually:

```text
Your Application
      ↓
   ChatClient
      ↓
 Chat Model
      ↓
 Configuration
      ↓
   Gemini Model
      ↓
   Response
```

---

# 2. Model Selection

You can specify which Gemini model your application should use.

For example:

```properties
spring.ai.google.genai.chat.model=gemini-2.5-flash
```

Here:

```text
gemini-2.5-flash
       ↑
    AI model
```

Different models can have different capabilities, speed, cost, and limits.

For your learning project, a **Flash-type model** is generally a good choice because it is designed for fast responses.

> Model names and availability can change, so use the model supported by your current Spring AI/Google configuration.

---

# 3. Temperature 🌡️

**Temperature controls how deterministic or varied the model's output is.**

Think of it roughly like:

```text
Low temperature
      ↓
More predictable
More consistent
Less variation

High temperature
      ↓
More varied
More creative
Less predictable
```

For example:

### Temperature = 0.0

Prompt:

```text
Give me a Java method to add two numbers.
```

The model will tend to produce a very predictable answer.

### Higher temperature

For a creative prompt such as:

```text
Give me 5 creative names for a programming app.
```

Higher variation can be useful.

### Important

Temperature does **not** mean:

> “How intelligent is the AI?”

It controls the **randomness/variation of generation**, not intelligence.

---

# 4. Maximum Output Tokens

Another important setting is the maximum amount of output the model can generate.

Conceptually:

```text
max output tokens = maximum generated output
```

For example:

```text
max output = 100 tokens
```

means the model has a relatively small output limit.

Whereas:

```text
max output = 1000 tokens
```

allows a much longer response.

### Important distinction

There are two different ideas:

```text
Input tokens
    ↓
Your prompt

Output tokens
    ↓
AI's generated response
```

A maximum output-token setting primarily controls the **generated response length**.

---

# 5. Configuring Model Options in Spring AI

Spring AI allows model-specific configuration through the appropriate model options.

A common pattern is:

```java
ChatClient chatClient = ChatClient.builder(chatModel)
        .build();
```

The exact configuration API depends on the model/provider integration and Spring AI version.

For example, model options can conceptually look like:

```java
GoogleGenAiChatOptions options =
        GoogleGenAiChatOptions.builder()
                .model("gemini-2.5-flash")
                .temperature(0.3)
                .build();
```

Then those options can be supplied when making the request.

The important concept is:

```text
Chat Model
   +
Model Options
   ↓
Configured AI request
```

---

# 6. Request-Level Configuration

One powerful feature is configuring the model **for a particular request** rather than changing the entire application.

For example:

```java
String response = chatClient
        .prompt()
        .user("Explain Java interfaces.")
        .options(
                GoogleGenAiChatOptions.builder()
                        .temperature(0.2)
                        .build()
        )
        .call()
        .content();
```

This allows different requests to use different settings.

For example:

```text
Technical explanation
        ↓
Low temperature

Creative career ideas
        ↓
Higher temperature
```

That's useful in your project.

---

# 7. Application-Level vs Request-Level Configuration

This distinction is important.

### Application-level

Configuration applies broadly to your application/model.

Example:

```properties
spring.ai.google.genai.chat.model=gemini-2.5-flash
```

### Request-level

Configuration applies to a specific AI request.

Example:

```java
.options(...)
```

So:

```text
Application Configuration
        ↓
Default behavior

Request Configuration
        ↓
Override/customize for one request
```

---

# 8. Practical Example for Your Project 🎯

Suppose your **AI Skill and Career Management Platform** has two features.

### Feature 1 — Student performance scoring

You want consistent output.

Use a **lower temperature**.

```text
Temperature ≈ low
```

Because you don't want the score to change wildly between similar requests.

### Feature 2 — Career roadmap generation

You may want more variety.

```text
Temperature ≈ moderate
```

because the AI can suggest different learning paths, projects, and technologies.

So:

| Feature               | Temperature tendency |
| --------------------- | -------------------- |
| Performance scoring   | Low                  |
| Data extraction       | Low                  |
| Classification        | Low                  |
| Technical explanation | Low–moderate         |
| Career roadmap        | Moderate             |
| Brainstorming         | Higher               |
| Creative writing      | Higher               |

These are **general guidelines**, not strict rules.

---

# 9. Complete Example

Here's a simple controller demonstrating request-level configuration:

```java
package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.google.genai.GoogleGenAiChatOptions;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/explain")
    public String explain(@RequestParam String topic) {

        return chatClient
                .prompt()
                .system("""
                        You are an expert Java teacher.
                        Explain concepts simply and accurately.
                        """)
                .user("Explain: " + topic)
                .options(
                        GoogleGenAiChatOptions.builder()
                                .temperature(0.2)
                                .build()
                )
                .call()
                .content();
    }
}
```

Here:

```text
ChatClient
   ↓
prompt()
   ↓
system()
   ↓
user()
   ↓
options()
   ↓
call()
   ↓
content()
```

The important new part is:

```java
.options(
    GoogleGenAiChatOptions.builder()
        .temperature(0.2)
        .build()
)
```

---

# ⚠️ One Important Spring AI Point

Don't memorize provider-specific classes blindly.

Spring AI is designed around abstractions such as:

```text
ChatClient
ChatModel
ChatOptions
```

But individual providers can expose their own option classes.

For example:

```text
Spring AI
   │
   ├── Google Gemini options
   ├── OpenAI options
   ├── Anthropic options
   └── Other provider options
```

So when you change from Gemini to another provider, **the exact options class/configuration may change**.

That's one reason we learned **Spring AI Architecture** earlier. 😉

---
