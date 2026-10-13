# Phase 3 — Topic 3.3: Types of Conversation Memory

## 1. What are the types of conversation memory?

There isn't just one way to implement AI memory. Different approaches retain different kinds of information and have different storage requirements.

First, understand this important distinction: some memory approaches describe what information is retained, while others describe where it is stored. These categories overlap, so they aren't all mutually exclusive alternatives.

We'll learn five useful approaches:


1\. **Short-term or conversational memory**

Keeps recent messages or the current conversation available for follow-up questions.

Example: Remembering what the student asked a few messages ago.


2\. **Persistent memory**

Stores conversation data beyond the lifetime of a request or application process, often in a database.

Example: Reopening a student's previous conversation tomorrow.


3\. **Summary-based memory**

Keeps a concise summary of important information instead of relying exclusively on every message.

Example: Remembering that a student knows Java and SQL and aims to become a backend developer.


4\. **Semantic or retrieval-based memory**

Retrieves relevant information from stored data based on the meaning of a new question, often using embeddings and a vector database.

Example: Finding an older discussion about the student's interest in cloud computing.


5\. **Profile or fact-based memory**

Stores selected facts or preferences in a structured form instead of retaining only raw messages.

Example: A student's target role, current skills, preferred learning pace, and career goals.

These five approaches are useful conceptual categories for designing AI applications. They are not five interchangeable Spring AI classes. We'll learn the actual Spring AI APIs and their supported implementations in the following topics.

## 2. Understand each approach with a practical example

Let's use the same student throughout the examples.

### A. Short-term memory

The student says:

1. "I know Java and SQL."
2. "I want to become a backend developer."
3. "What should I learn next?"

Short-term memory makes the recent messages available when answering the third question.

A common implementation keeps a bounded window of recent messages. In Spring AI, we'll later explore `MessageWindowChatMemory`, which manages a limited message history.

Limitation: If older messages fall outside the retained window, their details may no longer be available unless they're stored or summarized elsewhere.

### B. Persistent memory

Suppose the student closes the browser and returns the next day.

If the application has saved their conversation history to PostgreSQL, it can retrieve the previous messages and continue the discussion.

```
Student sends message
       |
       v
Spring Boot + Spring AI
       |
       v
Save conversation messages
       |
       v
PostgreSQL
       |
       v
Retrieve history for a later request
```

Persistent storage helps the application survive restarts, but saving messages alone does not automatically make them available to Gemini. The application must retrieve and supply the relevant context.

We'll implement database-backed history later in Topic 3.9.

### C. Summary-based memory

Imagine a student has exchanged 100 messages with the career assistant.

Sending all 100 messages on every request could consume unnecessary tokens. Instead, the application could maintain a summary:

```
Student profile summary:
- Knows Java and SQL.
- Learning Spring Boot.
- Wants to become a backend developer.
- Prefers practical projects and concise explanations.
- Has not yet learned Docker.
```

When the student asks, "What should I learn next?", the application can provide this summary alongside the latest messages.

Limitation: Summaries can omit details or introduce inaccuracies, so important facts should be validated and updated carefully.

### D. Semantic or retrieval-based memory

Suppose the student discussed cloud computing 50 messages ago but is now asking:

> How can I deploy my career platform?

A retrieval-based system can search stored conversations for semantically relevant information—even if the original discussion is not among the most recent messages.

This commonly involves:

1. Converting text into embeddings.
2. Storing the embeddings and associated information.
3. Searching for relevant information using a new query.
4. Supplying the retrieved results to the model.

This is closely related to your upcoming Embeddings, Vector Databases, and RAG phases.

One caution: vector retrieval is not a perfect replacement for chronological chat history. It may retrieve relevant passages but miss important conversational details.

### E. Profile or fact-based memory

Instead of searching the entire conversation, the application stores structured facts:

```
{
  "targetRole": "Backend Developer",
  "knownSkills": ["Java", "SQL"],
  "learningSpringBoot": true,
  "preferredLearningStyle": "Practical examples"
}
```

Your backend could store this information in PostgreSQL and retrieve it when generating career recommendations.

This is especially useful when facts need to be queried, updated, validated, or used by other application features.

For example, your platform could compare a student's existing skills against the skills required for a target job and generate a personalized roadmap.

Important: These facts should be saved with appropriate user consent, access controls, and validation. The model should not be allowed to silently overwrite authoritative student records with unverified claims.

## 3. Compare the five approaches

| Approach           | Best suited for                    | Main limitation                |
| ------------------ | ---------------------------------- | ------------------------------ |
| Short-term         | Immediate follow-up questions      | Older context may be lost      |
| Persistent         | Resuming conversations later       | Requires storage and retrieval |
| Summary-based      | Long conversations                 | Some details may be omitted    |
| Semantic retrieval | Finding relevant older information | Retrieval may miss context     |
| Profile/fact-based | Personalized recommendations       | Facts can become outdated      |

Notice that these approaches can work together.

For example, a production career assistant might use:

- A recent-message window for natural conversation.
- PostgreSQL for persistent chat history.
- A summary for long-running conversations.
- A vector database for searching older discussions.
- Structured student records for verified skills and career goals.

This combination is often more useful than trying to make one memory mechanism handle every problem.

## 4. How does this connect to Spring AI?

Spring AI provides APIs and advisors that help you implement conversation memory and make contextual information available to the model.

Here are the concepts we'll encounter in this phase and the upcoming phases:

`ChatMemory`

The abstraction for managing conversation messages. We'll study it in Topic 3.4.

`MessageWindowChatMemory`

Maintains a bounded window of messages, helping control how much conversational history is retained.

PostgreSQL and persistent storage

Provides durable storage for conversation history or structured student facts, depending on your design.

Embeddings and vector retrieval

Help locate semantically relevant information from larger collections of stored data.

A key distinction: `ChatMemory` is a Spring AI abstraction, while persistent storage, summarization, and semantic retrieval are broader architectural choices. You shouldn't assume that implementing `ChatMemory` automatically gives you all five approaches.

## 5. Apply the concepts to your career platform

Let's test a practical design decision.

A student uses your application for several weeks. They want the assistant to remember their career goal, resume a conversation after logging in again, and find an older discussion about deploying a project.

A reasonable design would be:

- Recent messages: Use conversational memory for immediate context.
- Long-term history: Store messages in PostgreSQL.
- Career goal and verified skills: Maintain structured student records.
- Older deployment discussion: Retrieve relevant messages using search or semantic retrieval when appropriate.

This separates the responsibilities instead of treating all stored information as one giant chat prompt.
