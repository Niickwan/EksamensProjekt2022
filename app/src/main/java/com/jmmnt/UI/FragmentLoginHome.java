package com.jmmnt.UI;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.PopupMenu;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.jmmnt.R;
import com.jmmnt.UseCase.OperateDB;
import com.jmmnt.databinding.FragmentLoginHomeBinding;

public class FragmentLoginHome extends Fragment {

    private FragmentLoginHomeBinding binding;
    private OperateDB opDB = new OperateDB();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.threeDotMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(view);
            }
        });
        binding.loginBtn.setOnClickListener(v -> new Thread(() -> {
            int loginRights = -1;
            try {
                loginRights = opDB.validateLogin(binding.emailEt.getText().toString(), binding.passwordEt.getText().toString());
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
        }).start());
        binding.registerBtn.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(FragmentLoginHome.this).navigate(R.id.action_FragmentLoginHome_to_FragmentLoginRegister);
            binding.emailEt.getText().clear();
            binding.passwordEt.getText().clear();
        });
    }

    private void toastAlert(String text) {
        Looper.prepare();
        Toast.makeText(getActivity(), text, Toast.LENGTH_LONG).show();
        Looper.loop();
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

    private void showPopupMenu(View view) {
        PopupMenu ppm = new PopupMenu(getContext(), view);
        ppm.getMenuInflater().inflate(R.menu.popup_menu, ppm.getMenu());
        ppm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();
                if (id == R.id.recoverPassword) {
                    System.out.println("GENDAN PASSWORD");
                    return true;
                }
                if (id == R.id.about) {
                    showPopupMenuAbout();
                    return true;
                }
                return false;
            }
        });
        ppm.show();
    }

    private void showPopupMenuAbout() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.fragment_login_company_info);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }



}