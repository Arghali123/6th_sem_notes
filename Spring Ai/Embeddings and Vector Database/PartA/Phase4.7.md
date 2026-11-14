# Topic 4.7 — Practical Embedding Experiment

Today we're going to perform an actual experiment using Spring AI and your configured embedding model.

### 1. What are we building?
We'll create a REST API that accepts two sentences, generates their embeddings, and calculates their cosine similarity.

For example:

**Sentence A**
```
I want to become a Java backend developer.
```


**Sentence B**
```
I want to build server-side applications using Java.
```

Our application will:
1. Receive both sentences.
2. Generate an embedding for each sentence using EmbeddingModel.
3. Calculate cosine similarity between the vectors.
4. Return the similarity score.
Two input sentences


Spring AI EmbeddingModel
<img/>

### 2. Create the request DTO
Create `SimilarityRequest.java:`
```
public record SimilarityRequest(
        String textA,
        String textB
) {}
```

This DTO receives the two sentences from Postman.
### 3. Create the response DTO
Create `SimilarityResponse.java:`
```
public record SimilarityResponse(
        int dimensions,
        double similarityScore
) {}
```

We'll return the vector dimension and similarity score instead of printing hundreds or thousands of vector values.
### 4. Create the similarity service
Create `EmbeddingExperimentService.java:`
```
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.stereotype.Service;

@Service
public class EmbeddingExperimentService {

    private final EmbeddingModel embeddingModel;

    public EmbeddingExperimentService(EmbeddingModel embeddingModel) {
        this.embeddingModel = embeddingModel;
    }

    public SimilarityResponse compare(String textA, String textB) {

        // Generate embeddings
        float[] vectorA = embeddingModel.embed(textA);
        float[] vectorB = embeddingModel.embed(textB);

        // Calculate cosine similarity
        double similarity = cosineSimilarity(vectorA, vectorB);

        return new SimilarityResponse(
                vectorA.length,
                similarity
        );
    }

    private double cosineSimilarity(float[] a, float[] b) {

        if (a.length != b.length) {
            throw new IllegalArgumentException(
                    "Vectors must have the same dimensions"
            );
        }

        double dotProduct = 0;
        double magnitudeA = 0;
        double magnitudeB = 0;

        for (int i = 0; i < a.length; i++) {

            dotProduct += a[i] * b[i];

            magnitudeA += a[i] * a[i];

            magnitudeB += b[i] * b[i];
        }

        magnitudeA = Math.sqrt(magnitudeA);
        magnitudeB = Math.sqrt(magnitudeB);

        if (magnitudeA == 0 || magnitudeB == 0) {
            throw new IllegalArgumentException(
                    "Cannot compare a zero vector"
            );
        }

        return dotProduct / (magnitudeA * magnitudeB);
    }
}
```


**Understand the important part:**
```
float[] vectorA = embeddingModel.embed(textA);
float[] vectorB = embeddingModel.embed(textB);
```

We generate two separate embeddings.

Then:
```
dotProduct += a[i] * b[i];
```

Multiplies corresponding vector values and accumulates their products.
```
magnitudeA += a[i] * a[i];
magnitudeB += b[i] * b[i];
```

Calculates the squared magnitudes.

Finally:
return dotProduct / (magnitudeA * magnitudeB);


Returns the cosine similarity after dividing by the magnitudes. Notice that we take the square roots before this final calculation.
### 5. Create the REST controller
Create `EmbeddingExperimentController.java:`
```
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/embeddings")
public class EmbeddingExperimentController {

    private final EmbeddingExperimentService service;

    public EmbeddingExperimentController(
            EmbeddingExperimentService service) {
        this.service = service;
    }

    @PostMapping("/compare")
    public SimilarityResponse compare(
            @RequestBody SimilarityRequest request) {

        return service.compare(
                request.textA(),
                request.textB()
        );
    }
}
```

This endpoint delegates the embedding generation and comparison to the service.
### 6. Test using Postman
Start your application in IntelliJ IDEA.

Request
```
POST http://localhost:8080/api/embeddings/compare
```

Body → raw → JSON:
```
{
  "textA": "I want to become a Java backend developer.",
  "textB": "I want to build server-side applications using Java."
}
```

Expected response structure (illustrative values):
```
{
  "dimensions": 768,
  "similarityScore": 0.89
}
```

The actual score and dimension depend on your configured embedding model. The value 0.89 is only an example, not a promised result.

