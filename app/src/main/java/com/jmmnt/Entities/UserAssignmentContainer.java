package com.jmmnt.Entities;

import java.util.ArrayList;

public class UserAssignmentContainer {

    private ArrayList<Assignment> userAssignments;
    private static UserAssignmentContainer userAssignmentContainer;

    private UserAssignmentContainer() {
        userAssignments = new ArrayList<>();
    }

    public static UserAssignmentContainer getInstance() {
        if (userAssignmentContainer == null) {
            return userAssignmentContainer = new UserAssignmentContainer();
        } else
            return userAssignmentContainer;
    }

    public void addUserAssignmentToContainer(Assignment assignment) {
        this.userAssignments.add(assignment);
    }

    public ArrayList<Assignment> getUserAssignments() {
        return userAssignments;
    }

}
