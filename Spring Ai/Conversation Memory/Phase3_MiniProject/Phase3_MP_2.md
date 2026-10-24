## Step 2.1 — Prepare the project

Your original project used the package `com.example.aichat`. We'll keep that base package and organize the new career assistant code inside it.

career-assistant project structure

src/main/java/com/example/aichat/

controller/

service/

entity/

repository/

dto/

src/main/resources/application.properties

pom.xml

What belongs where?

- `controller` — REST API endpoints.
- `service` — business logic and calls to Spring AI.
- `entity` — database entities such as `Conversation`.
- `repository` — Spring Data repositories for conversations and ownership.
- `dto` — request and response objects exchanged through the API.

This is a package organization plan; you don't need to create every class yet.

## Step 2.2 — Add the required dependencies

Open your existing `pom.xml`. Keep your existing Spring Boot parent, Spring AI BOM, web starter, and Gemini starter. Add the dependencies below inside the existing `<dependencies>` section; don't create a second section.

```
<!-- Database entities and repositories -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- PostgreSQL JDBC driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Request validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>

<!-- Persistent Spring AI chat memory -->
<dependency>
    <groupId>org.springframework.ai</groupId>
    <artifactId>spring-ai-starter-model-chat-memory-repository-jdbc</artifactId>
</dependency>
```

The Spring AI JDBC memory starter provides database-backed chat-memory storage. Your Spring AI BOM should manage its version, so don't add a separate version to this dependency.


Why do we need these?

- Spring Data JPA: store conversation metadata and ownership.
- PostgreSQL driver: connect Spring Boot to PostgreSQL.
- Validation: validate incoming message requests.
- Spring AI JDBC memory starter: persist chat messages rather than relying only on in-memory storage.

## Step 2.3 — Create a PostgreSQL database

If PostgreSQL is already installed, open pgAdmin or your SQL client and run:

```
CREATE DATABASE career_assistant;
```

We'll configure the connection to this database next.

## Step 2.4 — Configure `application.properties`

Open `src/main/resources/application.properties` and add the following database configuration. Keep your existing Gemini API key and model settings from Phase 1.

```
# PostgreSQL connection
spring.datasource.url=jdbc:postgresql://localhost:5432/career_assistant
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

# JPA settings for local development
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

# Spring AI JDBC chat memory schema
spring.ai.chat.memory.repository.jdbc.initialize-schema=always
```

Set `DB_USERNAME` and `DB_PASSWORD` in your environment. Don't commit passwords or API keys to Git. The `ddl-auto=update` setting is for our learning environment; later, we'll discuss managed database migrations for production.

Important: Spring AI property names and dependency compatibility differ across releases. Before running the app, we should verify the exact Spring Boot and Spring AI versions in your existing `pom.xml`, particularly because your Gemini configuration was created earlier.


# Step 2.5 — Build verification in IntelliJ IDEA

## 1. Open the Maven tool window

Open Maven

In IntelliJ IDEA, open your project and select View → Tool Windows → Maven. In the Maven window, expand your project to see its lifecycle tasks.

Run clean and compile

Expand Lifecycle, double-click `clean`, then double-click `compile`. Alternatively, open the IntelliJ terminal and run `mvn clean compile` from the folder containing `pom.xml`.

## 2. Check the build output

- `BUILD SUCCESS` — dependency resolution and compilation succeeded.
- `BUILD FAILURE` — copy the first meaningful error from the Build window so we can fix it.

Also confirm that the project is configured for JDK 21 under File → Project Structure → Project. Your Maven runner should use JDK 21 as well.


## Step 2.6 — Verify the PostgreSQL connection

Before implementing conversation memory, we should confirm that Spring Boot can connect to your `career_assistant` database.

### 1. Check your database configuration

In IntelliJ IDEA, open:

`src/main/resources/application.properties`

Verify that you have these settings:

```
spring.datasource.url=jdbc:postgresql://localhost:5432/career_assistant
spring.datasource.username=${DB_USERNAME}
spring.datasource.password=${DB_PASSWORD}

spring.jpa.hibernate.ddl-auto=update
spring.ai.chat.memory.repository.jdbc.initialize-schema=always
```

Make sure `DB_USERNAME` and `DB_PASSWORD` are configured in your environment. Don't paste your actual password or API key into this chat.

### 2. Confirm the database exists

Open pgAdmin or your PostgreSQL SQL client and run:

```
SELECT datname
FROM pg_database
WHERE datname = 'career_assistant';
```

You should see `career_assistant` in the result.

If it doesn't exist, create it:

```
CREATE DATABASE career_assistant;
```

### 3. Start the Spring Boot application

In IntelliJ IDEA:

1. Open the class containing `@SpringBootApplication`.
2. Click the green Run icon beside its `main()` method.
3. Inspect the Run console for database connection errors.

If your app fails because the JDBC chat-memory schema or a configuration property is unsupported by your installed Spring AI version, don't randomly change versions. Share the error so we can correct the configuration.



# Step 2.7 — Verify the JDBC chat-memory configuration

Before writing code, let's check that Spring AI can initialize and use its JDBC memory repository. A successful startup is a great sign, but it doesn't necessarily prove that messages can be saved and retrieved.

In IntelliJ IDEA:

1. Open the Run window and inspect the startup logs for schema or database errors.
2. Open your PostgreSQL database `career_assistant` in pgAdmin.
3. Look for the Spring AI chat-memory tables. The exact table names depend on your Spring AI version and configuration.

Don't create these tables manually yet; we want the starter to initialize the schema as configured.

## Check the tables

Open pgAdmin's Query Tool for `career_assistant` and run:

```
SELECT table_schema, table_name
FROM information_schema.tables
WHERE table_schema = 'public'
  AND table_type = 'BASE TABLE'
ORDER BY table_name;
```

Look for the Spring AI chat-memory table, commonly named `SPRING_AI_CHAT_MEMORY` in the JDBC repository schema.

What the result tells us:

- If the chat-memory table exists, schema initialization appears to have succeeded.
- If it doesn't exist, don't create it manually. We'll check your Spring AI dependency and configuration first.


