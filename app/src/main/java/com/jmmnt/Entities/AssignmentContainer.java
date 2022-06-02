package com.jmmnt.Entities;

import java.util.ArrayList;

public class AssignmentContainer {

    private ArrayList<Assignment> assignments;
    private static AssignmentContainer assignmentContainer;
    private Assignment currentAssignment;

    private AssignmentContainer() {
        assignments = new ArrayList<>();
    }

    public static AssignmentContainer getInstance() {
        if (assignmentContainer == null) {
            return assignmentContainer = new AssignmentContainer();
        } else
            return assignmentContainer;
    }

    public void addAssignmentsToContainer(Assignment assignment) {
        this.assignments.add(assignment);
    }

    public ArrayList<Assignment> getAssignments() {
        return assignments;
    }

    public Assignment getCurrentAssignment() {
        return currentAssignment;
    }

    public void setCurrentAssignment(Assignment currentAssignment) {
        this.currentAssignment = currentAssignment;
    }



}
