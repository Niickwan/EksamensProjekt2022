package com.jmmnt.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.Entities.User;
import com.jmmnt.R;
import com.jmmnt.UseCase.Encryption;
import com.jmmnt.UseCase.GeneralUseCase;
import com.jmmnt.UseCase.OperateDB;
import com.jmmnt.databinding.FragmentLoginHomeBinding;

public class FragmentLoginHome extends Fragment {

    private FragmentLoginHomeBinding binding;
    private OperateDB opDB = new OperateDB();
    private GeneralUseCase gUC = GeneralUseCase.getInstance();
    private User user = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.loginBtn.setOnClickListener(v -> new Thread(() -> {
            opDB.validateLogin(binding.emailEt.getText().toString(), Encryption.encrypt(binding.passwordEt.getText().toString()));
            user = LoggedInUser.getInstance().getUser();
            if (user != null) {
                if (user.getUserRights() == 1) {
                    gUC.switchScene(getActivity(), ActivityAdmin.class);
                    clearInputFields();
                } else if (user.getUserRights() == 2) {
                    gUC.switchScene(getActivity(), ActivityUser.class);
                    clearInputFields();
                }
            } else {
                gUC.toastAlert(getActivity(), getString(R.string.fragment_login_wrong_input));
            }
        }).start());

        binding.registerBtn.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(FragmentLoginHome.this).navigate(R.id.action_FragmentLoginHome_to_FragmentLoginRegister);
            binding.emailEt.getText().clear();
            binding.passwordEt.getText().clear();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void clearInputFields() {
        binding.emailEt.getText().clear();
        binding.passwordEt.getText().clear();
    }



}