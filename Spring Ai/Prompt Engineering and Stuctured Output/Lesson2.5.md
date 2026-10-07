# 2.5 — Prompt Constraints

# 1. What are Prompt Constraints?

A **prompt constraint** is a rule that limits or controls what the AI is allowed or expected to produce.

Without constraints:

```text
Recommend skills for this student.
```

The AI has a lot of freedom.

With constraints:

```text
Recommend exactly 5 skills.
Only recommend backend-development skills.
Keep each skill name under 30 characters.
Do not recommend frontend skills.
```

Now the AI has clear boundaries.

### Mental model

```text
Task
  +
Instructions
  +
Constraints
      ↓
Controlled AI Output
```

---

# 2. Why are Constraints Important?

This is especially important for your project.

Imagine your React frontend expects:

```json
{
  "skills": [
    "Advanced SQL",
    "Spring Security",
    "Docker"
  ]
}
```

But the AI suddenly returns:

```text
Here are some skills you might consider...

1. Java
2. Python
3. HTML
4. CSS
5. JavaScript
6. Docker
7. Kubernetes
8. AWS
9. React
10. SQL
...
```

Your backend now has a problem.

The AI response isn't necessarily *wrong*, but it doesn't follow what your application needs.

Constraints help reduce this problem.

---

# 3. Common Types of Constraints

Your roadmap lists several important types.

Let's go through them.

---

## 3.1 Length Constraints

You can restrict the length of the response.

### Example

```text
Explain REST API in maximum 100 words.
```

Or:

```text
Give a career recommendation in 3 sentences.
```

Or:

```text
Keep each recommendation below 30 words.
```

Useful when you don't want huge responses.

---

# 3.2 Number of Items

This is very useful for your platform.

```text
Return exactly 5 skill gaps.
```

or:

```text
Provide exactly 3 recommendations.
```

Example:

```text
Identify exactly 3 skills the student should learn next.
```

Instead of:

```text
Identify the skills the student should learn.
```

The second has no defined quantity.

---

# 3.3 Allowed Values

Sometimes you want the AI to select only from a predefined set.

For example:

```text
Classify the student as exactly one of:

Poor
Average
Good
Excellent
```

The AI shouldn't return:

```text
Very Good
Outstanding
Needs Improvement
Intermediate
```

Those aren't part of your allowed values.

This becomes **very important for structured output and validation** later.

---

# 3.4 Forbidden Content

You can explicitly tell the model what it shouldn't produce.

For example:

```text
Do not recommend frontend technologies.
```

Or:

```text
Do not recommend skills unrelated to backend development.
```

For your backend career feature:

```text
Do not recommend:
React
Angular
Vue
CSS
HTML
```

if the feature is specifically evaluating backend skills.

---

# 3.5 Required Sections

You can tell the model that certain information must always be included.

For example:

```text
The response must contain:

1. Current Level
2. Skill Gaps
3. Recommendations
```

This is useful for predictable responses.

---

# 3.6 Domain Restrictions

This is particularly useful for your project.

Suppose:

```text
Goal = Backend Developer
```

You can say:

```text
Only recommend technologies and skills relevant
to backend software development.
```

Then the AI should focus on things such as:

```text
Java
Spring Boot
REST APIs
SQL
Databases
Security
Docker
System Design
Cloud
```

rather than randomly suggesting:

```text
Photoshop
Video Editing
Graphic Design
```

---

# 4. Let's Build a Strong Prompt

Here's a prompt combining multiple constraints:

```text
You are an experienced backend career advisor.

Analyze this student's current skills and career goal.

Student:
Skills: Java, SQL, Spring Boot
Experience: Beginner
Goal: Backend Developer

Identify the most important skills the student should learn next.

Constraints:
- Return exactly 5 skills.
- Only recommend backend-development skills.
- Do not recommend frontend technologies.
- Order the skills from most important to least important.
- Keep each recommendation concise.
- Do not include explanations outside the required output.
```

Now we've significantly reduced the AI's freedom.

---

# 5. Constraints in Your Student Evaluator

Let's say your project needs this:

```text
Score
Level
Skill gaps
Recommendations
```

We could write:

```text
Evaluate the student.

Constraints:
- Score must be between 0 and 100.
- Level must be one of: Poor, Average, Good, Excellent.
- Return exactly 3 skill gaps.
- Return exactly 3 recommendations.
- Recommendations must be relevant to backend development.
- Do not recommend frontend technologies.
- Keep each recommendation below 25 words.
```

That's much stronger than simply saying:

```text
Evaluate the student.
```

---

# 6. An Important Concept: Constraints vs Validation

This distinction is **very important** for your upcoming structured-output topics.

Suppose we say:

```text
Score must be between 0 and 100.
```

That's a **prompt constraint**.

We're telling the AI what we expect.

But the AI might still return:

```json
{
  "score": 150
}
```

😅

Therefore:

### Prompt constraint

```text
AI, please follow this rule.
```

### Validation

```text
Backend, verify that the AI actually followed this rule.
```

This gives us:

```text
Prompt Constraint
       ↓
       AI
       ↓
AI Output
       ↓
Validation
       ↓
Accept / Reject / Retry
```

This distinction will become **extremely important** when we reach:

### 2.12 Structured Output Validation

---

# 7. Spring AI Example

At this stage, we're still using normal text output:

```java
String result = chatClient
        .prompt()
        .user("""
            You are an experienced backend career advisor.

            Student:
            Skills: Java, SQL, Spring Boot
            Experience: Beginner
            Goal: Backend Developer

            Identify the skills the student should learn next.

            Constraints:
            - Return exactly 5 skills.
            - Only recommend backend-development skills.
            - Do not recommend frontend technologies.
            - Order the skills from most important to least important.
            - Keep the response concise.
            """)
        .call()
        .content();
```

Later, we'll replace the raw `String` with something like:

```java
CareerRecommendation result = chatClient
        .prompt()
        .user(...)
        .call()
        .entity(CareerRecommendation.class);
```

And that's where **structured output** becomes powerful.

---

# 8. Combining Everything We've Learned

Look at how far we've come.

We can now construct a prompt like this:

```text
ROLE
↓
You are an experienced backend career advisor.

CONTEXT
↓
Student knows Java, SQL and Spring Boot.
Experience: Beginner.
Goal: Backend Developer.

TASK
↓
Identify the student's skill gaps.

REASONING INSTRUCTION
↓
Carefully analyze the student's current skills
against the requirements of backend development.

CONSTRAINTS
↓
- Exactly 3 skill gaps
- Backend skills only
- No frontend technologies
- Prioritize most important gaps

OUTPUT
↓
Return the required result.
```

This is **much closer to a production-quality AI prompt**.

---

# 9. A Useful Rule for You

When designing prompts, ask yourself:

> **"What freedom does the AI have that my application doesn't want it to have?"**

Then turn that into a constraint.

For example:

### Problem

AI gives 10 recommendations.

### Constraint

```text
Return exactly 3 recommendations.
```

### Problem

AI recommends React for a backend roadmap.

### Constraint

```text
Only recommend backend-development skills.
```

### Problem

AI returns different labels.

### Constraint

```text
Level must be one of:
Poor, Average, Good, Excellent.
```

### Problem

AI gives huge explanations.

### Constraint

```text
Keep each recommendation under 30 words.
```

This mindset will serve you **very well** when building your actual platform.

---


