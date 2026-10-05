# 🚀 1.9 — Handling AI Responses

Now we move to the **response side** of Spring AI.

So far, you've learned:

```text
Prompt
   ↓
ChatClient
   ↓
Chat Model
   ↓
Gemini
```

But what exactly comes back from Gemini, and how do we handle it?

---

## 1. What is an AI Response?

When you call:

```java
chatClient
    .prompt("Explain Spring Boot")
    .call();
```

the model returns a **response object**, which contains information about the generated answer.

You can think of it as:

```text
Gemini
   ↓
ChatResponse
   ├── Generated text
   ├── Metadata
   └── Usage information
```

---

# 2. Getting Only the Text

Most of the time, you only need the AI's answer.

That's why we commonly use:

```java
.content()
```

Example:

```java
String response = chatClient
        .prompt("What is Spring Boot?")
        .call()
        .content();
```

The variable `response` contains the generated text.

For a REST API:

```java
@GetMapping("/ai/chat")
public String chat(@RequestParam String message) {

    return chatClient
            .prompt(message)
            .call()
            .content();
}
```

The client receives:

```text
GET /ai/chat?message=What is Spring Boot?
```

and gets something like:

```text
Spring Boot is a framework built on Spring...
```

### 🧠 Remember

```text
.call()
   ↓
AI response
   ↓
.content()
   ↓
Generated text
```

---

# 3. Getting the Complete `ChatResponse`

Sometimes you need more than just the text.

For example:

* Generated content
* Model information
* Token usage
* Response metadata
* Finish information

Then use:

```java
.chatResponse()
```

Example:

```java
ChatResponse response = chatClient
        .prompt("Explain Spring AI")
        .call()
        .chatResponse();
```

Now you have the complete response object.

---

# 4. `content()` vs `chatResponse()`

This is **very important**.

| Method            | What you get            |
| ----------------- | ----------------------- |
| `.content()`      | Generated text          |
| `.chatResponse()` | Complete `ChatResponse` |

### Use `.content()` when:

You simply want to return the AI answer.

```java
return chatClient
        .prompt(message)
        .call()
        .content();
```

### Use `.chatResponse()` when:

You need additional information about the response.

```java
ChatResponse response = chatClient
        .prompt(message)
        .call()
        .chatResponse();
```

---

# 5. Understanding `ChatResponse`

Conceptually, Spring AI's response structure looks like:

```text
ChatResponse
│
├── Results
│     │
│     └── Generation
│            │
│            └── Assistant message
│
└── Metadata
       │
       ├── Model information
       ├── Usage
       └── Other information
```

Don't worry about memorizing every internal class yet.

The important idea is:

> **ChatResponse represents the complete result of an AI chat request.**

---

# 6. Getting the Generated Text from `ChatResponse`

You can access the generated result from the response.

A typical pattern is:

```java
ChatResponse response = chatClient
        .prompt("Explain dependency injection")
        .call()
        .chatResponse();

String answer = response
        .getResult()
        .getOutput()
        .getText();

System.out.println(answer);
```

Depending on the Spring AI version you're using, some response accessor APIs may differ.

That's an important practical point: **Spring AI is evolving quickly**, so always check the API corresponding to your installed version rather than copying an old tutorial blindly.

---

# 7. Response Metadata

A response may contain metadata about the model request.

For example:

```text
ChatResponse
      ↓
Metadata
      ├── Model
      ├── Finish reason
      └── Usage
```

This can be useful for debugging, monitoring, and production applications.

For example, you might want to know:

```text
Which model generated this?
How many tokens were used?
Why did generation stop?
```

---

# 8. Token Usage

One particularly useful piece of information is **token usage**.

Conceptually:

```text
User Prompt
    ↓
Input Tokens
    +
AI Response
    ↓
Output Tokens
```

For example:

```text
Input tokens  = 50
Output tokens = 120
Total tokens  = 170
```

This matters because token usage can affect:

* 💰 Cost
* ⚡ Performance
* 📊 Monitoring
* 🚦 Rate/usage limits

For your Gemini learning project, you may not care much about cost initially if you're within the available free quota, but token usage becomes very important when you move toward production.

---

# 9. Handling Empty/Null Responses

A production application should not blindly assume that the AI always returns usable text.

For example:

```java
String response = chatClient
        .prompt(message)
        .call()
        .content();

if (response == null || response.isBlank()) {
    return "No response was generated.";
}

return response;
```

This is a simple defensive check.

---

# 10. Handling Exceptions

AI calls involve external services, so failures can happen.

For example:

```java
@GetMapping("/ai/chat")
public String chat(@RequestParam String message) {

    try {
        return chatClient
                .prompt(message)
                .call()
                .content();

    } catch (Exception e) {
        return "Unable to get AI response.";
    }
}
```

For a real production application, don't simply catch every exception and hide it. You'd normally use proper exception handling, logging, and meaningful HTTP responses.

For example:

```text
AI service unavailable
        ↓
Spring Boot
        ↓
HTTP 503 Service Unavailable
```

rather than returning `200 OK` with an error message.

---

# 11. Practical REST API Example

Here's a clean example combining what we've learned:

```java
package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ai")
public class ChatController {

    private final ChatClient chatClient;

    public ChatController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/chat")
    public String chat(@RequestParam String message) {

        try {

            String response = chatClient
                    .prompt()
                    .system("You are a helpful Java teacher.")
                    .user(message)
                    .call()
                    .content();

            if (response == null || response.isBlank()) {
                return "No response was generated.";
            }

            return response;

        } catch (Exception e) {
            return "Unable to get AI response.";
        }
    }
}
```

Request:

```text
/ai/chat?message=Explain%20interfaces%20in%20Java
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
AI Response
     ↓
.content()
     ↓
String
     ↓
HTTP Response
```

---

# 12. Why This Matters for Your Project 🎯

Your **AI Skill and Career Management Platform** won't always want to simply display raw AI text.

For example, later your performance-scoring feature might need:

```text
AI Response
   ↓
Score
Skill gaps
Suggestions
Explanation
```

And your career-roadmap feature might need:

```text
AI Response
   ↓
Career
Skills
Learning steps
Projects
Resources
```

This leads directly into a much more powerful concept we'll study in **Phase 2: Structured Output**.

Instead of:

```text
"Your score is 78 and you should improve SQL..."
```

you can eventually get structured data such as:

```json
{
  "score": 78,
  "skillGaps": ["SQL", "System Design"],
  "recommendations": [
    "Practice SQL joins",
    "Build a REST API project"
  ]
}
```

That is **much easier for a React frontend and Spring Boot backend to consume**.

---


