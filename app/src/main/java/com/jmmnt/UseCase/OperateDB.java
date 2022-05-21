package com.jmmnt.UseCase;

import com.jmmnt.Database.DB_Con;
import com.jmmnt.Entities.User;

public class OperateDB {

    private DB_Con db_con = DB_Con.getInstance();
    private OperateUser opU = new OperateUser();
    private static OperateDB operateDB = null;

    private OperateDB() {
    }

    public static OperateDB getInstance() {
        if (operateDB == null) {
            return operateDB = new OperateDB();
        } else
            return operateDB;
    }

    public boolean createUserInDB(User u) {
        return db_con.createNewUser(u);
    }

    public void validateLogin (String email, String password) {
        opU.setLoggedInUser(db_con.validateLogin(email, password));
    }

    public boolean isEmailOccupied(String email) {
        return db_con.isEmailOccupied(email);
    }

    public boolean isPhonenumberOccupied(String phoneNumber) {
        return db_con.isPhonenumberOccupied(phoneNumber);
    }

    public boolean updateUserInDB(User user) {
        return db_con.updateUser(user);
    }
}