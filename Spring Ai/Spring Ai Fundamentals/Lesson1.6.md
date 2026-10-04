# 🟢 1.6 — Prompts

Now we move into one of the **most important concepts in AI application development: prompts**.

You already know how to send a basic message through `ChatClient`. Now we'll learn how to **design and control that message**.

---

# 1. What is a Prompt?

A **prompt** is the input/instruction given to an AI model to produce a response.

For example:

```text
Explain dependency injection in Spring Boot.
```

That's a prompt.

The model receives it:

```text
Prompt
  ↓
AI Model
  ↓
Response
```

For example:

```text
Prompt:
"Explain dependency injection in Spring Boot."

Response:
"Dependency injection is a design pattern..."
```

### Simple definition

> **A prompt is an instruction or input provided to an AI model to guide it in generating a response.**

---

# 2. Why Prompts Matter

Consider these two requests.

### Prompt 1

```text
Explain Spring Boot.
```

The model has a lot of freedom.

### Prompt 2

```text
You are a Spring Boot teacher.

Explain Spring Boot dependency injection to a beginner.

Requirements:
- Use simple language.
- Give one Java example.
- Keep the explanation under 200 words.
```

The second prompt gives the model much more direction.

```text
Less instruction
      ↓
More freedom

More instruction
      ↓
More controlled output
```

This is the basic idea behind **prompt engineering**, which we'll study more deeply in the next phase.

---

# 3. Prompts in Spring AI

With `ChatClient`, you can create prompts using:

```java
chatClient
    .prompt()
    .system(...)
    .user(...)
    .call()
    .content();
```

There are two particularly important messages:

### System message

Defines the AI's behavior or role.

### User message

Contains the user's actual request.

For example:

```java
String response = chatClient
        .prompt()
        .system("You are an expert Spring Boot teacher.")
        .user("Explain dependency injection.")
        .call()
        .content();
```

Conceptually:

```text
SYSTEM
"You are an expert Spring Boot teacher."

        +

USER
"Explain dependency injection."

        ↓

     AI Model

        ↓

    Response
```

---

# 4. System Prompt

A **system prompt** provides high-level instructions about how the AI should behave.

Example:

```java
.system("""
    You are an expert Java teacher.
    Explain concepts simply.
    Always provide a small code example.
""")
```

Now the model has these instructions before processing the user's request.

### Example

User:

```text
Explain interfaces.
```

The AI should ideally respond according to the system instructions:

```text
Definition
↓
Simple explanation
↓
Java example
```

---

# 5. User Prompt

The **user prompt** represents the user's actual request.

Example:

```java
.user("Explain Java interfaces.")
```

You can also pass a variable:

```java
String question = "Explain Java interfaces.";

String response = chatClient
        .prompt()
        .user(question)
        .call()
        .content();
```

This is useful in real applications because the user's message normally comes from an HTTP request, database, UI, etc.

---

# 6. System + User Together

A common Spring AI pattern is:

```java
String response = chatClient
        .prompt()
        .system("You are an expert Java teacher.")
        .user("Explain polymorphism.")
        .call()
        .content();
```

Think of it as:

```text
┌──────────────────────────────────┐
│ SYSTEM                           │
│ You are an expert Java teacher.  │
└────────────────┬─────────────────┘
                 ↓
┌──────────────────────────────────┐
│ USER                             │
│ Explain polymorphism.            │
└────────────────┬─────────────────┘
                 ↓
              AI Model
                 ↓
              Response
```

---

# 7. Prompt Instructions

A prompt can contain multiple instructions.

For example:

```java
.system("""
    You are an experienced Java teacher.

    Follow these rules:
    1. Use simple language.
    2. Give practical examples.
    3. Use Java code when appropriate.
    4. Avoid unnecessary complexity.
""")
```

This is much more useful than simply saying:

```text
You are a Java teacher.
```

---

# 8. Giving the AI a Role

One common technique is assigning a role.

Examples:

```text
You are a Java teacher.
```

```text
You are a senior Spring Boot developer.
```

```text
You are a technical interviewer.
```

```text
You are a code reviewer.
```

In Spring AI:

```java
.system("You are a senior Spring Boot developer.")
```

Then:

```java
.user("Review this REST API design.")
```

---

# 9. Giving Context

You can provide additional information that the model should use.

For example:

```java
String response = chatClient
        .prompt()
        .system("""
            You are a Java teacher.
            The student already knows basic Java
            and is currently learning Spring Boot.
        """)
        .user("Explain dependency injection.")
        .call()
        .content();
```

The AI now has additional context.

---

# 10. Giving Output Instructions

You can also tell the model how the answer should look.

Example:

```java
String response = chatClient
        .prompt()
        .system("""
            You are a Java teacher.

            Explain concepts using:
            1. Definition
            2. Explanation
            3. Example
            4. Key points
        """)
        .user("Explain dependency injection.")
        .call()
        .content();
```

The output should approximately follow that structure.

---

# 11. Complete Runnable Example

Let's create a simple endpoint using everything we've learned.

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

    @GetMapping("/ai/teach")
    public String teach(@RequestParam String topic) {

        return chatClient
                .prompt()
                .system("""
                        You are an expert Java and Spring Boot teacher.

                        Explain concepts in simple language.
                        Give a practical example when appropriate.
                        Keep the explanation concise.
                        """)
                .user("Explain: " + topic)
                .call()
                .content();
    }
}
```

Now call:

```text
http://localhost:8080/ai/teach?topic=Dependency Injection
```

The request becomes conceptually:

```text
SYSTEM:
You are an expert Java and Spring Boot teacher.
Explain concepts in simple language.
Give a practical example when appropriate.
Keep the explanation concise.

USER:
Explain: Dependency Injection
```

---

# 12. Prompt vs Prompt Template

This distinction is important.

### Prompt

A specific instruction:

```text
Explain Java inheritance.
```

### Prompt Template

A reusable pattern:

```text
Explain the following Java topic:

{topic}
```

Then:

```text
topic = "Inheritance"
```

produces:

```text
Explain the following Java topic:

Inheritance
```

We'll study **Prompt Templates in 1.7**, so don't worry about implementation yet.

---

# 13. Good Prompt vs Poor Prompt

### ❌ Poor

```text
Tell me about Java.
```

Very broad.

### ✅ Better

```text
Explain Java inheritance to a beginner.
Use simple language and provide a short Java example.
```

### ⭐ Even more controlled

```text
You are a Java teacher.

Explain Java inheritance to a beginner.

Requirements:
- Start with a definition.
- Explain the concept simply.
- Give one Java example.
- Mention two advantages.
- Keep the answer under 200 words.
```

The third prompt gives the model:

```text
Role
 ↓
Task
 ↓
Requirements
 ↓
Output constraints
```

That's the foundation of prompt engineering.

---

# 14. Important Prompt Components

A useful mental model is:

```text
Prompt
 ├── Role
 ├── Context
 ├── Task
 ├── Instructions
 ├── Constraints
 └── Output format
```

For example:

```text
Role:
You are a Java teacher.

Context:
The student knows basic Java.

Task:
Explain dependency injection.

Instructions:
Use simple language.

Constraints:
Keep it under 200 words.

Output:
Definition + example + key points.
```

We'll build on this heavily in **Phase 2 — Prompt Engineering & Structured Output**.

---

# 15. System Prompt ≠ Security Boundary

One important professional concept:

Don't assume that putting something in a system prompt makes it a **security mechanism**.

For example:

```text
"You must never reveal confidential information."
```

is an instruction, not a guaranteed security control.

Sensitive data should be protected by your actual application:

```text
Authentication
Authorization
Database permissions
Input validation
Tool restrictions
```

We'll cover AI security later in the roadmap.

---

# 🧠 What You Should Remember

For now, remember these:

### Prompt

> Input/instruction given to an AI model.

### System prompt

> Defines behavior, role, context, or high-level instructions.

### User prompt

> Contains the user's actual request.

### Prompt engineering

> Designing prompts to produce more useful, reliable, and controlled outputs.

And the Spring AI pattern:

```java
chatClient
    .prompt()
    .system("...")
    .user("...")
    .call()
    .content();
```

---


