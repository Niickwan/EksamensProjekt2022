package com.jmmnt.Database;

import com.jmmnt.Entities.User;

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

    public User validateLogin(String email, String password) {
        User user = null;
        String MySQL = "SELECT * FROM User WHERE Email = ? AND Password = ?";
        try {
            connection = connection();
            preStmt = connection.prepareStatement(MySQL);
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
        int isUserCreated = 0;
        try {
            connection = connection();
            String userInfo = "INSERT INTO User (Email, Password, Name, Surname, UserRights, Phonenumber) "
                    + "VALUES ('"
                    + user.getEmail() + "', '"
                    + user.getPassword() + "', '"
                    + user.getFirstName() + "', '"
                    + user.getSurname() + "', '"
                    + user.getUserRights() + "')"
                    + user.getPhoneNumber() + "', '";
            preStmt = connection.prepareStatement(userInfo);
            isUserCreated = preStmt.executeUpdate();
            preStmt.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isUserCreated == 1;
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

}
