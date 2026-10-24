# Phase 3 — Step 6: Test Memory and Persistence

## Step 6.1 — Test conversational context and isolation

We'll use Postman and your existing endpoints. No code changes are needed for this first test.

### Test A — Does the assistant remember earlier context?

1\. Create a new conversation

```
POST http://localhost:8080/api/conversations
```

Use Alice's Basic Auth credentials. Copy the returned `conversationId`; call it `ID_A`.

2\. Send the first message

```
POST http://localhost:8080/api/conversations/ID_A/messages
```

Replace `ID_A` with the actual ID. Set Body → raw → JSON:

```
{
  "message": "My name is Alice. I am learning Java and Spring Boot, and my career goal is to become a backend developer."
}
```

Expect `200 OK`.

3\. Ask a follow-up question

Use the same conversation ID:

```
{
  "message": "What is my career goal?"
}
```

Expected: the assistant identifies backend developer from the previous message.

This tests whether conversation history is being supplied to the model. The wording need not match exactly.

### Test B — Are two conversations isolated?

1\. Create a second conversation using `POST /api/conversations`. Save its ID as `ID_B`.

2\. Ask the same question in the second conversation:

```
POST http://localhost:8080/api/conversations/ID_B/messages
```

```
{
  "message": "What is my career goal?"
}
```

Expected: the assistant should not claim it remembers the goal from conversation A. It may say it doesn't know yet or ask you to clarify.

Why this matters: Spring AI uses the conversation ID to select the appropriate memory. If you accidentally reuse an ID, separate conversations can become mixed together.

### Test C — Verify the stored messages

Retrieve Alice's transcript for `ID_A`:

```
GET http://localhost:8080/api/conversations/ID_A/messages
```

Expected: `200 OK`, with Alice's first message and follow-up in the transcript, along with the assistant's responses.

Then retrieve `ID_B` using its ID. Its transcript should contain only messages exchanged in conversation B.

//Part 2
## 3. Next: Step 6.2 — Persistence after restart

Now we'll confirm that your application doesn't depend on in-memory state to recover earlier conversations.

### Test procedure

1. In Postman, create a new conversation using Alice's credentials.
2. Send this message to that conversation:
   ```
   {
     "message": "Remember this: my target role is Java backend developer, and my current framework is Spring Boot."
   }
   ```
3. Copy the conversation ID and verify that its transcript contains the message.
4. Stop the Spring Boot application completely in IntelliJ IDEA.
5. Start it again without clearing or recreating your PostgreSQL database.
6. Using Alice's credentials, request the same transcript:
   ```
   GET http://localhost:8080/api/conversations/{conversationId}/messages
   ```
7. Send a follow-up question to the same conversation:
   ```
   {
     "message": "Which role and framework did I ask you to remember?"
   }
   ```

### Expected results

- The transcript remains available after restart.
- The follow-up answer identifies Java backend developer and Spring Boot, using the stored conversation history.
- The same conversation ID is reused; don't create a new conversation for this test.

Why this matters: PostgreSQL-backed chat memory should survive an application restart, unlike purely in-memory storage. A successful transcript retrieval verifies stored messages are available, while the follow-up tests whether they can still provide conversational context.

//Part 3
## Step 6.3 — Test unknown conversation IDs

Now let's check what happens when a client supplies a conversation ID that does not exist.

### Test A — Send a message to a nonexistent ID

In Postman, authenticate as Alice using Basic Auth.

Request

```
POST http://localhost:8080/api/conversations/00000000-0000-0000-0000-000000000000/messages
```

Body → raw → JSON:

```
{
  "message": "Hello, can you help me plan my career?"
}
```

Expected: the request must be rejected, and Gemini must not be called.

Your current ownership service throws `AccessDeniedException` when the conversation is missing or does not belong to the authenticated user. Depending on your exception handling, the HTTP status may currently be `403 Forbidden`. We will check the actual response rather than assume it.

### Test B — Retrieve a nonexistent transcript

With Alice's credentials, send:

```
GET http://localhost:8080/api/conversations/00000000-0000-0000-0000-000000000000/messages
```

Expected: the request is rejected without exposing any transcript.

### Why we test this

A secure API must not accidentally create memory for arbitrary IDs or return data for conversations that aren't registered in PostgreSQL. It should also avoid revealing another user's data when the requested ID is invalid.

Run both tests and report:

1. The HTTP status for Test A.
2. The HTTP status for Test B.
3. Whether either request returned a Gemini response.

//Part 4
# Step 6.4 — Invalid requests and authorization

In this lesson, we'll test whether your API rejects malformed requests and unauthenticated access safely. We'll test first and change code only if a test reveals a problem.

## 1. Test A — Reject a blank message

In Postman, use Alice's Basic Auth credentials and a valid conversation ID that belongs to Alice.

Request

```
POST http://localhost:8080/api/conversations/{aliceId}/messages
```

Select Body → raw → JSON:

```
{
  "message": "   "
}
```

Expected: `400 Bad Request`.

Why? Your `CareerChatRequest` record uses `@NotBlank`:

```
@NotBlank(message = "Message must not be blank")
@Size(max = 4000, message = "Message must not exceed 4000 characters")
String message
```

The `@Valid` annotation on the controller activates validation before the method processes the request.

## 2. Test B — Reject a missing message

Use the same endpoint and valid Alice credentials, but send:

```
{}
```

Expected: `400 Bad Request`, because the required `message` field is missing and fails validation.

Also try malformed JSON:

```
{
  "message":
```

Expected: `400 Bad Request` because the request body cannot be parsed as valid JSON.

## 3. Test C — Reject an oversized message

Your DTO limits messages to 4,000 characters.

Send a message containing 4,001 characters to Alice's valid conversation. You can generate the value in Postman using a Pre-request Script:

```
pm.variables.set("longMessage", "a".repeat(4001));
```

Then use this JSON body:

```
{
  "message": "{{longMessage}}"
}
```

Expected: `400 Bad Request` due to `@Size(max = 4000)`.

If Postman doesn't substitute the variable, check that the variable is defined in the active request context. You can also paste a 4,001-character string directly into the request body.

## 4. Test D — Reject unauthenticated access

Remove Basic Auth credentials by selecting Authorization → No Auth.

Try both requests:

```
POST http://localhost:8080/api/conversations/{aliceId}/messages
```

and

```
GET http://localhost:8080/api/conversations/{aliceId}/messages
```

Expected for both: `401 Unauthorized`.

This confirms that users must authenticate before they can send messages or read transcripts.

## 5. Test E — Verify cross-user authorization again

Use Bob's credentials with Alice's valid conversation ID:

```
GET http://localhost:8080/api/conversations/{aliceId}/messages
```

Expected: `403 Forbidden`.

This rechecks that transcript retrieval enforces ownership—not merely authentication.


//Step 5
## Step 6.5 — Final memory and persistence review

Run these four final checks in Postman:

1. Memory continuity: In one conversation, tell the assistant your target role is `Java backend developer`. In a follow-up message, ask what role you mentioned. Confirm the reply recalls it.
2. Conversation isolation: Create a second conversation and ask what target role you mentioned. Confirm the assistant does not receive the first conversation’s history.
3. Persistence after restart: Restart the Spring Boot application. Fetch the first conversation’s transcript and ask a follow-up question to confirm its stored history remains available.
4. Ownership protection: Using Bob’s credentials, try to read Alice’s transcript and send a message to Alice’s conversation. Confirm both return `403 Forbidden`.


