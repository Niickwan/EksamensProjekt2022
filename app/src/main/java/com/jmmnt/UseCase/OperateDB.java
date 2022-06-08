package com.jmmnt.UseCase;

import com.jmmnt.Controller.Database.DB_Con;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.User;
import com.jmmnt.Entities.UserContainer;
import java.util.ArrayList;

public class OperateDB {

    /**
     * This class is forwarding information from the UI controllers to the database controller (DB_Con).
     */

    private DB_Con db_con = DB_Con.getInstance();
    private OperateUser opU = new OperateUser();
    private static OperateDB operateDB;

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

    public boolean createNewAssignment(Assignment assignment, int ID) {
        return db_con.createNewAssignment(assignment, ID);
    }

    public boolean createNewAssignment(Assignment assignment) {
        return db_con.createNewAssignment(assignment);
    }

    public ArrayList<String> getAssignmentStructure(String orderNr) {
        return db_con.getAssignmentStructure(orderNr);
    }

    public void fillUserContainer(){
        db_con.fillUserContainer();
        //UserContainer.getUsers().set(0, new User());
    }

    public boolean fillAssignmentContainer() {
       return db_con.fillAssignmentContainer();
    }

    public boolean doesOrderNumberExist(String orderNumber) {
        return db_con.doesOrderNumberExist(orderNumber);
    }

    public void fillUserAssignmentsIDs(int userID) {
        db_con.fillUserAssignmentsContainer(userID);
    }


}