# Lesson 11.8 — Reading All Form Parameters


## 1. What does “reading all form parameters” mean?

In Lesson 11.7, we read individual fields using `request.getParameter()`:

```
String name = request.getParameter("studentName");
String email = request.getParameter("email");
String course = request.getParameter("course");
```

This works well when we know the exact parameter names. But what if a form has 20 fields, or we don't know the field names in advance?

We can use methods from the `ServletRequest` API to retrieve all parameter names and values. These methods are documented in the [official Tomcat 10.1 ServletRequest API](https://tomcat.apache.org/tomcat-10.1-doc/servletapi/jakarta/servlet/ServletRequest.html?utm_source=chatgpt.com).&#x20;

[image](https://www.google.com/s2/favicons?domain=https://tomcat.apache.org\&sz=32)

Apache Tomcat 10.1.61)



### Why do we need this?

- To process forms with many fields without hardcoding every field name.
- To build generic form-processing and debugging utilities.
- To handle fields that allow multiple selections, such as checkboxes.
- To inspect all submitted parameters before applying validation or business logic.

## 2. The three important methods

### 1. `getParameterNames()`

Returns an `Enumeration<String>` containing the names of all request parameters.

```
Enumeration<String> names = request.getParameterNames();
```

Use it when you want to iterate over parameter names.

### 2. `getParameterMap()`

Returns a `Map<String, String[]>` containing parameter names and their values.

```
Map<String, String[]> params = request.getParameterMap();
```

Use it when you want to access all parameters as a map.

### 3. `getParameterValues(name)`

Returns a `String[]` containing all values associated with one parameter.

```
String[] skills = request.getParameterValues("skills");
```

Use it when one field can submit multiple values.

These return types matter: `getParameterNames()` returns an `Enumeration`, `getParameterMap()` returns a map whose values are arrays, and `getParameterValues()` returns a string array.



## 3. Full example: HTML form

Create or update this file:

`src/main/webapp/all-parameters.html`

```

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Read All Form Parameters</title>
</head>
<body>
    <h2>Student Registration</h2>

    <form action="read-all-parameters" method="post">
        <label>Student Name:</label>
        <input type="text" name="studentName" required>
        <br><br>

        <label>Email:</label>
        <input type="email" name="email" required>
        <br><br>

        <label>Course:</label>
        <select name="course">
            <option value="Java">Java</option>
            <option value="React">React</option>
            <option value="PostgreSQL">PostgreSQL</option>
        </select>
        <br><br>

        <p>Select your skills:</p>
        <label>
            <input type="checkbox" name="skills" value="Java">
            Java
        </label>
        <label>
            <input type="checkbox" name="skills" value="SQL">
            SQL
        </label>
        <label>
            <input type="checkbox" name="skills" value="React">
            React
        </label>
        <br><br>

        <button type="submit">Submit</button>
    </form>
</body>
</html>

```

Notice that all three skill checkboxes share the same `name="skills"` but have different values. If the user selects Java and SQL, the request contains two values for the `skills` parameter.

## 4. Full Java Servlet

Create this file:

`src/main/java/com/advancejava/ReadAllParametersServlet.java`

```

package com.advancejava;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.Map;

@WebServlet("/read-all-parameters")
public class ReadAllParametersServlet extends HttpServlet {

    @Override
    protected void doPost(
            HttpServletRequest request,
            HttpServletResponse response)
            throws ServletException, IOException {

        // Set encoding before reading request parameters.
        request.setCharacterEncoding("UTF-8");

        response.setContentType("text/plain;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {

            out.println("=== Using getParameterNames() ===");

            Enumeration<String> names =
                    request.getParameterNames();

            while (names.hasMoreElements()) {
                String name = names.nextElement();

                String[] values =
                        request.getParameterValues(name);

                out.println(name + " = "
                        + String.join(", ", values));
            }

            out.println();
            out.println("=== Using getParameterMap() ===");

            Map<String, String[]> parameterMap =
                    request.getParameterMap();

            for (Map.Entry<String, String[]> entry
                    : parameterMap.entrySet()) {

                String name = entry.getKey();
                String[] values = entry.getValue();

                out.println(name + " = "
                        + String.join(", ", values));
            }
        }
    }
}

```

### Understand the code

1. `@WebServlet("/read-all-parameters")` maps the servlet to the URL path.
2. `doPost()` handles the form's POST request.
3. `request.setCharacterEncoding("UTF-8")` sets request-body decoding before parameters are read.
4. `getParameterNames()` lets us loop through every submitted parameter name.
5. `getParameterValues(name)` retrieves every value for a given parameter.
6. `getParameterMap()` provides another way to iterate through all parameters.

Important: The parameter map is immutable. Do not try to add or remove entries from `request.getParameterMap()`. Also, do not assume parameters arrive in a particular order.

## 5. How to run it

1. Save both files in the locations shown above.
2. Build your project from IntelliJ's Terminal:

   `bash       mvn clean package       `
3. Redeploy or restart your application in Tomcat if needed.
4. Open the form in your browser:

   `http://localhost:8080/servlet-learning/all-parameters.html`
5. Enter the student name and email, choose a course, select two or more skills, and click Submit.

## 6. Expected output

If you submit the following values:

- Student name: `Asha`
- Email: `asha@example.com`
- Course: `Java`
- Skills: `Java` and `SQL`

The response will look similar to this:

```
=== Using getParameterNames() ===
studentName = Asha
email = asha@example.com
course = Java
skills = Java, SQL

=== Using getParameterMap() ===
studentName = Asha
email = asha@example.com
course = Java
skills = Java, SQL
```

The ordering may differ. The two sections demonstrate different ways to read the same submitted parameters.

## 7. Test with Postman

You can test the servlet without opening the HTML form.

POST

Request URL

`http://localhost:8080/servlet-learning/read-all-parameters`

Body → x-www-form-urlencoded

| Key           | Value             |
| ------------- | ----------------- |
| `studentName` | Asha              |
| `email`       | asha\@example.com |
| `course`      | Java              |
| `skills`      | Java              |
| `skills`      | SQL               |

Add `skills` twice as separate rows to send two values under the same parameter name.

## 8. Important concepts to remember

| Method                         | Return type             | Best use                          |
| ------------------------------ | ----------------------- | --------------------------------- |
| `getParameter("name")`         | `String`                | One value                         |
| `getParameterNames()`          | `Enumeration<String>`   | All parameter names               |
| `getParameterValues("skills")` | `String[]`              | Multiple values for one parameter |
| `getParameterMap()`            | `Map<String, String[]>` | All names and values              |

Security note: This example returns plain text to avoid inserting submitted values directly into HTML. In a real application, validate every field on the server, enforce appropriate limits, and escape user-controlled data before displaying it as HTML.
