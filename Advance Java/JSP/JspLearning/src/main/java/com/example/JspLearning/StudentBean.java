package com.example.JspLearning;

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
