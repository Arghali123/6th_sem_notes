# Lesson 12.10 — JavaBeans in JSP
### 1. What is a JavaBean?
A **JavaBean** is a reusable Java class that follows standard conventions for exposing its properties through getter and setter methods.

JavaBeans help separate data from JSP presentation code.

For example, instead of writing all student data and processing logic directly inside a JSP, we can create a JavaBean:
```
public class StudentBean {
    private String name;
    private String email;
}
```

The JSP can then access the bean's properties.
### Why do we need JavaBeans?
- Reuse data objects across different JSP pages.
- Keep Java data separate from HTML.
- Make JSP pages easier to maintain.
- Transfer data between application layers.
- Work with JSP standard actions and Expression Language.
### 2. JavaBean conventions
A typical JavaBean follows these conventions:
1. The class is public.
2. It has a public no-argument constructor.
3. Its properties are generally private.
4. It exposes properties using public getter and setter methods.
5. It may implement Serializable when serialization is required.
Example:
```
public class StudentBean {

    private String name;
    private int age;

    public StudentBean() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

### Understand getter and setter methods
|Method	|Purpose|
|-------|-------|
|getName()|	Reads the name property|
|setName(String name)|	Updates the name property|
|getAge()|	Reads the age property|
|setAge(int age)|	Updates the age property|

For a boolean property, JavaBeans conventions also allow an `isProperty()` getter, such as `isActive()`.


### 3. JSP standard actions for JavaBeans
JSP provides three important standard actions:
|JSP action|	Purpose|
|----------|-----------|
|<jsp:useBean>|	Finds or creates a JavaBean|
|<jsp:setProperty>|	Sets a bean property|
|<jsp:getProperty>|	Reads and displays a bean property|

Let's understand them one by one.

**A.** <jsp:useBean>

This action finds an existing bean in the specified scope or creates one if it does not exist.

Syntax:
```
<jsp:useBean
    id="student"
    class="com.example.model.StudentBean"
    scope="request" />
```

Attributes:
- id: Name used to access the bean.
- class: Fully qualified Java class name.
- scope: Lifetime and storage scope of the bean.

The available scopes are:
- page
- request
- session
- application

**B.** <jsp:setProperty>
Used to assign a value to a bean property.
```
<jsp:setProperty
    name="student"
    property="name"
    value="Daenish" />

```
This effectively calls:
```
student.setName("Daenish");
```

**C.** <jsp:getProperty>
Used to retrieve and display a bean property.
```
<jsp:getProperty
    name="student"
    property="name" />

```
This effectively reads:
```
student.getName();
```

### 4. Complete example — Student Registration using JavaBeans
Let's build a small JSP application using a JavaBean.

**Project structure**
```
StudentRegistration/
 └── src/main/java/
      └── com/example/model/
           └── StudentBean.java

 └── src/main/webapp/
      ├── student-form.jsp
      └── student-result.jsp

```

**File 1:** `StudentBean.java`
Location:

`src/main/java/com/example/model/StudentBean.java`
```
package com.example.model;

public class StudentBean {

    private String name;
    private String email;
    private String course;

    // Public no-argument constructor
    public StudentBean() {
    }

    // Name property
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Email property
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Course property
    public String getCourse() {
        return course;
    }

    public void setCourse(String course) {
        this.course = course;
    }
}
```

**File 2:** `student-form.jsp`

This page collects student information.
```
<%@ page contentType="text/html; charset=UTF-8" %>

<!DOCTYPE html>
<html>
<head>
    <title>Student Registration</title>
</head>
<body>

    <h2>Student Registration Form</h2>

    <form action="student-result.jsp" method="post">

        <label>Name:</label>
        <input type="text" name="name" required>
        <br><br>

        <label>Email:</label>
        <input type="email" name="email" required>
        <br><br>

        <label>Course:</label>
        <input type="text" name="course" required>
        <br><br>

        <button type="submit">Register</button>

    </form>

</body>
</html>
```

**File 3:** `student-result.jsp`

This page creates the bean, populates its properties, and displays the information.
```
<%@ page contentType="text/html; charset=UTF-8" %>

<jsp:useBean
    id="student"
    class="com.example.model.StudentBean"
    scope="request" />

<jsp:setProperty
    name="student"
    property="*" />

<!DOCTYPE html>
<html>
<head>
    <title>Registration Result</title>
</head>
<body>

    <h2>Registration Successful!</h2>

    <p>
        Name:
        <jsp:getProperty
            name="student"
            property="name" />
    </p>

    <p>
        Email:
        <jsp:getProperty
            name="student"
            property="email" />
    </p>

    <p>
        Course:
        <jsp:getProperty
            name="student"
            property="course" />
    </p>

</body>
</html>
```

#### Understand property="*"

This is an important feature.
```
<jsp:setProperty name="student" property="*" />
```

It automatically matches incoming request parameter names with bean property names.

For example:
|HTML form parameter|	JavaBean setter|
|-------------------|------------------|
|name	|setName()|
|email	|setEmail()|
|course	|setCourse()|

The names must match. JSP uses the appropriate setter to populate the bean.

**Important security note:** Automatic property population is convenient for learning, but in production, do not blindly bind all request parameters to sensitive domain objects. Validate input and use a dedicated request DTO/controller.

### 5. Using JavaBeans with Expression Language (EL)
You can display bean properties using EL instead of `<jsp:getProperty>.`

For example:
```
<p>Name: ${student.name}</p>
<p>Email: ${student.email}</p>
<p>Course: ${student.course}</p>
```

EL property access resolves the corresponding JavaBean getter methods.

For example:
```
${student.name}
```

accesses the value exposed by:
```
student.getName();
```

This is cleaner than writing Java scriptlets in JSP.
