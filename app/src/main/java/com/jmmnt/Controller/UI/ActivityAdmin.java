package com.jmmnt.Controller.UI;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.Entities.User;
import com.jmmnt.R;
import com.jmmnt.databinding.ActivityAdminBinding;

public class ActivityAdmin extends AppCompatActivity {

    private ActivityAdminBinding binding;
    private User user = LoggedInUser.getInstance().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAdminBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.profileFirstnameTv.setText(user.getFirstname());
        binding.profileSurnameTv.setText(user.getSurname());

        binding.profilePictureImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentPopupMenu fpm = FragmentPopupMenu.getInstance();
                fpm.showProfileMenu(view, getLayoutInflater(), ActivityAdmin.this, R.id.nav_host_fragment_content_admin);
            }
        });
    }

}