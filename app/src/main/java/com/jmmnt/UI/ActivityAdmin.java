package com.jmmnt.UI;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import android.view.View;
import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.Entities.User;
import com.jmmnt.databinding.ActivityAdminBinding;

public class ActivityAdmin extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private User user = LoggedInUser.getInstance().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.profileFirstnameTv.setText(user.getFirstName());
        binding.profileSurnameTv.setText(user.getSurname());

        binding.profilePictureImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentPopupMenu fpm = FragmentPopupMenu.getInstance();
                fpm.showProfileMenu(view, getLayoutInflater(), ActivityAdmin.this);
            }
        });
    }

    public void fragmentManager(Fragment fragment, int resourceID) {
        if (!fragment.isVisible()) {
            FragmentManager fm = getSupportFragmentManager();
            fm.beginTransaction()
                    .replace(resourceID, fragment, null)
                    .setReorderingAllowed(true)
                    .addToBackStack(null)
                    .commit();
        }
    }


}