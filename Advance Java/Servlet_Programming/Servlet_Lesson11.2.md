# Lesson 11.2 — Server Side of the Web Application

## 1. Learning objectives

By the end of this lesson, you should be able to:

1. Explain server-side programming and why it is necessary.
2. Distinguish a web server, servlet container, application server, and database server.
3. Trace an HTTP request from React through a Java Servlet to PostgreSQL.
4. Understand request processing, business logic, and response generation.
5. Build and test a small server-side application using Postman.

## 2. What is the server side of a web application?

The server side is the part of an application that runs on a server and processes client requests. It is responsible for operations such as authentication, validation, business rules, data retrieval, and database updates.

Imagine you're building a student management system.

- React displays a student registration form.
- Java receives the submitted student details.
- The backend validates the details and determines whether registration is allowed.
- PostgreSQL stores the student record.
- Java returns a response that React can display.

The important point is that the server is responsible for enforcing rules and protecting access to data. A user can bypass the React interface and send a request directly using Postman, so the backend must validate every request independently.

## 3. Architecture of our application

React frontend

User interface running in the browser

HTTP / JSON

Apache Tomcat

Accepts HTTP traffic and hosts our Servlet application

Java Servlet

Processes requests and applies application rules

JDBC / SQL

PostgreSQL

Stores student records persistently

Postman can send HTTP requests directly to Tomcat, bypassing React.

This follows the standard Servlet request-processing model defined by the Jakarta Servlet specification and illustrated in the official Tomcat architecture documentation.

## 4. Understanding the server-side components

These components work together, but they do different jobs.


**A. Web server**

Receives HTTP traffic and serves web content or passes requests into application-processing components. In practice, servers such as Apache HTTP Server can sit in front of an application server.

Example: Apache HTTP Server.


**B. Servlet container**

Provides the runtime for Java Servlets. It manages servlet life cycles, URL mappings, request and response objects, and other servlet services.

Our example: Apache Tomcat 10.1.



**C. Application logic**

This is the Java code that implements the application's rules, such as checking student details, calculating fees, or deciding whether a user can access a resource.

Our example: `HelloServlet.java` and future service classes.



**D. Database server**

Stores and manages persistent records, executes SQL queries, and enforces database constraints.

Our example: PostgreSQL, accessed from Java through JDBC.

Important distinction: Tomcat is a servlet container with web-server capabilities. It is not a full Jakarta EE application server providing every enterprise service.



## 5. How a server-side request is processed

Let's trace a student registration request.

1. Client sends a request. React or Postman sends an HTTP request to a URL, for example `POST /servlet-learning/api/students`.
2. Tomcat receives the request. It processes the HTTP connection and identifies the target web application.
3. The servlet mapping is matched. Tomcat finds the servlet responsible for the requested path and method.
4. Java processes the request. The servlet reads submitted values and validates them.
5. The database is accessed if necessary. Java uses JDBC to execute a SQL statement against PostgreSQL.
6. A response is returned. The servlet sets an appropriate status code, headers, and response body. React or Postman receives the result.

Tomcat's documented request flow explains how HTTP traffic passes through its connector and servlet-processing pipeline.



## 6. Practical example — A server-side endpoint

We'll extend the idea from Lesson 11.1. For this exercise, we don't need PostgreSQL yet; we're focusing on how Java processes requests on the server.

Create `ServerInfoServlet.java` in the same package:

```

package com.advancejava;

import java.io.IOException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/api/server-info")
public class ServerInfoServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String json = """
                {
                    "application": "Student Management System",
                    "backend": "Java Servlet",
                    "server": "Apache Tomcat",
                    "message": "Request processed successfully"
                }
                """;

        response.getWriter().write(json);
    }
}

```

### What happens when you call this endpoint?

- `@WebServlet("/api/server-info")` maps the servlet to the specified path.
- `doGet()` processes GET requests.
- `setContentType("application/json")` tells the client that the response contains JSON.
- `getWriter().write(json)` sends the response body to the client.

The JSON here is a fixed example, not a database result. Later, we will generate responses from real PostgreSQL data. The Servlet API defines the response methods we are using.



### Run and test it

Rebuild and redeploy the project using the same approach that worked for you in IntelliJ IDEA.

In Postman, send:

GET

`http://localhost:8080/servlet-learning/api/server-info`

Expected JSON response

{   "application": "Student Management System",   "backend": "Java Servlet",   "server": "Apache Tomcat",   "message": "Request processed successfully" }

If you use a different context path in your IntelliJ deployment configuration, replace `servlet-learning` with your actual context path.

## 7. Client-side vs server-side programming

| Feature              | Client side           | Server side                   |
| -------------------- | --------------------- | ----------------------------- |
| Runs on              | Browser               | Server                        |
| Our technology       | React                 | Java Servlets                 |
| Main responsibility  | User interface        | Business logic                |
| Database credentials | Should not be exposed | Managed securely              |
| Can be tested with   | Browser               | Postman or other HTTP clients |
| Example              | Display student form  | Validate and save student     |

One more important point: a React application and a Java Servlet backend are separate applications communicating over HTTP. In development, they may run on different ports, such as React on `localhost:5173` and Tomcat on `localhost:8080`. This can require CORS configuration. Postman generally doesn't enforce browser CORS restrictions.

## 8. Common mistakes to avoid

- Assuming that hiding a button in React secures an operation. The backend must enforce authorization.
- Connecting React directly to PostgreSQL. Database credentials and database access should remain on the server.
- Assuming every request reaches the same servlet. URL mappings and HTTP methods determine request routing.
- Assuming a successful HTTP response means a database operation succeeded. Check the actual business result and database outcome.
- Storing per-request data in ordinary servlet instance fields. Servlet instances may process concurrent requests, so shared mutable state can cause race conditions.


