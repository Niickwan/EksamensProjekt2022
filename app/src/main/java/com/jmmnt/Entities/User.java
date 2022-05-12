package com.jmmnt.Entities;

public class User extends Person {
    String email;
    String password;
    int userRights;

    //general-use user constructor, not consisting potential confidential information
    public User(String firstName, String lastName, String email) {
        super(firstName, lastName);
        this.email = email;
    }

    //user constructor for creation of username, password and userrights.
    public User(String firstName, String surname, String email, String password, int userRights) {
        super(firstName, surname);
        this.email = email;
        this.password = password;
        this.userRights = userRights;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public int getUserRights() {
        return userRights;
    }
}
