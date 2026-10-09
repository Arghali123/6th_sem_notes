# 🚀 2.12 — Structured Output Validation

Now let's learn a critical production skill: How do we make sure AI returns acceptable data?

## 1. The problem

Imagine your Career Management Platform asks Gemini to evaluate a student.

Your Java record expects:

Java

```
public record StudentEvaluation(
    String level,
    int score,
    List<String> skillGaps
) {}
```

Your application expects a score between `0` and `100` and exactly three skill gaps.

But the AI might return:

JSON

```
{
  "level": "EXCELLENT",
  "score": 150,
  "skillGaps": [
    "Spring Security"
  ]
}
```

The response is valid JSON, but the data violates our requirements!

There are three different things to distinguish:

* Parsing: Can the response be converted into a Java object?

* Validation: Does the object satisfy our rules?

* Business logic: Is the result appropriate for our application?

These are not the same thing.

## 2. Validate a record using Jakarta Bean Validation

Spring Boot supports Jakarta Bean Validation, which lets us define rules using annotations.

Java

```
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import java.util.List;

public record StudentEvaluation(

    @NotBlank
    String level,

    @Min(0)
    @Max(100)
    int score,

    @NotEmpty
    @Size(min = 3, max = 3)
    List<@NotBlank String> skillGaps

) {}
```

Let's understand the annotations:

| Annotation            | Purpose                                          |
| --------------------- | ------------------------------------------------ |
| `@NotBlank`           | Text must not be null, empty, or whitespace-only |
| `@Min(0)`             | Score must be at least 0                         |
| `@Max(100)`           | Score must not exceed 100                        |
| `@NotEmpty`           | List must not be empty                           |
| `@Size(min=3, max=3)` | List must contain exactly 3 elements             |

Notice that `@Size` checks the number of items, not whether the items are useful or correct.

## 3. How do we trigger validation?

Adding annotations defines the rules, but doesn't automatically guarantee validation every time a record is created.

For example, use a `Validator` to check the AI result:

Java

```
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import java.util.Set;

public class ValidationExample {

    public static void main(String[] args) {

        StudentEvaluation evaluation =
            new StudentEvaluation(
                "EXCELLENT",
                150,
                List.of("Spring Security")
            );

        try (ValidatorFactory factory =
                 Validation.buildDefaultValidatorFactory()) {

            Validator validator = factory.getValidator();

            Set<ConstraintViolation<StudentEvaluation>> errors =
                validator.validate(evaluation);

            if (errors.isEmpty()) {
                System.out.println("Evaluation is valid.");
            } else {
                errors.forEach(error ->
                    System.out.println(
                        error.getPropertyPath() + ": "
                            + error.getMessage()
                    )
                );
            }
        }
    }
}
```

This example requires a Jakarta Bean Validation implementation, such as Hibernate Validator, on the classpath. In a Spring Boot project, `spring-boot-starter-validation` is the usual dependency.

The invalid score and one-item list will be rejected by validation. The exact error messages depend on the validation provider.

## 4. The production flow

For your project, think of the process like this:

Gemini returns a response

Spring AI converts it to `StudentEvaluation`

Validate the object

Valid

Continue to business logic

Invalid

Handle the error or retry safely

One important detail: if validation fails, don't blindly retry forever or save the invalid result. Handle failures explicitly and enforce critical business rules in your backend.


