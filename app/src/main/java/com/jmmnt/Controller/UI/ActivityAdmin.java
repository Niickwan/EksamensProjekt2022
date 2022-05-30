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

    //DETTE KAN BRUGES I STEDET FOR NAV_GRAPH

    //private FragmentManager fm = getSupportFragmentManager();

    /*public void fragmentManager(Fragment fragment, String tag) {
        if (fm.getBackStackEntryCount() == 0 || !fm.getBackStackEntryAt(fm.getBackStackEntryCount()-1).getName().equals(tag)) {
            System.out.println("REPLACE ON STACK"); //TODO sout
            fm.beginTransaction()
                    .replace(R.id.adminFragmentContainer, fragment, tag)
                    .setReorderingAllowed(true)
                    .addToBackStack(tag)
                    .commit();
        }
    }

    //((ActivityAdmin) getActivity()).fragmentManager(new FragmentAdminHome().getFragment(), getClass().getName());

    @Override
    public void onBackPressed(){
        Fragment fragment = fm.findFragmentById(R.id.adminFragmentContainer);
        if (fragment != null){
            System.out.println("REMOVE FROM STACK"); //TODO sout
            fm.beginTransaction()
                    .remove(fragment)
                    .commit();
            fm.popBackStack();
        }else
            super.onBackPressed();
    }*/

    //DETTE ER TIL ACTIVITY SKIFT - addflag rydder stakken ved aktivitetsskrift

    /*public void switchScene(Context fromScene, Class toScene) {
        Intent switchActivity = new Intent(fromScene, toScene);
        switchActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        fromScene.startActivity(switchActivity);
    }*/
}