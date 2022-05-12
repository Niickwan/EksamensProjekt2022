package com.jmmnt.UseCase;

import static org.junit.Assert.*;

import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.Customer;
import com.jmmnt.Entities.User;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class EditAssignmentTest {
    EditAssignment ea = new EditAssignment();

    @Test
    public void sortCasesByindex() {

    }

    @Test
    public void findCaseMatchingInput() {
        //----------------------------------------------------------------
        ArrayList<Assignment> assignmentList = new ArrayList<>();
        assignmentList.add(new Assignment(new Customer("lars", "sørensen"),
                new User("christian", " jørgensen", "abc2"),
                "f01594", "snerlevej","191", "4700", "Næstved"));

        assignmentList.add(new Assignment(new Customer("lars", "sørensen"),
                new User("christian", " jørgensen", "abc1"),
                "f01594", "jordbærgade","12", "3300", "København"));

        assignmentList.add(new Assignment(new Customer("lars", "sørensen"),
                new User("christian", " jørgensen", "abc3"),
                "f01594", "snerlevej","11", "4700", "Næstved"));

        assignmentList.add(new Assignment(new Customer("lars", "sørensen"),
                new User("christian", " jørgensen", "abc4"),
                "f01594", "hjørnet", "23", "4760", "Vordingborg"));
        //----------------------------------------------------------------
        //test1
        ArrayList<Integer> expected1 = new ArrayList<>();
        expected1.add(0);
        expected1.add(2);
        List<Integer> res1 = ea.findCaseMatchingInput(assignmentList, "Snerle");
        assertEquals(expected1,res1);
        //test1

        //test2
        ArrayList<Integer> expected2 = new ArrayList<>();
        expected2.add(0);
        expected2.add(2);
        expected2.add(3);
        List<Integer> res2 = ea.findCaseMatchingInput(assignmentList, "47");
        assertEquals(expected2,res2);
        //test2

        //test3
        ArrayList<Integer> expected3 = new ArrayList<>();
        expected3.add(3);
        List<Integer> res3 = ea.findCaseMatchingInput(assignmentList, "23");
        assertEquals(expected3,res3);
        //test3

        //test4
        ArrayList<Integer> expected4 = new ArrayList<>();
        expected4.add(1);
        List<Integer> res4 = ea.findCaseMatchingInput(assignmentList, "havn");
        assertEquals(expected4,res4);
        //test4

        //test5
        ArrayList<Integer> expected5 = new ArrayList<>();
        expected5.add(3);
        List<Integer> res5 = ea.findCaseMatchingInput(assignmentList, "Jørgen");
        assertNotEquals(expected5,res5);

    }

    @Test
    public void checkIfStringMatchesInput() {
        boolean res1 = ea.checkIfStringMatchesInput("Hans","an");
        assertTrue(res1);
        boolean res2 = ea.checkIfStringMatchesInput("Hans","hans");
        assertTrue(res2);
        boolean res3 = ea.checkIfStringMatchesInput("Hans", "Hans");
        assertTrue(res3);
        boolean res4 = ea.checkIfStringMatchesInput("Hans","hanl");
        assertFalse(res4);
        boolean res5 = ea.checkIfStringMatchesInput("Hans", "l");
        assertFalse(res5);
        boolean res6 = ea.checkIfStringMatchesInput("Hans", "4");
        assertFalse(res6);
    }
}