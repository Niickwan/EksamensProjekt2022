package com.jmmnt.UI;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.Entities.User;
import com.jmmnt.R;
import com.jmmnt.databinding.ActivityUserBinding;

public class ActivityUser extends AppCompatActivity {

    private ActivityUserBinding binding;
    private User user = LoggedInUser.getInstance().getUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.profileFirstnameTv.setText(user.getFirstname());
        binding.profileSurnameTv.setText(user.getSurname());

        binding.profilePictureImg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentPopupMenu fpm = FragmentPopupMenu.getInstance();
                fpm.showProfileMenu(view, getLayoutInflater(), ActivityUser.this, R.id.nav_host_fragment_content_user);
            }
        });
    }

}