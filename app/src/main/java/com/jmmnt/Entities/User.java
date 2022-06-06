package com.jmmnt.Entities;

public class User {

    /**
     * User class storing information about a user.
     */

    private String email;
    private String password;
    private String firstname;
    private String surname;
    private String phonenumber;
    private int userID;
    private int userRights;

    //general-use user constructor, not consisting potential confidential information
    public User(String firstName, String surName, String email) {
        this.firstname = firstName;
        this.surname = surName;
        this.email = email;
    }


    public User(String email, String firstName, String surName, String phoneNumber, int userID, int userRights) {
        this.email = email;
        this.firstname = firstName;
        this.surname = surName;
        this.phonenumber = phoneNumber;
        this.userID = userID;
        this.userRights = userRights;
    }

    //user constructor for creation of username, password and user rights.
    public User(String firstName, String surname, String phoneNumber, String email, String password, int userID, int userRights) {
        this.firstname = firstName;
        this.surname = surname;
        this.phonenumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.userID = userID;
        this.userRights = userRights;
    }

    public User(String firstName, String surname, String phoneNumber, String email, String password, int userRights) {
        this.firstname = firstName;
        this.surname = surname;
        this.phonenumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.userRights = userRights;
    }

    public User(String firstName, String surname, String phoneNumber, int userID) {
        this.firstname = firstName;
        this.surname = surname;
        this.phonenumber = phoneNumber;
        this.userID = userID;
    }

    //default user
    public User() {
        this.firstname = "Afvent eller vælg medarbejder";
        this.surname = "";
        this.phonenumber = "";
        this.email = "";
        this.userID = -1;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", firstName='" + firstname + '\'' +
                ", surName='" + surname + '\'' +
                ", phoneNumber='" + phonenumber + '\'' +
                ", userID=" + userID +
                ", userRights=" + userRights +
                '}';
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getEmail() {
        return email;
    }

    public String getFullName() {
        return firstname + " " + surname;
    }

    public String getPassword() {
        return password;
    }

    public int getUserRights() {
        return userRights;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getSurname() {
        return surname;
    }

    public int getUserID() {
        return userID;
    }

}
