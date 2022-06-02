package com.jmmnt.Controller.UI;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.Entities.User;
import com.jmmnt.R;
import com.jmmnt.UseCase.Encryption;
import com.jmmnt.UseCase.GeneralUseCase;
import com.jmmnt.UseCase.OperateDB;
import com.jmmnt.databinding.FragmentLoginHomeBinding;

import java.util.Timer;
import java.util.TimerTask;

public class FragmentLoginHome extends Fragment {

    private FragmentLoginHomeBinding binding;
    private OperateDB opDB = OperateDB.getInstance();
    private GeneralUseCase gUC = GeneralUseCase.getInstance();
    private OperateDB oDB = OperateDB.getInstance();
    private User user = null;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.loginBtn.setOnClickListener(v -> new Thread(() -> {
            if (!TextUtils.isEmpty(binding.emailEt.getEditText().getText().toString()) && !TextUtils.isEmpty(binding.passwordEt.getEditText().getText().toString())){
                opDB.validateLogin(binding.emailEt.getEditText().getText().toString(), Encryption.encrypt(binding.passwordEt.getEditText().getText().toString()));
                user = LoggedInUser.getInstance().getUser();
                System.out.println(user.getFirstname());
                if (user != null) {
                    if (user.getUserRights() == 1) {
                        fillContainers();
                        //Delaying the activity switch for the containers to fill up
                        Timer delay = new Timer();
                        delay.schedule(new TimerTask() {
                            @Override
                            public void run() {
                                getActivity().runOnUiThread(() -> {
                                    gUC.switchScene(getActivity(), ActivityAdmin.class);
                                    clearInputFields();
                                });
                            }
                        },1000);
                    }
                } else {
                    gUC.toastAlert(getActivity(), getString(R.string.fragment_login_wrong_input));
                }
            }else {
                gUC.toastAlert(getActivity(), getString(R.string.fragment_login_fill_out_fields));
            }
        }).start());
        binding.registerBtn.setOnClickListener(view1 -> {
            NavHostFragment.findNavController(FragmentLoginHome.this).navigate(R.id.action_FragmentLoginHome_to_FragmentLoginRegister);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private void clearInputFields() {
        binding.emailEt.getEditText().getText().clear();
        binding.passwordEt.getEditText().getText().clear();
    }

    private void fillContainers() {
        Thread fillContainersThread = new Thread(() -> {
            oDB.fillAssignmentContainer();
            oDB.fillUserContainer();
            oDB.fillUserAssignmentsIDs(LoggedInUser.getInstance().getUser().getUserID());
        });
        fillContainersThread.start();
    }
}