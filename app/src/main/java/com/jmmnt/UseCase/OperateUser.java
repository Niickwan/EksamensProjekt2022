package com.jmmnt.UseCase;

import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.Entities.User;

public class OperateUser {

    private LoggedInUser loggedInUser = LoggedInUser.getInstance();

    public User CreateUserLoginInfo(String firstName, String surname, String phoneNumber, String email, String password, int userRights) {
        return new User(firstName, surname, phoneNumber, email, password, userRights);
    } //TODO WhatToDo

    public User CreateDefaultUserLoginInfo(String firstName, String surname, String phoneNumber, String email, String password) {
        return new User(firstName, surname, phoneNumber, email, password, 2);
    }

    public void setLoggedInUser(User user){
        loggedInUser.setUser(user);
    }
}
