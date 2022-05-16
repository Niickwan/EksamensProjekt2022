package com.jmmnt.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.jmmnt.Entities.User;
import com.jmmnt.R;
import com.jmmnt.UseCase.OperateDB;
import com.jmmnt.UseCase.OperateUser;
import com.jmmnt.databinding.FragmentLoginRegisterBinding;
import java.sql.SQLException;


public class FragmentLoginRegister extends Fragment{

    private OperateDB opDB = new OperateDB();
    private OperateUser opUsr = new OperateUser();
    private View.OnFocusChangeListener setOnFocusChangeListener;
    private FragmentLoginRegisterBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.createBtn.setOnClickListener(v -> new Thread(() -> {
            User user = opUsr.CreateDefaultUserLoginInfo(binding.registerFirstNameEt.getText().toString(), binding.registerSurnameEt.getText().toString(),
                    binding.registerEmailEt.getText().toString(), binding.registerPasswordEt.getText().toString());
            try {
                opDB.createUserInDB(user);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }).start());

       setOnFocusChangeListener = (view1, hasFocus) -> {
           switch (view1.getId()){
               case R.id.registerFirstName_et:
                   if (hasFocus) binding.registerFirstNameEt.setHint("");
                   else binding.registerFirstNameEt.setHint(getResources().getString(R.string.fragment_login_register_firstname_hint));
                   break;
               case R.id.registerSurname_et:
                   if (hasFocus) binding.registerSurnameEt.setHint("");
                   else binding.registerSurnameEt.setHint(getResources().getString(R.string.fragment_login_register_surname_hint));
                   break;
               case R.id.registerEmail_et:
                   if (hasFocus) binding.registerEmailEt.setHint("");
                   else binding.registerEmailEt.setHint("E-mail");
                   break;
               case R.id.registerPassword_et:
                   if (hasFocus) binding.registerPasswordEt.setHint("");
                   else binding.registerPasswordEt.setHint("Password");
                   break;
               default:
                   break;
           }
       };
        binding.registerFirstNameEt.setOnFocusChangeListener(setOnFocusChangeListener);
        binding.registerSurnameEt.setOnFocusChangeListener(setOnFocusChangeListener);
        binding.registerEmailEt.setOnFocusChangeListener(setOnFocusChangeListener);
        binding.registerPasswordEt.setOnFocusChangeListener(setOnFocusChangeListener);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}