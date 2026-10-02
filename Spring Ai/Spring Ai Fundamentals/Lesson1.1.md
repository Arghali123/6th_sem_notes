## 🟢 Phase 1 — Spring AI Fundamentals

We'll cover these in order:

**1.1** What is Spring AI?
**1.2** Spring AI Architecture
**1.3** AI Models & Providers
**1.4** Setting up a Spring AI project
**1.5** `ChatClient`
**1.6** Prompts
**1.7** Prompt Templates
**1.8** Chat Model Configuration
**1.9** Handling AI Responses
**1.10** Streaming Responses
**1.11** Mini Project — AI Chat REST API

Since you already have good Spring Boot experience, **I won't waste time teaching Spring Boot basics again.** We'll focus on what's new in Spring AI.

### 🎯 How we'll study

For each topic I'll give you:

* **Concept** — what it is
* **Why it matters**
* **How it works**
* **Spring AI implementation**
* **Complete runnable code**
* **Simple example**
* **Important points**
* **Small practice task**

And after completing a group of topics, we'll do a **quick revision**, just like we've done with your other study topics.

---

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


