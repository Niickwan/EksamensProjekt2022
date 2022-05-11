package com.jmmnt.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.jmmnt.Database.DB_Con;
import com.jmmnt.R;
import com.jmmnt.databinding.FragmentLoginHomeBinding;

public class FragmentLoginHome extends Fragment {

    private FragmentLoginHomeBinding binding;

    private DB_Con db_con = DB_Con.getInstance();

    private Button login;
    private TextView loginLabel;
    private EditText username, password;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        binding = FragmentLoginHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.loginBtn.setOnClickListener(v -> new Thread(() -> {
            int loginRights = -1;
            try {
                loginRights = db_con.validateLogin(binding.usernameEt.toString(), binding.passwordEt.toString());
                if (loginRights == 1){
                    switchScene(getActivity(), ActivityAdmin.class);
                } else if(loginRights == 2) {
                    switchScene(getActivity(), ActivityUser.class);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void switchScene(Context fromScene, Class toScene) {
        Intent switchActivity = new Intent(fromScene, toScene);
        startActivity(switchActivity);
    }

}