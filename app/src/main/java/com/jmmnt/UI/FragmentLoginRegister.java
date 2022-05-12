package com.jmmnt.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jmmnt.databinding.FragmentLoginRegisterBinding;


public class FragmentLoginRegister extends Fragment {

    private FragmentLoginRegisterBinding binding;

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLoginRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.createBtn.setOnClickListener(v -> new Thread(() -> {
/*

            int loginRights = -1;
            try {
                loginRights = db_con.(binding.usernameEt.getText().toString(), binding.passwordEt.getText().toString());
                if (loginRights == 1) {
                    switchScene(getActivity(), ActivityAdmin.class);
                } else if (loginRights == 2) {
                    switchScene(getActivity(), ActivityUser.class);
                } else {
                    toastAlert("Forkert Login");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


 */
        }).start());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}