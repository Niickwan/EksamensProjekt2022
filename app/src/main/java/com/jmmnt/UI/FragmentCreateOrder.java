package com.jmmnt.UI;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.jmmnt.Entities.UserContainer;
import com.jmmnt.R;
import com.jmmnt.databinding.FragmentAdminCreateOrderBinding;
import java.util.ArrayList;
import java.util.List;

public class FragmentCreateOrder extends Fragment implements AdapterView.OnItemSelectedListener {

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
        spinnerArray.add(getString(R.string.fragment_admin_create_spinner_default)); //Default value //TODO kan den bruges til som afventer value?
        for (int i = 1; i < UserContainer.getUsers().size(); i++) {
            spinnerArray.add(UserContainer.getUsers().get(i).getFullName() + " " + UserContainer.getUsers().get(i).getPhonenumber());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = binding.chooseUserSpinner;
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        System.out.println("MEDARBEJDER VALGT: "+adapterView.getSelectedItem().toString()); //TODO sout
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}