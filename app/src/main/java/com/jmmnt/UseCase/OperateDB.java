package com.jmmnt.UseCase;

import com.jmmnt.Database.DB_Con;
import com.jmmnt.Entities.User;

public class OperateDB {
    DB_Con db_con = DB_Con.getInstance();

    public boolean createUserInDB(User u) {
        return db_con.createNewUser(u);
    }

    public int validateLogin (String email, String password) {
        return db_con.validateLogin(email, password);
    }

    public boolean isEmailOccupied(String email) {
        return db_con.isEmailOccupied(email);
    }

    public boolean isPhonenumberOccupied(String phoneNumber) {
        return db_con.isPhonenumberOccupied(phoneNumber);
    }
}