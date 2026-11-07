# Lesson 12.7 — Exception Handling in JSP


## 1. Learning objectives

By the end of this lesson, you should be able to:

- Explain why exception handling is needed in JSP.
- Distinguish compile-time, runtime, and application errors.
- Use JSP error-page directives.
- Access the `exception` implicit object.
- Configure a global error page in `web.xml`.
- Handle errors properly in a Servlet/controller.
- Avoid exposing sensitive technical details to users.

## 2. What is exception handling?

An exception is an event that interrupts the normal execution of a program.

For example, a JSP might try to:

- Read a missing parameter.
- Convert `"abc"` into an integer.
- Access a database that is unavailable.
- Call a method that throws an exception.
- Access an object that is `null`.

Without proper handling, the user may see a confusing server error page. Exception handling lets us respond gracefully, log the technical details, and show a useful message.

### Why is it needed?

| Without proper handling           | With proper handling                 |
| --------------------------------- | ------------------------------------ |
| User sees a technical error       | User sees a friendly message         |
| Internal details may leak         | Sensitive details stay on the server |
| Errors are difficult to diagnose  | Errors can be logged                 |
| Application flow is unpredictable | Errors have a defined response       |

## 3. JSP error-page directive

JSP provides the `errorPage` attribute of the page directive to specify which JSP should handle an uncaught exception.

### Syntax

```
<%@ page errorPage="error.jsp" %>
```

If an exception occurs while processing this JSP and it is not handled locally, the container can forward processing to `error.jsp`.

The error JSP must declare:

```
<%@ page isErrorPage="true" %>
```

This makes the JSP’s `exception` implicit object available.

## 4. Complete example: Handling an exception in JSP

### File 1: `calculate.jsp`

This example divides two numbers. If the denominator is zero, Java throws an `ArithmeticException`.

```
<%@ page contentType="text/html; charset=UTF-8" %>
<%@ page errorPage="error.jsp" %>

<!DOCTYPE html>
<html>
<head>
    <title>JSP Exception Handling</title>
</head>
<body>
    <h2>Division Calculator</h2>

    <%
        int numerator = 100;
        int denominator = 0;

        int result = numerator / denominator;
    %>

    <p>Result: <%= result %></p>
</body>
</html>
```

### File 2: `error.jsp`

```
<%@ page contentType="text/html; charset=UTF-8"
         isErrorPage="true" %>

<!DOCTYPE html>
<html>
<head>
    <title>Error</title>
</head>
<body>
    <h2>Sorry, something went wrong.</h2>

    <p>Your request could not be completed.</p>

    <%
        if (exception != null) {
            application.log(
                "Exception occurred in calculate.jsp",
                exception
            );
        }
    %>
</body>
</html>
```

### What happens?

1. The browser requests `calculate.jsp`.
2. The JSP attempts `100 / 0`.
3. Java throws `ArithmeticException`.
4. The exception is not caught in `calculate.jsp`.
5. The JSP container transfers control to `error.jsp`.
6. The user sees a friendly message.
7. The server logs the exception.

Important: We log the exception on the server, but do not print its stack trace or message to the browser. Technical details can reveal internal paths, SQL, class names, or configuration.

## 5. Handling an error with `try-catch` inside JSP

You can also catch an exception directly using Java’s `try-catch`.

```
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Try-Catch Example</title>
</head>
<body>
    <h2>Division Calculator</h2>

    <%
        int numerator = 100;
        int denominator = 0;

        try {
            int result = numerator / denominator;
            out.println("<p>Result: " + result + "</p>");
        } catch (ArithmeticException e) {
            out.println("<p>Cannot divide by zero.</p>");
            application.log("Division by zero occurred", e);
        }
    %>
</body>
</html>
```

### Difference between `try-catch` and `errorPage`

| `try-catch`                                 | `errorPage`                                |
| ------------------------------------------- | ------------------------------------------ |
| Handles a specific exception locally        | Handles an uncaught exception from the JSP |
| Lets the JSP continue after the catch       | Transfers error handling to another page   |
| Useful for expected, recoverable situations | Useful for centralized error presentation  |

Best practice: Use `try-catch` for errors you can meaningfully recover from. Use an error page for unexpected failures that escape the page.

## 6. Configure a global error page in `web.xml`

A JSP-level `errorPage` applies to that JSP. You can also configure error handling for the web application in `WEB-INF/web.xml`.

### `WEB-INF/web.xml`

```
<?xml version="1.0" encoding="UTF-8"?>
<web-app
    xmlns="https://jakarta.ee/xml/ns/jakartaee"
    xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
    xsi:schemaLocation="
        https://jakarta.ee/xml/ns/jakartaee
        https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
    version="6.0">

    <error-page>
        <exception-type>java.lang.Throwable</exception-type>
        <location>/error.jsp</location>
    </error-page>

    <error-page>
        <error-code>404</error-code>
        <location>/not-found.jsp</location>
    </error-page>

    <error-page>
        <error-code>500</error-code>
        <location>/error.jsp</location>
    </error-page>

</web-app>
```

This configuration handles:

- Uncaught exceptions using `/error.jsp`
- HTTP `404` errors using `/not-found.jsp`
- HTTP `500` errors using `/error.jsp`

### `not-found.jsp`

```
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Page Not Found</title>
</head>
<body>
    <h2>404 — Page Not Found</h2>
    <p>The page you requested could not be found.</p>
    <a href="index.jsp">Return to Home</a>
</body>
</html>
```

Important: Keep error JSPs accessible to the container’s error dispatch. Avoid placing them in a location that requires the user to be authenticated unless your error-handling design accounts for that.

## 7. Recommended architecture: Servlet handles errors, JSP displays them

In your React + Servlet + PostgreSQL stack, business logic and database operations should not be placed in JSPs. A Servlet/controller should call the service or DAO, handle exceptions, and decide what response to return.

### Example Servlet

```
package com.example.controller;

import com.example.dao.StudentDAO;
import com.example.model.Student;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/students")
public class StudentListServlet extends HttpServlet {

    private StudentDAO studentDAO;

    @Override
    public void init() {
        studentDAO = new StudentDAO();
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        try {
            List<Student> students = studentDAO.findAll();

            request.setAttribute("students", students);

            request.getRequestDispatcher("/students.jsp")
                   .forward(request, response);

        } catch (SQLException e) {
            getServletContext().log(
                "Unable to retrieve students from the database",
                e
            );

            response.sendError(
                HttpServletResponse.SC_INTERNAL_SERVER_ERROR
            );
        }
    }
}
```

### What this design achieves

1. `StudentDAO` performs database access.
2. The Servlet catches the expected `SQLException`.
3. The server logs technical details.
4. The response uses HTTP status `500`.
5. The configured error page displays a user-friendly message.

For a React frontend, the same controller would normally return a JSON error response rather than forward to a JSP. The central principle remains: log details on the server; return a safe, meaningful response to the client.

## 8. Common mistakes to avoid

- Printing `exception` directly: may reveal internal information.
- Catching `Exception` and doing nothing: hides failures and makes debugging difficult.
- Putting JDBC code in JSP: mixes presentation and data access.
- Using error pages for validation: invalid user input is usually a normal application outcome, not a server error.
- Forgetting `isErrorPage="true"`: the JSP `exception` implicit object is unavailable unless this is enabled.
- Showing stack traces in production: useful during local development, unsafe for users.

## 9. Quick recap

| Concept                     | Meaning                                                 |
| --------------------------- | ------------------------------------------------------- |
| `errorPage="error.jsp"`     | Sends uncaught JSP exceptions to a specified error page |
| `isErrorPage="true"`        | Declares a JSP as an error page                         |
| `exception`                 | The uncaught `Throwable` available in an error JSP      |
| `try-catch`                 | Handles a specific exception locally                    |
| `application.log(...)`      | Logs technical details on the server                    |
| `<error-page>` in `web.xml` | Configures application-wide error handling              |


