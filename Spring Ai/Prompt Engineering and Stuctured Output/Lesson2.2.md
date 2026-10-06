# 2.2 Zero-Shot Prompting

## 1. What is Zero-Shot Prompting?

**Zero-shot prompting** means asking the AI to perform a task **without providing examples of how the task should be performed**.

For example:

```text
Classify this student's performance as Poor, Average, Good, or Excellent.

Student:
Java: 85
SQL: 72
Spring Boot: 80
```

We didn't give the AI any examples like:

```text
Example:
Score 90 → Excellent
Score 70 → Good
```

The AI has to understand the task from our instructions alone.

That's **zero-shot prompting**.

---

## 2. Simple Mental Model

```text
Instruction
     +
New Input
     ↓
    AI
     ↓
Result
```

There are **no examples** between the instruction and input.

---

# 3. Why is it called "Zero-Shot"?

Think of a "shot" as an **example provided to the model**.

### Zero-shot

```text
Task
 ↓
Input
 ↓
Output
```

**0 examples**

### Few-shot

```text
Task
 ↓
Example 1
Example 2
Example 3
 ↓
New Input
 ↓
Output
```

We'll study few-shot next.

---

# 4. Your Project Example

Suppose your platform receives:

```json
{
  "skills": ["Java", "SQL", "Spring Boot"],
  "experience": "Beginner",
  "testScore": 78
}
```

A zero-shot prompt could be:

```text
You are a backend development mentor.

Evaluate this student's backend development performance.

Student skills:
Java, SQL, Spring Boot

Experience:
Beginner

Test score:
78

Classify the student's performance as one of:
Poor, Average, Good, Excellent.

Also identify important skill gaps.
```

Notice something important:

**We haven't shown the AI any previous student evaluations.**

That's zero-shot.

---

# 5. When Should We Use Zero-Shot?

Zero-shot is useful when:

### ✅ The task is relatively simple

For example:

```text
Classify the student's performance.
```

### ✅ The instruction itself is clear

```text
Summarize this student's skills in 3 bullet points.
```

### ✅ You don't have good examples

Sometimes you simply don't have enough representative examples to provide.

### ✅ You want a simple prompt

Zero-shot prompts are generally easier to maintain than prompts containing many examples.

---

# 6. Advantages

### 👍 Simple

No examples are required.

### 👍 Shorter prompt

This can reduce prompt size and potentially token usage.

### 👍 Easy to maintain

You only maintain the instruction.

### 👍 Good for straightforward tasks

For example:

```text
Extract the student's programming languages.
```

---

# 7. Limitations

Here's where things become interesting.

Suppose you ask:

```text
Classify this student as Poor, Average, Good, or Excellent.
```

What exactly determines the classification?

Is:

```text
60 → Average?
```

or:

```text
60 → Good?
```

The model may make its own interpretation.

That's because the instruction isn't sufficiently precise.

You could improve it:

```text
Use these score ranges:

0-39   → Poor
40-59  → Average
60-79  → Good
80-100 → Excellent
```

Now the model has a much clearer rule.

But there's still another problem.

What if your classification depends on **multiple factors**, not just the test score?

That's where **few-shot prompting** can become useful.

---

# 8. Zero-Shot in Spring AI

Since you've already learned `ChatClient`, this should look familiar.

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
                    You are a backend development mentor.

                    Evaluate this student's performance.

                    Skills:
                    Java, SQL, Spring Boot

                    Experience:
                    Beginner

                    Test Score:
                    78

                    Classify the student as:
                    Poor, Average, Good, or Excellent.

                    Also identify important skill gaps.
                    """)
                .call()
                .content();
    }
}
```

The important part is:

```java
.call()
.content();
```

We're simply asking Gemini to generate the response.

There are **no examples** in the prompt.

Therefore:

> **This is zero-shot prompting.**

---

# 🧠 Zero-Shot vs Few-Shot

Keep this distinction very clear for your exam/interview/project:

|                        | Zero-Shot               | Few-Shot                       |
| ---------------------- | ----------------------- | ------------------------------ |
| Examples provided      | ❌ No                    | ✅ Yes                          |
| Prompt size            | Smaller                 | Larger                         |
| Simple tasks           | ✅ Good                  | ✅ Good                         |
| Complex classification | Sometimes unreliable    | Often better                   |
| Consistency            | Depends on instructions | Can improve with good examples |

---

# 🎯 Important Spring AI Perspective

Don't think:

> "Zero-shot = bad, Few-shot = good."

That's not the idea.

Instead:

```text
Simple + clear task
       ↓
   Zero-shot
```

If the model struggles to consistently understand the desired behavior:

```text
Clear task
   +
Representative examples
   ↓
Few-shot
```

And later:

```text
Prompt
 +
Structured Output
 +
Validation
 +
Business Rules
 ↓
Reliable AI feature
```

That last approach is especially important for your **Student Evaluation** and **Career Roadmap** features.

---


