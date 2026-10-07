# 🚀 2.10 Spring AI `.entity()`

This is where things get really interesting.

Until now, we learned:

```text
AI
 ↓
JSON
 ↓
Java Record
```

Now Spring AI can help us do this much more directly:

```text
AI
 ↓
Java Record
```

using:

```java
.entity(CareerEvaluation.class)
```

---

## 1. The Problem We Had Before

Suppose we ask the AI:

```java
String response = chatClient.prompt()
        .user("""
            Evaluate this student:

            Skills: Java, SQL, Spring Boot
            Goal: Backend Developer
            Score: 85

            Return career, score and skills as JSON.
            """)
        .call()
        .content();
```

The result is:

```java
String response
```

We then need to convert that response into our Java object.

But Spring AI provides a convenient way to request the response directly as a Java type.

---

# 2. Using `.entity()`

First create our record:

```java
public record CareerEvaluation(
        String career,
        int score,
        List<String> skills
) {
}
```

Then:

```java
CareerEvaluation evaluation = chatClient.prompt()
        .user("""
            Evaluate this student.

            Skills: Java, SQL, Spring Boot
            Goal: Backend Developer
            Test Score: 85

            Return the career, score and skills.
            """)
        .call()
        .entity(CareerEvaluation.class);
```

Now:

```java
evaluation
```

is already a:

```java
CareerEvaluation
```

instead of:

```java
String
```

That's the big idea.

---

# 3. Compare `.content()` vs `.entity()`

### `.content()`

```java
String response = chatClient.prompt()
        .user(prompt)
        .call()
        .content();
```

You get:

```text
String
```

### `.entity()`

```java
CareerEvaluation evaluation = chatClient.prompt()
        .user(prompt)
        .call()
        .entity(CareerEvaluation.class);
```

You get:

```text
CareerEvaluation
```

So remember:

```text
.content()
    ↓
String

.entity(MyClass.class)
    ↓
MyClass
```

🔥 This is one of the most important Spring AI methods for structured output.

---

# 4. Complete Example

Let's create a small service.

```java
@Service
public class CareerService {

    private final ChatClient chatClient;

    public CareerService(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    public CareerEvaluation evaluateStudent() {

        return chatClient.prompt()
                .user("""
                    You are an expert career advisor.

                    Student skills:
                    Java, SQL, Spring Boot

                    Goal:
                    Backend Developer

                    Test Score:
                    85

                    Evaluate the student.

                    Return:
                    - Career
                    - Score
                    - Skills
                    """)
                .call()
                .entity(CareerEvaluation.class);
    }
}
```

Record:

```java
public record CareerEvaluation(
        String career,
        int score,
        List<String> skills
) {
}
```

The flow is:

```text
Student Data
     ↓
ChatClient
     ↓
AI Model
     ↓
Structured Response
     ↓
.entity(CareerEvaluation.class)
     ↓
CareerEvaluation
```

---

# 5. Using the Result

Because we now have a Java record:

```java
CareerEvaluation evaluation
```

we can simply do:

```java
System.out.println(evaluation.career());
System.out.println(evaluation.score());
System.out.println(evaluation.skills());
```

For example:

```text
Backend Developer
85
[Java, SQL, Spring Boot]
```

No manual JSON parsing in our service code. 👍

---

# 6. `.entity()` with Your Project

This becomes especially useful for your **AI Skill and Career Management Platform**.

Suppose your AI response should contain:

```java
public record StudentEvaluation(
        String level,
        int score,
        List<String> skillGaps,
        List<String> recommendations
) {
}
```

Then your service can return:

```java
public StudentEvaluation evaluate(StudentProfile student) {

    return chatClient.prompt()
            .user("""
                You are an experienced backend career advisor.

                Student skills:
                {skills}

                Experience:
                {experience}

                Goal:
                {goal}

                Test Score:
                {testScore}

                Identify exactly 3 skill gaps and provide
                relevant recommendations.
                """)
            .param("skills", String.join(", ", student.skills()))
            .param("experience", student.experience())
            .param("goal", student.goal())
            .param("testScore", student.testScore())
            .call()
            .entity(StudentEvaluation.class);
}
```

Now your service directly returns:

```text
StudentEvaluation
```

instead of a raw AI string.

---

# 7. Why `.entity()` is powerful

Without `.entity()`:

```text
AI
 ↓
String
 ↓
JSON parsing
 ↓
Java object
```

With `.entity()`:

```text
AI
 ↓
Spring AI conversion
 ↓
Java object
```

This makes your AI service code much cleaner.

---

# ⚠️ One Important Point

`.entity()` doesn't magically guarantee that the AI's answer is **correct**.

For example, suppose your record expects:

```java
int score
```

but the AI produces something inappropriate.

Or it returns:

```json
{
  "career": "Backend Developer"
}
```

and omits `score`.

You still need **validation**.

That's why our upcoming topic:

### **2.12 Structured Output Validation**

will be important.

Think:

```text
.entity()
   ↓
Java Object
   ↓
Validation
   ↓
Business Rules
```

---

# 🧠 2.10 Mental Model

Memorize this:

> **`.content()` → Give me the AI response as text.**

> **`.entity(Class)` → Give me the AI response converted into this Java type.**

Example:

```java
String result = ...content();
```

versus:

```java
CareerEvaluation result = ...entity(CareerEvaluation.class);
```

---


