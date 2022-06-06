package com.jmmnt.Entities;

public class LoggedInUser{

    /**
     * When a user is logged in to the system the data involving the user gets stored here.
     */

    private static LoggedInUser loggedInUser;
    private User user;

    public static LoggedInUser getInstance() {
        if (loggedInUser  == null)
            loggedInUser  = new LoggedInUser();
        return loggedInUser;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
