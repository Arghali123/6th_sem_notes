# 🚀 Let's start 2.1 — Prompt Engineering Fundamentals

The first thing I want you to understand is this:

> **Prompt engineering is not simply "asking ChatGPT good questions."**

In an application, you're designing instructions that an AI model will repeatedly receive and that your backend needs to depend on.

For example, your platform might ask:

> "Evaluate this student."

That's too vague.

Instead, your backend can construct something like:

```text
Role:
You are an experienced software engineering mentor.

Context:
The student is learning backend development.

Student:
Skills: Java, SQL, Spring Boot
Experience: Beginner
Test Score: 78

Task:
Evaluate the student's current backend-development readiness.

Constraints:
- Score the student from 0 to 100.
- Identify important skill gaps.
- Recommend practical improvements.
- Consider the student's experience level.

Output:
Return the evaluation in the required structured format.
```

That's prompt engineering.

---

## 🧠 The mental model

A useful way to design prompts is:

```text
Role
  +
Context
  +
Task
  +
Instructions
  +
Constraints
  +
Output Format
  ↓
Better AI Response
```

Let's understand each one.

### 1. Role

Tell the model **what perspective it should take**.

```text
You are an experienced Java backend developer.
```

or:

```text
You are a career advisor specializing in software engineering.
```

This can help establish the desired behavior and domain focus.

---

### 2. Context

Give the model the information it needs.

```text
Student skills:
Java, SQL, Spring Boot

Experience:
Beginner

Career goal:
Backend Developer
```

Without context:

```text
Recommend a career.
```

With context:

```text
The student knows Java and SQL, has beginner-level Spring Boot
experience, and wants to become a backend developer.
Recommend an appropriate learning roadmap.
```

Obviously, the second prompt gives the model much more useful information.

---

### 3. Task

Clearly tell the model **what you want it to do**.

Weak:

```text
Student information: ...
```

Better:

```text
Evaluate the student's backend development skills.
```

Even better:

```text
Evaluate the student's backend development readiness
and identify the three most important skill gaps.
```

---

### 4. Instructions

Instructions tell the model **how to perform the task**.

For example:

```text
Consider both technical skills and practical experience.
Prioritize skills required for backend development.
```

---

### 5. Constraints

Constraints limit the model's freedom.

For example:

```text
Return exactly 3 skill gaps.

Do not recommend frontend technologies.

Keep recommendations appropriate for a beginner.
```

This becomes extremely important when we build reliable AI applications.

---

### 6. Output Format

Finally, tell the model what kind of result you need.

For example:

```text
Return the result as JSON containing:

score
level
skillGaps
recommendations
```

Later we'll improve this further using **Spring AI structured output**, so the backend doesn't have to blindly trust raw text.

---

# 🔥 Bad vs Good Prompt

### ❌ Bad

```text
Evaluate this student.

Java, SQL, Spring Boot, score 78.
```

Problems:

* What does "evaluate" mean?
* What should the score represent?
* What career is being considered?
* What should the AI recommend?
* How many recommendations?
* What format should it return?

The model has to guess.

---

### ✅ Better

```text
You are a software engineering mentor.

Evaluate this student's readiness for a backend developer career.

Student skills:
- Java
- SQL
- Spring Boot

Experience:
Beginner

Test score:
78/100

Identify the student's current level, important skill gaps,
and practical recommendations for improvement.

Return exactly 3 skill gaps and 3 recommendations.
Focus only on backend development.
```

Now the model has much less ambiguity.

---

# 💡 Why this matters for your project

Imagine your React frontend sends:

```json
{
  "skills": ["Java", "SQL", "Spring Boot"],
  "experience": "Beginner",
  "testScore": 78
}
```

Your Spring Boot backend can use those values to construct a carefully designed prompt.

```text
                    Student
                       │
                       ▼
              Spring Boot API
                       │
                       ▼
                 Build Prompt
                       │
                       ▼
                  Spring AI
                       │
                       ▼
                    Gemini
                       │
                       ▼
              Structured Result
                       │
                       ▼
                Java DTO
                       │
                       ▼
                 REST Response
                       │
                       ▼
                 React Frontend
```

This is where prompt engineering stops being an AI curiosity and becomes **backend engineering**.

---


