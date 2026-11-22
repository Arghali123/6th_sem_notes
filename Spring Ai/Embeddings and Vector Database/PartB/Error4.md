I found the issue! Your PostgreSQL configuration is now working. The problem is that **Google discontinued&#x20;**`text-embedding-004`**&#x20;on January 14, 2026**.

Your application is still requesting that retired model, which is why Google's API returns HTTP 404.



We'll migrate to Google's replacement: `gemini-embedding-001`.

## **Step 1 — Change the embedding model**

Open `application.properties`.

Find:

```
spring.ai.google.genai.embedding.text.model=text-embedding-004
```

Replace it with:

```
spring.ai.google.genai.embedding.text.model=gemini-embedding-001
```
