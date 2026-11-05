# Lesson 12.5 — Form Processing in JSP

## Learning objectives

By the end, you should be able to:

1. Create an HTML form that submits data to a JSP.
2. Read form values using `request.getParameter()`.
3. Handle GET and POST submissions.
4. Validate user input.
5. Understand why JSP form processing is usually separated from business logic.

## 1. How form processing works

A browser form sends an HTTP request containing user-entered values. The server reads those values and generates a response.

### a. HTML Form
User enters name, email, and course

### b. HTTP Request
GET query string or POST request body

### c. JSP / Server
Reads parameters, validates, prepares output

### d. HTTP Response
Generated HTML displayed in the browser

## 2. Create a basic HTML form

Create `student-form.html` in your web application:

```
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Registration</title>
</head>
<body>

    <h1>Student Registration</h1>

    <form action="student-result.jsp" method="post">
        <label for="name">Name:</label>
        <input type="text" id="name" name="studentName" required>
        <br><br>

        <label for="email">Email:</label>
        <input type="email" id="email" name="email" required>
        <br><br>

        <label for="course">Course:</label>
        <select id="course" name="course">
            <option value="Advanced Java">Advanced Java</option>
            <option value="Spring Boot">Spring Boot</option>
            <option value="React">React</option>
        </select>
        <br><br>

        <button type="submit">Register</button>
    </form>

</body>
</html>
```

### Important form attributes

| Attribute  | Purpose                                             |
| ---------- | --------------------------------------------------- |
| `action`   | URL that receives the form submission               |
| `method`   | HTTP method, usually `get` or `post`                |
| `name`     | Key used to identify the submitted parameter        |
| `required` | Browser-side validation that the field is not empty |

Remember: The server receives the input’s `name`, not its `id`. For example, `name="studentName"` becomes the parameter name.

## 3. Read form data in JSP

Create `student-result.jsp`:

```
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registration Result</title>
</head>
<body>

    <h1>Registration Result</h1>

    <%
        String studentName = request.getParameter("studentName");
        String email = request.getParameter("email");
        String course = request.getParameter("course");
    %>

    <p>Name: <%= studentName %></p>
    <p>Email: <%= email %></p>
    <p>Course: <%= course %></p>

</body>
</html>
```

### Key concept

```
request.getParameter("studentName")
```

This returns the submitted value as a `String`.

If the parameter is missing, `getParameter()` returns `null`. Therefore, server-side code should validate input instead of assuming every value exists.

## 4. GET vs POST

| GET                                                    | POST                                                   |
| ------------------------------------------------------ | ------------------------------------------------------ |
| Form values are usually placed in the URL query string | Form values are usually sent in the request body       |
| Useful for searches and filters                        | Common for submissions that create or change data      |
| Can be bookmarked with its query string                | Request body is not normally represented in a bookmark |
| Avoid for sensitive values                             | HTTPS is still required for sensitive data             |

Example GET URL:

```
/student-result.jsp?studentName=Aarav&course=Advanced+Java
```

For a registration form, POST is generally the better choice. However, POST by itself does not encrypt data; use HTTPS for data in transit.

## 5. Validate form data on the server

HTML validation such as `required` is useful, but it can be bypassed. Always validate important input on the server.

Here is a simple JSP validation example:

```
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%
    request.setCharacterEncoding("UTF-8");

    String studentName = request.getParameter("studentName");
    String email = request.getParameter("email");

    String errorMessage = null;

    if (studentName == null || studentName.trim().isEmpty()) {
        errorMessage = "Student name is required.";
    } else if (email == null || email.trim().isEmpty()) {
        errorMessage = "Email is required.";
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Validation Result</title>
</head>
<body>

<% if (errorMessage != null) { %>
    <p style="color: red;"><%= errorMessage %></p>
    <a href="student-form.html">Try again</a>
<% } else { %>
    <h2>Registration received</h2>
    <p>Name: <%= studentName %></p>
    <p>Email: <%= email %></p>
<% } %>

</body>
</html>
```

This demonstrates validation, but it is not production-ready output handling: directly printing user input can create a Cross-Site Scripting (XSS) risk. In real applications, escape user-provided values before placing them in HTML.

Also, `request.setCharacterEncoding("UTF-8")` should be called before reading request parameters.

## 6. Reading a parameter with Expression Language (EL)

Instead of Java scriptlets:

```
<%= request.getParameter("studentName") %>
```

You may use EL:

```
${param.studentName}
```

The `param` implicit object represents request parameters as single values. For example:

```
<p>Student: ${param.studentName}</p>
<p>Email: ${param.email}</p>
```

EL is cleaner for JSP views, but remember: EL does not replace server-side validation or output escaping.

## 7. Reading multiple values from a form

Some form controls submit multiple values under the same parameter name, such as checkboxes.

### Form

```
<form action="skills-result.jsp" method="post">
    <p>Select your skills:</p>

    <label>
        <input type="checkbox" name="skills" value="Java">
        Java
    </label>

    <label>
        <input type="checkbox" name="skills" value="React">
        React
    </label>

    <label>
        <input type="checkbox" name="skills" value="SQL">
        SQL
    </label>

    <button type="submit">Submit</button>
</form>
```

### JSP

```
<%
    String[] skills = request.getParameterValues("skills");
%>

<h2>Selected Skills</h2>

<%
    if (skills == null || skills.length == 0) {
        out.println("<p>No skills selected.</p>");
    } else {
        for (String skill : skills) {
            out.println("<p>" + skill + "</p>");
        }
    }
%>
```

Use:

- `getParameter("name")` when one value is expected.
- `getParameterValues("name")` when multiple values may be submitted.

The Jakarta Servlet API documents these methods and notes that `getParameter()` returns `null` if the parameter is absent.



## 8. Recommended architecture: Servlet processes, JSP displays

For learning, processing a form directly in JSP is useful. In a more maintainable MVC application, separate responsibilities:

```
HTML Form
   ↓
Servlet Controller
   ↓
Validate input
   ↓
Service / DAO (if database work is needed)
   ↓
Set request attributes
   ↓
Forward to JSP
   ↓
JSP displays result
```

For your React + Java + PostgreSQL stack, the common production approach is:

```
React Form → Servlet / REST API → Service → DAO / JDBC → PostgreSQL
                    ↓
               JSON Response
                    ↓
              React displays UI
```

So, learn JSP form processing to understand the technology, but keep business logic and database access out of JSP pages.

## 9. Common mistakes to avoid

| Mistake                                        | Better approach                |
| ---------------------------------------------- | ------------------------------ |
| Using an input’s `id` as the parameter name    | Use its `name` attribute       |
| Assuming `getParameter()` never returns `null` | Check for missing values       |
| Trusting only HTML validation                  | Validate on the server         |
| Using `getParameter()` for checkbox groups     | Use `getParameterValues()`     |
| Printing raw user input into HTML              | Escape output to prevent XSS   |
| Putting database logic in JSP                  | Use Servlet/service/DAO layers |
