package com.example.servlet_learning.Db;

public class Student {

    private long id;
    private String name;
    private String email;
    private String course;

    public Student() {
    }

    public Student(
            long id,
            String name,
            String email,
            String course) {

        this.id = id;
        this.name = name;
        this.email = email;
        this.course = course;
    }

    public Student(
            String name,
            String email,
            String course) {

        this.name = name;
        this.email = email;
        this.course = course;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCourse() {
        return course;
    }
}