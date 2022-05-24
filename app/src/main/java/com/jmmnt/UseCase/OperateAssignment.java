package com.jmmnt.UseCase;



import android.os.Environment;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import jxl.Cell;
import jxl.CellType;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

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

    //json methods-----------------------------------------------------
    private String readAll(Reader bufferedReader) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        int currentChar;
        while ((currentChar = bufferedReader.read()) != -1) {
            stringBuilder.append((char) currentChar);
        }
        return stringBuilder.toString();
    }

    private JSONObject readJsonUrl(String url) throws IOException, JSONException {
        InputStream inputStream = new URL(url).openStream();

        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, Charset.forName("UTF-8")));
        String jsonText = readAll(bufferedReader);
        JSONObject json = new JSONObject(jsonText);
        inputStream.close();
        return json;
    }

    public String getCityMatchingZipCode(String url, String zipCode){
        try {
            JSONObject jsonObject = readJsonUrl(url + "/" + zipCode);
            return jsonObject.get("navn").toString();
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    //json methods-----------------------------------------------------

    //EXCEL Methods
    public ArrayList<String> getExcelAsArrayList (String fileName) {
        ArrayList<String> arr = new ArrayList<>();
        File path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File inputWorkbook = new File(path, fileName);
        Workbook w;
        try {
            w = Workbook.getWorkbook(inputWorkbook);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over first 10 column and lines

            for (int j = 0; j < sheet.getRows(); j++) {
                for (int i = 0; i < sheet.getColumns(); i++) {
                    Cell cell = sheet.getCell(i, j);
                    CellType type = cell.getType();
                    if (cell.getType() == CellType.LABEL) {
                        arr.add(cell.getContents());
                    }

                    if (cell.getType() == CellType.NUMBER) {
                        arr.add(String.valueOf(cell.getCellFeatures()));
                    }

                }
            }
        } catch (BiffException | IOException e) {
            e.printStackTrace();
        }
        arr.forEach(System.out::println);
        return arr;
    }
}
