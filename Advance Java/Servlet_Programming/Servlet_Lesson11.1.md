## Lesson 1 — 11.1 Fundamentals of Web Programming

### Learning objectives

By the end of this lesson, you should be able to:

- Explain how web applications work.
- Distinguish frontend, backend, and database.
- Understand HTTP requests and responses.
- Explain where Java Servlets fit into a modern web application.
- Run a basic Servlet endpoint and test it with Postman.

### 1. What is web programming?

Web programming is the process of creating applications that communicate over a network, usually using HTTP or HTTPS.

Examples include online shopping systems, banking applications, student portals, and social media platforms.

A typical web application has three main layers:

<img src="SP_Lesson11.1_Fig1.PNG" alt="Servlet Programming Lesson 11.1 Fig 1"/>

Postman is a separate testing client. It can send requests directly to the Java backend without requiring the React frontend.

### 2. Client-server architecture

Web applications commonly use the client-server model.

- Client: The application or software that sends a request, such as a browser, React application, or Postman.
- Server: The software that receives the request, processes it, and sends a response.
- Protocol: The agreed rules for communication. For web applications, this is usually HTTP or HTTPS.

Consider a student registration application.

1. The user enters a student's name in the React form.
2. React sends an HTTP `POST` request to a Java Servlet.
3. The Servlet validates the submitted information.
4. The Servlet uses JDBC to execute a SQL statement against PostgreSQL.
5. The backend sends an HTTP response, often containing JSON.
6. React displays the result to the user.

Why do we need this architecture?

It separates responsibilities. React can manage the interface without directly accessing database credentials, while Java handles business logic and database access on the server.

### 3. What is HTTP?

HTTP stands for Hypertext Transfer Protocol. It defines how clients and servers exchange requests and responses.

A request typically contains:

- An HTTP method, such as `GET` or `POST`.
- A URL identifying the resource.
- Headers containing metadata.
- Optionally, a body containing submitted data.

A response typically contains:

- A status code, such as `200 OK` or `404 Not Found`.
- Response headers.
- An optional body containing HTML, JSON, or other content.

Common HTTP methods:

| Method   | Typical purpose                  |
| -------- | -------------------------------- |
| `GET`    | Retrieve data                    |
| `POST`   | Submit data or create a resource |
| `PUT`    | Replace a resource               |
| `PATCH`  | Partially update a resource      |
| `DELETE` | Delete a resource                |

For our application, React might request `GET /api/students` to retrieve students or send `POST /api/students` to register one.

### 4. What is server-side programming?

Server-side programming means executing application logic on the server rather than in the user's browser.

For example, suppose a student submits a registration form.

The server should verify required fields, enforce business rules, interact with PostgreSQL, and decide which HTTP response to return. Client-side validation in React is useful, but it cannot replace server-side validation because clients can send requests without using the React interface.

This is where Java Servlets become useful.

### 5. What is a Servlet?

According to the official Servlet API documentation, a servlet is a Java program that runs inside a servlet container and receives and responds to client requests, typically over HTTP.



A servlet container, such as Apache Tomcat, manages servlet initialization, request handling, and destruction.

Our chosen setup uses Jakarta Servlet 6.0 with Apache Tomcat 10.1. In this version, servlet classes use the `jakarta.servlet.*` packages. Older tutorials may use `javax.servlet.*`; those imports are not interchangeable with the Jakarta packages.



## 6. First practical example — Hello Servlet

Let's write our first Java Servlet and test it directly with Postman.

We'll use Maven and Java 17 or later with Tomcat 10.1. Tomcat 10.1 requires Java 11 or later, and supports the Servlet 6.0 API.


### Step 1: Create the project

Create a Maven project with this structure:

```
servlet-learning/
├── pom.xml
└── src/
    └── main/
        └── java/
            └── com/
                └── advancejava/
                    └── HelloServlet.java
```

### Step 2: Configure Maven

Create `pom.xml`:

```

<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0
         https://maven.apache.org/xsd/maven-4.0.0.xsd">

    <modelVersion>4.0.0</modelVersion>

    <groupId>com.advancejava</groupId>
    <artifactId>servlet-learning</artifactId>
    <version>1.0.0</version>
    <packaging>war</packaging>

    <properties>
        <maven.compiler.release>17</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencies>
        <dependency>
            <groupId>jakarta.servlet</groupId>
            <artifactId>jakarta.servlet-api</artifactId>
            <version>6.0.0</version>
            <scope>provided</scope>
        </dependency>
    </dependencies>

    <build>
        <finalName>servlet-learning</finalName>
    </build>
</project>

```

Why `provided`?

Tomcat provides the Servlet API at runtime. Maven needs the API to compile our code, but we generally should not package a second copy of it into the WAR file.

### Step 3: Write the Servlet

Create `HelloServlet.java`:

```

package com.advancejava;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/hello")
public class HelloServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().println(
                "Hello! Welcome to Advance Java Servlets."
        );
    }
}

```

### Understand the code

| Code                           | Purpose                                                       |
| ------------------------------ | ------------------------------------------------------------- |
| `extends HttpServlet`          | Creates an HTTP servlet by extending the provided base class. |
| `@WebServlet("/hello")`        | Maps the servlet to the `/hello` URL path.                    |
| `doGet()`                      | Handles HTTP GET requests.                                    |
| `HttpServletRequest`           | Gives access to information about the incoming request.       |
| `HttpServletResponse`          | Lets us configure and produce the response.                   |
| `setContentType("text/plain")` | Declares that the response body is plain text.                |
| `getWriter()`                  | Obtains a writer for sending text to the client.              |

The `HttpServlet` class dispatches HTTP requests to methods such as `doGet()` and `doPost()`. For ordinary GET handling, you override `doGet()` rather than implementing the entire request-processing mechanism yourself.


### Step 4: Build and deploy

Ensure Maven and Tomcat are installed, and that Tomcat is configured to use a compatible Java version.

From the project directory, run:

```
mvn clean package
```

If the build succeeds, Maven creates:

```
target/servlet-learning.war
```

Copy that WAR file into Tomcat's `webapps` directory, start Tomcat, and make sure it has deployed the application successfully.

On a default local Tomcat installation, the application should be available at:

```
http://localhost:8080/servlet-learning/hello
```

The URL consists of the server address, Tomcat port, application context path, and servlet mapping.

### Step 5: Test with Postman

Request configuration

Send a request directly to the servlet

GET

[http://localhost:8080/servlet-learning/hello](http://localhost:8080/servlet-learning/hello?utm_source=chatgpt.com)

In Postman, select GET, enter the URL, and click Send.

Expected response

200 OK

text/plain

Hello! Welcome to Advance Java Servlets.

This is your first working backend endpoint. No React frontend or database is needed yet; we're isolating the Servlet concept so you can understand it before adding more components.

## 7. Official documentation to keep nearby

Use these as our reference materials throughout the chapter:

- [Servlet API — official Javadoc](https://tomcat.apache.org/tomcat-10.1-doc/servletapi/jakarta/servlet/Servlet.html?utm_source=chatgpt.com) — servlet fundamentals and life cycle.
- [HttpServlet — official Javadoc](https://tomcat.apache.org/tomcat-10.1-doc/servletapi/jakarta/servlet/http/HttpServlet.html?utm_source=chatgpt.com) — HTTP methods and request handling.
- [Apache Tomcat setup guide](https://tomcat.apache.org/tomcat-10.1-doc/setup.html?utm_source=chatgpt.com) — installation and configuration.


//part 2
## Step 2 — Build the project in IntelliJ

1. Open the project in IntelliJ IDEA.
2. Open `pom.xml` and check that IntelliJ recognizes it as a Maven project. If prompted, select Load Maven Project or Add as Maven Project.
3. Open the Maven tool window, usually on the right side.
4. Expand your project → Lifecycle → double-click `clean`, then double-click `package`.
5. Wait for the build to finish and inspect the Build output for `BUILD SUCCESS`.

Alternatively, open IntelliJ's Terminal at the project root and run:

```
mvn clean package
```

If successful, the generated file should be:

```
target/servlet-learning.war
```

If that file doesn't exist, don't proceed to deployment yet. We need to fix the build first.

## Step 3 — Deploy using IntelliJ IDEA

There are two ways to deploy your Servlet application. First, let's use the method that works even if your IntelliJ edition doesn't offer Tomcat integration.

### Method A: Deploy the WAR file manually (recommended)

1. Download and extract Apache Tomcat 10.1 if you haven't installed it. Keep track of the extracted folder, for example `C:\apache-tomcat-10.1.x`.
2. In IntelliJ, run Maven's `package` goal as explained above.
3. Open your Tomcat installation folder and locate `webapps`.
4. Copy `target\servlet-learning.war` into Tomcat's `webapps` directory.
5. Start Tomcat on Windows by opening Command Prompt in the Tomcat `bin` directory and running:

```
startup.bat
```

If Windows blocks the script or the terminal closes immediately, run it from an already-open Command Prompt. To see startup errors directly, you can instead run:

```
catalina.bat run
```

Leave that terminal running. Tomcat should deploy the WAR automatically when automatic deployment is enabled.



### Method B: Run Tomcat directly from IntelliJ

If your IntelliJ installation supports the Tomcat Server run configuration:

1. Go to Run → Edit Configurations.
2. Click `+` and select Tomcat Server → Local.
3. Under Application server, configure the path to your Tomcat installation.
4. Open the Deployment tab.
5. Click `+` and select `servlet-learning:war exploded` or the WAR artifact available for your project.
6. Set the application context to `/servlet-learning`.
7. Set the server URL to `http://localhost:8080/servlet-learning/hello`.
8. Click Apply → OK, then click Run.

If the Tomcat Server option is missing, don't get stuck here. IntelliJ's server integration depends on the available IDE features and plugins; use Method A instead. The official JetBrains guide explains the run configuration and deployment artifact settings.


## Step 4 — Test your Servlet

Once Tomcat has started and deployed the application, open Postman and send:

GET

`http://localhost:8080/servlet-learning/hello`

&#x20;Copy URL

Expected response:

```
Hello! Welcome to Advance Java Servlets.
```

## Step 5 — If it still doesn't work

Use this table to identify the likely problem:

| Error                                     | What to check                                                   |
| ----------------------------------------- | --------------------------------------------------------------- |
| `mvn is not recognized`                   | Maven installation or IntelliJ's Maven configuration            |
| `BUILD FAILURE`                           | Read the first compiler or dependency error in the Maven output |
| `404 Not Found`                           | Check deployment, application context, and `/hello` mapping     |
| `500 Internal Server Error`               | Read the Tomcat console or log for the exception                |
| `Unable to access jarfile` or Java errors | Check the Java and Tomcat installation                          |
| `Address already in use`                  | Port 8080 may already be occupied                               |

Useful official guides: [IntelliJ Tomcat configuration](https://www.jetbrains.com/help/idea/run-debug-configuration-tomcat-server.html?utm_source=chatgpt.com) and [Tomcat documentation](https://tomcat.apache.org/tomcat-10.1-doc/?utm_source=chatgpt.com)

.
