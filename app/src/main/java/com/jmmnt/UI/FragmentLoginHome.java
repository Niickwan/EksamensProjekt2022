package com.jmmnt.UI;

import android.os.Bundle;
import android.os.Looper;
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

public class FragmentLoginHome extends Fragment {

    private FragmentLoginHomeBinding binding;
    private OperateDB opDB = OperateDB.getInstance();
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
            if (!TextUtils.isEmpty(binding.emailEt.getEditText().getText().toString()) && !TextUtils.isEmpty(binding.passwordEt.getEditText().getText().toString())){
                opDB.validateLogin(binding.emailEt.getEditText().getText().toString(), Encryption.encrypt(binding.passwordEt.getEditText().getText().toString()));
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
        Looper.prepare();
        binding.emailEt.getEditText().getText().clear();
        binding.passwordEt.getEditText().getText().clear();
        Looper.loop();
    }


}