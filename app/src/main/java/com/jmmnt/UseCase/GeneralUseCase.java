package com.jmmnt.UseCase;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.jmmnt.R;
import com.jmmnt.databinding.ActivityUserBinding;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeneralUseCase extends Activity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static Toast toast = null;
    private static GeneralUseCase generalUseCase = null;

    private GeneralUseCase(){
    }

    public static GeneralUseCase getInstance() {
        if (generalUseCase  == null)
            generalUseCase  = new GeneralUseCase();
        return generalUseCase;
    }

    //This method takes two strings and checks if one of the strings (s)
    //Contains the same chars as the other string (input)
    //The method then returns true if they do contain the same chars, and false if they do not.
    public boolean checkIfStringMatchesInput(String s, String input){
        return s.toLowerCase().matches("(.*)" + input.toLowerCase() + "(.*)");
    }

    public boolean checkIfNumber(String text, int lengthOfNumber) {
        return text.matches("[0-9]+") && text.length() == lengthOfNumber;
    }

    public boolean checkIfNumber(String text) {
        return text.matches("[0-9]+");
    }

    public boolean checkIfLetters(String text){
        return !text.matches(".*[0-9].*");
    }

    public boolean checkIfEmail(String text){
        String emailTjek = "^[a-zA-Z0-9_+&*-]+(?:\\."+
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$";
        return text.matches(emailTjek);
    }

    public void toastAlert(Context activity, String text) {
        if (toast != null) {
            toast.cancel();
        }
        Looper.prepare();
        toast = Toast.makeText(activity, text, Toast.LENGTH_LONG);
        toast.show();
        Looper.loop();
    }


    public void createCamera(ActivityUserBinding binding, ImageView image) {
    }

    public void switchScene(Context fromScene, Class toScene) {
        Intent switchActivity = new Intent(fromScene, toScene);
        fromScene.startActivity(switchActivity);
    }

    public boolean isInputMatching(String firstUserInput, String secondUserInput) {
        return firstUserInput.equals(secondUserInput);
    }

    public boolean isFieldsEmpty(EditText[] fields){
        for(int i = 0; i < fields.length; i++){
            EditText currentField = fields[i];
            if(currentField.getText().toString().length() <= 0){
                return true;
            }
        }
        return false;
    }
    public boolean isFieldsEmpty(TextInputLayout[] fields){
        for(int i = 0; i < fields.length; i++){
            TextInputLayout currentField = fields[i];
            if(currentField.getEditText().getText().toString().length() <= 0){
                return true;
            }
        }
        return false;
    }


    public ArrayList<String> getSplittedString(ArrayList<String> arr, String orderNr, String splitBy) {
        ArrayList<String> split = new ArrayList<>();
        String[] parts = new String[0];
        for (int i = 0; i < arr.size(); i++) {
            parts = arr.get(i).split("[" + splitBy + "]");
        }

        for (int i = 0; i < parts.length; i++) {
            if (parts[i].equals(orderNr)) {
                split.add(parts[i+1]);
            }
        }
        return split;
    }

    public Button createBtnForHSV(String name, Activity activity, int height, int width) {
        Button b = new Button(activity);
        b.setText(name);
        b.setHeight(height);
        b.setWidth(width);
        return b;
    }

    public ArrayList<String> sortStringBeforeNumbers(ArrayList<String> arr) {
        ArrayList<String> numbers = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();
        for (int i = 0; i < arr.size(); i++) {
            if (checkIfNumber(arr.get(i).substring(0, 1))) {
                numbers.add(arr.get(i));
            } else {
                strings.add(arr.get(i));
            }
        }
        numbers.sort(String::compareTo);
        strings.sort(String::compareTo);
        ArrayList<String> sortedList = new ArrayList<>(strings);
        sortedList.addAll(numbers);
        return sortedList;
    }

    public void clearList(List<?> list) {
        if (!list.isEmpty()) {
            list.clear();
        }
    }

    public String formatDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return date.format(formatter);
    };

//    public ArrayList<String> getAssignmentStructure(String orderNr) {
//
//    }

}
