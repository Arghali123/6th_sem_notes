# 🚀 1.7 — Prompt Templates


### 1. What is a Prompt Template?

A **Prompt Template** is a reusable prompt containing **variables/placeholders**.

Instead of writing a new prompt every time:

```text
Explain Java inheritance.
Explain Java interfaces.
Explain Java polymorphism.
```

we create one template:

```text
Explain the following Java topic in simple language:

Topic: {topic}
```

Then `{topic}` can change dynamically.

### 2. Why use Prompt Templates?

They help you:

* ♻️ Reuse prompts
* 🎯 Keep prompts consistent
* 🔄 Dynamically insert user data
* 🧹 Avoid hard-coded prompts
* 🏗️ Build scalable AI applications

For your **AI Skill and Career Management Platform**, this will be very useful.

For example:

```text
Create a career roadmap for a student.

Student skill: {skill}
Experience level: {level}
Career goal: {career}
```

The values can come from your database or frontend.

---

# 3. Prompt Template with ChatClient

A simple approach is to create a reusable prompt string and insert variables.

```java
package com.example.demo.controller;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PromptTemplateController {

    private final ChatClient chatClient;

    public PromptTemplateController(ChatClient.Builder chatClientBuilder) {
        this.chatClient = chatClientBuilder.build();
    }

    @GetMapping("/ai/explain")
    public String explain(@RequestParam String topic) {

        String prompt = """
                Explain the following Java topic in simple language.

                Topic: %s

                Include:
                1. Definition
                2. Simple explanation
                3. Example
                """.formatted(topic);

        return chatClient
                .prompt()
                .user(prompt)
                .call()
                .content();
    }
}
```

Now:

```text
/ai/explain?topic=Interfaces
```

generates a prompt where `Interfaces` replaces `%s`.

---

# 4. Spring AI's `PromptTemplate`

Spring AI also provides a dedicated `PromptTemplate` abstraction for creating prompts with variables.

Conceptually:

```text
Template
   ↓
Variables
   ↓
Prompt
   ↓
ChatClient
   ↓
AI Model
```

For example:

```java
String template = """
        Explain the following topic:

        Topic: {topic}

        Give a simple explanation and one example.
        """;
```

Here:

```text
{topic}
```

is a **template variable**.

You can then provide:

```text
topic = "Java Interfaces"
```

and the resulting prompt becomes:

```text
Explain the following topic:

Topic: Java Interfaces

Give a simple explanation and one example.
```

---

# 5. Multiple Variables

This is where templates become really useful.

Suppose you want an AI career advisor.

Template:

```text
You are a career advisor.

Student skills: {skills}
Experience level: {experience}
Career goal: {goal}

Create a learning roadmap for this student.
```

Variables:

```text
skills = "Java, Spring Boot, SQL"
experience = "Beginner"
goal = "Backend Developer"
```

The AI receives a fully constructed prompt containing those values.

### Your project could use:

```text
{studentName}
{skills}
{experience}
{careerGoal}
{preferredTechnology}
```

This is much better than creating separate prompts for every student.

---

# 6. System Prompt Template vs User Prompt Template

You can template both.

### System template

```text
You are an expert {role}.

Always explain concepts at a {level} level.
```

Variables:

```text
role = "Java teacher"
level = "beginner"
```

### User template

```text
Explain {topic} with a practical {language} example.
```

Variables:

```text
topic = "REST API"
language = "Java"
```

Then:

```text
ChatClient
    ↓
System Prompt Template
    +
User Prompt Template
    ↓
Final Prompt
    ↓
Gemini
```

---

# 7. Why this matters for your project 🎯

Your **AI Skill and Career Management Platform** will have dynamic student information.

Imagine your database contains:

```text
Student:
Name: Ram
Skills: Java, SQL
Experience: Beginner
Goal: Backend Developer
```

You could have one reusable template:

```text
You are an expert career advisor.

Student skills: {skills}
Experience: {experience}
Career goal: {goal}

Create a step-by-step career roadmap.
Identify skill gaps.
Suggest technologies to learn.
Suggest projects to build.
```

For another student, you simply change the variables.

**Same template → different student → personalized AI response.**

That's a major reason prompt templates are important in real Spring AI applications.

---

# 🧠 Remember This

| Concept         | Meaning                              |
| --------------- | ------------------------------------ |
| Prompt          | Actual instruction sent to AI        |
| Prompt Template | Reusable prompt containing variables |
| Variable        | Dynamic value inserted into template |
| Example         | `{topic}`, `{skills}`, `{goal}`      |
| Main benefit    | Reusability + dynamic prompts        |

### Exam definition

> **Prompt Template is a reusable prompt structure containing placeholders or variables that can be dynamically replaced with actual values before sending the prompt to an AI model.**

---


