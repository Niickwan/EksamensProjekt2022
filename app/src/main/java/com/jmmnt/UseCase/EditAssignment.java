package com.jmmnt.UseCase;



import com.jmmnt.Entities.Assignment;

import java.util.ArrayList;
import java.util.List;
//TODO eventuel lave en ekstra funktion der tjekker status (aktiv, ikke aktiv) sådan at man kan
//TODO søge KUN på aktive eller KUN på ikke aktive
public class EditAssignment{

    //This method takes an arbitrary list of assignments and an arbitrary list of integers.
    //The method then finds all the assignments whose indexes match the integers in the integer list
    //and returns the assignments in a new list, sortedAssignments.
    public List<Assignment> sortCasesByindex(List<Assignment> assignments, List<Integer> indexList){
        ArrayList<Assignment> sortedAssignments = new ArrayList<    >();
        for (Integer integer : indexList) {
            sortedAssignments.add(assignments.get(integer));
        }
        return sortedAssignments;
    }

    //This method takes an abitrary list of assignments and an arbitrary String.
    //The method then finds all
    public List<Integer> findCaseMatchingInput(List<Assignment> assignments, String input){
        ArrayList<Integer> matchingCasesIndex = new ArrayList<>();
        for (int i = 0; i < assignments.size(); i++) {
            if(checkIfStringMatchesInput(assignments.get(i).getAddress(), input)
               || checkIfStringMatchesInput(assignments.get(i).getPostalCode(), input)
               || checkIfStringMatchesInput(assignments.get(i).getCity(), input)
               || checkIfStringMatchesInput(assignments.get(i).getAddressNumber(), input))
            {
                matchingCasesIndex.add(i);
            }
        }
        return matchingCasesIndex;
    }

    //This method takes two strings and checks if one of the strings (s)
    //Contains the same chars as the other string (input)
    //The method then returns true if they do contain the same chars, and false if they do not.
    //TODO skal lægge i general use-case (måske??????)
    public boolean checkIfStringMatchesInput(String s, String input){

            return s.toLowerCase().matches("(.*)" + input.toLowerCase() + "(.*)");
    }
    //This method takes a list of assignments and a string
    //the method then uses the methods findCaseMatchingInput and sortCaseByIndex
    //to get and return a list of assignments whose address, postalCode or city matches the
    //string
    public List<Assignment> getSearchedCases(List<Assignment> assignments, String input){
        return sortCasesByindex(assignments, findCaseMatchingInput(assignments, input));
    }

}
