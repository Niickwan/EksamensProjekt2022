package com.jmmnt.UseCase;

import com.jmmnt.Entities.User;



public class OperateUser {

    User u = new User();
    public User CreateUserLoginInfo(String firstName, String surname, String email, String password, int userRights) {
        return new User(firstName, surname, email, password, userRights);
    }

    public User CreateDefaultUserLoginInfo(String firstName, String surname, String email, String password) {
        return new User(firstName, surname, email, password, 2);
    }
}
