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
    public void getCityMatchingZipCode() {
        String zipCode = "4700";
        String rs = operateAssignment.getCityMatchingZipCode(zipCode);
        assertEquals(rs, "Næstved");
    }

    @Test
    public void bubbleSortAssignmentsByDate() {
        Assignment a1 = new Assignment(67, "123289DSA", "Mærsk", "Oktobervej 4", "4700", LocalDate.now().plusDays(2), "waiting");
        Assignment a2 = new Assignment(123, "879845663", "Tiger", "Vildtbanevej 11", "4700", LocalDate.of(2022, 7, 22), "waiting");
        Assignment a3 = new Assignment(65, "3214987913", "Nordea", "Svingkærvej 334", "4733", LocalDate.of(2022, 7, 15), "waiting");

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