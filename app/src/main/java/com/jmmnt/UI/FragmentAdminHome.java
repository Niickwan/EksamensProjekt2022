package com.jmmnt.UI;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.jmmnt.R;
import com.jmmnt.databinding.FragmentAdminHomeBinding;

public class FragmentAdminHome extends Fragment {

    private FragmentAdminHomeBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showProfileMenu();
            }
        });

        binding.createNewAssignmentBtn.setOnClickListener(view1 -> NavHostFragment.findNavController(FragmentAdminHome.this)
                .navigate(R.id.action_FragmentAdminHome_to_FragmentAdminNotInUse));


    }

    private void showProfileMenu() {
        PopupWindow pw = new PopupWindow(getLayoutInflater().inflate(R.layout.general_fragment_profile_popup_menu, null, false),900,1200, true);



        System.out.println("X coord: "+binding.profilePicture.getX());
        System.out.println("Y coord: "+binding.profilePicture.getY());
        System.out.println("XML LAYOUT "+binding);
        pw.showAtLocation(this.getView(), Gravity.CENTER, -15, -250);


    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}