package com.jmmnt.UI;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
                popupMenuEditProfile(); //TODO DER SKAL LAVES VALIDERING PÅ INPUTSFELTER
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
        boolean isMatching = generalUseCase.isInputMatching(binding.editPasswordEt.getText().toString(),(binding.editConfirmPasswordEt.getText().toString()));
        if (isMatching){
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
                        NavHostFragment.findNavController(FragmentEditProfile.this).navigate(R.id.action_fragmentEditProfile_to_FragmentAdminHome);
                    }
                });
            }
        }else
            gUC.toastAlert(getActivity(), getString(R.string.popup_menu_password_not_matching));
    }

}