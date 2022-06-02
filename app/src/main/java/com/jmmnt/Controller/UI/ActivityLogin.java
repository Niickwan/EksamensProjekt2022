package com.jmmnt.Controller.UI;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import androidx.appcompat.widget.PopupMenu;
import com.jmmnt.R;
import com.jmmnt.databinding.ActivityLoginBinding;
import android.view.MenuItem;

public class ActivityLogin extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.threeDotMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
    }

    public void showPopupMenu(View view) {
        PopupMenu ppm = new PopupMenu(ActivityLogin.this, view);
        ppm.getMenuInflater().inflate(R.menu.popup_menu, ppm.getMenu());
        ppm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.recoverPassword) {
                    System.out.println("GENDAN PASSWORD"); //TODO sout
                    return true;
                }
                if (id == R.id.about) {
                    FragmentPopupMenu fpm = FragmentPopupMenu.getInstance();
                    fpm.popupMenuShowAbout(ActivityLogin.this);
                    return true;
                }
                return false;
            }
        });
        ppm.show();
    }

}