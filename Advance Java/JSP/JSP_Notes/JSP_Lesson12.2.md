# Lesson 12.2 — JSP Syntax

## 1. What is JSP syntax?

JSP syntax is the set of rules and special elements that allow us to combine HTML, Java code, Expression Language (EL), directives, and actions inside a JSP page.

The Jakarta Server Pages specification organizes JSP elements into three main categories:

1. Directives — provide information to the JSP container.
2. Scripting elements — allow Java declarations, statements, and expressions.
3. Actions — perform operations during request processing.

Source: [Jakarta Server Pages 3.1 Specification, Section 1.3](https://jakarta.ee/specifications/pages/3.1/jakarta-server-pages-spec-3.1.pdf?utm_source=chatgpt.com).

## 2. JSP syntax overview

JSP elements

Core syntax

DIRECTIVES — configuration

`<%@ page ... %>`

`<%@ include ... %>`

`<%@ taglib ... %>`

SCRIPTING ELEMENTS — Java code

`<%! declaration %>`

`<% scriptlet %>`

`<%= expression %>`

EXPRESSION LANGUAGE — simplified data access

`${expression}`

STANDARD ACTIONS — JSP operations

`<jsp:include>`

`<jsp:useBean>`

`<jsp:forward>`

Let's study each one.

## 3. JSP Directives

A directive provides instructions to the JSP container during the translation phase.

Directives generally do not directly produce output in the response.

There are three main directives:

| Directive | Purpose                                 |
| --------- | --------------------------------------- |
| `page`    | Configure page properties               |
| `include` | Include another file during translation |
| `taglib`  | Declare a tag library                   |

### 3.1 Page directive

Syntax:

```
<%@ page attribute="value" %>
```

Example:

```
<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
```

This tells the JSP container:

- Use Java as the scripting language.
- Return HTML content.
- Use UTF-8 encoding for the page.

#### Common page directive attributes

| Attribute      | Purpose                                              |
| -------------- | ---------------------------------------------------- |
| `language`     | Specifies the scripting language                     |
| `contentType`  | Sets the response content type                       |
| `pageEncoding` | Sets the JSP source encoding                         |
| `import`       | Imports Java classes                                 |
| `session`      | Controls availability of the implicit session object |
| `errorPage`    | Specifies an error-handling JSP                      |
| `isErrorPage`  | Indicates whether the page handles exceptions        |

Example:

```
<%@ page import="java.util.Date" %>

<html>
<body>

    <h2>Current Date</h2>

    <%= new Date() %>

</body>
</html>
```

Here, `import` allows us to use `Date` without writing its fully qualified name.

### 3.2 Include directive

The include directive inserts the contents of another file during JSP translation.

Syntax:

```
<%@ include file="header.jsp" %>
```

Example:

header.jsp

```
<header>
    <h1>My Java Website</h1>
</header>
```

home.jsp

```
<%@ page contentType="text/html; charset=UTF-8" %>

<html>
<body>

    <%@ include file="header.jsp" %>

    <h2>Home Page</h2>

</body>
</html>
```

The header content becomes part of the translated JSP page.

Why is it useful? It helps reuse common page sections such as headers, navigation bars, and footers.

### 3.3 Taglib directive

The taglib directive declares a tag library so that we can use its tags in JSP.

Syntax:

```
<%@ taglib prefix="prefixName" uri="libraryURI" %>
```

Example using JSTL:

```
<%@ taglib prefix="c"
           uri="jakarta.tags.core" %>

<c:if test="${not empty student}">
    <p>Student information is available.</p>
</c:if>
```

Here:

- `c` is the prefix.
- `jakarta.tags.core` identifies the JSTL core tag library.
- `<c:if>` conditionally displays content.

We'll explore JSTL and EL in later lessons.

## 4. JSP Scripting Elements

Scripting elements allow Java code to be embedded inside JSP pages.

There are three types:

| Element     | Syntax       | Purpose                       |
| ----------- | ------------ | ----------------------------- |
| Declaration | `<%! ... %>` | Declare variables and methods |
| Scriptlet   | `<% ... %>`  | Execute Java statements       |
| Expression  | `<%= ... %>` | Evaluate and output a value   |

These are part of the JSP specification, although modern JSP development generally prefers EL and tag libraries over embedded Java code.



### 4.1 JSP Declaration

A declaration defines a variable or method at the JSP class level.

Syntax:

```
<%! Java declaration %>
```

Example:

```
<%! 
    private String getCourseName() {
        return "Advance Java";
    }
%>

<html>
<body>

    <h2>Course: <%= getCourseName() %></h2>

</body>
</html>
```

Important: A declaration does not directly produce output.

#### A serious caution about declaration variables

```
<%!
    int counter = 0;
%>
```

This variable is a field of the generated Servlet instance, not a local variable created separately for every request.

Because a Servlet instance may handle concurrent requests, mutable declaration fields can create thread-safety problems.

Avoid using declaration fields for request-specific data.

### 4.2 JSP Scriptlet

A scriptlet contains Java statements that execute when the JSP processes a request.

Syntax:

```
<% Java statements %>
```

Example:

```
<%
    String name = "Daenish";
    int marks = 85;

    if (marks >= 40) {
        out.println("Pass");
    } else {
        out.println("Fail");
    }
%>
```

Scriptlets can contain:

- Variables
- Conditions
- Loops
- Method calls
- Other valid Java statements

However, avoid placing business logic or database operations directly inside scriptlets in production applications.

### 4.3 JSP Expression

A JSP expression evaluates a Java expression and inserts its result into the response.

Syntax:

```
<%= Java expression %>
```

Example:

```
<%
    String name = "Daenish";
    int marks = 85;
%>

<p>Name: <%= name %></p>
<p>Marks: <%= marks %></p>
<p>Result: <%= marks >= 40 ? "Pass" : "Fail" %></p>
```

Expected output:

Name: Daenish

Marks: 85

Result: Pass

Remember: Do not put a semicolon at the end of a JSP expression.

Correct:

```
<%= 10 + 20 %>
```

Incorrect:

```
<%= 10 + 20; %>
```

## 5. Expression Language (EL)

Expression Language (EL) provides a simpler way to access data in JSP without writing Java scriptlets.

Syntax:

```
${expression}
```

### Example: Displaying data using EL

Suppose a Servlet stores a student's name in a request attribute:

```
request.setAttribute("studentName", "Daenish");

request.getRequestDispatcher("/student.jsp")
       .forward(request, response);
```

In `student.jsp`:

```
<h2>Welcome, ${studentName}!</h2>
```

Output:

```
Welcome, Daenish!
```

Compare the two approaches:

| Scriptlet                                    | EL                   |
| -------------------------------------------- | -------------------- |
| `<%= request.getAttribute("studentName") %>` | `${studentName}`     |
| Requires Java syntax                         | Simpler syntax       |
| Can mix Java with HTML                       | Cleaner presentation |

Best practice: Prefer EL for displaying data in JSP views.

## 6. JSP Comments

JSP supports two major comment types.

### HTML comments

```
<!-- This comment is sent to the browser -->
```

The browser receives this comment as part of the HTML source.

### JSP comments

```
<%-- This comment is processed on the server --%>
```

JSP comments are removed during JSP processing and are not sent to the browser.

| Type         | Visible in browser source? |
| ------------ | -------------------------- |
| HTML comment | Yes                        |
| JSP comment  | No                         |

Use JSP comments when you want to document server-side JSP code without exposing the comment to the client.

## 7. JSP Standard Actions

Standard actions use XML-style syntax and perform operations during request processing.

### Common JSP actions

| Action              | Purpose                                  |
| ------------------- | ---------------------------------------- |
| `<jsp:include>`     | Include another resource at request time |
| `<jsp:forward>`     | Forward a request to another resource    |
| `<jsp:useBean>`     | Locate or create a JavaBean              |
| `<jsp:setProperty>` | Set a JavaBean property                  |
| `<jsp:getProperty>` | Retrieve a JavaBean property             |
| `<jsp:param>`       | Pass a parameter to an action            |

### Example: Dynamic include

```
<jsp:include page="header.jsp" />
```

Unlike the include directive, this action includes the resource at request time.

### Include directive vs include action

| Feature          | Include directive           | Include action               |
| ---------------- | --------------------------- | ---------------------------- |
| Syntax           | `<%@ include file="..." %>` | `<jsp:include page="..." />` |
| Processing       | Translation time            | Request time                 |
| Included content | Source is combined          | Resource is executed         |
| Best use         | Static page fragments       | Dynamic resources            |

## 8. Complete JSP Syntax Example

Let's combine what we learned into one JSP page.

### File: `student.jsp`

```
<%@ page language="java"
         contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"
         import="java.time.LocalDate" %>

<%!
    // Declaration
    private String getCourse() {
        return "Advance Java";
    }
%>

<%
    // Scriptlet
    String studentName = "Daenish";
    int marks = 85;

    String result = marks >= 40 ? "Pass" : "Fail";
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Student Information</title>
</head>

<body>

    <%-- JSP comment --%>

    <h1>Student Information</h1>

    <p>Name: <%= studentName %></p>

    <p>Course: <%= getCourse() %></p>

    <p>Marks: <%= marks %></p>

    <p>Result: <%= result %></p>

    <p>Date: <%= LocalDate.now() %></p>

</body>
</html>
```

### What we used

| JSP element    | Where it appears      |
| -------------- | --------------------- |
| Page directive | Beginning of the file |
| Declaration    | `getCourse()` method  |
| Scriptlet      | Student variables     |
| Expression     | Displaying values     |
| JSP comment    | Server-side comment   |
| HTML           | Page structure        |

This example is useful for learning syntax. In a real application, we would move the student data and business logic into Java classes and use EL to display the data.

## 9. Modern JSP Best Practices

The JSP specification supports scriptlets, but modern Java web development encourages a cleaner separation of concerns.



| Avoid                          | Prefer                                               |
| ------------------------------ | ---------------------------------------------------- |
| Java code inside JSP           | Java classes and Servlets                            |
| Scriptlets for displaying data | Expression Language                                  |
| Java loops in JSP              | JSTL tags                                            |
| Database queries in JSP        | DAO/service layer                                    |
| Mutable declaration fields     | Request/session attributes or properly managed beans |

For example, instead of:

```
<%
    String name = (String) request.getAttribute("studentName");
%>

<h2><%= name %></h2>
```

Prefer:

```
<h2>${studentName}</h2>
```
