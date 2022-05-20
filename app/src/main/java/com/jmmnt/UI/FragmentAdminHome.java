package com.jmmnt.UI;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.ContentView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.jmmnt.R;
import com.jmmnt.databinding.FragmentAdminHomeBinding;

public class FragmentAdminHome extends Fragment {

    private FragmentAdminHomeBinding binding;
    private FragmentPopupMenu fpm = FragmentPopupMenu.getInstance();

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
                fpm.showProfileMenu(view, getLayoutInflater());
            }
        });
        binding.createNewAssignmentBtn.setOnClickListener(view1 -> NavHostFragment.findNavController(FragmentAdminHome.this)
                .navigate(R.id.action_FragmentAdminHome_to_FragmentAdminNotInUse));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}