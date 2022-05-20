package com.jmmnt.UseCase;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class OperateAssignment {
    private GeneralUseCase gUC = GeneralUseCase.getInstance();

    //TODO lave en ekstra funktion der tjekker status (aktiv, ikke aktiv) sådan at man kan
    //TODO søge KUN på aktive eller KUN på ikke aktive
    //SEARCHING METHODS------------------------------------------------------

    //Search for objects by comparing multiple object strings AND comparing the objects status
    public List<Object> getSearchedObjectsMultipleStrings(List<Object> objects, String[] objectStrings, String input, String objectStatus){
        ArrayList<Object> multipleStringSearchArray = new ArrayList<>();
        for (String objectString : objectStrings) {
            multipleStringSearchArray.addAll(getSearchedObjects(objects, objectString, input, objectStatus));
        }
        return multipleStringSearchArray;
    }

    //Search for object by comparing multiple object strings WITHOUT comparing the objects status
    public List<Object> getSearchedObjectsMultipleStrings(List<Object> objects, String[] objectStrings, String input){
        ArrayList<Object> multipleStringSearchArray = new ArrayList<>();
        for (String objectString : objectStrings) {
            multipleStringSearchArray.addAll(getSearchedObjects(objects, objectString, input));
        }
        return multipleStringSearchArray;
    }

    public List<Object> sortObjectsByindex(List<Object> objects, List<Integer> indexList){
        ArrayList<Object> sortedObjects = new ArrayList<>();
        for (Integer integer : indexList) {
            sortedObjects.add(objects.get(integer));
        }
        return sortedObjects;
    }

    public List<Integer> findObjectsMatchingInput(List<Object> objects, String objectString, String input){
        ArrayList<Integer> matchingCasesIndex = new ArrayList<>();
        for (int i = 0; i < objects.size(); i++) {
            if(gUC.checkIfStringMatchesInput(objectString, input))
            {
                matchingCasesIndex.add(i);
            }
        }
        return matchingCasesIndex;
    }

    //Search for objects by comparing a single object string AND comparing the objects status
    public List<Object> getSearchedObjects(List<Object> objects, String objectString, String input, String objectStatus){
        return sortObjectsByindex(objects, findObjectsMatchingInput(objects, objectString, input));
    }

    //Search for objects by comparing a single object string WITHOUT comparing the objects status
    public List<Object> getSearchedObjects(List<Object> objects, String objectString, String input){
        return sortObjectsByindex(objects, findObjectsMatchingInput(objects, objectString, input));
    }

    //Create folder on server
    public boolean createFolderOnServer(String orderNr, String floor, String room) {
        boolean isFolderCreated = false;
        try {
            URL url = new URL("https://dat32.dk/createAssignment.php?assignment="
                    + orderNr + "/"
                    + floor + "/"
                    + room);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String isSuccessful = in.readLine();
            in.close();
            isFolderCreated = isSuccessful.equalsIgnoreCase("True");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isFolderCreated;
    }


    //SEARCHING METHODS------------------------------------------------------

}
