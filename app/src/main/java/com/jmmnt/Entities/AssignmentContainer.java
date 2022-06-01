package com.jmmnt.Entities;

import java.util.ArrayList;

public class AssignmentContainer {
    private ArrayList<Assignment> assignmentList = new ArrayList<>();

    private ArrayList<Assignment> assignments;
    private static AssignmentContainer as;
    private static int created = 0;

    private AssignmentContainer() {
        assignments = new ArrayList<>();
    }

    public void addAssignmentsToContainer(Assignment assignment) {
        this.assignmentList.add(assignment);
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public static AssignmentContainer getInstance() {
        if (created == 0) {
            created = 1;
            as = new AssignmentContainer();
            return as;
        } else
            return as;
    }

    /*public ArrayList<Assignment> getAssignmentList() {
        return assignmentList;
    }
     */
    //TODO skal slettes, bruges til test
//    public AssignmentContainer(){
//        assignmentList.add(new Assignment(new Customer("lars sørensen"),
//                new User("christian", " jørgensen", "abc2"),
//                "f01594", "snerlevej 191", "4700", "Næstved"));
//
//        assignmentList.add(new Assignment(new Customer("lars sørensen"),
//                new User("christian", " jørgensen", "abc1"),
//                "f01594", "jordbærgade 12", "3300", "København"));
//
//        assignmentList.add(new Assignment(new Customer("lars sørensen"),
//                new User("christian", " jørgensen", "abc3"),
//                "f01594", "snerlevej 11", "4700", "Næstved"));
//
//        assignmentList.add(new Assignment(new Customer("lars sørensen"),
//                new User("christian", " jørgensen", "abc4"),
//                "f01594", "hjørnet 23", "4760", "Vordingborg"));
//    }
}
