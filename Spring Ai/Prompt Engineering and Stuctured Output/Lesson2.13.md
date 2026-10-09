# 🚀 2.13 — Provider-Native Structured Output

So far, we've asked AI for structured data and converted the response into Java objects. Now let's learn how the AI provider itself can help enforce the desired output format.

## 1. The problem with prompt-only JSON

Imagine you send Gemini this prompt:

```
Return the result as valid JSON.

{
  "career": "Backend Developer",
  "score": 85
}
```

You're asking the model to return JSON, but a prompt instruction alone doesn't guarantee valid JSON every time.

The model might return:

```
Here is your result:
{
  "career": "Backend Developer",
  "score": "eighty-five"
}
```

Problems:

* Extra text appears outside the JSON.

* `score` is a string rather than a number.

* The response might not match your expected schema.

## 2. What is provider-native structured output?

Provider-native structured output means using structured-output capabilities provided by the AI provider or model API, rather than relying only on prompt instructions.

Depending on the provider and model, this may involve:

* JSON response modes

* Schema-constrained output

* Structured response formats

The exact guarantees vary. JSON mode may ensure valid JSON without guaranteeing that every required field or business rule is satisfied. Schema-constrained output can enforce more of the required structure when supported.

## 3. Three approaches compared

| Approach                          | How it works                                            | Main limitation                                |
| --------------------------------- | ------------------------------------------------------- | ---------------------------------------------- |
| Prompt-only JSON                  | Ask the model to return JSON                            | The model may ignore the instruction           |
| Spring AI conversion              | Convert the response into a Java type using `.entity()` | Conversion doesn't guarantee business validity |
| Provider-native structured output | Use supported model/API formatting constraints          | Support and guarantees vary by provider/model  |

These approaches can also work together. For example, you can use provider-native structured output, convert to a Java record, and validate the result.

## 4. How does this fit into Spring AI?

Your familiar code is:

Java

```
StudentEvaluation evaluation = chatClient.prompt()
    .user("Evaluate the student and return JSON.")
    .call()
    .entity(StudentEvaluation.class);
```

This requests conversion into a Java type. It does not, by itself, prove that provider-native schema enforcement is enabled.

Provider-native configuration depends on your Spring AI version and the specific model integration. Since you're learning with Gemini, we should use the Gemini integration's supported configuration rather than assume every provider uses the same API.

The architecture we're aiming for is:

Prompt + Expected Structure

Provider-Native Formatting Constraints (when supported)

AI Response → Java Record

Backend Validation → Business Logic

## 5. Why does this matter in your project?

Your AI Career Management Platform needs predictable data to display student scores, skill gaps, and career recommendations in React.

If your frontend expects:

JSON

```
{
  "career": "Backend Developer",
  "score": 85,
  "skillGaps": ["Spring Security", "Docker", "Testing"]
}
```

you want the model to produce the expected structure as reliably as possible. Native formatting constraints can help, but backend validation remains necessary.

The rule to remember:

* Prompt instructions tell the model what you want.

* Provider-native constraints can restrict the output format.

* Java validation checks whether the result meets your application's rules.


