# 🚀 2.9 Java DTOs / Records for AI Output

We learned in **2.8** that structured output allows us to turn an AI response into data that our Java application can work with.

Now the question is:

> **How do we define the structure in Java?**

That's where **DTOs and Records** come in.

---

## 1. What is a DTO?

**DTO = Data Transfer Object**

A DTO is a Java object used to carry structured data between different parts of an application.

For example, suppose AI returns:

```json
{
  "level": "GOOD",
  "score": 78,
  "skillGaps": [
    "Spring Security",
    "REST API Design",
    "Docker"
  ]
}
```

We can represent that structure in Java using a DTO.

```java
public class StudentEvaluation {

    private String level;
    private int score;
    private List<String> skillGaps;

    // getters and setters
}
```

Now instead of dealing with raw text, our application can work with:

```java
StudentEvaluation evaluation;
```

---

# 2. Why DTOs are useful with AI

Without structured output:

```text
AI
 ↓
String
 ↓
You manually parse the String
 ↓
Business logic
```

With DTOs:

```text
AI
 ↓
Structured response
 ↓
StudentEvaluation DTO
 ↓
Business logic
```

This is much cleaner.

For your project:

```text
Gemini
 ↓
StudentEvaluation
 ↓
Spring Boot
 ↓
PostgreSQL / React
```

---

# 3. Java Records

Since you're using modern Java, **records** are especially convenient for AI responses.

A record is a compact way of creating a data-carrying class.

Instead of:

```java
public class StudentEvaluation {

    private String level;
    private int score;
    private List<String> skillGaps;

    public StudentEvaluation(
            String level,
            int score,
            List<String> skillGaps) {
        this.level = level;
        this.score = score;
        this.skillGaps = skillGaps;
    }

    public String getLevel() {
        return level;
    }

    public int getScore() {
        return score;
    }

    public List<String> getSkillGaps() {
        return skillGaps;
    }
}
```

we can write:

```java
public record StudentEvaluation(
        String level,
        int score,
        List<String> skillGaps
) {
}
```

Much cleaner. 😎

---

# 4. Mapping JSON to a Record

Suppose AI returns:

```json
{
  "level": "GOOD",
  "score": 78,
  "skillGaps": [
    "Spring Security",
    "REST API Design",
    "Docker"
  ]
}
```

Our record is:

```java
public record StudentEvaluation(
        String level,
        int score,
        List<String> skillGaps
) {
}
```

The fields correspond:

| AI JSON       | Java Record              |
| ------------- | ------------------------ |
| `"level"`     | `String level`           |
| `"score"`     | `int score`              |
| `"skillGaps"` | `List<String> skillGaps` |

So conceptually:

```text
JSON
 ↓
Jackson / Spring AI conversion
 ↓
StudentEvaluation
```

---

# 5. Using the Record

Once we have:

```java
StudentEvaluation evaluation;
```

we can access its values:

```java
evaluation.level();
evaluation.score();
evaluation.skillGaps();
```

For example:

```java
System.out.println(evaluation.level());
System.out.println(evaluation.score());
System.out.println(evaluation.skillGaps());
```

Output:

```text
GOOD
78
[Spring Security, REST API Design, Docker]
```

Notice that records use:

```java
evaluation.level()
```

instead of the traditional:

```java
evaluation.getLevel()
```

---

# 6. A More Realistic AI Response

Let's improve our structure.

Our AI career evaluator could return:

```json
{
  "level": "GOOD",
  "score": 78,
  "skillGaps": [
    "Spring Security",
    "REST API Design",
    "Docker"
  ],
  "recommendations": [
    "Learn Spring Security fundamentals",
    "Build REST APIs",
    "Learn Docker basics"
  ]
}
```

Our Java record becomes:

```java
import java.util.List;

public record StudentEvaluation(
        String level,
        int score,
        List<String> skillGaps,
        List<String> recommendations
) {
}
```

Now our Java application has a clear contract for the AI response.

---

# 7. DTO vs Record

You should understand the difference.

### Traditional DTO

```java
public class StudentEvaluation {

    private String level;
    private int score;

    // constructor
    // getters
    // setters
}
```

### Record DTO

```java
public record StudentEvaluation(
        String level,
        int score
) {
}
```

Records are excellent when your object is primarily **data** and should be immutable.

For AI responses, that's often exactly what we want.

---

# 8. Important: DTO Does NOT Make AI Reliable

This is an important concept.

Suppose we define:

```java
public record StudentEvaluation(
        String level,
        int score,
        List<String> skillGaps
) {}
```

That does **not** automatically guarantee that AI will return correct data.

The record defines the structure we **expect**.

We still need:

```text
Prompt
 ↓
AI
 ↓
Structured Output
 ↓
Java Record
 ↓
Validation
```

That's why **2.12 Structured Output Validation** will be important later.

---

# 9. Your Career Platform Example

Imagine your controller receives:

```json
{
  "skills": ["Java", "SQL", "Spring Boot"],
  "experience": "Beginner",
  "goal": "Backend Developer",
  "testScore": 78
}
```

AI produces:

```json
{
  "level": "GOOD",
  "score": 78,
  "skillGaps": [
    "Spring Security",
    "Docker",
    "Microservices"
  ],
  "recommendations": [
    "Learn Spring Security",
    "Learn Docker",
    "Study Microservices"
  ]
}
```

Java:

```java
public record StudentEvaluation(
        String level,
        int score,
        List<String> skillGaps,
        List<String> recommendations
) {
}
```

Now your backend can do things like:

```java
if (evaluation.score() >= 80) {
    // mark student as high performing
}
```

or:

```java
evaluation.skillGaps()
        .forEach(System.out::println);
```

And eventually store relevant information in PostgreSQL or send it to React.

---

# 🧠 Key takeaway

Remember this chain:

```text
AI Response
     ↓
Structured JSON
     ↓
Java DTO / Record
     ↓
Spring Boot Business Logic
```

And the difference:

> **DTO/Record defines the Java-side structure.**

> **Structured output is the mechanism that makes the AI response fit that structure.**

---


