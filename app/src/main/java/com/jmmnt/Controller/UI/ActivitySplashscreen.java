package com.jmmnt.Controller.UI;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class ActivitySplashscreen extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        new Handler().postDelayed(() -> {
            startActivity(new Intent(ActivitySplashscreen.this, ActivityLogin.class));
            finish();
        }, 3000);
    }


}
