# Lesson 11.6: Servlet APIs

## Part 2 — Learning objectives

By the end of Lesson 11.6, you should be able to:

1. Explain what the Servlet API is and why it is needed.
2. Identify the major Servlet API classes and interfaces.
3. Distinguish `HttpServletRequest` from `HttpServletResponse`.
4. Understand `ServletConfig` versus `ServletContext`.
5. Use `HttpSession` and `RequestDispatcher` in practical code.

## 1. What are Servlet APIs?

An API (Application Programming Interface) defines the classes, interfaces, and methods that programmers use to perform particular tasks.

The Jakarta Servlet API provides the contracts your Java web application uses to receive requests, send responses, manage servlet configuration, maintain sessions, and dispatch requests to other server-side resources. Tomcat implements these contracts.


Why do we need it? Without the Servlet API, your servlet code would have no standard way to communicate with the web container, read HTTP request data, or create an HTTP response.

For our project, we're using Jakarta Servlet 6.0 with Tomcat 10.1, so imports use `jakarta.servlet.*`, not the older `javax.servlet.*` namespace.

Official references:

- [Jakarta Servlet 6.0 API documentation](https://jakarta.ee/specifications/servlet/6.0/apidocs/?utm_source=chatgpt.com)
- [Servlet API package summary](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/package-summary?utm_source=chatgpt.com)
- [HTTP Servlet API package summary](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/http/package-summary?utm_source=chatgpt.com)

## 2. The most important Servlet APIs

Think of these as a toolbox: each API has a specific responsibility.

`Servlet`

The core interface defining servlet life-cycle methods such as `init()`, `service()`, and `destroy()`.

[Official API](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/servlet?utm_source=chatgpt.com)

`GenericServlet` and `HttpServlet`

Convenient base classes. `GenericServlet` is protocol-independent; `HttpServlet` is designed for HTTP and is the usual choice for web endpoints.

[Official ](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/http/httpservlet?utm_source=chatgpt.com)[`HttpServlet`](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/http/httpservlet?utm_source=chatgpt.com)[ API](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/http/httpservlet?utm_source=chatgpt.com)

`HttpServletRequest`

Reads request parameters, headers, HTTP method, path information, and session-related information.

[Official API](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/http/httpservletrequest?utm_source=chatgpt.com)

`HttpServletResponse`

Sets the response status and headers, specifies the content type, and sends the response body.

[Official API](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/http/httpservletresponse?utm_source=chatgpt.com)

`ServletConfig`

Provides initialization settings for a particular servlet.

`ServletContext`

Provides information and operations for the web application as a whole, such as application-level configuration and logging.

[Official API](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/servletcontext?utm_source=chatgpt.com)

`HttpSession`

Maintains user-specific state across multiple HTTP requests, such as a logged-in user's session data.

`RequestDispatcher`

Forwards a request to another server-side resource or includes that resource's output in the response.

Other important APIs we'll cover in later lessons include `Cookie`, `ServletInputStream`, and `ServletOutputStream`.

## 3. Understand request versus response

This distinction is essential in almost every servlet you write.

| `HttpServletRequest`   | `HttpServletResponse`                |
| ---------------------- | ------------------------------------ |
| Client → server        | Server → client                      |
| Reads incoming data    | Creates outgoing data                |
| `getParameter("name")` | `setStatus(201)`                     |
| `getHeader("Accept")`  | `setContentType("application/json")` |
| `getMethod()`          | `getWriter()`                        |

Example:

```
String name = request.getParameter("name");

response.setContentType("text/plain");
response.setCharacterEncoding("UTF-8");
response.getWriter().println("Hello, " + name);
```

If the client submits `name=Daenish`, the servlet can read that value from the request and send `Hello, Daenish` in the response. In a production application, validate user input before using it.

## 4. `ServletConfig` versus `ServletContext`

These two APIs sound similar, but their scope differs.

| Feature        | `ServletConfig`                      | `ServletContext`                            |
| -------------- | ------------------------------------ | ------------------------------------------- |
| Scope          | One servlet                          | One web application                         |
| Main purpose   | Servlet-specific configuration       | Application-wide information and operations |
| Obtain it with | `getServletConfig()`                 | `getServletContext()`                       |
| Example        | A servlet's initialization parameter | Application logging                         |

### Example: Read a servlet initialization parameter

```
package com.advancejava;

import java.io.IOException;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet(
    value = "/config-demo",
    initParams = {
        @jakarta.servlet.annotation.WebInitParam(
            name = "courseName",
            value = "Advance Java"
        )
    }
)
public class ConfigDemoServlet extends HttpServlet {

    private String courseName;

    @Override
    public void init() {
        ServletConfig config = getServletConfig();
        courseName = config.getInitParameter("courseName");
    }

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().println("Course: " + courseName);
    }
}
```

Test with:

`http://localhost:8080/servlet-learning/config-demo`

Expected output:

```
Course: Advance Java
```

Why initialize the field in `init()`? The configuration value is read once during initialization rather than being looked up repeatedly for every request. The `courseName` field is set during initialization and then read by requests; don't use this pattern for mutable, request-specific values.

## 5. Practical API example: `HttpSession`

HTTP is stateless: a request does not automatically remember the previous request. `HttpSession` lets your application maintain user-specific data across requests.

For example, we can count how many times a particular browser session visits a servlet.

Create `SessionDemoServlet.java`:

```

package com.advancejava;

import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/session-demo")
public class SessionDemoServlet extends HttpServlet {

    @Override
    protected void doGet(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession();

        Integer count = (Integer) session.getAttribute("visitCount");

        if (count == null) {
            count = 0;
        }

        count++;
        session.setAttribute("visitCount", count);

        response.setContentType("text/plain");
        response.setCharacterEncoding("UTF-8");

        response.getWriter().println(
            "Visits in this session: " + count
        );
    }
}

```

Test it by refreshing:

`http://localhost:8080/servlet-learning/session-demo`

You should see the count increase for the same session:

```
Visits in this session: 1
Visits in this session: 2
Visits in this session: 3
```

A new session generally starts its own count. The browser and container typically associate a session using a session cookie. This is a demonstration, not a reliable global visit counter: sessions can expire, and concurrent requests can race when updating the count.

## 6. What does `RequestDispatcher` do?

Suppose one servlet checks a request and then hands it to another resource. `RequestDispatcher` supports two commonly used operations:

- `forward()` — transfers processing to another resource on the server. The target generates the response; the response must not already be committed.
- `include()` — includes output from another resource in the current response.

Example:

```
var dispatcher =
    request.getRequestDispatcher("/result");

dispatcher.forward(request, response);
```

This forwards the request to a resource mapped to `/result` within the same web application. We will explore it in detail in Lesson 11.13: Request Dispatcher.

Official reference: [Jakarta Servlet 6.0 API — ](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/requestdispatcher?utm_source=chatgpt.com)[`RequestDispatcher`](https://jakarta.ee/specifications/servlet/6.0/apidocs/jakarta.servlet/jakarta/servlet/requestdispatcher?utm_source=chatgpt.com).
