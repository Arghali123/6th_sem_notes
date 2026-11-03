# Phase 4 — Topic 4.3: Embedding Models

## 1. What is an Embedding Model?

An Embedding Model is a machine learning model that converts input data, such as text, into numerical vectors called embeddings.

Remember our previous example:

Input:

```
Java is useful for backend development.
```

An embedding model processes this text and produces something like:

```
float[] embedding = {
    0.21f,
    -0.45f,
    0.78f,
    0.12f,
    -0.33f
};
```

This is an illustrative five-dimensional vector.

The embedding model performs the conversion automatically. We don't manually decide which number represents Java, backend, or development.

### Why do we need a separate embedding model?

You might be thinking:

"We already have Gemini for generating AI responses. Why do we need another model?"

Good question!

Because generating a response and generating an embedding are two different tasks.

Consider your Career Management Platform.

A student asks:

```
What should I learn to become a backend developer?
```

Your application needs two different capabilities:

1. Find relevant career documents.
2. Generate a useful answer using those documents.

An embedding model helps with the first task, while a chat model handles the second.

## 2. Chat Model vs Embedding Model — Detailed Comparison

Let's compare them using your existing Spring AI knowledge.

| Feature      | Chat Model                | Embedding Model        |
| ------------ | ------------------------- | ---------------------- |
| Main purpose | Generate responses        | Generate vectors       |
| Input        | Prompt/messages           | Text or supported data |
| Output       | Natural-language response | Numerical vector       |
| Primary use  | Conversation              | Semantic search        |
| Example      | Gemini chat model         | Gemini embedding model |

### Example from your Career Management Platform

Suppose a student asks:

"What skills do I need to become a Java backend developer?"

Chat Model:

```
To become a Java backend developer, you should
learn Java, Spring Boot, REST APIs, SQL,
PostgreSQL, and basic system design.
```

Embedding Model:

```
[0.21, -0.45, 0.78, 0.12, -0.33, ...]
```

The embedding model does not answer the student's question.

It generates a representation that can be compared with your stored career documents.

### Can we use a Chat Model as an Embedding Model?

Not simply by changing a method call.

A chat model and embedding model have different model capabilities and API operations.

You need a model that specifically supports embedding generation.

For example, having a Gemini chat model configured in Spring AI does not automatically mean that you have an embedding model configured.

This distinction will become important when we configure your Maven dependencies in Topic 4.4.

## 3. How do Embedding Models generate vectors?

Let's understand what happens internally at a high level.

Suppose we provide:

```
Spring Boot is used to build Java backend applications.
```

The embedding model performs the following operations:

1\. Input text

Spring Boot is used to build Java backend applications.

2\. Tokenization and numerical encoding

Text is divided into tokens and converted into numerical representations.

3\. Neural network processing

The model processes the input using learned parameters.

4\. Embedding generation

[0.21, -0.45, 0.78, ...]

The embedding model has already learned patterns from its training data.

When we provide new text, it uses those learned patterns to generate a numerical representation.

We don't train the model ourselves when using a pretrained embedding API.

We simply send input text and receive the generated vector.

## 4. Different Embedding Model Providers

Now let's explore the major providers available in the AI ecosystem.

An embedding model provider is a company or platform that makes embedding models available through APIs or downloadable models.


**1\. Google Gemini**

Provides embedding models such as `gemini-embedding-001` and `gemini-embedding-2`.

Useful for semantic search, multilingual retrieval, and multimodal applications.

Our planned provider for this learning journey.



**2\. OpenAI**

Provides embedding models such as `text-embedding-3-small` and `text-embedding-3-large`.

Useful for semantic search, classification, and document retrieval.


**3\. Cohere**

Provides Embed models, including multilingual embedding capabilities.

Useful for enterprise search and retrieval applications.


**4\. Hugging Face**

Provides access to many open-source embedding models.

Useful when developers want more control over model selection and deployment.


5\. Voyage AI

Specializes in embedding models for retrieval, including technical and code-related search.


### Important observation

Different providers can generate embeddings for the same text, but their vectors are not necessarily compatible.

For example:

- Gemini generates a vector in its learned embedding space.
- OpenAI generates a vector in its own learned embedding space.
- Cohere generates a vector in its own learned embedding space.

Even if two models produce 768-dimensional vectors, that doesn't mean their vectors can be compared directly.

Same dimension does not mean same embedding space.

This is an important production concept.

## 5. How do we choose an Embedding Model?

You shouldn't select an embedding model just because it is popular.

You should consider your application's requirements.

Here are the main factors.

| Factor           | What should you consider?                   |
| ---------------- | ------------------------------------------- |
| Accuracy         | Does it retrieve relevant documents?        |
| Dimensions       | How much storage will vectors require?      |
| Cost             | How much does generating embeddings cost?   |
| Speed            | How quickly can it generate vectors?        |
| Language support | Does it understand your required languages? |
| Token limit      | How much text can it process per request?   |
| Integration      | Does Spring AI support it?                  |
| Privacy          | Where is your data processed and stored?    |

### Example

Imagine two embedding models:

| Feature            | Model A        | Model B        |
| ------------------ | -------------- | -------------- |
| Dimensions         | 768            | 1536           |
| Cost               | Lower          | Higher         |
| Storage            | Lower          | Higher         |
| Retrieval accuracy | Must be tested | Must be tested |

Which one is better?

We cannot decide based on dimensions alone.

We should test both using our actual documents and queries.

A smaller vector can be sufficient for your application, while a larger vector might provide better retrieval quality in another application.

Remember: More dimensions do not automatically mean better search results.

## 6. Understanding Gemini Embedding Models

Now let's focus on Google Gemini because that's the provider you've been using throughout your Spring AI journey.

Google provides dedicated embedding models.

Two important models in its current Gemini API documentation are:

Gemini Embedding 001

Text embedding

# gemini-embedding-001

- Text-only embedding model.
- Maximum input: 2,048 tokens.
- Configurable output dimensions: 128–3072.
- Recommended dimensions: 768, 1536, or 3072.

Gemini Embedding 2

Multimodal

# gemini-embedding-2

- Supports text, images, audio, video, and PDF documents.
- Maximum text input: 8,192 tokens.
- Configurable output dimensions: 128–3072.
- Recommended dimensions: 768, 1536, or 3072.
- Supports multilingual and cross-modal retrieval.


### What does multimodal mean?

Multimodal means working with different types of information.

For example:

- Text: Java developer roadmap.
- Image: A diagram of Spring Boot architecture.
- Audio: A recorded technical interview.
- Video: A programming tutorial.
- PDF: A backend development course document.

Gemini Embedding 2 can map these supported modalities into a unified embedding space. This allows cross-modal search.

For example, a user could search using text and retrieve a relevant image or document.

That's a powerful capability for future AI applications!

### What about embedding dimensions?

Google allows you to configure the output dimensions.

For example:

```
Model: gemini-embedding-2

Output dimensions: 768
```

The generated vector will contain 768 numerical values.

Google uses a technique called Matryoshka Representation Learning (MRL) to make shorter prefixes of its embeddings useful representations too.

This allows smaller vectors to save storage and computational resources, with some possible quality trade-offs.

Google recommends 768, 1536, and 3072 dimensions for these models.



## 7. Which Gemini Model Should We Use for Your Career Platform?

Let's make an engineering decision.

Your platform currently needs to:

1. Store Java and Spring Boot learning documents.
2. Store backend development roadmaps.
3. Retrieve relevant career information.
4. Search using natural-language questions.
5. Store embeddings in PostgreSQL pgvector.

For these requirements, a text embedding model is sufficient.

You don't currently need image, audio, or video embeddings.

My recommendation

# Gemini text embeddings

Start with a text-only embedding model and evaluate retrieval quality using your actual career documents.

Why?

- Your current data is primarily text.
- Your initial application needs semantic search.
- Text embeddings are sufficient for this functionality.
- You can investigate multimodal capabilities later.

### One important Spring AI compatibility detail

Here's something I want you to understand before we start coding.

Your project uses:

- Spring Boot 4.1.1
- Spring AI 2.0.1
- Java 21
- Gemini Developer API

Spring AI provides a dedicated Google GenAI embedding integration.

However, there is an important distinction:

Google's Gemini API documentation currently lists `gemini-embedding-001` and `gemini-embedding-2`, while Spring AI's published Google GenAI embedding reference lists older model names such as `text-embedding-004` and `text-multilingual-embedding-002`.

Therefore, we must not assume that every newly released Gemini embedding model is automatically supported by your Spring AI integration.





Another critical rule: embeddings from `gemini-embedding-001` and `gemini-embedding-2` belong to incompatible embedding spaces. If you migrate between them, you must regenerate the embeddings for your stored documents



That's why choosing an embedding model is an architectural decision, not just a configuration change.

# 8. Topic 4.3 — Quick Revision

Let's summarize the most important concepts.

| Concept             | Remember this                                         |
| ------------------- | ----------------------------------------------------- |
| Embedding Model     | Converts input into numerical vectors                 |
| Chat Model          | Generates natural-language responses                  |
| Provider            | Company/platform supplying the model                  |
| Dimensions          | Number of values in an embedding                      |
| Multimodal          | Supports different data types                         |
| Embedding space     | Mathematical representation learned by a model        |
| Model compatibility | Use compatible models for stored and query embeddings |

The key takeaway:

An embedding model is a specialized AI model designed to convert information into numerical vectors. Selecting the right model affects search quality, storage, performance, cost, and future scalability.
