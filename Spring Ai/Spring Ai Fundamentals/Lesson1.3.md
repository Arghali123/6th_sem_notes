# 🟢 1.3 AI Models & Providers

This is an important concept because **Spring AI doesn't itself "think."** It connects your Spring Boot application to AI models provided by different companies or platforms.

---

## 1. What is an AI Model?

An **AI model** is the actual trained model that processes your input and generates an output.

For example:

```text
Prompt
   ↓
AI Model
   ↓
Response
```

If you send:

> "Explain polymorphism in Java."

The AI model processes the prompt and generates the explanation.

Different models can have different capabilities, speed, context sizes, costs, and supported features.

---

# 2. What is an AI Provider?

An **AI provider** is the company/platform that provides access to AI models.

Think of it this way:

```text
Provider = Company/Platform
Model    = AI model provided by that platform
```

For example:

| Provider  | Example models/platform |
| --------- | ----------------------- |
| OpenAI    | GPT models              |
| Google    | Gemini models           |
| Anthropic | Claude models           |
| Ollama    | Locally running models  |
| Azure     | Azure-hosted AI models  |

So:

```text
OpenAI
  ↓
GPT model
```

and:

```text
Google
  ↓
Gemini model
```

---

# 3. How Spring AI Fits In

Spring AI provides a common programming model for interacting with different AI providers.

The basic architecture is:

```text
                 Spring Boot
                     ↓
                 Spring AI
                     ↓
                ChatClient
                     ↓
                Chat Model
                     ↓
        ┌────────────┼────────────┐
        ↓            ↓            ↓
     OpenAI        Gemini       Ollama
        ↓            ↓            ↓
     AI Model      AI Model     Local Model
```

Your Java application can use Spring AI APIs instead of building all provider-specific communication yourself.

---

# 4. Major Types of AI Models

Spring AI isn't limited to just chat.

The major model categories you'll encounter are:

### 💬 1. Chat Models

Used for conversations and text generation.

```text
User Prompt
     ↓
Chat Model
     ↓
Text Response
```

We'll work with these first.

---

### 🔢 2. Embedding Models

Convert text into numerical vectors.

For example:

```text
"Java is a programming language"
             ↓
       Embedding Model
             ↓
[0.12, -0.43, 0.87, ...]
```

These are extremely important for **RAG and vector databases**.

We'll learn them later in Phase 4.

---

### 🖼️ 3. Image Models

Used for generating or processing images, depending on the provider/model capabilities.

Conceptually:

```text
Text Prompt
    ↓
Image Model
    ↓
Image
```

---

### 🎤 4. Audio Models

Used for tasks such as:

* Speech-to-text
* Text-to-speech
* Audio understanding

These become useful when building voice-enabled AI applications.

---

# 5. Cloud Models vs Local Models

This is an important distinction.

## ☁️ Cloud AI

The model runs on a provider's infrastructure.

```text
Your Application
       ↓
Internet
       ↓
AI Provider
       ↓
AI Model
       ↓
Response
```

Examples include cloud-hosted models from OpenAI, Google, and Anthropic.

### Advantages

* No powerful GPU required
* Easy to get started
* Access to large models
* Provider manages infrastructure

### Disadvantages

* Requires network access
* API costs may apply
* Data leaves your infrastructure

---

# 6. Local AI

With something like **Ollama**, you can run supported models on your own computer.

```text
Your Spring Boot App
        ↓
     Spring AI
        ↓
      Ollama
        ↓
   Local AI Model
```

The model runs locally rather than calling a cloud API.

### Advantages

* Can work without sending prompts to a cloud provider
* Useful for development and experimentation
* No per-request cloud API charge

### Disadvantages

* Requires sufficient hardware
* Large models can require substantial RAM/VRAM
* Performance depends on your machine

---

# 7. Why Spring AI Supports Multiple Providers

Imagine you initially build your application with:

```text
Spring Boot
    ↓
Spring AI
    ↓
OpenAI
```

Later you want to experiment with another provider:

```text
Spring Boot
    ↓
Spring AI
    ↓
Gemini
```

The Spring AI abstraction can reduce the amount of application-level code that needs to change.

However, **don't assume every provider is interchangeable**. Different models can support different capabilities, parameters, context limits, tool-calling behavior, and modalities.

---

# 8. Simple Real-World Example

Suppose we're building an AI customer-support application.

### Option A — Cloud model

```text
Customer
   ↓
Spring Boot
   ↓
Spring AI
   ↓
Cloud AI Provider
   ↓
AI Model
   ↓
Answer
```

### Option B — Local model

```text
Customer
   ↓
Spring Boot
   ↓
Spring AI
   ↓
Ollama
   ↓
Local AI Model
   ↓
Answer
```

The **application architecture remains conceptually similar**, while the model/provider configuration differs.

---

# 9. Important Terms to Remember

| Term                | Meaning                                                 |
| ------------------- | ------------------------------------------------------- |
| **AI Model**        | The trained model that processes/generates information  |
| **AI Provider**     | Platform/company providing access to models             |
| **Chat Model**      | Model interface used for conversational/text generation |
| **Embedding Model** | Converts data into vector representations               |
| **Cloud Model**     | Model accessed through a remote provider                |
| **Local Model**     | Model running on your own infrastructure                |

---

# 🧠 Easy Analogy

Think of a restaurant:

```text
Restaurant = AI Provider
Chef       = AI Model
Waiter     = Spring AI / ChatClient
Customer   = Your Application/User
```

You tell the waiter what you want:

> "Give me a pizza."

The waiter communicates with the chef and brings the result back.

Similarly:

```text
Your Application
       ↓
   ChatClient
       ↓
   Chat Model
       ↓
   AI Provider
       ↓
   AI Model
       ↓
    Response
```

---

# 🎯 What you need to remember

For now, remember these **three levels**:

```text
Provider
   ↓
Model
   ↓
Capability
```

For example:

```text
OpenAI
  ↓
A particular GPT model
  ↓
Text generation / tool calling / etc.
```

And Spring AI sits between your application and the model ecosystem:

```text
┌─────────────────────┐
│    Spring Boot      │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│      Spring AI      │
└──────────┬──────────┘
           ↓
┌─────────────────────┐
│     Chat Model      │
└──────────┬──────────┘
           ↓
     AI Provider
           ↓
       AI Model
```

