package com.jmmnt.UseCase;



import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.provider.MediaStore;

import com.jmmnt.Entities.Assignment;

import java.util.ArrayList;
import java.util.List;

public class OperateAssignment {
    private GeneralUseCase gUC = new GeneralUseCase();

    //TODO lave en ekstra funktion der tjekker status (aktiv, ikke aktiv) sådan at man kan
    //TODO søge KUN på aktive eller KUN på ikke aktive
    //SEARCHING METHODS------------------------------------------------------
    //TODO more or less all variables in this method needs to have changed names
    //TODO code written at 3 am... please send help
    public List<Object> getSearchInMultipleStrings(List<Object> objects, String[] objectStrings, String input){
        ArrayList<Object> multipleStringSearchArray = new ArrayList<>();
        for (String objectString : objectStrings) {
            multipleStringSearchArray.addAll(getSearchedCases(objects, objectString, input));
        }
        return multipleStringSearchArray;
    }
    //This method takes an arbitrary list of assignments and an arbitrary list of integers.
    //The method then finds all the assignments whose indexes match the integers in the integer list
    //and returns the assignments in a new list, sortedAssignments.
    public List<Object> sortCasesByindex(List<Object> objects, List<Integer> indexList){
        ArrayList<Object> sortedObjects = new ArrayList<>();
        for (Integer integer : indexList) {
            sortedObjects.add(objects.get(integer));
        }
        return sortedObjects;
    }

    //This method takes an abitrary list of assignments and an arbitrary String.
    //The method then finds all the assignments whose address/postalcode/city/addressNumber match
    //the string, and places their index numbers into an integer arraylist and then returns the
    //integer arraylist.
    public List<Integer> findCaseMatchingInput(List<Object> objects,String objectString, String input){
        ArrayList<Integer> matchingCasesIndex = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            if(gUC.checkIfStringMatchesInput(objectString, input))
            {
                matchingCasesIndex.add(i);
            }
        }
        return matchingCasesIndex;
    }

    //This method takes a list of assignments and a string
    //the method then uses the methods findCaseMatchingInput and sortCaseByIndex
    //to get and return a list of assignments whose address, postalCode or city matches the
    //string
    public List<Object> getSearchedCases(List<Object> objects,String objectString, String input){
        return sortCasesByindex(objects, findCaseMatchingInput(objects,objectString, input));
    }
    //SEARCHING METHODS------------------------------------------------------

}
