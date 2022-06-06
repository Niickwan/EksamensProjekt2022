package com.jmmnt.UseCase;

import android.os.Environment;
import com.jmmnt.Controller.Database.DB_Con;
import com.jmmnt.Entities.Assignment;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

public class OperateAssignment {

    /**
     * This is for methods used to operate a specific assignment
     */

    private GeneralUseCase gUC = GeneralUseCase.getInstance();
    private DB_Con db_con = DB_Con.getInstance();
    private static OperateAssignment operateAssignment;

    private OperateAssignment(){
    }

    public static OperateAssignment getInstance(){
        if (operateAssignment == null){
            return operateAssignment = new OperateAssignment();
        }else
            return operateAssignment;
    }

    //SERVER------------------------------------------------------------------------------------
    public boolean renameFolderOnServer(String orderNr, String oldName, String newName) {
        boolean isFolderCreated = false;
        try {
            URL url = new URL("https://dat32.dk/renameFolder.php?" +
                    "oldName=" + oldName +
                    "&newName=" + newName +
                    "&orderNr=" + orderNr);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String isSuccessful = in.readLine();
            in.close();
            isFolderCreated = isSuccessful.equalsIgnoreCase("True");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isFolderCreated;
    }

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

    public boolean copyFilesOnServer(String templateFileName, String orderNumber, String floor, String fileName) {
        String destination = "public_html/assignments/" + orderNumber + "/" + floor;
        boolean isFolderCreated = false;
        try {
            URL url = new URL("https://dat32.dk/copyFileOnServer.php?"
                    + "defaultLocation=public_html/TemplateChecklists/" + templateFileName + "&"
                    + "destinationLocation=" + destination + "&"
                    + "fileName=" + fileName);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String isSuccessful = in.readLine();
            in.close();
            isFolderCreated = isSuccessful.equalsIgnoreCase("True");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isFolderCreated;
    }

    public boolean deleteDirectoryOnServer(String directoryLocation) {
        boolean isFolderCreated = false;

        try {
            URL url = new URL("https://dat32.dk/deleteDirectoryOnServer.php?dirLocation=public_html/assignments/" + directoryLocation);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            String isSuccessful = in.readLine();
            in.close();
            isFolderCreated = isSuccessful.equalsIgnoreCase("True");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return isFolderCreated;
    }

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

    //https://api.dataforsyningen.dk/postnumre/ --- USABLE URL FOR ZIPCODES
    public String getCityMatchingZipCode(String zipCode){
        try {
            final String URL = "https://api.dataforsyningen.dk/postnumre/";
            JSONObject jsonObject = readJsonUrl(URL + zipCode);
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
        WorkbookSettings ws = new WorkbookSettings();
        ws.setEncoding("Cp1252");
        try {
            w = Workbook.getWorkbook(inputWorkbook, ws);
            // Get the first sheet
            Sheet sheet = w.getSheet(0);
            // Loop over first 10 column and lines

            for (int j = 0; j < sheet.getRows(); j++) {
                for (int i = 0; i < sheet.getColumns(); i++) {
                    Cell cell = sheet.getCell(i, j);
                    if (cell.getType() == CellType.LABEL) {
                        arr.add(cell.getContents());
                    }

                    if (cell.getType() == CellType.NUMBER) {
                        arr.add(String.valueOf(cell.getContents()));
                    }

                }
            }
            w.close();
        } catch (BiffException | IOException e) {
            e.printStackTrace();
        }
        return arr;
    }

    public List sortAssignmentsByCheckboxIsChecked(List<Assignment> assignments, List<Assignment> userAssignmentIDs, boolean isActive, boolean isWaiting, boolean isFinished, boolean isUserCasesChecked){
        List<Assignment> sortedList = new ArrayList<>();
        for (int i = 0; i < userAssignmentIDs.size(); i++) {
            for (int j = 0; j < assignments.size(); j++) {
                if (isUserCasesChecked && userAssignmentIDs.get(i).getAssignmentID() == assignments.get(j).getAssignmentID()) {
                    if (isActive && assignments.get(j).getStatus().equalsIgnoreCase("active"))
                        sortedList.add(assignments.get(j));
                    else if (isFinished && assignments.get(j).getStatus().equalsIgnoreCase("finished"))
                        sortedList.add(assignments.get(j));
                }
            }
        }
        for (int i = 0; i < assignments.size(); i++) {
            if (isActive && assignments.get(i).getStatus().equalsIgnoreCase("active") && !isUserCasesChecked)
                sortedList.add(assignments.get(i));
            else if (isWaiting && assignments.get(i).getStatus().equalsIgnoreCase("waiting"))
                sortedList.add(assignments.get(i));
            else if (isFinished && assignments.get(i).getStatus().equalsIgnoreCase("finished") && !isUserCasesChecked)
                sortedList.add(assignments.get(i));
        }
        return sortedList;
    }

    public List bubbleSortAssignmentsByDate(List<Assignment> list) {
        Assignment temp;
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = i + 1; j < list.size(); j++) {
                if (list.get(i).getStatusDate().compareTo(list.get(j).getStatusDate()) > 0) {
                    temp = list.get(i);
                    list.set(i, list.get(j));
                    list.set(j, temp);
                }
            }
        }
        return list;
    }


}
