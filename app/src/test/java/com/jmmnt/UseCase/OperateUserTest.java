package com.jmmnt.UseCase;

import static org.junit.Assert.*;

import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.Entities.User;

import org.junit.Test;

public class OperateUserTest {

    @Test
    public void setLoggedInUser() {
        OperateUser ou = new OperateUser();
        User user = new User();
        ou.setLoggedInUser(user);
        assertEquals(user, LoggedInUser.getInstance().getUser());
    }
}