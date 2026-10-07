# 🚀 2.8 Why Structured Output?

Now we're entering a **very important part of Spring AI**.

Until now, we've mostly done this:

```java
String response = chatClient.prompt()
        .user(prompt)
        .call()
        .content();
```

The problem is that `response` is just a **String**.

---

## 1. The Problem with Plain Text

Suppose your AI evaluates a student.

You ask:

```text
Identify the student's skill gaps.
```

The AI might return:

```text
The student should improve REST APIs, Spring Security,
and Docker.
```

That's understandable for a human.

But your backend might want:

```java
studentEvaluation.getSkillGaps();
studentEvaluation.getScore();
studentEvaluation.getLevel();
```

A plain String doesn't give us that structure.

---

# 2. What is Structured Output?

**Structured output means asking the AI to return data in a predefined structure that your application can process.**

Instead of:

```text
Improve REST APIs, Spring Security and Docker.
```

we want something like:

```json
{
  "level": "GOOD",
  "score": 78,
  "skillGaps": [
    "REST APIs",
    "Spring Security",
    "Docker"
  ]
}
```

Now our Java application can work with individual fields.

---

# 3. Why is this important?

Think about the difference:

### ❌ Unstructured

```text
The student is good but should improve REST APIs...
```

Java sees:

```java
String
```

### ✅ Structured

```json
{
  "level": "GOOD",
  "score": 78,
  "skillGaps": [
    "REST APIs",
    "Spring Security",
    "Docker"
  ]
}
```

Java can map this to:

```java
StudentEvaluation
```

So:

```text
AI
 ↓
Structured Response
 ↓
Java Object
 ↓
Business Logic
 ↓
Database / API / React
```

This is **much more useful in real applications**.

---

# 4. Structured Output in Your Project

Your **AI Skill and Career Management Platform** is a perfect example.

Suppose a student submits:

```json
{
  "skills": [
    "Java",
    "SQL",
    "Spring Boot"
  ],
  "experience": "Beginner",
  "goal": "Backend Developer",
  "testScore": 72
}
```

Your AI could return:

```json
{
  "level": "GOOD",
  "score": 72,
  "skillGaps": [
    "Spring Security",
    "REST API Design",
    "Docker"
  ],
  "nextSteps": [
    "Learn Spring Security",
    "Build REST APIs",
    "Learn Docker basics"
  ]
}
```

Your Spring Boot application can then use these values directly.

For example:

```java
evaluation.getLevel();
evaluation.getSkillGaps();
evaluation.getNextSteps();
```

🔥 This is where Spring AI becomes much more powerful than simply calling an AI API and getting text back.

---

# 5. Why not just parse JSON ourselves?

You might think:

> "Can't I just ask Gemini to return JSON and parse it manually?"

Yes, you can.

For example:

```java
String json = chatClient.prompt()
        .user(prompt)
        .call()
        .content();
```

Then manually parse it.

But this approach has problems:

* AI might return invalid JSON
* Fields might be missing
* Formatting may change
* Parsing becomes repetitive
* Error handling becomes annoying

Spring AI provides mechanisms to make structured output much easier.

---

# 6. Structured Output Mental Model

Remember this:

```text
Normal AI
────────────────────
Prompt
  ↓
AI
  ↓
String
```

Structured AI:

```text
Prompt
  ↓
AI
  ↓
Structured Data
  ↓
Java Object
```

And eventually:

```text
AI
 ↓
Java DTO
 ↓
Validation
 ↓
Business Logic
 ↓
Database / REST API
```

---

# 7. What we'll learn in Phase 2

From here, our roadmap becomes:

```text
2.8  Why Structured Output?          ← WE ARE HERE
 ↓
2.9  Java DTOs / Records
 ↓
2.10 Spring AI .entity()
 ↓
2.11 Lists & Generic Types
 ↓
2.12 Validation
 ↓
2.13 Provider-Native Structured Output
 ↓
2.14 Combining Structured Output + Prompt Engineering
 ↓
2.15 AI Student Evaluator Mini Project
 ↓
2.16 Phase 2 Revision
```

---

## 🧠 One key concept to remember

**Prompt engineering controls what the AI should produce.**

**Structured output controls how your application receives the result.**

For example:

```text
Prompt Engineering
        ↓
"Give exactly 3 skill gaps"
        ↓
AI
        ↓
Structured Output
        ↓
StudentEvaluation Java Object
        ↓
Spring Boot
```

This distinction is **very important** for the next topics.


