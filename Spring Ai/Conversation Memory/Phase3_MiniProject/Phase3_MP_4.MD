# Step 4 — Manage Multiple Conversations and Conversation IDs

Our next goal is to make sure each chat has its own identity and that messages from different conversations never get mixed together.

## 4.1 Understand the design

Imagine one student has two conversations:

Conversation A — Backend Career

Conversation ID: a unique identifier

- “I'm learning Java.”
- “What should I learn next?”

Conversation B — Interview Preparation

A different conversation ID

- “Help me prepare for interviews.”
- “Give me a Java interview question.”

The rule is simple:

- Same conversation ID → retrieve that conversation's available history.
- Different conversation ID → separate conversation history.
- A UUID helps create unique identifiers, but it does not prove ownership or authorization. We'll implement ownership checks in Step 5.

## 4.2 Add an endpoint to create a conversation

Currently, you manually type `demo-001` into Postman. Let's generate conversation IDs automatically instead.

In IntelliJ IDEA, create this record inside `com.example.Phase2.dto`.

`CreateConversationResponse.java`

```
package com.example.Phase2.dto;

public record CreateConversationResponse(
        String conversationId
) {
}
```

Now update your existing `CareerChatController.java` to add a creation endpoint. Keep your existing message endpoint as it is.

```
package com.example.Phase2.controller;

import com.example.Phase2.dto.CareerChatRequest;
import com.example.Phase2.dto.CareerChatResponse;
import com.example.Phase2.dto.CreateConversationResponse;
import com.example.Phase2.service.CareerChatService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/conversations")
public class CareerChatController {

    private final CareerChatService careerChatService;

    public CareerChatController(CareerChatService careerChatService) {
        this.careerChatService = careerChatService;
    }

    @PostMapping
    public CreateConversationResponse createConversation() {
        String conversationId = UUID.randomUUID().toString();

        return new CreateConversationResponse(conversationId);
    }

    @PostMapping("/{conversationId}/messages")
    public CareerChatResponse sendMessage(
            @PathVariable String conversationId,
            @Valid @RequestBody CareerChatRequest request) {

        String reply = careerChatService.chat(
                conversationId,
                request.message()
        );

        return new CareerChatResponse(conversationId, reply);
    }
}
```

### What does `UUID.randomUUID()` do?

It generates a unique identifier, for example:

```
a3c9c1d2-7b14-4e6a-9f25-8d6e4c1a502b
```

Each call generates a new ID. In this step, the endpoint returns an ID but doesn't yet persist conversation metadata in a `Conversation` table. We'll add that in Step 5.

## 4.3 Test it in Postman

Restart the application from IntelliJ IDEA after the code changes.

### Test A — Create Conversation A

POST

`http://localhost:8080/api/conversations`

In Postman, choose Body → none, then click Send.

Expected response:

```
{
  "conversationId": "a3c9c1d2-7b14-4e6a-9f25-8d6e4c1a502b"
}
```

Your actual ID will be different. Copy it; we'll call it ID A.

### Test B — Send the first message

POST

`http://localhost:8080/api/conversations/{ID A}/messages`

Replace `{ID A}` with the actual ID returned by Postman.

Under Body → raw → JSON, enter:

```
{
  "message": "I'm learning Java and Spring Boot. I want to become a backend developer."
}
```

Click Send and inspect the assistant's reply.

### Test C — Continue Conversation A

Send another request to the same URL using ID A:

```
{
  "message": "What career goal did I mention?"
}
```

The assistant should use the earlier message to answer that your goal is to become a backend developer.

### Test D — Create Conversation B

Send another request:

`POST http://localhost:8080/api/conversations`

Copy the newly returned ID as ID B. Send this message to the URL containing ID B:

```
{
  "message": "What career goal did I mention?"
}
```

Since Conversation B has no earlier message describing your goal, the assistant should not claim that it remembers the goal from Conversation A.

