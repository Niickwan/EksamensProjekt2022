package com.jmmnt.Database;

import static org.junit.Assert.*;

import com.jmmnt.Entities.User;

import org.junit.Test;

import java.sql.SQLException;

public class DB_ConTest {
    DB_Con db = new DB_Con();

    @Test
    public void createNewUser() throws SQLException {
        //Test1
        User u = new User("Marcus", "Christiansen", "Marcus@jensenpost.dk", "pass", 1);
        boolean res1 = db.createNewUser(u);
        assertTrue(res1);
        //Test1
    }

    @Test
    public void validateLogin() throws SQLException {
        assertEquals(1, db.validateLogin("Nicklas", "Jensen"));
    }
}