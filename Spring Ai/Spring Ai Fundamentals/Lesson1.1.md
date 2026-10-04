

# 🚀 Let's start: 1.1 What is Spring AI?

**Spring AI** is a Spring framework designed to make it easier to build **AI-powered applications using Java and Spring Boot**.

Instead of directly dealing with every AI provider's API differently, Spring AI provides common abstractions for things such as:

```text
Spring Boot Application
        ↓
     Spring AI
        ↓
 ┌──────┼─────────┐
 ↓      ↓         ↓
OpenAI Gemini   Ollama
```

So your application can interact with different AI models using Spring-style APIs.

### Example

Without Spring AI, you might have to learn the API and Java SDK of every AI provider separately.

With Spring AI, your application can work through Spring AI abstractions such as:

```java
ChatClient
```

and the underlying model/provider can be configured separately.

### 🧠 Main idea

Think of Spring AI as:

> **Spring Boot + AI application development**

It provides building blocks for:

* 💬 Chat
* 📝 Prompting
* 📄 Document processing
* 🔢 Embeddings
* 🗄️ Vector databases
* 🔍 RAG
* 🔧 Tool calling
* 🤖 Agents
* 🔌 MCP

We'll learn each of these later—**not yet**.

For now, our immediate goal is simply:

> **Understand how Spring AI connects a Spring Boot application to an AI model and generates a response.**


