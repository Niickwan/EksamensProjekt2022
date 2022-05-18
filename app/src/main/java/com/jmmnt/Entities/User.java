package com.jmmnt.Entities;

public class User{
    String email;
    String password;
    String firstName;
    String surName;
    int userRights;

    //general-use user constructor, not consisting potential confidential information
    public User(String firstName, String surName, String email) {
        this.firstName = firstName;
        this.surName = surName;
        this.email = email;
    }

    //user constructor for creation of username, password and userrights.
    public User(String firstName, String surname, String email, String password, int userRights) {
        this.firstName = firstName;
        this.surName = surname;
        this.email = email;
        this.password = password;
        this.userRights = userRights;
    }

    public User() {
        super();
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {return firstName+" "+surName;}

    public String getPassword() {
        return password;
    }

    public int getUserRights() {
        return userRights;
    }
}
