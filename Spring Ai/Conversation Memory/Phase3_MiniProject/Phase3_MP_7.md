
# Step 7 — Security Review and Final Project Assessment

## 7.1 Review the architecture you have built

Your application now has several layers working together:
<img src="./Phase3_MP_Step7_Fig1.PNG" alt="Phase 3 MP Step 7 Fig 1"/>

The central security principle is this: the backend, not the AI model or the conversation ID, decides who can access a conversation.

## 7.2 Security review checklist

Review each item against your current implementation. These checks are designed to identify what you should improve before treating the mini-project as production-ready.

### Security audit checklist

1. Authentication
All conversation endpoints require authentication.
Review: Your current Basic Auth setup satisfies this for the learning project.

2. Authorization
Every read and write verifies conversation ownership.
Review: Keep the ownership check before accessing chat memory or calling Gemini.

3. Credentials and secrets
Passwords, Gemini API keys, and database credentials are not committed to source control.
Review: Use environment variables or a secret manager.

4. Transport security
Credentials and chat messages are protected in transit.
Review: Basic Auth must be used over HTTPS outside local development.

5. CSRF configuration
CSRF protection matches the authentication mechanism and client type.
Review: Your current configuration disables CSRF; review this before production. Do not blindly enable or disable it without considering the client and authentication design.

6. Persistent chat history
Conversation history has suitable access controls and retention rules.
Review: Users should not retain private conversation data indefinitely without a reason.

7. AI input and output
User messages are treated as untrusted input, and AI responses are not trusted automatically.
Review: Prompt instructions are not a substitute for backend authorization and validation.

8. Abuse protection
Requests have sensible size limits, rate limits, and safe error handling.
Review: Your 4,000-character limit is a good start; it does not prevent request flooding by itself.



## Step 7.2 — Database-backed users + JWT authentication

### What we're building

<img src="./Phase3_MP_Step7_Fig2.PNG" alt="Phase 3 MP Step 7 Fig 2"/>

A key distinction: JWT authentication does not replace authorization. Even with a valid JWT, Bob must not be able to access Alice's conversation.

We'll work in this order:

1. Create the `User` entity and PostgreSQL table.
2. Create `UserRepository`.
3. Implement database-backed `UserDetailsService`.
4. Configure JWT issuance and JWT validation in Spring Security.
5. Test registration/login, invalid credentials, and conversation ownership in Postman.

- **Stage 1 — User entity and repository**
- **Stage 2 — Database-backed user loading**
- **Stage 3 — JWT login and token issuance**
- **Stage 4 — JWT validation and security configuration**
- **Stage 5 — Postman security tests**


## Stage 1 — Create the user entity and repository

### 1. Create `AppUser.java`

In IntelliJ IDEA, open `com.example.Phase2.entity`, create a new Java class named `AppUser`, and replace its contents with this complete code.

We use `AppUser` rather than `User` to avoid confusion with Spring Security's own `User` class.

```

package com.example.Phase2.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;

import java.time.Instant;

@Entity
@Table(name = "app_users")
public class AppUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false, length = 100)
    private String passwordHash;

    @Column(nullable = false, length = 20)
    private String role = "USER";

    @Column(nullable = false)
    private boolean enabled = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected AppUser() {
        // Required by JPA
    }

    public AppUser(String username, String passwordHash) {
        this.username = username;
        this.passwordHash = passwordHash;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = Instant.now();
        }
    }

    public Long getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getRole() {
        return role;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}

```

### Understand the important fields

| Field          | Purpose                                          |
| -------------- | ------------------------------------------------ |
| `id`           | Unique database identity for each user           |
| `username`     | Unique login name                                |
| `passwordHash` | Stores the BCrypt hash, never the plain password |
| `role`         | Defines the user's application role              |
| `enabled`      | Allows an account to be disabled                 |
| `createdAt`    | Records when the account was created             |

The `@PrePersist` method automatically sets the creation timestamp before a new entity is inserted.

### 2. Create `UserRepository.java`

Open `com.example.Phase2.repository` and create `UserRepository.java`.

```

package com.example.Phase2.repository;

import com.example.Phase2.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    boolean existsByUsername(String username);
}

```

Spring Data JPA generates the queries for these methods automatically:

- `findByUsername("alice")` — finds Alice's account.
- `existsByUsername("alice")` — checks whether that username is already registered.
- `save(appUser)` — inserts a new account or updates an existing one.

We will use these methods when implementing registration and database-backed authentication.

//Stage 2

## Stage 2 — Implement database-backed `UserDetailsService`

### What are we changing?

Currently, your `SecurityConfig` contains hard-coded users:

- Alice
- Bob

Spring Security authenticates those users from memory. We want it to retrieve user accounts from PostgreSQL instead.

The flow will become:

<img src="./Phase3_MP_Step7_Fig3.PNG" alt="Phase 3 MP Step 7 Fig 3"/>

`UserDetailsService` is a Spring Security interface for loading a user by username. It doesn't issue JWTs itself; we'll use it during login and later to validate credentials.

## 2.1 Create `DatabaseUserDetailsService.java`

In IntelliJ IDEA, open `com.example.Phase2.service`.

Create a new Java class named `DatabaseUserDetailsService` and use this complete code:

```

package com.example.Phase2.service;

import com.example.Phase2.entity.AppUser;
import com.example.Phase2.repository.UserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class DatabaseUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public DatabaseUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        AppUser appUser = userRepository.findByUsername(username)
                .orElseThrow(() ->
                        new UsernameNotFoundException(
                                "User not found: " + username
                        )
                );

        String authority = appUser.getRole().startsWith("ROLE_")
                ? appUser.getRole()
                : "ROLE_" + appUser.getRole();

        return User.withUsername(appUser.getUsername())
                .password(appUser.getPasswordHash())
                .authorities(authority)
                .disabled(!appUser.isEnabled())
                .build();
    }
}

```

### 2.2 Understand the code

1\. Inject `UserRepository`

```
private final UserRepository userRepository;
```

Spring injects the repository, allowing this service to query PostgreSQL.

2\. Find the account

```
AppUser appUser = userRepository.findByUsername(username)
        .orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + username)
        );
```

If the username exists, we retrieve its account. Otherwise, Spring Security receives a `UsernameNotFoundException`.

3\. Return Spring Security's `UserDetails`

```
return User.withUsername(appUser.getUsername())
        .password(appUser.getPasswordHash())
        .authorities(authority)
        .disabled(!appUser.isEnabled())
        .build();
```

This maps your database entity into the format Spring Security understands.

Notice that `.password()` receives the stored BCrypt hash, not the user's original password. Spring Security's authentication provider will compare the submitted password against that hash when we connect the authentication flow.





