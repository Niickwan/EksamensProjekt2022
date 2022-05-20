package com.jmmnt.UseCase;

import com.jmmnt.Entities.User;



public class OperateUser {

    public User CreateUserLoginInfo(String firstName, String surname, String phoneNumber, String email, String password, int userRights) {
        return new User(firstName, surname, phoneNumber, email, password, userRights);
    }

    public User CreateDefaultUserLoginInfo(String firstName, String surname, String phoneNumber, String email, String password) {
        return new User(firstName, surname, phoneNumber, email, password, 2);
    }
}
