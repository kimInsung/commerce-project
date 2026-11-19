package com.example.commerce;

public class Customer {
    private String name;
    private String email;
    private String grade;

    public Customer(String name, String email, String grade) {
        setName(name);
        setEmail(email);
        setGrade(grade);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("고객명은 null이거나 공백일 수 없습니다.");
        }
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("이메일은 null이거나 공백일 수 없습니다.");
        }
        this.email = email;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        if (grade == null || grade.trim().isEmpty()) {
            throw new IllegalArgumentException("등급은 null이거나 공백일 수 없습니다.");
        }
        this.grade = grade;
    }
}
