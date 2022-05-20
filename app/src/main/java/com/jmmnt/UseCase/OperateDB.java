package com.jmmnt.UseCase;

import com.jmmnt.Database.DB_Con;
import com.jmmnt.Entities.User;

public class OperateDB {
    DB_Con db_con = DB_Con.getInstance();
    OperateUser opU = new OperateUser();

    public boolean createUserInDB(User u) {
        return db_con.createNewUser(u);
    }

    public void validateLogin (String email, String password) {
        opU.setLoggedInUser(db_con.validateLogin(email, password));
    }

    public boolean isEmailOccupied(String email) {
        return db_con.isEmailOccupied(email);
    }
}