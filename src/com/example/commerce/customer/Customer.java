package com.example.commerce.customer;

public class Customer {
    private String name;
    private String email;
    private Grade grade;

    public Customer(String name, String email, Grade grade) {
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

    public Grade getGrade() {
        return grade;
    }

    public void setGrade(Grade grade) {
        if (grade == null) {
            throw new IllegalArgumentException("등급은 null일 수 없습니다.");
        }
        this.grade = grade;
    }
}
