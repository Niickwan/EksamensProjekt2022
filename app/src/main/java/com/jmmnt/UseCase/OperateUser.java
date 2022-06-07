package com.jmmnt.UseCase;

import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.Entities.User;

public class OperateUser {

    /**
     * This class is forwarding information from the UI controller.
     */

    private LoggedInUser loggedInUser = LoggedInUser.getInstance();

    public User CreateDefaultUserLoginInfo(String firstName, String surname, String phoneNumber, String email, String password) {
        return new User(firstName, surname, phoneNumber, email, password, 1);
    }

    public void setLoggedInUser(User user){
        loggedInUser.setUser(user);
    }
}
