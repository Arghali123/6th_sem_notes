# 🚀 2.3 Few-Shot Prompting

# 1. What is Few-Shot Prompting?

**Few-shot prompting** means giving the AI **a small number of examples** showing how you want it to perform a task, followed by a new input.

### Mental model

```text
Instructions
     ↓
Example 1
Example 2
Example 3
     ↓
New Input
     ↓
AI
     ↓
Expected-style Output
```

The examples are called **shots**.

So:

* **Zero-shot** → 0 examples
* **One-shot** → 1 example
* **Few-shot** → a few examples

---

# 2. Why Do We Need Few-Shot Prompting?

Imagine you tell the AI:

```text
Classify students as Poor, Average, Good, or Excellent.
```

The AI doesn't necessarily know **your exact definition** of those categories.

You could provide examples:

```text
Student:
Java: 40
SQL: 35

Classification:
Poor
```

Then:

```text
Student:
Java: 65
SQL: 70

Classification:
Good
```

Then:

```text
Student:
Java: 90
SQL: 88

Classification:
Excellent
```

Now you're teaching the model the **pattern you expect**.

---

# 3. Zero-Shot vs Few-Shot

### Zero-shot

```text
Classify this student:

Java: 75
SQL: 70
Spring Boot: 65
```

The AI must figure out your classification behavior.

### Few-shot

```text
Classify students according to these examples.

Example 1:
Java: 40
SQL: 45
Spring Boot: 35
Result: Poor

Example 2:
Java: 65
SQL: 70
Spring Boot: 68
Result: Good

Example 3:
Java: 90
SQL: 85
Spring Boot: 92
Result: Excellent

Now classify:

Java: 78
SQL: 75
Spring Boot: 80
```

The AI now has **examples to follow**.

---

# 4. The Important Idea: Examples Demonstrate Behavior

This is probably the most important thing to understand.

Few-shot examples aren't only about providing data.

They can demonstrate:

* desired format
* classification behavior
* terminology
* level of detail
* style
* decision patterns

For example:

```text
Input:
Student knows Java but has no SQL knowledge.

Output:
Skill Gap: SQL
Recommendation: Learn SQL fundamentals and practice joins.
```

Then another:

```text
Input:
Student knows Java and SQL but has no Spring Boot experience.

Output:
Skill Gap: Spring Boot
Recommendation: Learn Spring Boot REST APIs and dependency injection.
```

Now the AI can infer how you want **skill gaps → recommendations** to work.

---

# 5. Few-Shot for Your Career Platform

Let's create a realistic example.

Suppose your feature is:

> **Identify the student's skill gap based on their career goal.**

### Prompt

```text
You are a software career advisor.

Your task is to identify missing skills based on
the student's current skills and career goal.

Follow these examples.

Example 1:

Current skills:
Java, SQL

Career goal:
Backend Developer

Output:
Missing skills:
Spring Boot, REST APIs, Git, Docker


Example 2:

Current skills:
Java, SQL, Spring Boot, REST APIs

Career goal:
Backend Developer

Output:
Missing skills:
Docker, System Design, Cloud


Now evaluate this student:

Current skills:
Java, SQL, Spring Boot

Career goal:
Backend Developer

Output:
```

The model might produce:

```text
Missing skills:
REST APIs, Git, Docker
```

The examples establish the expected pattern.

---

# 6. Designing Good Few-Shot Examples

This is where prompt engineering becomes an actual skill.

### Rule 1 — Examples should be relevant

If you're evaluating backend developers, don't provide examples about graphic designers.

Bad:

```text
Example:
Student wants to become a photographer...
```

Not relevant.

---

### Rule 2 — Examples should be representative

Your examples should cover different situations.

For example:

```text
Beginner student
Intermediate student
Advanced student
```

This helps the model understand the range of expected behavior.

---

### Rule 3 — Keep examples consistent

Suppose:

```text
Java + SQL → Good
```

but another example says:

```text
Java + SQL → Poor
```

without explaining why.

You've created conflicting instructions.

The model may become unpredictable.

---

### Rule 4 — Show the desired output format

Suppose you want:

```json
{
  "level": "Good",
  "skillGaps": ["Docker", "System Design"]
}
```

Your examples should use that format.

This is particularly useful because we're eventually going to combine:

**Few-shot prompting + Structured Output.**

---

# 7. Few-Shot + JSON

For your project, you might eventually create a prompt like:

```text
You are a student performance evaluator.

Return the evaluation using the following format.

Example 1:

Input:
Test score: 40
Skills: Java

Output:
{
  "level": "Poor",
  "skillGaps": ["SQL", "Spring Boot"]
}


Example 2:

Input:
Test score: 85
Skills: Java, SQL, Spring Boot

Output:
{
  "level": "Excellent",
  "skillGaps": ["System Design"]
}


Now evaluate:

Input:
Test score: 78
Skills: Java, SQL, Spring Boot

Output:
```

This gives the model both:

1. **Behavior examples**
2. **Output format examples**

However, remember:

> **Prompting for JSON does NOT guarantee valid JSON.**

That's something we'll address properly in **Structured Output**.

---

# 8. Few-Shot in Spring AI

Because you've already learned `ChatClient`, implementation is straightforward.

```java
@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final ChatClient chatClient;

    public StudentController(ChatClient.Builder builder) {
        this.chatClient = builder.build();
    }

    @PostMapping("/evaluate")
    public String evaluateStudent() {

        return chatClient
                .prompt()
                .user("""
                    You are a student performance evaluator.

                    Classify students as:
                    Poor, Average, Good, or Excellent.

                    Follow these examples.

                    Example 1:
                    Test Score: 40
                    Skills: Java
                    Result: Poor

                    Example 2:
                    Test Score: 70
                    Skills: Java, SQL
                    Result: Good

                    Example 3:
                    Test Score: 90
                    Skills: Java, SQL, Spring Boot
                    Result: Excellent

                    Now evaluate:

                    Test Score: 78
                    Skills: Java, SQL, Spring Boot

                    Return only the classification.
                    """)
                .call()
                .content();
    }
}
```

Notice the key difference from zero-shot.

### Zero-shot:

```text
Instruction
+
Student
```

### Few-shot:

```text
Instruction
+
Example
+
Example
+
Example
+
Student
```

---

# 9. When Should You Use Few-Shot?

Use it when the task is difficult to describe purely with instructions.

Especially when you need:

### 🎯 Consistent classification

```text
Poor / Average / Good / Excellent
```

### 🎯 Specific output style

```text
Skill Gap → Recommendation
```

### 🎯 Domain-specific behavior

For example, your university might have its own definition of student levels.

### 🎯 Consistent terminology

You might want:

```text
"Skill Gap"
```

instead of:

```text
"Missing Competency"
```

Examples can demonstrate that preference.

---

# 10. Disadvantages

Few-shot isn't free.

### More tokens

Every example becomes part of the prompt.

```text
More examples
     ↓
Larger prompt
     ↓
More tokens
```

This matters when you're using an API with token limits or cost constraints.

### Bad examples can make things worse

This is extremely important:

> **Few-shot prompting does not magically make a prompt better.**

If your examples are inconsistent or poor, the AI can learn the wrong pattern.

Think:

```text
Bad examples
     ↓
AI learns bad pattern
     ↓
Bad output
```

---

# 11. Few-Shot vs Zero-Shot — Final Comparison

| Feature          | Zero-Shot                       | Few-Shot            |
| ---------------- | ------------------------------- | ------------------- |
| Examples         | 0                               | Few                 |
| Prompt size      | Small                           | Larger              |
| Easy tasks       | Excellent                       | Usually unnecessary |
| Complex behavior | May struggle                    | Often better        |
| Consistency      | Depends heavily on instructions | Can improve         |
| Token usage      | Lower                           | Higher              |
| Maintenance      | Easier                          | More work           |

---

# 🧠 A Very Important Production Insight

For your **AI Skill and Career Management Platform**, don't automatically put 10–20 examples into every prompt.

Instead:

```text
Start
 ↓
Zero-shot
 ↓
Evaluate results
 ↓
If inconsistent
 ↓
Improve instructions
 ↓
If still inconsistent
 ↓
Try few-shot
 ↓
Validate output
```

That's a much better engineering approach than:

> "Let's put lots of examples into every prompt."

---


