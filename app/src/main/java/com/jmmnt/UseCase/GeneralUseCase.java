package com.jmmnt.UseCase;

import android.content.Context;
import android.os.Looper;
import android.widget.Toast;

public class GeneralUseCase {
    private static Toast toast = null;
    //This method takes two strings and checks if one of the strings (s)
    //Contains the same chars as the other string (input)
    //The method then returns true if they do contain the same chars, and false if they do not.
    public boolean checkIfStringMatchesInput(String s, String input){
        return s.toLowerCase().matches("(.*)" + input.toLowerCase() + "(.*)");
    }

    public Boolean checkIfNumber(String text, int lengthOfNumber) {
        return text.matches("[0-9]+") && text.length() == lengthOfNumber;
    }

    public Boolean checkIfLetters(String text){
        return !text.matches(".*[0-9].*");
    }

    public Boolean checkIfEmail(String text){
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
}
