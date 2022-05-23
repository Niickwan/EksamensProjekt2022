package com.jmmnt.Entities;

public class User {
    private String email;
    private String password;
    private String firstName;
    private String surName;
    private String phoneNumber;
    private int userID;
    private int userRights;

    //general-use user constructor, not consisting potential confidential information
    public User(String firstName, String surName, String email) {
        this.firstName = firstName;
        this.surName = surName;
        this.email = email;
    }

    public User(String email, String firstName, String surName, String phoneNumber, int userID, int userRights) {
        this.email = email;
        this.firstName = firstName;
        this.surName = surName;
        this.phoneNumber = phoneNumber;
        this.userID = userID;
        this.userRights = userRights;
    }

    //user constructor for creation of username, password and user rights.
    public User(String firstName, String surname, String phoneNumber, String email, String password, int userID, int userRights) {
        this.firstName = firstName;
        this.surName = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.userID = userID;
        this.userRights = userRights;
    }

    public User(String firstName, String surname, String phoneNumber, String email, String password, int userRights) {
        this.firstName = firstName;
        this.surName = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.userRights = userRights;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstName + '\'' +
                ", surName='" + surName + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", userID=" + userID +
                ", userRights=" + userRights +
                '}';
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return firstName + " " + surName;
    }

    public String getPassword() {
        return password;
    }

    public int getUserRights() {
        return userRights;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getSurname() {
        return surName;
    }

    public String getSurName() {
        return surName;
    }

    public int getUserID() {
        return userID;
    }
}
