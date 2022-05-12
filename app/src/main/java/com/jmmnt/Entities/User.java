package com.jmmnt.Entities;

public class User extends Person {
    String employeeNumber;

    public User(String firstName, String lastName, String employeeNumber) {
        super(firstName, lastName);
        this.employeeNumber = employeeNumber;
    }
}
