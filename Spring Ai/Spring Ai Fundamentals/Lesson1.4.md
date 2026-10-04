# 🟢 1.4 — Setting Up Your First Spring AI Project

Great. For our roadmap, we'll use **Google Gemini through the Gemini Developer API**, using the **Free Tier** for learning.

The current Spring AI documentation uses the `spring-ai-starter-model-google-genai` starter for this integration. ([Home][1])

> **Important:** API keys are secrets. Never paste your real key into chat, GitHub, screenshots, or your `application.properties` if that file will be committed to Git.

---

# Part A — Get Your Gemini API Key 🔑

Google currently provides Gemini API keys through **Google AI Studio**. New users can have a project and API key created automatically; otherwise, you can create one from the API Keys page. ([Google AI for Developers][2])

### Step 1 — Open Google AI Studio

[Open Google AI Studio](https://aistudio.google.com/?utm_source=chatgpt.com)

Sign in with your Google account.

### Step 2 — Go to API Keys

In AI Studio, open the **API Keys** section.

Google's current documentation says that new users may automatically get a project and API key, while existing users can create a new key through **Create API key**. ([Google AI for Developers][2])

### Step 3 — Create the key

Click:

**Create API key**

If you're asked to select a project, create/select a project for this Spring AI learning project.

You'll receive something resembling:

```text
AIzaSy................................
```

**Do not share the actual value.**

### Step 4 — Keep it on the Free Tier

For our learning project, **do not set up billing yet**.

Google currently has a Free Tier for the Gemini API with access to certain models subject to their free-tier limits. Higher tiers require billing. ([Google AI for Developers][3])

You can monitor your usage and limits from Google AI Studio. ([Google AI for Developers][3])

---

# Part B — Create the Spring Boot Project

Go to:

[Spring Initializr](https://start.spring.io/?utm_source=chatgpt.com)

Use:

```text
Project:       Maven
Language:      Java
Spring Boot:   Current stable version
Packaging:     Jar
Java:          17 or newer
```

### Dependencies

Add:

```text
Spring Web
```

and the Spring AI Google GenAI dependency.

Current Spring AI documentation lists:

```xml
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-google-genai</artifactId>
</dependency>
```

as the Google GenAI starter. ([Home][1])

### Your `pom.xml`

If you're creating the project from scratch, the important dependency section looks like:

```xml
<dependencies>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <dependency>
        <groupId>org.springframework.ai</groupId>
        <artifactId>spring-ai-starter-model-google-genai</artifactId>
    </dependency>

</dependencies>
```

Spring AI's current documentation is on the **2.0.x** line, which supports Spring Boot 4.x. ([Home][4])

So if you're following an older tutorial that tells you to use a different Gemini starter/artifact, **don't blindly copy it**. Spring AI's Google integration has changed over time.

---

# Part C — Configure Gemini

Now open:

```text
src/main/resources/application.properties
```

We need to tell Spring AI:

1. What API key to use
2. Which Gemini model to use

The current configuration uses:

```text
spring.ai.google.genai.api-key
spring.ai.google.genai.chat.model
```

according to the Spring AI documentation. ([Home][1])

### ❌ Don't do this

```properties
spring.ai.google.genai.api-key=AIzaSyYourRealKey
```

if `application.properties` is going into Git.

Instead, use an environment variable.

### Windows

Set an environment variable:

```text
GOOGLE_API_KEY=YOUR_API_KEY
```

Then configure:

```properties
spring.ai.google.genai.api-key=${GOOGLE_API_KEY}
spring.ai.google.genai.chat.model=gemini-2.5-flash
```

The current Spring AI documentation lists `gemini-2.5-flash` among supported Google GenAI chat models. ([Home][1])

---

# Part D — Create Our First AI Controller

Now we'll make a very small REST API.

Create:

```text
src/main/java/com/example/demo/controller/ChatController.java
```

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
    public String chat(
            @RequestParam String message) {

        return chatClient
                .prompt(message)
                .call()
                .content();
    }
}
```

### What's happening here?

This line:

```java
private final ChatClient chatClient;
```

creates our Spring AI client.

Then:

```java
this.chatClient = chatClientBuilder.build();
```

creates the `ChatClient`.

And:

```java
chatClient
    .prompt(message)
    .call()
    .content();
```

means:

```text
User message
     ↓
  ChatClient
     ↓
 Gemini
     ↓
 AI response
     ↓
 String
```

We'll study every part of this properly in **1.5 — ChatClient**. For now, we're just getting the application running.

---

# Part E — Run the Application

Start your Spring Boot application.

You should see something similar to:

```text
Started DemoApplication
```

Then open:

```text
http://localhost:8080/ai/chat?message=Explain%20Java%20interfaces
```

Your application sends:

```text
Explain Java interfaces
```

to Gemini.

Gemini generates the answer.

Spring AI returns it to your browser.

---

# 🧠 Understand the Complete Flow

You've now created this:

```text
                 USER
                   │
                   │
                   ▼
        ┌───────────────────┐
        │   Spring Boot     │
        │   REST Controller  │
        └─────────┬─────────┘
                  │
                  ▼
        ┌───────────────────┐
        │    ChatClient     │
        └─────────┬─────────┘
                  │
                  ▼
        ┌───────────────────┐
        │    Spring AI      │
        │   Google GenAI    │
        └─────────┬─────────┘
                  │
                  ▼
        ┌───────────────────┐
        │   Gemini Model    │
        └─────────┬─────────┘
                  │
                  ▼
               RESPONSE
```

🎉 **This is your first real Spring AI application.**

---

# 🔐 One Important Security Practice

For your project, keep:

```text
application.properties
```

free of the actual secret.

For example:

```properties
spring.ai.google.genai.api-key=${GOOGLE_API_KEY}
```

Then keep the actual key in your environment.

Also add sensitive local configuration to `.gitignore` if applicable.

**Never upload this:**

```text
GOOGLE_API_KEY=AIzaSy...
```

to GitHub.

Google specifically documents API-key security and currently recommends securing keys; new AI Studio keys are created as auth keys, while unrestricted older standard keys may need restrictions. ([Google AI for Developers][5])

---

# ⚠️ About the Gemini Free Tier

One thing to understand before we continue:

**Free doesn't mean unlimited.**

Google's Free Tier has model-specific rate/usage limits. The exact limits can change, so don't rely on old tutorials claiming a particular number of requests per minute/day. Check the current limits in AI Studio when needed. ([Google AI for Developers][3])

For **learning Spring AI**, though, the free tier is a very practical starting point.

---

# 🧪 Your First Practice

Before we move to `ChatClient`, make sure you can get this working:

```text
GET /ai/chat?message=What is Spring AI?
```

Then try:

```text
GET /ai/chat?message=Explain dependency injection in Spring Boot
```

And:

```text
GET /ai/chat?message=Give me 5 Java interview questions
```

Notice something important:

**We haven't learned prompt engineering yet.**

We're simply sending a string to the model.

That's intentional.

In the next topic, **1.5 — `ChatClient`**, we'll properly understand this:

```java
chatClient
    .prompt()
    .user(...)
    .call()
    .content();
```

and learn how `ChatClient` actually works.

---


