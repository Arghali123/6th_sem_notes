# 🚀 2.11 Structured Output with Lists and Generic Types[Part 1]

So far, we've converted AI output into **one Java object**:

```java
CareerEvaluation
```

But real applications often need something more complex:

* A list of careers
* A list of skill gaps
* A list of recommendations
* A list of Java objects
* Nested objects containing lists

That's what we're learning now.

---

## 1. Simple List Output

Suppose we ask AI:

> Give me 5 backend technologies a beginner should learn.

AI might return:

```json
[
  "Java",
  "Spring Boot",
  "Spring Security",
  "Docker",
  "PostgreSQL"
]
```

We want:

```java
List<String>
```

Conceptually:

```text
AI
 ↓
["Java", "Spring Boot", "Docker"]
 ↓
List<String>
```

---

# 2. Why `List.class` Isn't Enough

You might initially think:

```java
.entity(List.class)
```

But there's a problem.

Java's generic information is not preserved at runtime in the same simple way because of **type erasure**.

For example:

```java
List<String>
List<Integer>
List<Career>
```

all become essentially:

```java
List
```

at runtime.

So Spring AI needs more information about the generic type.

That's where **ParameterizedTypeReference** comes in.

---

# 3. `ParameterizedTypeReference`

For example:

```java
ParameterizedTypeReference<List<String>> type =
        new ParameterizedTypeReference<>() {};
```

Conceptually, this tells Spring:

> "I don't just want a List. I want a List of Strings."

Then we can use the type when requesting structured output.

The important concept is:

```text
List.class
    ↓
"I need a List"

ParameterizedTypeReference<List<String>>
    ↓
"I need a List<String>"
```

---

# 4. List of Java Objects

Now let's make this more realistic.

Suppose AI returns:

```json
[
  {
    "name": "Spring Security",
    "priority": "HIGH"
  },
  {
    "name": "Docker",
    "priority": "MEDIUM"
  },
  {
    "name": "Microservices",
    "priority": "LOW"
  }
]
```

We can represent one item with:

```java
public record SkillGap(
        String name,
        String priority
) {
}
```

But our final result is:

```java
List<SkillGap>
```

So conceptually:

```text
AI
 ↓
JSON array
 ↓
List<SkillGap>
```

---

# 5. Why Generic Types Matter

Consider your Career Management Platform.

You might ask AI:

> Identify the top 3 skill gaps for this student.

Instead of returning one object containing a list:

```json
{
  "skillGaps": [
    {
      "name": "Spring Security",
      "priority": "HIGH"
    },
    {
      "name": "Docker",
      "priority": "MEDIUM"
    },
    {
      "name": "Microservices",
      "priority": "LOW"
    }
  ]
}
```

you could ask for a direct list:

```json
[
  {
    "name": "Spring Security",
    "priority": "HIGH"
  },
  {
    "name": "Docker",
    "priority": "MEDIUM"
  },
  {
    "name": "Microservices",
    "priority": "LOW"
  }
]
```

Then Java needs to understand:

```java
List<SkillGap>
```

not merely:

```java
List
```

---

# 6. Nested Structured Output

Here's where this becomes even more useful.

Suppose your AI returns:

```json
{
  "career": "Backend Developer",
  "skillGaps": [
    {
      "skill": "Spring Security",
      "priority": "HIGH"
    },
    {
      "skill": "Docker",
      "priority": "MEDIUM"
    }
  ],
  "recommendations": [
    "Build a secure REST API",
    "Containerize a Spring Boot application"
  ]
}
```

Java:

```java
public record SkillGap(
        String skill,
        String priority
) {
}
```

and:

```java
public record CareerEvaluation(
        String career,
        List<SkillGap> skillGaps,
        List<String> recommendations
) {
}
```

Now we have:

```text
CareerEvaluation
 ├── career
 ├── List<SkillGap>
 │      ├── SkillGap
 │      └── SkillGap
 │
 └── List<String>
```

This is a **nested structured output**.

---

# 7. Why This Matters in Your Project

Your project isn't going to return simple AI text.

You will probably eventually have something like:

```java
public record CareerEvaluation(
        String career,
        int score,
        String level,
        List<SkillGap> skillGaps,
        List<Recommendation> recommendations
) {
}
```

with:

```java
public record SkillGap(
        String skill,
        String priority
) {
}
```

and:

```java
public record Recommendation(
        String skill,
        String reason,
        String action
) {
}
```

That's a proper structured AI response.

The architecture becomes:

```text
                 Gemini
                   ↓
            Structured JSON
                   ↓
          CareerEvaluation
             /          \
            ↓            ↓
      List<SkillGap>   List<Recommendation>
            ↓            ↓
         Backend Business Logic
                   ↓
             React / Database
```

🔥 Now we're building something that looks like an actual production AI backend.

---

# 8. Important Generic Types to Recognize

You'll commonly encounter:

```java
List<String>
```

A list of strings.

```java
List<SkillGap>
```

A list of objects.

```java
List<CareerEvaluation>
```

A list of evaluations.

And sometimes:

```java
Map<String, String>
```

or:

```java
Map<String, List<String>>
```

These are all **generic types**.

---

# 🧠 The Main Concept

Don't get too stuck on the Java generics details yet.

The important progression is:

```text
2.9
Java Record
     ↓
CareerEvaluation

2.10
.entity()
     ↓
AI → CareerEvaluation

2.11
Generic Types
     ↓
AI → List<SkillGap>
     ↓
AI → CareerEvaluation containing Lists
```

So we're gradually increasing the complexity of our AI responses.

---

# ⚠️ One Important Spring AI Detail

The exact APIs for handling generic structured output can vary with the **Spring AI version and model/provider** you're using.

So when we implement this in your actual Spring AI project, we'll use the current API supported by your version rather than memorizing an outdated snippet.

For now, focus on the **concept**:

> When the target is a generic type such as `List<SkillGap>`, Spring needs the complete generic type information, not just `List.class`.

---



# 2.11 Structured Output with Lists and Generic Types[Part 2]

## 1. First, understand the problem

Suppose your Career Management Platform asks AI to recommend three backend skills.

AI returns:

JSON

```
[
  "Spring Security",
  "Docker",
  "Microservices"
]
```

How should Java store this data?

Java

```
List<String> skills = List.of(
    "Spring Security",
    "Docker",
    "Microservices"
);
```

Here, `List<String>` means a list containing String values.

Other examples:

| Java type        | Meaning                     |
| ---------------- | --------------------------- |
| `String`         | One text value              |
| `List<String>`   | A list of text values       |
| `SkillGap`       | One skill-gap object        |
| `List<SkillGap>` | A list of skill-gap objects |

## 2. What if AI returns objects instead of strings?

Suppose AI returns:

JSON

```
[
  {
    "skill": "Spring Security",
    "priority": "HIGH"
  },
  {
    "skill": "Docker",
    "priority": "MEDIUM"
  },
  {
    "skill": "Microservices",
    "priority": "LOW"
  }
]
```

First, define the structure of one item:

Java

```
public record SkillGap(
    String skill,
    String priority
) {}
```

One `SkillGap` represents one skill and its priority.

For example:

Java

```
SkillGap gap = new SkillGap(
    "Spring Security",
    "HIGH"
);

System.out.println(gap.skill());    // Spring Security
System.out.println(gap.priority()); // HIGH
```

But the AI returned three objects, so we need a list:

Java

```
List<SkillGap> gaps;
```

Think of it this way:

List of SkillGap

SkillGap 1

Spring Security

HIGH

SkillGap 2

Docker

MEDIUM

SkillGap 3

Microservices

LOW

## 3. How does this relate to `.entity()`?

You already learned this in topic 2.10:

Java

```
.entity(CareerEvaluation.class)
```

It requests conversion into a single `CareerEvaluation` object.

Now imagine the AI returns a list of `SkillGap` objects. We want the target type to be:

Java

```
List<SkillGap>
```

We cannot simply use `List.class` when we need Spring AI to know the element type. Java's generic type erasure makes that information unavailable through an ordinary `List.class` token.

Spring AI supports `ParameterizedTypeReference` to represent generic types. For example:

Java

```
import java.util.List;
import org.springframework.core.ParameterizedTypeReference;

List<SkillGap> gaps = chatClient.prompt()
    .user("""
        Return three backend skill gaps as a JSON array.
        Each item must contain "skill" and "priority".
        """)
    .call()
    .entity(new ParameterizedTypeReference<List<SkillGap>>() {});
```

The important part is:

Java

```
new ParameterizedTypeReference<List<SkillGap>>() {}
```

It tells Spring AI that we want a list of `SkillGap` objects, not just some unspecified list.

The exact behavior can depend on your Spring AI version and model provider, but this is the generic-type pattern to understand.

## 4. What about nested lists inside a record?

You can also keep a list inside a larger object.

Java

```
import java.util.List;

public record CareerEvaluation(
    String career,
    int score,
    List<SkillGap> skillGaps
) {}

public record SkillGap(
    String skill,
    String priority
) {}
```

Example response:

JSON

```
{
  "career": "Backend Developer",
  "score": 85,
  "skillGaps": [
    {
      "skill": "Spring Security",
      "priority": "HIGH"
    },
    {
      "skill": "Docker",
      "priority": "MEDIUM"
    }
  ]
}
```

You can then access the data:

Java

```
CareerEvaluation result = /* response converted by Spring AI */;

System.out.println(result.career());
System.out.println(result.skillGaps().get(0).skill());
```

Output:

```
Backend Developer
Spring Security
```

This structure is useful when your React frontend needs to display a student's score, skill gaps, and priorities separately.



