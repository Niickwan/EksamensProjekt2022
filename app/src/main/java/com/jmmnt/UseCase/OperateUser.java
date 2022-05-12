package com.jmmnt.UseCase;

import com.jmmnt.Database.DB_Con;
import com.jmmnt.Entities.User;

import java.sql.SQLException;

public class OperateUser {

    public User CreateUserLoginInfo(String firstName, String surname, String email, String password, String userRights){
        return new User(firstName, surname, email, password, userRights);
    }

}
