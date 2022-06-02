package com.jmmnt.Entities;

import java.util.ArrayList;

public class UserContainer {

    private static ArrayList<User> users;
    private static UserContainer uc;
    private static int created = 0;

    private UserContainer() {
        users = new ArrayList<>();
    }

    public static UserContainer getInstance() {
        if (created == 0) {
            created = 1;
            uc = new UserContainer();
            return uc;
        } else
            return uc;
    }

    public void addUserToContainer(User user) {
        this.users.add(user);
    }

    public static ArrayList<User> getUserByName() {
        return users;
    }

    public static ArrayList<User> getUsers() {
        return users;
    }


}
