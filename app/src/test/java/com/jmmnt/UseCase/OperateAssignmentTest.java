package com.jmmnt.UseCase;

import static org.junit.Assert.*;

import com.jmmnt.Entities.Assignment;

import org.junit.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OperateAssignmentTest {
    OperateAssignment operateAssignment = OperateAssignment.getInstance();

    @Test
    public void sortCasesByindex() {

    }

    @Test
    public void findCaseMatchingInput() {
        //----------------------------------------------------------------

        ArrayList<Assignment> assignmentList = new ArrayList<>();/*
        assignmentList.add(new Assignment(new Customer("lars sørensen"),
                new User("christian", " jørgensen", "abc2"),
                "f01594", "snerlevej","191", "4700", "Næstved"));

        assignmentList.add(new Assignment(new Customer("lars sørensen"),
                new User("christian", " jørgensen", "abc1"),
                "f01594", "jordbærgade","12", "3300", "København"));

        assignmentList.add(new Assignment(new Customer("lars sørensen"),
                new User("christian", " jørgensen", "abc3"),
                "f01594", "snerlevej","11", "4700", "Næstved"));

        assignmentList.add(new Assignment(new Customer("lars sørensen"),
                new User("christian", " jørgensen", "abc4"),
                "f01594", "hjørnet", "23", "4760", "Vordingborg"));/*

        //----------------------------------------------------------------
        /*
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

         */
    }

    @Test
    public void getCityMatchingZipCode() {
        String zipCode = "4700";
        String rs = operateAssignment.getCityMatchingZipCode(zipCode);
        assertEquals(rs, "Næstved");
    }

    @Test
    public void bubbleSortAssignmentsByDate() {
        Assignment a1 = new Assignment(67, "123289DSA", "Mærsk", "Oktobervej 4", "4700", "Næstved", LocalDate.now().plusDays(2), "waiting");
        Assignment a2 = new Assignment(123, "879845663", "Tiger", "Vildtbanevej 11", "4700", "Næstved", LocalDate.of(2022, 7, 22), "waiting");
        Assignment a3 = new Assignment(65, "3214987913", "Nordea", "Svingkærvej 334", "4733", "Næstved", LocalDate.of(2022, 7, 15), "waiting");

        List<Assignment> aList = new ArrayList<>();
        aList.add(a1);
        aList.add(a2);
        aList.add(a3);

        List<Assignment> sortedList = operateAssignment.bubbleSortAssignmentsByDate(aList);
        boolean sorted = false;
        if (sortedList.get(0) == a1 && sortedList.get(1) == a3 && sortedList.get(2) == a2) sorted = true;
        assertTrue(sorted);

        boolean sorted2 = false;
        if (sortedList.get(0) == a1 && sortedList.get(1) == a2 && sortedList.get(2) == a3) sorted2 = true;
        assertFalse(sorted2);
    }


}