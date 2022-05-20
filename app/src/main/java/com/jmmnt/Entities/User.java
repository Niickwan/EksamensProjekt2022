package com.jmmnt.Entities;

import com.jmmnt.UI.FragmentPopupMenu;

public class User {
    private String email;
    private String password;
    private String firstName;
    private String surName;
    private String phoneNumber;
    private int userID;
    private int userRights;

    private User(){
    }


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
    public User(String firstName, String surname, String phoneNumber, String email, String password, int userRights) {
        this.firstName = firstName;
        this.surName = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.userRights = userRights;
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


}
