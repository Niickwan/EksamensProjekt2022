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
import android.widget.Spinner;
import com.google.android.material.textfield.TextInputLayout;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.AssignmentContainer;
import com.jmmnt.Entities.User;
import com.jmmnt.Entities.UserContainer;
import com.jmmnt.R;
import com.jmmnt.UseCase.Adapters.SpinnerAdapter;
import com.jmmnt.UseCase.GeneralUseCase;
import com.jmmnt.UseCase.OperateAssignment;
import com.jmmnt.UseCase.OperateDB;
import com.jmmnt.databinding.FragmentAdminCreateOrderBinding;
import java.time.LocalDate;

public class FragmentCreateOrder extends Fragment {

    private FragmentAdminCreateOrderBinding binding;
    private OperateAssignment oA = OperateAssignment.getInstance();
    private String findCity = "";
    private GeneralUseCase gUC = GeneralUseCase.getInstance();
    private Assignment createNewAssignment;
    private int userPicked;
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

                boolean isUpdated;
                if (userPicked == -1){
                    status = "waiting";
                    createNewAssignment = new Assignment(orderNumber, customerName, address, postalCode, city, "", userPicked, 26, LocalDate.now(), status);
                    isUpdated = operateDB.createNewAssignment(createNewAssignment);
                }
                else{
                    status = "active";
                    createNewAssignment = new Assignment(orderNumber, customerName, address, postalCode, city, "", userPicked, 26, LocalDate.now(), status);
                    isUpdated = operateDB.createNewAssignment(createNewAssignment, userPicked);
                }
                if (isUpdated) {
                    AssignmentContainer.getInstance().addAssignmentsToContainer(createNewAssignment);
                    String orderNum = binding.orderNumberEt.getEditText().getText().toString();
                    String fileName = orderNum + "_" + getString(R.string.default_value_floor_name) + ".xls";
                    oA.createFolderOnServer(orderNum, getString(R.string.default_value_floor_name), getString(R.string.default_value_room_name));
                    // TemplateFileName Hardcoded
                    oA.copyFilesOnServer("DefaultElectricianChecklist.xls", orderNum, getString(R.string.default_value_floor_name), fileName);
                    AssignmentContainer.getInstance().setCurrentAssignment(AssignmentContainer.getInstance().getAssignments().get(AssignmentContainer.getInstance().getAssignments().size()-1));
                    getActivity().runOnUiThread(() -> {
                        NavHostFragment.findNavController(FragmentCreateOrder.this).navigate(R.id.action_fragmentCreateOrder_to_FragmentAdminChecklist);
                    });
                }
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


        SpinnerAdapter adapter = new SpinnerAdapter(getContext(), android.R.layout.simple_spinner_item, UserContainer.getUsers());
        Spinner spinner = binding.chooseUserSpinner;
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                User user = (User) adapterView.getAdapter().getItem(position);
                userPicked = user.getUserID();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
    }

    private boolean isCreateNewCaseInformationValid() {
        boolean isCustomerNameValid = gUC.checkIfLetters(binding.customerNameEt.getEditText().getText().toString());
        boolean isCityNameValid = gUC.checkIfLetters(binding.cityEt.getEditText().getText().toString());
        boolean isOrderNumberAvailable = operateDB.doesOrderNumberExist(binding.orderNumberEt.getEditText().getText().toString());
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
        } else if (!isOrderNumberAvailable) gUC.toastAlert(getActivity(), getString(R.string.create_order_order_number_is_occupied));
        return isCustomerNameValid && isCityNameValid && !isInputFieldsEmpty && isOrderNumberAvailable;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}