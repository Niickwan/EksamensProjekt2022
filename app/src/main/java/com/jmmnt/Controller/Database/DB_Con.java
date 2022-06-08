package com.jmmnt.Controller.Database;

import com.jmmnt.Entities.AssignmentContainer;
import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.User;
import com.jmmnt.Entities.UserAssignmentContainer;
import com.jmmnt.Entities.UserContainer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class DB_Con {
    private Connection connection;
    private PreparedStatement preStmt;
    private Statement stmt;
    private ResultSet rs;
    private static DB_Con dbCon;
    private final String URL = "jdbc:mysql://mysql61.unoeuro.com:3306/dat32_dk_db_eksamen?useSSL=true";

    //Default constructor
    private DB_Con(){
    }

    //Getinstance for singleton
    public static DB_Con getInstance() {
        if (dbCon == null) {
            return dbCon = new DB_Con();
        } else
            return dbCon;
    }

    //Excutes sql queries
    private boolean uploadMySQLCall(String sqlString) {
        int SQLCallSucceded = 0;
        try {
            connection = connection();
            preStmt = connection.prepareStatement(sqlString);
            SQLCallSucceded = preStmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closeConnection(connection);
        }
        return SQLCallSucceded == 1;
    }

    //Closes the connection to the database
    private void closeConnection(Connection connection) {
        try {
            preStmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //Establish connection to the database
    private Connection connection() {
        try {
            connection = DriverManager.getConnection(URL, "", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    //Get all user data and add it to a container
    public void fillUserContainer() {
        UserContainer userContainer = UserContainer.getInstance();
        if (!UserContainer.getUsers().isEmpty()) UserContainer.getUsers().clear();
        String fill = "SELECT Firstname, Surname, Phonenumber, User_ID FROM User ORDER BY Firstname";
        try {
            connection = connection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(fill);
            userContainer.addUserToContainer(new User()); //Add default user
            while (rs.next()) {
                userContainer.addUserToContainer(new User(
                        rs.getString("Firstname"),
                        rs.getString("Surname"),
                        rs.getString("Phonenumber"),
                        rs.getInt("User_ID")));
            }
            connection.close();
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    //Get all assignment data and add it to a container
    public boolean fillAssignmentContainer() {
        LocalDate localDate;
        boolean isUsed = false;
        AssignmentContainer assignmentContainer = AssignmentContainer.getInstance();
        if (!assignmentContainer.getAssignments().isEmpty())
            assignmentContainer.getAssignments().clear();
        String fill = "SELECT Assignment_ID, Customer_Name, Order_number, Address, Postal_Code, Status, Status_Date FROM Assignment";
        try {
            connection = connection();
            stmt = connection.createStatement();
            rs = stmt.executeQuery(fill);
            while (rs.next()) {
                isUsed = true;
                localDate = LocalDate.parse(rs.getDate("Status_Date").toString());
                assignmentContainer.addAssignmentsToContainer(new Assignment(
                        rs.getInt("Assignment_ID"),
                        rs.getString("Order_Number"),
                        rs.getString("Customer_Name"),
                        rs.getString("Address"),
                        rs.getString("Postal_Code"),
                        localDate,
                        rs.getString("Status")));
            }
            connection.close();
            stmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isUsed;
    }

    //Get all user related assignment data and add it to a container
    public boolean fillUserAssignmentsContainer(int userID) {
        UserAssignmentContainer uAC = UserAssignmentContainer.getInstance();
        if (!uAC.getUserAssignments().isEmpty())
            uAC.getUserAssignments().clear();
        boolean isFilled = false;
        String sql = "SELECT (Assignment_ID) " +
                "FROM User_Assignment " +
                "WHERE User_ID = "+userID+" ";
        try {
            connection = connection();
            preStmt = connection.prepareStatement(sql);
            rs = preStmt.executeQuery();
            while (rs.next()) {
                isFilled = true;
                uAC.addUserAssignmentToContainer(new Assignment(rs.getInt("Assignment_ID")));
            }
            connection.close();
            preStmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isFilled;
    }


    //Check if given arguments matches data in the database
    public User validateLogin(String email, String password) {
        User user = null;
        String mySQL = "SELECT * FROM User WHERE Email = ? AND Password = ?";
        try {
            connection = connection();
            preStmt = connection.prepareStatement(mySQL);
            preStmt.setString(1, email);
            preStmt.setString(2, password);
            rs = preStmt.executeQuery();
            if (rs.next()) {
                user = new User(
                        rs.getString("Email"),
                        rs.getString("Firstname"),
                        rs.getString("Surname"),
                        rs.getString("Phonenumber"),
                        rs.getInt("User_ID"),
                        rs.getInt("User_Rights"));
            }
            connection.close();
            preStmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    //Insert given argument data into the database
    public boolean createNewUser(User user) {
        connection = connection();
        String userInfo = "INSERT INTO User (Email, Password, Firstname, Surname, User_Rights, Phonenumber) "
                + "VALUES ('"
                + user.getEmail() + "', '"
                + user.getPassword() + "', '"
                + user.getFirstname() + "', '"
                + user.getSurname() + "', '"
                + user.getUserRights() + "', '"
                + user.getPhonenumber() + "')";
        return uploadMySQLCall(userInfo);
    }

    //Check if given argument matches data in the database
    public boolean isPhonenumberOccupied(String phoneNumber) {
        boolean isPhoneNumberAvailable = false;
        String mySQL = "SELECT * FROM User WHERE Phonenumber = '" + phoneNumber + "'";
        try {
            connection = connection();
            preStmt = connection.prepareStatement(mySQL);
            rs = preStmt.executeQuery();
            if (rs.next()) isPhoneNumberAvailable = true;
            connection.close();
            preStmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isPhoneNumberAvailable;
    }

    //Check if given argument matches data in the database
    public boolean isEmailOccupied(String email) {
        boolean isEmailAvailable = false;
        String mySQL = "SELECT * FROM User WHERE Email = '" + email + "'";
        try {
            connection = connection();
            preStmt = connection.prepareStatement(mySQL);
            rs = preStmt.executeQuery();
            if (rs.next()) isEmailAvailable = true;
            connection.close();
            preStmt.close();
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isEmailAvailable;
    }

    //Update a specific user in the database
    public boolean updateUser(User user) {
        connection = connection();
        String updateUser = "UPDATE User " +
                "SET Email = '" + user.getEmail() + "', " +
                "Password = '" + user.getPassword() + "', " +
                "Firstname = '" + user.getFirstname() + "', " +
                "Surname = '" + user.getSurname() + "', " +
                "Phonenumber = '" + user.getPhonenumber() + "' " +
                "WHERE User_ID = " + LoggedInUser.getInstance().getUser().getUserID() + "";
        return uploadMySQLCall(updateUser);
    }

    //Insert new assignment without userID as a parameter
    public boolean createNewAssignment(Assignment assignment) {
        connection = connection();
        String userInfo = "INSERT INTO Assignment (Address, Postal_Code, Status, Order_Number, Status_Date, Customer_Name) "
                + "VALUES ('"
                + assignment.getAddress() + "', '"
                + assignment.getPostalCode() + "', '"
                + assignment.getStatus() + "', '"
                + assignment.getOrderNumber() + "', '"
                + assignment.getStatusDate() + "', '"
                + assignment.getCustomerName() + "')";
        return uploadMySQLCall(userInfo);
    }

    //Insert new assignment with userID as a parameter
    public boolean createNewAssignment(Assignment assignment, int userID) {
        int isUpdated;
        String userInfo = "INSERT INTO Assignment (Address, Postal_Code, Status, Order_Number, Status_Date, Customer_Name) "
                + "VALUES ('"
                + assignment.getAddress() + "', '"
                + assignment.getPostalCode() + "', '"
                + assignment.getStatus() + "', '"
                + assignment.getOrderNumber() + "', '"
                + assignment.getStatusDate() + "', '"
                + assignment.getCustomerName() + "')";
        try {
            connection = connection();
            preStmt = connection.prepareStatement(userInfo);
            isUpdated = preStmt.executeUpdate();
            if (isUpdated == 1) {
                String getLatestAssignmentID = "SELECT LAST_INSERT_ID()";
                stmt = connection.createStatement();
                rs = stmt.executeQuery(getLatestAssignmentID);
                if (rs.next()){
                    String insertIntoUserAssignment = "INSERT INTO User_Assignment (User_ID, Assignment_ID)"
                            + "VALUES (" + userID + ", " + rs.getInt("LAST_INSERT_ID()") + ")";
                    return (uploadMySQLCall(insertIntoUserAssignment));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //Gathering directory paths for a specific order
    public ArrayList<String> getAssignmentStructure(String orderNr) {
        ArrayList<String> arr = new ArrayList<>();
        try {
            java.net.URL url = new URL("https://dat32.dk/getAssignmentStructure.php?orderNr=" + orderNr);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            for (String line = in.readLine(); line != null; line = in.readLine()) {
                arr.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr;
    }

    //Check if order exist in the database
    public boolean doesOrderNumberExist(String orderNumber) {
        boolean isOrderNumberAvailable = true;
        String mySQL = "SELECT * FROM Assignment WHERE Order_Number = '" + orderNumber + "'";
        try {
            connection = connection();
            preStmt = connection.prepareStatement(mySQL);
            rs = preStmt.executeQuery();
            if (rs.next()) isOrderNumberAvailable = false;
            connection.close();
            preStmt.close();
            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return isOrderNumberAvailable;
    }

}
