package com.jmmnt.UI;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jmmnt.Database.DB_Con;
import com.jmmnt.Entities.UserContainer;
import com.jmmnt.R;
import com.jmmnt.databinding.FragmentAdminCreateOrderBinding;
import com.jmmnt.databinding.FragmentAdminNotInUseBinding;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FragmentCreateOrder extends Fragment {

    private FragmentAdminCreateOrderBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminCreateOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.createOrderBtn.setOnClickListener(v -> {
            System.out.println("OPRET SAG"); //TODO sout
        });

        List<String> spinnerArray =  new ArrayList<>();
        for (int i = 0; i < UserContainer.getUsers().size(); i++) {
            spinnerArray.add(UserContainer.getUsers().get(i).getFullName() + " " + UserContainer.getUsers().get(i).getPhonenumber());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner sItems = binding.chooseUserSpinner;
        sItems.setAdapter(adapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}