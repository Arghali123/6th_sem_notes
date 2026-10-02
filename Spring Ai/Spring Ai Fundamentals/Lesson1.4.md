# 🟢 1.4 — Setting Up Your First Spring AI Project
## 1. What We're Going to Build

Our first application will be very simple:

```text
User
 ↓
Spring Boot REST API
 ↓
Spring AI
 ↓
AI Model
 ↓
AI Response
 ↓
User
```

We'll expose:

```text
GET /ai?message=Explain Java inheritance
```

and receive an AI-generated response.

---

# 2. Create the Spring Boot Project

Go to **Spring Initializr** and create a normal Maven Spring Boot project.

Select:

```text
Project: Maven
Language: Java
Packaging: Jar
Java: 17+
```

Add:

```text
Spring Web
```

For Spring AI, add the appropriate **Spring AI model starter** for the provider/model you intend to use.

For example, if you're learning with OpenAI, the dependency is the Spring AI OpenAI starter for your selected Spring AI release.

Your project will roughly look like:

```text
spring-ai-demo/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com.example.demo/
│   │   │       └── DemoApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

---

# 3. Add the Spring AI Dependency

For a Maven project, Spring AI dependencies are managed through the Spring AI BOM, which keeps compatible Spring AI module versions aligned.

Conceptually:

```xml
<dependencyManagement>
    ...
    spring-ai-bom
    ...
</dependencyManagement>
```

Then you add the provider starter you need.

For example, the OpenAI integration uses the Spring AI OpenAI Spring Boot starter.

**Don't blindly copy a version from an old tutorial.** Spring AI has evolved quickly, so use the version shown by the current Spring Initializr/Spring AI documentation for your project.

---

# 4. Configure Your API Key

If you're using a cloud provider, you generally need an API key.

For example:

```properties
spring.ai.openai.api-key=${OPENAI_API_KEY}
```

Then define the environment variable:

```text
OPENAI_API_KEY=your-api-key
```

### ⚠️ Important

Don't do this:

```properties
spring.ai.openai.api-key=sk-your-real-key
```

and then upload the project to GitHub.

Instead:

```text
Environment Variable
        ↓
Spring Boot
        ↓
Spring AI
        ↓
AI Provider
```

This keeps your secret outside the source code.

---

# 5. Create a ChatClient

Now we get to one of the most important Spring AI concepts.

We'll eventually use:

```java
ChatClient
```

A simple Spring configuration can create it from the available chat model:

```java
@Configuration
public class AIConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.create(chatModel);
    }
}
```

Now Spring can inject `ChatClient` into your services/controllers.

---

# 6. Create the REST Controller

Here's our **complete runnable example**:

```java
package com.example.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIController {

    private final ChatClient chatClient;

    public AIController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai")
    public String askAI(@RequestParam String message) {

        return chatClient
                .prompt(message)
                .call()
                .content();
    }
}
```

Now you can send:

```text
GET /ai?message=What is polymorphism in Java?
```

and the application sends the prompt to the configured AI model.

---

# 7. What's Happening Here?

Let's break this down:

### Step 1

Spring injects our `ChatClient`:

```java
private final ChatClient chatClient;
```

### Step 2

We give it a prompt:

```java
chatClient.prompt(message)
```

### Step 3

We execute the request:

```java
.call()
```

### Step 4

We extract the generated text:

```java
.content()
```

So:

```text
chatClient
     ↓
.prompt(message)
     ↓
.call()
     ↓
.content()
     ↓
String response
```

This tiny chain is something you'll see **again and again** throughout Spring AI.

---

# 8. Complete Project Structure

A clean beginner project could look like:

```text
src/main/java/com/example/demo/
│
├── DemoApplication.java
├── AIConfig.java
└── AIController.java
```

### `DemoApplication.java`

```java
package com.example.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
}
```

### `AIConfig.java`

```java
package com.example.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {

    @Bean
    public ChatClient chatClient(ChatModel chatModel) {
        return ChatClient.create(chatModel);
    }
}
```

### `AIController.java`

```java
package com.example.demo;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AIController {

    private final ChatClient chatClient;

    public AIController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai")
    public String askAI(@RequestParam String message) {

        return chatClient
                .prompt(message)
                .call()
                .content();
    }
}
```

---

# 9. Request Flow

When you call:

```text
http://localhost:8080/ai?message=Explain%20inheritance
```

the flow is:

```text
Browser/Postman
      ↓
GET /ai
      ↓
AIController
      ↓
ChatClient
      ↓
ChatModel
      ↓
AI Provider
      ↓
AI Model
      ↓
Response
      ↓
ChatClient
      ↓
AIController
      ↓
Browser/Postman
```

🔥 **This is the architecture we learned in 1.2 now working in real code.**

---

# 10. One Important Thing Before Moving On

There are **two things you should understand before we continue**:

### `ChatModel`

Represents the underlying model integration.

```java
ChatModel
```

### `ChatClient`

Provides the convenient API that your application uses:

```java
chatClient
    .prompt(...)
    .call()
    .content();
```

We'll explore `ChatClient` properly in **1.5**.

---



