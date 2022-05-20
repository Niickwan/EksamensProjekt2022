package com.jmmnt.Entities;

import java.util.ArrayList;

public class AssignmentContainer {
    private ArrayList<Assignment> assignmentList = new ArrayList<>();

    public ArrayList<Assignment> getAssignmentList() {
        return assignmentList;
    }

    //TODO skal slettes, bruges til test
    public AssignmentContainer(){
        assignmentList.add(new Assignment(new Customer("lars sørensen"),
                new User("christian", " jørgensen", "abc2"),
                "f01594", "snerlevej 191", "4700", "Næstved"));

        assignmentList.add(new Assignment(new Customer("lars sørensen"),
                new User("christian", " jørgensen", "abc1"),
                "f01594", "jordbærgade 12", "3300", "København"));

        assignmentList.add(new Assignment(new Customer("lars sørensen"),
                new User("christian", " jørgensen", "abc3"),
                "f01594", "snerlevej 11", "4700", "Næstved"));

        assignmentList.add(new Assignment(new Customer("lars sørensen"),
                new User("christian", " jørgensen", "abc4"),
                "f01594", "hjørnet 23", "4760", "Vordingborg"));
    }
}
