# Lesson 11.9: Reading Initialization Parameters

## 1. What are initialization parameters?

Initialization parameters are configuration values supplied to a servlet or an entire web application when it is configured. A servlet can read these values without hardcoding them directly into its Java code.

For example, imagine your application needs a support email address, a default course name, or a page size.

Instead of writing:

```
String supportEmail = "support@example.com";
```

inside your servlet, you can define the value in the application's configuration and retrieve it when the servlet runs.

### Why do we need them?

- Maintainability: change configuration without changing Java logic.
- Reusability: configure different servlet instances differently.
- Separation of concerns: keep application settings separate from request-processing code.
- Consistency: share settings across multiple servlets when they are application-wide.

The official [Tomcat 10.1 Servlet 6.0 documentation](https://tomcat.apache.org/tomcat-10.1-doc/servletapi/jakarta/servlet/ServletConfig.html?utm_source=chatgpt.com) describes `ServletConfig` as the servlet's configuration object. `ServletContext` provides access to initialization parameters for the whole web application.


## 2. Two types of initialization parameters

## A. Servlet-specific parameters

ServletConfig

These settings belong to one particular servlet.

Example: configure a maximum number of students displayed by `StudentServlet`.

```
String maxStudents =
    getServletConfig().getInitParameter("maxStudents");
```

## B. Application-wide parameters

ServletContext

These settings belong to the whole web application and can be read by its servlets.

Example: configure the application's support email.

```
String supportEmail =
    getServletContext().getInitParameter("supportEmail");
```

Remember this distinction:

| Feature                  | `ServletConfig`           | `ServletContext`           |
| ------------------------ | ------------------------- | -------------------------- |
| Scope                    | One servlet               | Entire web application     |
| Configuration purpose    | Servlet-specific          | Shared application setting |
| Read a value             | `getInitParameter()`      | `getInitParameter()`       |
| Read all parameter names | `getInitParameterNames()` | `getInitParameterNames()`  |

Both APIs return a `String` for an individual value, or `null` when the named parameter does not exist.


## 3. Configure initialization parameters in `web.xml`

For this lesson, we'll use `web.xml`, the deployment descriptor of your web application. This makes the difference between servlet-specific and application-wide settings easy to see in one place.

Create or update:

`src/main/webapp/WEB-INF/web.xml`

```

<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee
         https://jakarta.ee/xml/ns/jakartaee/web-app_6_0.xsd"
         version="6.0">

    <!-- Application-wide initialization parameters -->
    <context-param>
        <param-name>appName</param-name>
        <param-value>Advance Java Learning</param-value>
    </context-param>

    <context-param>
        <param-name>supportEmail</param-name>
        <param-value>support@example.com</param-value>
    </context-param>

    <!-- Register the servlet -->
    <servlet>
        <servlet-name>InitParameterServlet</servlet-name>
        <servlet-class>
            com.advancejava.InitParameterServlet
        </servlet-class>

        <!-- Servlet-specific initialization parameters -->
        <init-param>
            <param-name>defaultCourse</param-name>
            <param-value>Advanced Java</param-value>
        </init-param>

        <init-param>
            <param-name>maxStudents</param-name>
            <param-value>50</param-value>
        </init-param>
    </servlet>

    <!-- Map the servlet to a URL -->
    <servlet-mapping>
        <servlet-name>InitParameterServlet</servlet-name>
        <url-pattern>/init-parameters</url-pattern>
    </servlet-mapping>

</web-app>

```

What did we configure?

- `appName` and `supportEmail` are application-wide parameters declared with `<context-param>`.
- `defaultCourse` and `maxStudents` are servlet-specific parameters declared inside `<servlet>` using `<init-param>`.
- `/init-parameters` is the URL mapping for our servlet.

Because this `web.xml` declares the servlet mapping, we don't need `@WebServlet` on the example servlet.

## 4. Full Java Servlet example

Create:

`src/main/java/com/advancejava/InitParameterServlet.java`

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

public class InitParameterServlet extends HttpServlet {

    private String defaultCourse;
    private int maxStudents;
    private String appName;
    private String supportEmail;

    @Override
    public void init() throws ServletException {

        // Servlet-specific configuration
        defaultCourse = getServletConfig()
                .getInitParameter("defaultCourse");

        String maxStudentsValue = getServletConfig()
                .getInitParameter("maxStudents");

        if (defaultCourse == null || maxStudentsValue == null) {
            throw new ServletException(
                    "Required servlet parameters are missing.");
        }

        try {
            maxStudents = Integer.parseInt(maxStudentsValue);
        } catch (NumberFormatException e) {
            throw new ServletException(
                    "maxStudents must be a valid integer.", e);
        }

        if (maxStudents <= 0) {
            throw new ServletException(
                    "maxStudents must be greater than zero.");
        }

        // Application-wide configuration
        appName = getServletContext()
                .getInitParameter("appName");

        supportEmail = getServletContext()
                .getInitParameter("supportEmail");

        if (appName == null || supportEmail == null) {
            throw new ServletException(
                    "Required application parameters are missing.");
        }
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response)
            throws IOException {

        response.setContentType("text/plain;charset=UTF-8");

        try (PrintWriter out = response.getWriter()) {
            out.println("Application Name: " + appName);
            out.println("Support Email: " + supportEmail);
            out.println("Default Course: " + defaultCourse);
            out.println("Maximum Students: " + maxStudents);

            out.println();
            out.println("Servlet-specific parameter names:");

            Enumeration<String> names =
                    getServletConfig().getInitParameterNames();

            while (names.hasMoreElements()) {
                String name = names.nextElement();
                out.println(name + " = "
                        + getServletConfig().getInitParameter(name));
            }

            out.println();
            out.println("Application-wide parameter names:");

            Enumeration<String> contextNames =
                    getServletContext().getInitParameterNames();

            while (contextNames.hasMoreElements()) {
                String name = contextNames.nextElement();
                out.println(name + " = "
                        + getServletContext().getInitParameter(name));
            }
        }
    }
}

```

Remove the unused `WebServlet` import if your IDE flags it; this example uses `web.xml` for servlet registration.

### Understand the important parts

- `init()` runs when the container initializes the servlet. It's a suitable place to read and validate configuration that the servlet needs.
- `getServletConfig()` retrieves this servlet's configuration.
- `getServletContext()` retrieves the web application's context.
- `getInitParameterNames()` lets us inspect the names of configured parameters.
- `Integer.parseInt()` converts the `maxStudents` string into an integer. Initialization parameter values are strings, so validate them before using them as numbers.

The servlet lifecycle and configuration APIs are described in the [official Servlet 6.0 documentation](https://tomcat.apache.org/tomcat-10.1-doc/servletapi/jakarta/servlet/Servlet.html?utm_source=chatgpt.com).



## 5. Build and test in IntelliJ IDEA

1. Save both files.
2. Run this command in the project terminal:
   ```
   mvn clean package
   ```
3. Ensure the generated WAR is redeployed to Tomcat.
4. Open this URL:

   `http://localhost:8080/servlet-learning/init-parameters`

Expected output:

```
Application Name: Advance Java Learning
Support Email: support@example.com
Default Course: Advanced Java
Maximum Students: 50

Servlet-specific parameter names:
defaultCourse = Advanced Java
maxStudents = 50

Application-wide parameter names:
appName = Advance Java Learning
supportEmail = support@example.com
```

The order of the parameter names may vary.

Security note: Initialization parameters are configuration, not a secure secret store. Don't put real database passwords, API keys, or other sensitive credentials in source-controlled `web.xml`.

## 6. Alternative: use `@WebInitParam`

You can also configure servlet-specific parameters directly in Java using `@WebServlet` and `@WebInitParam`:

```
import jakarta.servlet.annotation.WebInitParam;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;

@WebServlet(
    urlPatterns = "/annotation-config",
    initParams = {
        @WebInitParam(
            name = "defaultCourse",
            value = "Advanced Java"
        ),
        @WebInitParam(
            name = "maxStudents",
            value = "50"
        )
    }
)
public class AnnotationConfigServlet extends HttpServlet {
    // Read values with getServletConfig().getInitParameter(...)
}
```

Use this annotation-based approach when the configuration is small and belongs to a single servlet. Use `web.xml` when you want the deployment descriptor to hold the configuration explicitly. Don't register the same example servlet using both approaches unnecessarily.
