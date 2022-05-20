package com.jmmnt.Entities;

public class LoggedInUser{
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
