package com.jmmnt.Controller.UI;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;
import com.jmmnt.Entities.UserContainer;
import com.jmmnt.R;
import com.jmmnt.UseCase.GeneralUseCase;
import com.jmmnt.UseCase.OperateAssignment;
import com.jmmnt.databinding.FragmentAdminCreateOrderBinding;

import java.util.ArrayList;
import java.util.List;

public class FragmentCreateOrder extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentAdminCreateOrderBinding binding;
    private OperateAssignment oA = OperateAssignment.getInstance();
    private String city = "";
    private GeneralUseCase gUC = GeneralUseCase.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminCreateOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.createOrderBtn.setOnClickListener(v -> new Thread(() -> {
            boolean isCustomerNameValid = gUC.checkIfLetters(binding.customerNameEt.getEditText().getText().toString());
            boolean isCityNameValid = gUC.checkIfLetters(binding.cityEt.getEditText().getText().toString());
            boolean isEmpty = gUC.isFieldsEmpty(new TextInputLayout[] {
                    binding.orderNumberEt,
                    binding.customerNameEt,
                    binding.addressEt,
                    binding.postalCodeEt,
                    binding.cityEt});
            if(isEmpty){
                gUC.toastAlert(getActivity(), "Udfyld alle felter");
            }
            else if (!isCustomerNameValid) {
                gUC.toastAlert(getActivity(), "Kundenavn må kun indeholde bogstaver");
            }
            else if (!isCityNameValid) {
                gUC.toastAlert(getActivity(), "By må kun indeholde bogstaver");
            }


            System.out.println("TEST TEST TEST " + city);


            System.out.println("OPRET SAG"); //TODO sout
        }).start());


        binding.postalCodeInnerEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KEYCODE_ENTER && keyEvent.getAction() == ACTION_DOWN) {
                        Thread t = new Thread(() -> {
                            city = oA.getCityMatchingZipCode(binding.postalCodeEt.getEditText().getText().toString());
                            if (!city.isEmpty()) {
                                binding.cityEt.getEditText().setText(city);
                            } else {
                                gUC.toastAlert(getActivity(), "Ugyldig postnummer");
                            }
                        });
                        t.start();




                } else
                    System.out.println("FUCK");


                return false;
            }
        });


        List<String> spinnerArray = new ArrayList<>();
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
        System.out.println("MEDARBEJDER VALGT: " + adapterView.getSelectedItem().toString()); //TODO sout
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}