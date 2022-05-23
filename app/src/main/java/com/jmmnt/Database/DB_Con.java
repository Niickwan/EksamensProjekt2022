package com.jmmnt.Database;


import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.User;
import com.jmmnt.Entities.UserContainer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DB_Con {
    private Connection connection;
    private PreparedStatement preStmt;
    private Statement stmt;
    private ResultSet rs;
    private static DB_Con dbCon;
    private final String URL = "jdbc:mysql://mysql61.unoeuro.com:3306/dat32_dk_db_eksamen?useSSL=true"; //TODO SSL Run error "autoReconnect=true&useSSL=false"
    private final String format = "yyyy-MM-dd";


    private boolean uploadMySQLCall(String sqlString){
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

    private void closeConnection(Connection connection){
        try {
            preStmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private Connection connection() {
        connection = null;
        try {
            connection = DriverManager.getConnection(URL, "dat32_dk", "9hkdpBFtAg34");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static DB_Con getInstance() {
        if (dbCon == null) {
            System.out.println("DB_Con Created");
            return dbCon = new DB_Con();
        } else
            return dbCon;
    }

    public void fillUserContainer() throws SQLException {
        UserContainer userContainer = UserContainer.getInstance();
        if (!UserContainer.getUsers().isEmpty()) UserContainer.getUsers().clear();
        String mySQL = "SELECT * FROM User ORDER BY Firstname";
        connection = connection();
        stmt = connection.createStatement();
        rs = stmt.executeQuery(mySQL);
        while (rs.next()) {
            userContainer.addUserToContainer(new User(rs.getString("Firstname"),
                    rs.getString("Surname"),
                    rs.getString("Phonenumber"),
                    rs.getInt("User_ID")));
        }
        connection.close();
        stmt.close();
        rs.close();
    }


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
  
    public boolean isPhonenumberOccupied(String phoneNumber) {
        boolean isPhoneNumberAvailable = false;
        String MySQL = "SELECT * FROM User WHERE Phonenumber = '" + phoneNumber + "'";
        try {
            connection = connection();
            preStmt = connection.prepareStatement(MySQL);
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
    public boolean isEmailOccupied(String email) {
        boolean isEmailAvailable = false;
        String MySQL = "SELECT * FROM User WHERE Email = '" + email + "'";
        try {
            connection = connection();
            preStmt = connection.prepareStatement(MySQL);
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
   

    public boolean updateUser(User user) {
        connection = connection();
        String updateUser = "UPDATE User " +
                "SET Email = '"+user.getEmail()+"', " +
                "Password = '"+user.getPassword()+"', " +
                "Firstname = '"+user.getFirstname()+"', " +
                "Surname = '"+user.getSurname()+"', " +
                "Phonenumber = '"+user.getPhonenumber()+"' " +
                "WHERE User_ID = "+LoggedInUser.getInstance().getUser().getUserID()+"";
        return uploadMySQLCall(updateUser);
    }
  
    public boolean createNewAssignment(Assignment assignment) {
        connection = connection();
        String userInfo = "INSERT INTO Assignment (Foreman_ID, Address, Postal_Code, Status, Order_Number, Customer_Name) "
                + "VALUES ('"
                + assignment.getForemanId() + "', '"
                + assignment.getAddress() + "', '"
                + assignment.getPostalCode() + "', '"
                + assignment.getStatus() + "', '"
                + assignment.getOrderNumber() + "', '"
                + assignment.getCustomerName() + "')";
        return uploadMySQLCall(userInfo);
    }
   
}
