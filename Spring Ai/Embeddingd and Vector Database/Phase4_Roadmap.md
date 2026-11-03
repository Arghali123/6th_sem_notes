# Spring AI — Phase 4: Embeddings and Vector Databases

### Teaching Instructions

Act as my Spring AI teacher.

1. Teach one topic at a time, in a structured, beginner-friendly manner.
2. Explain what a concept is, why it exists, how it works internally, and where it is used in real-world applications.
3. Provide practical examples using Java and Spring AI.
4. Provide complete runnable code when implementing concepts.
5. Explain important methods and classes rather than just providing code.
6. Use IntelliJ IDEA for IDE instructions.
7. Give me checkpoints and quizzes after topics.
8. Grade my answers, explain mistakes, and track my progress.
9. Do not move to the next topic until I demonstrate understanding of the current topic.
10. Do not skip setup steps or assume I already know something.
11. Connect concepts to my AI Skill and Career Management Platform wherever relevant.

---

# Phase 4 — Embeddings and Vector Databases

## Part A: Understanding Embeddings

### 4.1 Introduction to Embeddings

- What are embeddings?
- Why do AI applications need embeddings?
- How are embeddings different from ordinary text?
- Real-world use cases of embeddings.
- Relationship between embeddings and LLMs.

### 4.2 How Embeddings Work

- How text is converted into numerical vectors.
- Understanding vectors and dimensions.
- What does an embedding vector represent?
- Why similar meanings produce similar vectors.
- Understanding text-to-vector conversion with examples.

### 4.3 Embedding Models

- What is an embedding model?
- Difference between chat models and embedding models.
- How embedding models generate vectors.
- Different embedding model providers.
- Choosing an embedding model.
- Understanding embedding dimensions.
- Introduction to Gemini embedding models and their suitability.

### 4.4 Generating Embeddings Using Spring AI

- Introduction to Spring AI's `EmbeddingModel`.
- Understanding the `embed()` method.
- Understanding `embedForResponse()`.
- Understanding `EmbeddingResponse`.
- Understanding `Embedding`.
- Generating embeddings from a single text.
- Generating embeddings from multiple texts.
- Building a simple REST API that generates embeddings.

### 4.5 Semantic Similarity

- What is semantic similarity?
- Difference between keyword matching and semantic matching.
- How computers compare two embedding vectors.
- Understanding similarity scores.
- Practical semantic similarity examples.

### 4.6 Vector Similarity Algorithms

- Cosine similarity.
- Dot product.
- Euclidean distance.
- When to use each similarity measure.
- Understanding similarity ranking with examples.

### 4.7 Practical Embedding Experiment

- Generate embeddings for different sentences.
- Compare similar and unrelated sentences.
- Observe similarity scores.
- Understand how semantic meaning affects vector similarity.

---

## Part B: Understanding Vector Databases

### 4.8 Introduction to Vector Databases

- What is a vector database?
- Why do we need vector databases?
- Difference between relational databases and vector databases.
- Difference between traditional SQL search and vector search.
- Real-world applications.

### 4.9 How Vector Databases Work

- Storing embedding vectors.
- Storing original text and metadata.
- Understanding vector collections/tables.
- Similarity search.
- Top-K nearest neighbor search.
- Metadata filtering.
- Understanding the vector retrieval process.

### 4.10 Vector Database Options

Understand the purpose and basic architecture of:

- PostgreSQL with pgvector.
- Chroma.
- Qdrant.
- Pinecone.
- Milvus.

Focus on understanding their differences, advantages, and use cases.

### 4.11 PostgreSQL and pgvector Setup

- What is pgvector?
- Why use pgvector with PostgreSQL?
- Installing/enabling the pgvector extension.
- Understanding the `vector` data type.
- Creating tables containing embedding columns.
- Storing vectors in PostgreSQL.
- Retrieving stored vectors.
- Performing similarity searches using SQL.

### 4.12 Similarity Search with pgvector

- Understanding nearest-neighbor search.
- Understanding distance operators.
- Cosine distance.
- Euclidean distance.
- Inner product.
- Ordering search results by similarity.
- Limiting results with Top-K.
- Filtering results using metadata.

---

## Part C: Spring AI Integration with Vector Databases

### 4.13 Spring AI Vector Store

- What is a Vector Store?
- Why does Spring AI provide the `VectorStore` abstraction?
- Understanding the `VectorStore` interface.
- Understanding `Document`.
- Understanding document metadata.
- Understanding document IDs.
- Understanding vector storage architecture.

### 4.14 Configure Spring AI with PostgreSQL pgvector

- Add the required Maven dependency.
- Configure PostgreSQL.
- Configure the embedding model.
- Configure the PostgreSQL vector store.
- Understand automatic schema initialization.
- Understand vector dimensions and database compatibility.
- Verify successful application startup.

### 4.15 Store Documents in a Vector Database

- Understanding Spring AI `Document`.
- Creating documents programmatically.
- Adding document metadata.
- Using `VectorStore.add()`.
- Storing multiple documents.
- Understanding how Spring AI generates and stores embeddings.

### 4.16 Search Documents Using Spring AI

- Understanding `SearchRequest`.
- Using `VectorStore.similaritySearch()`.
- Configuring Top-K results.
- Configuring similarity thresholds.
- Performing metadata-filtered searches.
- Understanding returned documents and similarity scores.

### 4.17 Build a Semantic Search REST API

- Create a Spring Boot REST endpoint.
- Accept a user query.
- Generate an embedding for the query.
- Search the PostgreSQL vector store.
- Return the most relevant documents.
- Test the endpoint using Postman.

---

## Part D: Advanced Concepts and Practical Understanding

### 4.18 Metadata Filtering

- What is metadata filtering?
- Why is metadata important?
- Filtering documents by category, user, skill, or date.
- Combining metadata filters with vector similarity search.

### 4.19 Chunking Fundamentals

- What is document chunking?
- Why can't we always embed an entire document?
- Fixed-size chunking.
- Overlapping chunks.
- Chunk size and overlap.
- How chunking affects retrieval quality.
- Introduction to Spring AI document readers and token-based splitting.

### 4.20 Vector Indexing

- Why do vector databases need indexes?
- Exact search versus approximate nearest-neighbor search.
- Introduction to HNSW.
- Introduction to IVFFlat.
- Performance versus search accuracy.
- When indexing becomes important.

### 4.21 Embedding Limitations and Best Practices

- Embedding dimensions and compatibility.
- Token limits.
- Cost and performance considerations.
- Choosing an appropriate embedding model.
- Updating and deleting stored embeddings.
- Handling duplicate documents.
- Data privacy and access control.

---

# Part E: Phase 4 Mini Project

## 4.22 Mini Project — AI Career Knowledge Search Engine

Build a small semantic-search application that will later become part of my AI Skill and Career Management Platform.

### Technology Stack

- Java 21
- Spring Boot
- Spring AI
- Gemini embedding model
- PostgreSQL
- pgvector
- Maven
- IntelliJ IDEA
- Postman

### Required Features

1. Create a REST API to add career-related documents.
2. Generate embeddings for uploaded text.
3. Store documents, metadata, and vectors in PostgreSQL.
4. Create an API to search career-related information using natural language.
5. Retrieve the most semantically similar documents.
6. Support metadata filtering.
7. Test semantic search with different queries.
8. Verify persistence after application restart.

### Suggested Document Examples

- Java developer skills.
- Spring Boot learning roadmaps.
- Backend developer career requirements.
- Database concepts.
- Internship preparation.
- Software engineering interview preparation.

### Mini Project Steps

1. Design the architecture.
2. Configure Spring AI and PostgreSQL pgvector.
3. Configure the embedding model.
4. Create document and metadata structures.
5. Implement document ingestion.
6. Generate and store embeddings.
7. Implement similarity search.
8. Implement metadata filtering.
9. Create REST APIs.
10. Test using Postman.
11. Verify persistence and retrieval accuracy.
12. Complete a final assessment.

---

## Phase 4 Final Assessment

After completing all topics and the mini-project, conduct:

- Conceptual questions.
- Multiple-choice questions.
- Java and Spring AI coding questions.
- Vector database and SQL exercises.
- Real-world scenario questions.
- Mini-project code review.

Grade my answers and update my Phase 4 progress.

## Important Learning Rule

Do not start Phase 5 — RAG yet.

Phase 4 must be completed first. I want to understand embeddings and vector databases properly because they are the foundation for learning Retrieval-Augmented Generation in Phase 5.

Start with:

