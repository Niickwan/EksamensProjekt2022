package com.jmmnt.Controller.UI;

import static android.view.KeyEvent.ACTION_DOWN;
import static android.view.KeyEvent.KEYCODE_ENTER;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.android.material.textfield.TextInputLayout;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.User;
import com.jmmnt.Entities.UserContainer;
import com.jmmnt.R;
import com.jmmnt.UseCase.GeneralUseCase;
import com.jmmnt.UseCase.OperateAssignment;
import com.jmmnt.UseCase.OperateDB;
import com.jmmnt.databinding.FragmentAdminCreateOrderBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FragmentCreateOrder extends Fragment {

    private FragmentAdminCreateOrderBinding binding;
    private OperateAssignment oA = OperateAssignment.getInstance();
    private String findCity = "";
    private GeneralUseCase gUC = GeneralUseCase.getInstance();
    private Assignment createNewAssignment;
    private int employeePicked;
    private OperateDB operateDB = OperateDB.getInstance();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminCreateOrderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.createOrderBtn.setOnClickListener(v -> new Thread(() -> {
            boolean isCaseInformationValid = isCreateNewCaseInformationValid();
            if (isCaseInformationValid){
                String orderNumber = binding.orderNumberEt.getEditText().getText().toString();
                String customerName = binding.customerNameEt.getEditText().getText().toString();
                String address = binding.addressEt.getEditText().getText().toString();
                String postalCode = binding.postalCodeEt.getEditText().getText().toString();
                String city = binding.cityEt.getEditText().getText().toString();
                String status;
                int userPicked = binding.chooseUserSpinner.getSelectedItemPosition();

                if (userPicked == 0) status = "waiting";
                else status = "active";

                createNewAssignment = new Assignment(orderNumber, customerName, address, postalCode, city, LocalDate.now(), status, employeePicked);
                operateDB.createNewAssignment(createNewAssignment); //TODO
            }
        }).start());

        binding.postalCodeInnerEt.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (i == KEYCODE_ENTER && keyEvent.getAction() == ACTION_DOWN) {
                    Thread t = new Thread(() -> {
                        findCity = oA.getCityMatchingZipCode(binding.postalCodeEt.getEditText().getText().toString());
                        if (!findCity.isEmpty()) {
                            binding.cityEt.getEditText().setText(findCity);
                        } else {
                            gUC.toastAlert(getActivity(), getString(R.string.create_order_wrong_postalcode));
                        }
                    });
                    t.start();
                }
                return false;
            }
        });

        List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add(getString(R.string.fragment_admin_create_spinner_default)); //Default value //TODO kan den bruges til som afventer value?
        for (int i = 1; i < UserContainer.getUsers().size(); i++) {
            if (UserContainer.getUsers().get(i).getPhonenumber() != null)
                spinnerArray.add(UserContainer.getUsers().get(i).getFullName() + " " + UserContainer.getUsers().get(i).getPhonenumber());
            else
                spinnerArray.add(UserContainer.getUsers().get(i).getFullName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = binding.chooseUserSpinner;
        spinner.setAdapter(adapter);
    }

    private boolean isCreateNewCaseInformationValid() {
        boolean isCustomerNameValid = gUC.checkIfLetters(binding.customerNameEt.getEditText().getText().toString());
        boolean isCityNameValid = gUC.checkIfLetters(binding.cityEt.getEditText().getText().toString());
        boolean isInputFieldsEmpty = gUC.isFieldsEmpty(new TextInputLayout[]{
                binding.orderNumberEt,
                binding.customerNameEt,
                binding.addressEt,
                binding.postalCodeEt,
                binding.cityEt});
        if (isInputFieldsEmpty) {
            gUC.toastAlert(getActivity(), getString(R.string.create_order_empty_fields));
        } else if (!isCustomerNameValid) {
            gUC.toastAlert(getActivity(), getString(R.string.create_order_invalid_customername));
        } else if (!isCityNameValid) {
            gUC.toastAlert(getActivity(), getString(R.string.create_order_invalid_cityname));
        }
        if (isCustomerNameValid && isCityNameValid && !isInputFieldsEmpty) return true;
        return false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}