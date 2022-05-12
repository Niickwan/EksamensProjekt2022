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
    private final String URL = "jdbc:mysql://mysql61.unoeuro.com:3306/dat32_dk_db_eksamen";
    private final String format = "yyyy-MM-dd";

    private Connection connection() {
        connection = null;
        try {
//            Class.forName("com.mysql.cj.jdbc.Driver");
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

    public int validateLogin(String email, String password) throws SQLException {
        int userRights = -1;
        String MySQL = "SELECT * FROM User WHERE Email = ? AND Password = ?";
        connection = connection();
        preStmt = connection.prepareStatement(MySQL);
        preStmt.setString(1, email);
        preStmt.setString(2, password);
        rs = preStmt.executeQuery();
        if (rs.next()) {
            if (rs.getString("UserRights").equals("1")) userRights = 1;
            else if (rs.getString("UserRights").equals("2")) userRights = 2;
            connection.close();
            preStmt.close();
            rs.close();
        }
        return userRights;
    }

    public boolean createNewUser(User user) throws SQLException {
        int isUserCreated = 0;
        connection = connection();
        String userInfo = "INSERT INTO User (Email, Password, Name, Surname, UserRights) "
                + "VALUES ('"
                + user.getEmail() + "', '"
                + user.getPassword() + "', '"
                + user.getFirstName() + "', '"
                + user.getSurname() + "', '"
                + user.getUserRights() + "')";
            preStmt = connection.prepareStatement(userInfo);
            isUserCreated = preStmt.executeUpdate();
            preStmt.close();
            connection.close();

        return isUserCreated == 1;
    }

}
