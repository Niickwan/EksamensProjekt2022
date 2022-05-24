package com.jmmnt.UI;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.Entities.User;
import com.jmmnt.R;
import com.jmmnt.UseCase.Encryption;
import com.jmmnt.UseCase.GeneralUseCase;
import com.jmmnt.UseCase.OperateDB;
import com.jmmnt.databinding.FragmentEditProfileBinding;

public class FragmentEditProfile extends Fragment {

    private FragmentEditProfileBinding binding;
    private User loggedInUser = LoggedInUser.getInstance().getUser();
    private User user;
    private GeneralUseCase generalUseCase = GeneralUseCase.getInstance();
    private OperateDB operateDB = OperateDB.getInstance();
    private GeneralUseCase gUC = GeneralUseCase.getInstance();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentEditProfileBinding.inflate(inflater, container,false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.saveProfileBtn.setOnClickListener(v -> new Thread(() -> {
                popupMenuEditProfile();
        }).start());
        binding.editFirstNameEt.setText(loggedInUser.getFirstname());
        binding.editSurnameEt.setText(loggedInUser.getSurname());
        binding.editPhoneNumberEt.setText(loggedInUser.getPhonenumber());
        binding.editEmailEt.setText(loggedInUser.getEmail());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    public void popupMenuEditProfile() {
        user = null;
        boolean isEmpty = gUC.isFieldsEmpty(new EditText[] {
                binding.editFirstNameEt,
                binding.editSurnameEt,
                binding.editPasswordEt,
                binding.editConfirmPasswordEt});
        boolean isMatching = generalUseCase.isInputMatching(binding.editPasswordEt.getText().toString(),(binding.editConfirmPasswordEt.getText().toString()));
        boolean isFirstnameValid = gUC.checkIfLetters(binding.editFirstNameEt.getText().toString());
        boolean isSurnameValid = gUC.checkIfLetters(binding.editSurnameEt.getText().toString());
        boolean isEmailValid = gUC.checkIfEmail(binding.editEmailEt.getText().toString());
        boolean isPhoneNumbValid = gUC.checkIfNumber(binding.editPhoneNumberEt.getText().toString(),8);
        if (!isEmpty && isMatching && isFirstnameValid && isSurnameValid && (isEmailValid || isPhoneNumbValid)){
            user = new User(binding.editFirstNameEt.getText().toString(),
                    binding.editSurnameEt.getText().toString(),
                    binding.editPhoneNumberEt.getText().toString(),
                    binding.editEmailEt.getText().toString(),
                    Encryption.encrypt(binding.editPasswordEt.getText().toString()),
                    LoggedInUser.getInstance().getUser().getUserID(),
                    LoggedInUser.getInstance().getUser().getUserRights());
            boolean isProfileUpdated = operateDB.updateUserInDB(user);
            if (isProfileUpdated){
                LoggedInUser.getInstance().setUser(user);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TextView profileFirstname, profileSurname;
                        profileFirstname = getActivity().findViewById(R.id.profile_firstname_tv);
                        profileSurname = getActivity().findViewById(R.id.profile_surname_tv);
                        profileFirstname.setText(LoggedInUser.getInstance().getUser().getFirstname());
                        profileSurname.setText(LoggedInUser.getInstance().getUser().getSurname());
                        //Change fragment
                        NavHostFragment.findNavController(FragmentEditProfile.this).navigate(R.id.action_fragmentEditProfile_to_FragmentAdminHome);
                        //Show toastAlert - visual confirm for user
                        Thread toast = new Thread(() -> gUC.toastAlert(getActivity(), getString(R.string.popup_menu_edit_profile_confirm_update)));
                        toast.start();
                    }
                });
            }else
                gUC.toastAlert(getActivity(),getString(R.string.fragment_admin_not_in_use_label));
        }else {
            if (isEmpty) gUC.toastAlert(getActivity(), getString(R.string.popup_menu_edit_profile_fields_empty));
            else if (!isMatching) gUC.toastAlert(getActivity(), getString(R.string.popup_menu_edit_password_not_matching));
            else if (!isEmailValid && !isPhoneNumbValid) gUC.toastAlert(getActivity(), getString(R.string.popup_menu_edit_profile_eamil_and_phone_is_empty));
            else if (!isEmailValid) gUC.toastAlert(getActivity(), getString(R.string.popup_menu_edit_profile_email_not_valid));
            else if (!isPhoneNumbValid) gUC.toastAlert(getActivity(), getString(R.string.popup_menu_edit_profile_phone_not_valid));
        }

    }

}