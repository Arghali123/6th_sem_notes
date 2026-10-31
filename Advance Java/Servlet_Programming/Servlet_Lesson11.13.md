# Lesson 11.13 — Request Dispatcher

## Learning objectives

By the end of this lesson, you should be able to:

1. Explain what a `RequestDispatcher` is and why it is useful.
2. Distinguish forwarding from including a response.
3. Use `request.getRequestDispatcher()` correctly.
4. Pass data between servlets using request attributes.
5. Choose between `RequestDispatcher`, redirect, and direct response.
6. Apply the concept in our Java + React + PostgreSQL application.

## 1. What is a Request Dispatcher?

A `RequestDispatcher` is a Servlet API object that allows a servlet to transfer request processing to another server-side resource or include that resource’s output in its response.

The target can be, for example:

- Another servlet
- A JSP page
- An HTML resource

It is commonly used when a servlet wants to delegate work or display a result without making the browser send a new request.

### Why do we need it?

Imagine our application receives a request to show all students:

```
Browser / React
      |
      | GET /students
      v
StudentServlet
      |
      | Retrieves students from PostgreSQL
      v
StudentServlet
      |
      | Forwards request
      v
students.jsp
      |
      | Generates HTML
      v
Browser
```

The servlet handles the application logic, while the JSP handles presentation.

`RequestDispatcher` helps separate these responsibilities.

## 2. Two important methods

There are two main methods:

```
dispatcher.forward(request, response);
```

```
dispatcher.include(request, response);
```

| Feature                               | `forward()`                              | `include()`                                     |
| ------------------------------------- | ---------------------------------------- | ----------------------------------------------- |
| Purpose                               | Transfers processing to another resource | Inserts another resource’s output               |
| Target output                         | Becomes the response                     | Added into the current response                 |
| Original servlet continues afterward? | Normally no; control is transferred      | Yes                                             |
| Common use                            | Send request to a JSP for rendering      | Include a reusable header, footer, or component |
| Browser URL changes?                  | No                                       | No                                              |

### Important

Both operations happen inside the server. The browser does not receive a redirect instruction and does not make a second request.

## 3. `forward()` — transfer request processing

### Example scenario

A servlet checks a student’s name. If the name is missing, it forwards the request to an error page.

```
package com.example.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/check-student")
public class CheckStudentServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        String name = request.getParameter("name");

        if (name == null || name.isBlank()) {
            request.setAttribute(
                    "errorMessage",
                    "Student name is required."
            );

            RequestDispatcher dispatcher =
                    request.getRequestDispatcher("/student-error.jsp");

            dispatcher.forward(request, response);
            return;
        }

        response.setContentType("text/plain;charset=UTF-8");
        response.getWriter().println("Student name: " + name);
    }
}
```

### What happens?

1. The browser sends a POST request to `/check-student`.
2. The servlet reads the `name` parameter.
3. If it is missing, the servlet stores an error message as a request attribute.
4. The servlet forwards the same request to `/student-error.jsp`.
5. The JSP reads the attribute and renders the error.

The `return` after `forward()` makes it clear that the servlet should not continue its own response logic.

### Example JSP

`student-error.jsp`:

```
<%@ page contentType="text/html;charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Student Error</title>
</head>
<body>
    <h2>Unable to process student</h2>

    <p>
        ${requestScope.errorMessage}
    </p>
</body>
</html>
```

> Note: JSP is part of the traditional Servlet/Jakarta web application stack. In our React-based frontend, React usually renders the UI instead of JSP. The `RequestDispatcher` concept is still useful for servlet-to-servlet delegation and server-side resources.

## 4. Passing data with request attributes

Request parameters and request attributes are different.

| Request parameter             | Request attribute           |
| ----------------------------- | --------------------------- |
| Usually comes from the client | Set by server-side code     |
| Read with `getParameter()`    | Read with `getAttribute()`  |
| Values are strings            | Can hold Java objects       |
| Example: form field `name`    | Example: a `Student` object |

### Setting an attribute

```
request.setAttribute("message", "Student saved successfully.");
```

### Reading an attribute

```
String message =
        (String) request.getAttribute("message");
```

You can also pass an object:

```
Student student = new Student(1, "Asha", "asha@example.com", "Java");

request.setAttribute("student", student);
```

The forwarded resource can retrieve it:

```
Student student =
        (Student) request.getAttribute("student");
```

Request attributes are especially useful because the same request is passed to the forwarded resource.

## 5. `include()` — include another resource’s output

Suppose a servlet generates a page and wants to include a reusable footer.

```
package com.example.servlet;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/welcome")
public class WelcomeServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");

        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><body>");
        out.println("<h1>Welcome to Advance Java</h1>");

        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/footer");

        dispatcher.include(request, response);

        out.println("</body></html>");
    }
}
```

The included resource, `/footer`, can be another servlet:

```
package com.example.servlet;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/footer")
public class FooterServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        response.getWriter().println(
                "<footer><p>Advance Java Learning</p></footer>"
        );
    }
}
```

The footer output is inserted into the response produced by `WelcomeServlet`.

## 6. `forward()` vs `sendRedirect()`

This distinction is very important.

```
dispatcher.forward(request, response);
```

versus

```
response.sendRedirect("students");
```

| Feature                            | `forward()`                              | `sendRedirect()`             |
| ---------------------------------- | ---------------------------------------- | ---------------------------- |
| Happens                            | Server-side                              | Browser-side                 |
| Creates a new browser request?     | No                                       | Yes                          |
| Browser URL changes?               | No                                       | Yes                          |
| Same request attributes available? | Yes                                      | No; a new request is created |
| Typical use                        | Send data to a JSP or another servlet    | Navigate to another URL      |
| Can redirect to another website?   | No, it dispatches within the application | Yes                          |

### Example: Post/Redirect/Get

After successfully inserting a student into PostgreSQL:

```
response.sendRedirect(
        request.getContextPath() + "/students"
);
```

This is useful because refreshing the browser does not resubmit the original POST form.

Rule of thumb:

- Use `forward()` when server-side processing should continue with another resource and the same request data is needed.
- Use `include()` when another resource’s output should be inserted into the current response.
- Use `sendRedirect()` when the browser should navigate to a new URL or make a new request.

## 7. Complete example: Servlet forwards student data to another servlet

This example uses our project’s `Student` model from Lesson 11.12.

### `StudentDetailsServlet.java`

```
package com.example.servlet;

import com.example.model.Student;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/student-details")
public class StudentDetailsServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws ServletException, IOException {

        // In a real application, retrieve this from your DAO.
        Student student = new Student(
                1,
                "Asha Sharma",
                "asha@example.com",
                "Advance Java"
        );

        // Store a Java object in the request.
        request.setAttribute("student", student);

        // Forward the same request to another servlet.
        RequestDispatcher dispatcher =
                request.getRequestDispatcher("/display-student");

        dispatcher.forward(request, response);
    }
}
```

### `DisplayStudentServlet.java`

```
package com.example.servlet;

import com.example.model.Student;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/display-student")
public class DisplayStudentServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {

        Student student =
                (Student) request.getAttribute("student");

        response.setContentType("text/html;charset=UTF-8");

        if (student == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().println("Student data not found.");
            return;
        }

        response.getWriter().println("<!DOCTYPE html>");
        response.getWriter().println("<html><body>");
        response.getWriter().println("<h2>Student Details</h2>");
        response.getWriter().println(
                "<p>ID: " + student.getId() + "</p>"
        );
        response.getWriter().println(
                "<p>Name: " + escapeHtml(student.getName()) + "</p>"
        );
        response.getWriter().println(
                "<p>Email: " + escapeHtml(student.getEmail()) + "</p>"
        );
        response.getWriter().println(
                "<p>Course: " + escapeHtml(student.getCourse()) + "</p>"
        );
        response.getWriter().println("</body></html>");
    }

    private String escapeHtml(String value) {
        if (value == null) {
            return "";
        }

        return value
                .replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;")
                .replace("'", "&#x27;");
    }
}
```

### Testing

Start Tomcat and open:

```
http://localhost:8080/servlet-learning/student-details
```

The request reaches `StudentDetailsServlet`, which forwards it to `DisplayStudentServlet`. The browser’s URL remains `/student-details`.

## 8. Common mistakes

1. Writing to the response before forwarding\
   If the response is already committed, `forward()` can fail. Forward before writing response content.
2. Forgetting `return` after forwarding\
   The servlet may continue executing code that should not run.
3. Using `getParameter()` for an attribute\
   Use `getAttribute()` for data set by server-side code.
4. Expecting the browser URL to change after forwarding\
   Forwarding is internal to the server.
5. Using forwarding after a successful POST when refresh should not repeat it\
   Prefer redirecting to implement Post/Redirect/Get.
6. Assuming request attributes survive a redirect\
   A redirect creates a new request. Use a session, database, or another appropriate mechanism when data must persist across requests.

## Official documentation

- [Jakarta Servlet 6.0 — ](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/requestdispatcher?utm_source=chatgpt.com)[`RequestDispatcher`](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/requestdispatcher?utm_source=chatgpt.com)
- [Jakarta Servlet 6.0 — ](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/http/httpservletrequest?utm_source=chatgpt.com)[`HttpServletRequest`](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/http/httpservletrequest?utm_source=chatgpt.com)
- [Jakarta Servlet 6.0 — ](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/http/httpservletresponse?utm_source=chatgpt.com)[`HttpServletResponse`](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/http/httpservletresponse?utm_source=chatgpt.com)
