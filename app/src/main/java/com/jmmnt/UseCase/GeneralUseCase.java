package com.jmmnt.UseCase;

import static android.app.Activity.RESULT_OK;

import static androidx.activity.result.ActivityResultCallerKt.registerForActivityResult;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Looper;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;

import com.jmmnt.UI.FragmentPopupMenu;
import com.jmmnt.databinding.ActivityUserBinding;

public class GeneralUseCase {


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


}
