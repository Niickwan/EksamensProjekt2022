package com.jmmnt.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.jmmnt.R;
import com.jmmnt.databinding.FragmentLoginHomeBinding;

public class FragmentLoginHome extends Fragment {

    private FragmentLoginHomeBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLoginHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchScene(getActivity(), ActivityAdmin.class);
//                NavHostFragment.findNavController(FragmentLoginHome.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
            }
        });

        binding.userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switchScene(getActivity(), ActivityEmployee.class);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void switchScene(Context fromScene, Class toScene) {
        Intent switchActivity = new Intent(fromScene, toScene);
        startActivity(switchActivity);
    }

}