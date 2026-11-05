# Lesson 12.3 — JSP Implicit Objects

Chapter 12 · Lesson 3 of 10 · Learning Mode

## 1. What are JSP Implicit Objects?

JSP implicit objects are predefined objects automatically made available by the JSP container.

We can use them directly in a JSP page without creating them ourselves.

For example, in a Servlet we normally write:

```
String name = request.getParameter("name");
```

In a JSP scriptlet, we can use the same object directly:

```
<%
    String name = request.getParameter("name");
%>
```

We didn't create the `request` object. The JSP container provides it.

The Jakarta Server Pages 3.1 specification defines nine implicit objects available to Java scripting elements, with `exception` restricted to error pages. EL also provides its own set of implicit objects.



## 2. Why are implicit objects needed?

Imagine we are building a student registration application.

We need to:

- Read form data submitted by the user.
- Send an HTTP response.
- Store information for the current request.
- Maintain login information across multiple requests.
- Access application-wide settings.
- Display output.
- Handle exceptions.

JSP implicit objects provide convenient access to the Servlet APIs that perform these tasks.

## 3. The nine JSP implicit objects

JSP scripting implicit objects

9 objects

| Object      | Java type           |
| ----------- | ------------------- |
| request     | HttpServletRequest  |
| response    | HttpServletResponse |
| out         | JspWriter           |
| session     | HttpSession         |
| application | ServletContext      |
| config      | ServletConfig       |
| pageContext | PageContext         |
| page        | Object              |
| exception   | Throwable           |

The exception object is available only in JSP error pages configured with isErrorPage="true".

Source: [Jakarta Pages 3.1 Specification, Section 1.8](https://jakarta.ee/specifications/pages/3.1/jakarta-server-pages-spec-3.1.pdf?utm_source=chatgpt.com).

## 4. Understanding each implicit object

### 4.1 `request` — HttpServletRequest

The `request` object represents the incoming HTTP request.

It allows us to read:

- Form parameters
- Request attributes
- HTTP headers
- Request method
- Request URI

Example:

```
<%
    String studentName = request.getParameter("name");
    String course = request.getParameter("course");
%>

<h2>Student: <%= studentName %></h2>
<p>Course: <%= course %></p>
```

If the request URL is:

```
student.jsp?name=Daenish&course=AdvanceJava
```

The output will be:

```
Student: Daenish
Course: AdvanceJava
```

Remember: `getParameter()` returns a `String`, or `null` if the parameter is absent.

### 4.2 `response` — HttpServletResponse

The `response` object represents the HTTP response that the server sends to the client.

It can be used to:

- Set response headers
- Set the content type
- Redirect the browser
- Set the status code

Example:

```
<%
    response.setContentType("text/html;charset=UTF-8");
%>
```

Redirect example:

```
<%
    response.sendRedirect("login.jsp");
%>
```

This instructs the browser to make a new request to `login.jsp`.

Important: Avoid redirecting after the response has already been committed.

### 4.3 `out` — JspWriter

The `out` object is used to write content into the JSP response.

Example:

```
<%
    out.println("<h1>Welcome to JSP</h1>");
    out.println("<p>Learning Advance Java</p>");
%>
```

Output:

```
<h1>Welcome to JSP</h1>
<p>Learning Advance Java</p>
```

Unlike the Servlet's `PrintWriter`, JSP's `out` is a `JspWriter`, which supports JSP buffering and nested output handling.



### 4.4 `session` — HttpSession

The `session` object allows us to store information associated with a particular user's session across multiple requests.

Example:

```
<%
    session.setAttribute("username", "Daenish");
%>

<h2>Welcome, <%= session.getAttribute("username") %></h2>
```

This is useful for:

- Login information
- Shopping carts
- User preferences
- Multi-step forms

To remove an attribute:

```
<%
    session.removeAttribute("username");
%>
```

To invalidate the session:

```
<%
    session.invalidate();
%>
```

The session object is available by default unless session creation is disabled in the JSP page directive.

### 4.5 `application` — ServletContext

The `application` object represents the entire web application.

It is shared among JSP pages and Servlets within the same web application.

Example:

```
<%
    application.setAttribute("appName", "Student Portal");
%>

<h2>
    <%= application.getAttribute("appName") %>
</h2>
```

Typical uses:

- Application-wide configuration
- Shared resources
- Application-level counters

Warning: Application attributes are shared across users. Do not store private user-specific information in them.

### 4.6 `config` — ServletConfig

The `config` object provides configuration information for the current JSP's generated Servlet.

Example:

```
<%
    String servletName = config.getServletName();
%>

<p>Servlet Name: <%= servletName %></p>
```

It can also access initialization parameters configured for that Servlet.

This object is useful when we need configuration specific to a particular Servlet or JSP.

### 4.7 `pageContext` — PageContext

The `pageContext` object provides access to the JSP page's environment and the other JSP objects.

It also supports:

- Accessing attributes in different scopes
- Including resources
- Forwarding requests
- Accessing Servlet-related objects

Example:

```
<%
    pageContext.setAttribute("message", "Hello JSP");
%>

<p>
    <%= pageContext.getAttribute("message") %>
</p>
```

It is particularly useful when working with JSP scopes, which we will explore in Lesson 12.4.

Official documentation: [Jakarta PageContext API](https://jakarta.ee/specifications/pages/3.1/apidocs/jakarta.servlet.jsp/jakarta/servlet/jsp/pagecontext?utm_source=chatgpt.com).

### 4.8 `page` — Object

The `page` object represents the current JSP page's generated Servlet instance.

In Java scripting elements, `page` is effectively equivalent to `this`.

Example:

```
<%
    out.println(page.toString());
%>
```

This object is rarely used directly in modern JSP development.

### 4.9 `exception` — Throwable

The `exception` object represents an uncaught exception that caused an error page to be invoked.

It is available only when the JSP is configured as an error page.

Example:

```
<%@ page isErrorPage="true" %>

<h2>An error occurred</h2>

<p>
    <%= exception.getMessage() %>
</p>
```

We will study proper JSP exception handling in Lesson 12.7.

## 5. JSP Implicit Objects in Expression Language (EL)

An important distinction: EL has its own implicit objects, which are different from the nine Java scripting objects.

For example:

```
${param.name}
```

This accesses a request parameter named `name`.

The JSP specification defines EL implicit objects such as `param`, `paramValues`, `header`, `cookie`, `initParam`, and the four scope maps.


| EL object          | Purpose                           | Example                         |
| ------------------ | --------------------------------- | ------------------------------- |
| `pageContext`      | Access JSP context                | `${pageContext.request.method}` |
| `pageScope`        | Page attributes                   | `${pageScope.message}`          |
| `requestScope`     | Request attributes                | `${requestScope.student}`       |
| `sessionScope`     | Session attributes                | `${sessionScope.username}`      |
| `applicationScope` | Application attributes            | `${applicationScope.appName}`   |
| `param`            | Single request parameter          | `${param.name}`                 |
| `paramValues`      | Multiple values of a parameter    | `${paramValues.hobbies[0]}`     |
| `header`           | Single HTTP header value          | `${header["user-agent"]}`       |
| `headerValues`     | Multiple header values            | `${headerValues.accept}`        |
| `cookie`           | Cookie objects by name            | `${cookie.JSESSIONID.value}`    |
| `initParam`        | Context initialization parameters | `${initParam.appVersion}`       |

### Example: EL accessing request parameters

Suppose the user opens:

```
http://localhost:8080/JspLearning/student.jsp?name=Daenish
```

In JSP:

```
<h2>Hello, ${param.name}</h2>
```

Output:

```
Hello, Daenish
```

This is simpler than:

```
<%= request.getParameter("name") %>
```

## 6. Complete Example — Student Information Using JSP Implicit Objects

Let's create a small application that demonstrates `request`, `session`, `application`, `out`, and EL.

### Project structure

```
JspLearning/
└── src/main/webapp/
    ├── student-form.jsp
    └── student-result.jsp
```

### File 1: `student-form.jsp`

```
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Form</title>
</head>
<body>

    <h1>Student Registration</h1>

    <form action="student-result.jsp" method="post">

        <label>Student Name:</label>
        <input type="text" name="name" required>

        <br><br>

        <label>Course:</label>
        <input type="text" name="course" required>

        <br><br>

        <button type="submit">Submit</button>

    </form>

</body>
</html>
```

### File 2: `student-result.jsp`

```
<%@ page contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>

<%
    // Read form data using request
    String name = request.getParameter("name");
    String course = request.getParameter("course");

    // Store username in session
    session.setAttribute("username", name);

    // Store application-wide information
    application.setAttribute("appName", "Student Portal");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Result</title>
</head>
<body>

    <h1>Registration Successful</h1>

    <p>Student Name: ${param.name}</p>

    <p>Course: ${param.course}</p>

    <p>Session Username: ${sessionScope.username}</p>

    <p>Application: ${applicationScope.appName}</p>

    <%
        out.println("<p>Welcome to our JSP application!</p>");
    %>

</body>
</html>
```

### Expected output

Browser preview

## Registration Successful

Student Name: Daenish

Course: Advance Java

Session Username: Daenish

Application: Student Portal

Welcome to our JSP application!

### What did we learn from this example?

- `request.getParameter()` reads submitted form values.
- `session.setAttribute()` stores information for the current session.
- `application.setAttribute()` stores shared application information.
- `out.println()` writes content into the response.
- EL accesses parameters and scoped attributes.

For production applications, we would normally process form data in a Servlet or controller and use JSP only to display the result.

## 7. Important Interview Questions

### Q1. What are JSP implicit objects?
Predefined objects automatically made available by the JSP container without requiring explicit declaration.

### Q2. How many JSP scripting implicit objects are there?
Nine, with `exception` available only in error pages.

### Q3. What is the difference between `request` and `session`?
`request` represents one HTTP request. `session` stores information that can persist across multiple requests associated with a user's session.

### Q4. What is the difference between `application` and `session`?
`application` is shared across the web application, while `session` is associated with a particular user's session.

### Q5. What is the purpose of `pageContext`?
It provides access to JSP page context, attributes, scopes, and several Servlet-related objects.

### Q6. What is the difference between `request.getParameter("name")` and `${param.name}`?
Both can access a request parameter. The first uses Java scripting syntax, while the second uses Expression Language.

