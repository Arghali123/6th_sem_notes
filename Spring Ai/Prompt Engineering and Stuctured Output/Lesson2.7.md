# 🚀 2.7 Advanced Prompt Templates

Now we're moving from **writing prompts** → **building reusable prompts**.

This is very important in real Spring AI applications because you don't want to hard-code a different prompt for every request.

### What we'll learn

By the end of 2.7, you should understand:

* What an advanced prompt template is
* Dynamic variables
* Multiple variables
* Reusable prompt templates
* Separating prompt logic from Java code
* Spring AI `PromptTemplate`
* Templates for real applications
* How this connects to your **AI Career Management Platform**

---

## 1. Why do we need Prompt Templates?

Imagine your application receives this request:

```text
Student:
Skills: Java, SQL
Experience: Beginner
Goal: Backend Developer
```

You could create a prompt directly:

```java
String prompt = """
You are an experienced career advisor.

Student skills: Java, SQL
Experience: Beginner
Goal: Backend Developer

Identify the student's skill gaps.
""";
```

But now another student comes:

```text
Skills: Python, Django
Experience: Intermediate
Goal: Data Engineer
```

You would have to construct another prompt.

That's inefficient.

Instead, create a **template**:

```text
You are an experienced career advisor.

Student skills: {skills}
Experience: {experience}
Goal: {goal}

Identify the student's skill gaps.
```

Now we simply provide different values.

### Mental model

```text
Prompt Template
      +
Dynamic Data
      ↓
Final Prompt
      ↓
AI Model
      ↓
Response
```

---

# 2. What is a Prompt Template?

A **Prompt Template** is a reusable prompt containing **placeholders/variables** that are replaced with actual values at runtime.

Example:

```text
You are a {role}.

Student skills:
{skills}

Experience:
{experience}

Goal:
{goal}
```

Here:

```text
{role}
{skills}
{experience}
{goal}
```

are variables.

---

# 3. Simple Example

Suppose we have:

```java
String template = """
You are a {role}.

Student skills: {skills}
Experience: {experience}
Career goal: {goal}
""";
```

And data:

```text
role = career advisor
skills = Java, SQL, Spring Boot
experience = Beginner
goal = Backend Developer
```

The final prompt becomes:

```text
You are a career advisor.

Student skills: Java, SQL, Spring Boot
Experience: Beginner
Career goal: Backend Developer
```

The AI doesn't need to know that this came from a template.

It simply receives the final prompt.

---

# 4. Spring AI `PromptTemplate`

Spring AI provides `PromptTemplate` for creating prompts dynamically.

A basic example:

```java
PromptTemplate template = new PromptTemplate("""
    You are an experienced career advisor.

    Student skills: {skills}
    Experience: {experience}
    Goal: {goal}

    Identify the student's skill gaps.
    """);
```

Then provide values:

```java
Map<String, Object> variables = Map.of(
    "skills", "Java, SQL, Spring Boot",
    "experience", "Beginner",
    "goal", "Backend Developer"
);
```

Then create the final prompt:

```java
Prompt prompt = template.create(variables);
```

And send it to the model.

---

# 5. With `ChatClient`

In modern Spring AI applications, you'll commonly use `ChatClient` together with dynamic prompt data.

For example:

```java
String response = chatClient.prompt()
        .user("""
            You are an experienced career advisor.

            Student skills: {skills}
            Experience: {experience}
            Goal: {goal}

            Identify the student's skill gaps.
            """)
        .param("skills", "Java, SQL, Spring Boot")
        .param("experience", "Beginner")
        .param("goal", "Backend Developer")
        .call()
        .content();
```

The important part is:

```java
.param("skills", ...)
.param("experience", ...)
.param("goal", ...)
```

Spring AI replaces the variables with the supplied values.

---

# 6. Why is this useful?

Imagine your **AI Skill and Career Management Platform**.

Your React frontend sends:

```json
{
  "skills": ["Java", "SQL", "Spring Boot"],
  "experience": "Beginner",
  "goal": "Backend Developer"
}
```

Your backend can use one reusable template:

```text
You are an experienced career advisor.

Current skills:
{skills}

Experience:
{experience}

Career goal:
{goal}

Identify skill gaps and recommend what the student should learn next.
```

Every student can use the same template.

Only the data changes.

```text
Student A ──┐
Student B ──┤
Student C ──┼──> Same Prompt Template ──> AI
Student D ──┤
Student E ──┘
```

That's the real power of templates.

---

# 7. Template + Constraints

Now we combine what we've already learned.

Remember our **Prompt Constraints**?

We can put them directly into the template:

```text
You are an experienced backend career advisor.

Student skills:
{skills}

Experience:
{experience}

Career goal:
{goal}

TASK:
Identify the student's skill gaps.

CONSTRAINTS:
- Return exactly 3 skill gaps.
- Only recommend backend-development skills.
- Prioritize the most important gaps.
- Keep the response concise.
```

Now we have:

**Template + Variables + Constraints**

This is much closer to a production-quality prompt.

---

# 8. Template Design Best Practice

A good template should separate these three things:

### ① Static instructions

Things that don't change:

```text
You are an experienced backend career advisor.

Identify skill gaps.
Return exactly 3 gaps.
```

### ② Dynamic data

Things that change:

```text
Skills: {skills}
Experience: {experience}
Goal: {goal}
```

### ③ Output requirements

```text
Return the result in JSON format.
```

So:

```text
STATIC INSTRUCTIONS
        +
DYNAMIC DATA
        +
OUTPUT REQUIREMENTS
        ↓
     TEMPLATE
```

---

# 9. Advanced Example — Your Project

Suppose your backend has this DTO:

```java
public record StudentProfile(
        List<String> skills,
        String experience,
        String goal
) {
}
```

You can create a prompt based on it:

```java
String prompt = """
You are an experienced backend career advisor.

Student Information:

Skills:
{skills}

Experience:
{experience}

Career Goal:
{goal}

TASK:
Analyze the student's current skills against the requirements
of their career goal.

CONSTRAINTS:
- Identify exactly 3 skill gaps.
- Prioritize the most important gaps.
- Recommend only relevant technical skills.
- Keep the response concise.

Return the final answer only.
""";
```

Then dynamically provide:

```java
chatClient.prompt()
        .user(prompt)
        .param("skills", String.join(", ", student.skills()))
        .param("experience", student.experience())
        .param("goal", student.goal())
        .call()
        .content();
```

This is a **reusable AI prompt**.

---

# 🧠 Important distinction

Don't confuse:

### Prompt

```text
You are a career advisor.

Skills: Java, SQL
Goal: Backend Developer
```

with:

### Prompt Template

```text
You are a career advisor.

Skills: {skills}
Goal: {goal}
```

The first is designed for **one particular input**.

The second is designed for **many inputs**.

---

# 10. What makes a template "Advanced"?

We're not just replacing `{name}` anymore.

Advanced templates can combine:

```text
Role
+
Context
+
Dynamic user data
+
Constraints
+
Examples
+
Output requirements
```

For example:

```text
SYSTEM INSTRUCTIONS
        ↓
ROLE
        ↓
CONTEXT
        ↓
FEW-SHOT EXAMPLES
        ↓
DYNAMIC STUDENT DATA
        ↓
CONSTRAINTS
        ↓
OUTPUT FORMAT
```

This connects almost everything we've learned in **2.1–2.6**.

🔥 That's why 2.7 is an important bridge before we enter **Structured Output**.

---


