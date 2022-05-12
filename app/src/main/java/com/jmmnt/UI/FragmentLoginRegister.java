package com.jmmnt.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jmmnt.Database.DB_Con;
import com.jmmnt.Entities.User;
import com.jmmnt.UseCase.OperateDB;
import com.jmmnt.UseCase.OperateUser;
import com.jmmnt.databinding.FragmentLoginRegisterBinding;

import java.sql.SQLException;


public class FragmentLoginRegister extends Fragment {

    OperateDB opDB = new OperateDB();
    OperateUser opUsr = new OperateUser();

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

            User user = opUsr.CreateDefaultUserLoginInfo(binding.userNameEt2.getText().toString(), binding.userSurnameEt2.getText().toString(),
                    binding.userEmailEt2.getText().toString(), binding.passwordEt2.getText().toString());

            try {
                opDB.createUserInDB(user);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }).start());

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}