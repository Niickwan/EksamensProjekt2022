package com.jmmnt.UI;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContract;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.jmmnt.Entities.User;
import com.jmmnt.R;
import com.jmmnt.UseCase.GeneralUseCase;
import com.jmmnt.UseCase.OperateDB;
import com.jmmnt.UseCase.OperateUser;
import com.jmmnt.databinding.FragmentLoginRegisterBinding;

import java.util.Objects;

public class FragmentLoginRegister extends Fragment{

    private OperateDB opDB = new OperateDB();
    private OperateUser opUsr = new OperateUser();
    private GeneralUseCase gUC = new GeneralUseCase();
    private View.OnFocusChangeListener setOnFocusChangeListener;
    private FragmentLoginRegisterBinding binding;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //TODO skal flyttes til det fragment, hvor der bliver taget billeder
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    binding.imageView32.setImageBitmap(bitmap);
                }
            }
        });
        //TODO skal flyttes til det fragment, hvor der bliver taget billeder
        binding.TrykForBillede.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 101);
                }
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) { //TODO fix så der ikke skal klikkes to gange for at åbne cam
                    openCamera();
                }
            }
        });

        binding.createBtn.setOnClickListener(v -> new Thread(() -> {
            if(!gUC.checkIfLetters(binding.registerFirstNameEt.getText().toString())
                || !gUC.checkIfLetters(binding.registerSurnameEt.getText().toString())
                || binding.registerSurnameEt.getText().toString().isEmpty()
                || binding.registerSurnameEt.getText().toString().isEmpty()) {
                gUC.toastAlert(getActivity(), "Fejl i navn");
            }

            else if(!gUC.checkIfEmail(binding.registerEmailEt.getText().toString()) ){
                gUC.toastAlert(getActivity(), "Ugyldig Email");
            }
            else if(opDB.isEmailOccupied(binding.registerEmailEt.getText().toString())){
                gUC.toastAlert(getActivity(), "Email er allerede oprettet");
            }
            else{
                User user = opUsr.CreateDefaultUserLoginInfo(
                        binding.registerFirstNameEt.getText().toString(),
                        binding.registerSurnameEt.getText().toString(),
                        binding.registerEmailEt.getText().toString(),
                        binding.registerPasswordEt.getText().toString());
                opDB.createUserInDB(user);
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
    //TODO skal flyttes til det fragment, hvor der bliver taget billeder
    public void openCamera(){
        Intent camera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        activityResultLauncher.launch(camera);
    }
}