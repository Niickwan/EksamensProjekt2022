package com.jmmnt.UseCase;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Environment;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import com.jmmnt.Controller.Database.DB_Con;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.R;
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

    private GeneralUseCase gUC = GeneralUseCase.getInstance();
    private DB_Con db_con = DB_Con.getInstance();
    private static OperateAssignment oA;

    private OperateAssignment(){
    }

    public static OperateAssignment getInstance(){
        if (oA == null){
            return oA = new OperateAssignment();
        }else
            return oA;
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
    //END SERVER------------------------------------------------------------------------------------

    //TODO lave en ekstra funktion der tjekker status (aktiv, ikke aktiv) sådan at man kan
    //TODO søge KUN på aktive eller KUN på ikke aktive
    //SEARCHING METHODS------------------------------------------------------

    //Search for objects by comparing multiple object strings AND comparing the objects status
    public List<Object> getSearchedObjectsMultipleStrings(List<Object> objects, String[] objectStrings, String input, String[] objectStatus){
        objects = getSearchedObjectsByStatus(objects, objectStatus);
        ArrayList<Object> multipleStringSearchArray = new ArrayList<>();
        for (String objectString : objectStrings) {
            multipleStringSearchArray.addAll(getSearchedObjects(objects, objectString, input));
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


    //Search for objects by comparing the objects status
    public List<Object> getSearchedObjectsByStatus(List<Object> objects, String[] objectStatus){
        ArrayList<Object> objectsByStatus = new ArrayList<>();
        for (String status : objectStatus) {
            objectsByStatus.addAll(sortObjectsByindex(objects, findObjectsMatchingInput(objects, status, status)));
        }
        return objectsByStatus;
    }

    //Search for objects by comparing a single object string
    public List<Object> getSearchedObjects(List<Object> objects, String objectString, String input){
        return sortObjectsByindex(objects, findObjectsMatchingInput(objects, objectString, input));
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
        arr.forEach(System.out::println);
        return arr;
    }
//---------------------------------------------------------------------------------------------
    //---------------------------------------------------------------------------------------------------------
    /**
     * returner liste af cardviews som indeholder alle questions under én headline.
     * parametre: Activity activtity, Resources resources
     */
    public ArrayList<CardView> genereteCardviewArray (Activity activity, Resources resources, String checklistName) {
        int headlineFirst = 0;
        int headlineSecond = 0;
        List<String> excelCardView;
        ArrayList<CardView> headlineCardViews = new ArrayList<>();
        ArrayList<String> excelChecklist = getExcelAsArrayList(checklistName);
        for (int i = 0; i < excelChecklist.size(); i++) {
            if (excelChecklist.get(i).equals("<Headline>")) {
                headlineSecond = i;
                if (headlineSecond > headlineFirst) {
                    excelCardView = excelChecklist.subList(headlineFirst + 1, headlineSecond - 1);
                    headlineCardViews.add(genereteCardView(excelCardView, activity, resources));
                    headlineFirst = headlineSecond;
                }
            }
        }
        return headlineCardViews;
    }

    public CardView genereteCardView(List<String> excelCardView, Activity activity, Resources resources){
        CardView cardView = new CardView(activity);

        LinearLayout cardviewLinearLayout = new LinearLayout(activity);

        LinearLayout linearLayoutHeadlineCardview = new LinearLayout(activity);
        TextView headline = new TextView(activity);


        LinearLayout linearLayoutBodyCardview = new LinearLayout(activity);

        cardView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        cardviewLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        cardviewLinearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayoutHeadlineCardview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        headline.setTextColor(resources.getColor(R.color.black, activity.getTheme()));
        headline.setText(excelCardView.get(0));
        headline.setTextSize(20);
        headline.setTypeface(null, Typeface.BOLD);

        linearLayoutBodyCardview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayoutHeadlineCardview.addView(headline);

        linearLayoutHeadlineCardview.setOnClickListener(v -> {
            if (linearLayoutBodyCardview.getVisibility() == View.GONE){
                TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                linearLayoutBodyCardview.setVisibility(View.VISIBLE);
            }
            else {
                TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                linearLayoutBodyCardview.setVisibility(View.GONE);
            }
        });

        cardviewLinearLayout.addView(linearLayoutHeadlineCardview);
        cardviewLinearLayout.addView(linearLayoutBodyCardview);
        cardView.addView(cardviewLinearLayout);

        return cardView;
    }

    public List sortAssignmentsByCheckboxIsChecked(List<Assignment> list, boolean activeCase, boolean waitingCase, boolean finishedCase, boolean userCase){
        List<Assignment> sortedList = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (userCase && list.get(i).getUserID() == LoggedInUser.getInstance().getUser().getUserID()) {
                if (activeCase && list.get(i).getStatus().equalsIgnoreCase("active"))
                    sortedList.add(list.get(i));
                if (waitingCase && list.get(i).getStatus().equalsIgnoreCase("waiting"))
                    sortedList.add(list.get(i));
                if (finishedCase && list.get(i).getStatus().equalsIgnoreCase("finished"))
                    sortedList.add(list.get(i));
            } else if (activeCase && list.get(i).getStatus().equalsIgnoreCase("active") && !userCase)
                sortedList.add(list.get(i));
            else if (waitingCase && list.get(i).getStatus().equalsIgnoreCase("waiting"))
                sortedList.add(list.get(i));
            else if (finishedCase && list.get(i).getStatus().equalsIgnoreCase("finished") && !userCase)
                sortedList.add(list.get(i));
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
