## **1. Why is this error occurring?**

The most important line is:

```
ERROR: column cannot have more than 2000 dimensions for hnsw index
```

Your application is trying to create this index:

```
CREATE INDEX IF NOT EXISTS spring_ai_vector_index
ON public.vector_store
USING HNSW (embedding vector_cosine_ops);
```

The problem is that:

- Your embedding model generates vectors with more than 2,000 dimensions.
- PostgreSQL's pgvector HNSW index using the `vector` type supports a maximum of 2,000 dimensions.
- Spring AI attempts to create the index during application startup.
- PostgreSQL rejects the operation, so Spring Boot fails to start.

**Important distinction:** pgvector can store vectors with more dimensions than the HNSW index supports. The limitation here is specifically the HNSW index, not necessarily vector storage itself.

## **2. Recommended solution: Use 768-dimensional embeddings**

For our learning project, I recommend using **768 dimensions with Google's&#x20;**`text-embedding-004`**&#x20;model**.

Why?

- `text-embedding-004` supports 768-dimensional embeddings by default.
- 768 is below pgvector's 2,000-dimensional HNSW limit.
- We can continue using HNSW and cosine similarity.
- We don't need to change databases or install anything else.



### **Step 1 — Update&#x20;**`application.properties`

Open:

`src/main/resources/application.properties`

Add or update these properties:

```
# Google embedding model
spring.ai.google.genai.embedding.text.model=text-embedding-004

# Generate 768-dimensional embeddings
spring.ai.google.genai.embedding.text.dimensions=768

# PostgreSQL Vector Store
spring.ai.vectorstore.pgvector.dimensions=768
spring.ai.vectorstore.pgvector.initialize-schema=true
spring.ai.vectorstore.pgvector.distance-type=COSINE_DISTANCE
spring.ai.vectorstore.pgvector.index-type=HNSW
```

**Understand the two dimensions properties:**

| Property                                 | Responsibility                                                            |
| ---------------------------------------- | ------------------------------------------------------------------------- |
| `google.genai.embedding.text.dimensions` | Tells Google to generate 768-dimensional embeddings.                      |
| `vectorstore.pgvector.dimensions`        | Tells Spring AI to create a PostgreSQL vector column with 768 dimensions. |

Both must match. Otherwise, PostgreSQL and the embedding model will disagree about vector size. Spring AI 2.0.1 supports configuring the PgVectorStore dimensions.



## **3. Step 2 — Check your existing vector table**

Because your application attempted initialization before failing, PostgreSQL might already have created the `vector_store` table with the old dimensions.

In pgAdmin, execute:

```
SELECT
    format_type(a.atttypid, a.atttypmod) AS embedding_type
FROM pg_attribute a
JOIN pg_class c ON a.attrelid = c.oid
JOIN pg_namespace n ON c.relnamespace = n.oid
WHERE c.relname = 'vector_store'
  AND n.nspname = 'public'
  AND a.attname = 'embedding';
```

Possible result:

```
vector(3072)
```

or:

```
vector(768)
```

### **If the result is&#x20;**`vector(3072)`

Since this is our new learning table, first check whether it contains any documents:

```
SELECT COUNT(*) FROM public.vector_store;
```

If it is empty and contains no data you need, remove the incorrectly configured table:

```
DROP TABLE public.vector_store;
```

Don't drop it if it contains documents you want to keep. Those documents would need to be migrated or re-embedded.

If the table is already `vector(768)`, don't drop it.

## **4. Step 3 — Restart Spring Boot**

Return to IntelliJ IDEA.

1. Save `application.properties`.
2. Reload Maven only if you've changed dependencies.
3. Run your Spring Boot application again.

Spring AI should now initialize PgVectorStore using 768-dimensional embeddings and create the HNSW index successfully.

## **5. Why not simply remove HNSW?**

You might wonder why we don't change:

```
spring.ai.vectorstore.pgvector.index-type=NONE
```

That is another possible solution. It performs exact similarity searches without creating an approximate index. However, for our learning project, keeping HNSW with 768-dimensional embeddings is a better starting point.

