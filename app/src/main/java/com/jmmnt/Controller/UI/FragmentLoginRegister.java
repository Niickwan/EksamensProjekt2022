package com.jmmnt.Controller.UI;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.CircuitDetails;
import com.jmmnt.Entities.Questions;
import com.jmmnt.Entities.ShortCircuitCurrentAndVoltageDrop;
import com.jmmnt.Entities.TestingRCD;
import com.jmmnt.Entities.TransitionResistance;
import com.jmmnt.Entities.User;
import com.jmmnt.UseCase.CreateExcelFile;
import com.jmmnt.UseCase.Encryption;
import com.jmmnt.UseCase.FTP.FTPClientFunctions;
import com.jmmnt.R;
import com.jmmnt.UseCase.GeneralUseCase;
import com.jmmnt.UseCase.OperateAssignment;
import com.jmmnt.UseCase.OperateDB;
import com.jmmnt.UseCase.OperateUser;
import com.jmmnt.UseCase.PDFGeneration.PDFGenerator;
import com.jmmnt.databinding.FragmentLoginRegisterBinding;

import org.apache.commons.net.ntp.TimeStamp;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class FragmentLoginRegister extends Fragment{

    //private OperateAssignment oAs = new OperateAssignment();
    private OperateDB opDB = OperateDB.getInstance();
    private OperateUser opUsr = new OperateUser();
    private GeneralUseCase gUC = GeneralUseCase.getInstance();
    private View.OnFocusChangeListener setOnFocusChangeListener;
    private FragmentLoginRegisterBinding binding;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private FTPClientFunctions ftpMethodClass = new FTPClientFunctions();
    private PDFGenerator pdfg = new PDFGenerator(new Assignment(1,"1","s","4700","s","s", LocalDate.now(),"s"));
    //TODO pdfGenerator skal tage det assignment som brugeren er inde på.
    //TODO SKAL INDSÆTTES I DEN RIGTIGE KLASSE

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoginRegisterBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //METODER TIL KAMERA------------------------------------------------------------
        //TODO skal flyttes til det fragment, hvor der bliver taget billeder
        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
           @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    ftpMethodClass.sendPicToFTP(bitmap, "TempName.png","/public_html/assignments",requireContext());
                }
            }
        });

        //TODO skal flyttes til det fragment, hvor der bliver taget billeder
//        binding.TrykForBillede.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
//                    ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.CAMERA}, 101);
//                }
//                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) { //TODO fix så der ikke skal klikkes to gange for at åbne cam
//                    openCamera();
//                }
//            }
//        });

        binding.FTPButton.setOnClickListener(v -> new Thread(() -> {
            ftpMethodClass.ftpDownload("/public_html/assignments/8888/3. Sal/ExcelTest.xls", "sl.xls");
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            }
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                try {
                    pdfg.createPDF(getContext());
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }

        }).start());

        binding.TrykForBillede.setOnClickListener(v -> {
            //oAs.getExcelAsArrayList("TjekListeNy.xls");

        });

        binding.writeExcelFile.setOnClickListener(v -> {
            // VARIABLES FROM CURRENT FOLDER AND DOCUMENT
            String fileName = "ExcelTest.xls";
            String folder = "/public_html/assignments/8888/3. Sal/";


            // TEST DATA //
            ArrayList<List<Object>> samletListe = new ArrayList<>();

            List<Object> general = new ArrayList<>();
            general.add(new Questions("Tavlen", 2, "Hvad sker der?"));
            general.add(new Questions("Elskab", 1, "Kom nu"));

            List<Object> electricalPanel = new ArrayList<>();
            electricalPanel.add(new Questions("El", 3, "Pas på"));
            electricalPanel.add(new Questions("Test", 2, "Hallo"));

            List<Object> installation = new ArrayList<>();
            installation.add(new Questions("Installation", 3, "Pas på"));
            installation.add(new Questions("Test Install", 3, "Hallo"));

            List<Object> protection = new ArrayList<>();
            protection.add(new Questions("Prot", 2, "Pas på"));
            protection.add(new Questions("Test Prot", 2, "Hallo"));

            List<Object> error = new ArrayList<>();
            error.add(new Questions("Error", 1, "Pas på"));
            error.add(new Questions("Test Error", 2, "Hallo"));

            List<Object> section = new ArrayList<>();
            section.add(new Questions("Section Section", 3, "Pas på"));
            section.add(new Questions("Test Section", 1, "Hallo"));

            List<Object> circuitDetailsResults = new ArrayList<>();
            circuitDetailsResults.add(new CircuitDetails("Tavlen", "400", "Lampe", "6", "500", 1, "340", "20"));
            circuitDetailsResults.add(new CircuitDetails("Stik", "40", "kontakt", "3", "300", 2, "34", "2"));

            List<Object> resistance = new ArrayList<>();
            resistance.add(new TransitionResistance(5.2));

            List<Object> voltageDropResults = new ArrayList<>();
            voltageDropResults.add(new ShortCircuitCurrentAndVoltageDrop("Elskab", "lk", "Kælderen", "1.sal", "560", "Kælderen"));
            voltageDropResults.add(new ShortCircuitCurrentAndVoltageDrop("Fryser", "lk", "Stue", "4.sal", "60", "Stue"));

            List<Object> testingRCDResults = new ArrayList<>();
            testingRCDResults.add(new TestingRCD("Test",1,"res1","res2","res3","res4","res5","res6"));
            testingRCDResults.add(new TestingRCD("Test",1,"res1","res2","res3","res4","res5","res6"));

            samletListe.add(general);
            samletListe.add(electricalPanel);
            samletListe.add(installation);
            samletListe.add(protection);
            samletListe.add(error);
            samletListe.add(section);
            samletListe.add(circuitDetailsResults);
            samletListe.add(resistance);
            samletListe.add(testingRCDResults);
            samletListe.add(voltageDropResults);

            //END TEST DATA //


            CreateExcelFile c = new CreateExcelFile();
            c.createExcelSheet(fileName, samletListe,"idfjiofdjgodjfoi");

            FTPClientFunctions ftp = new FTPClientFunctions();
            String uploadExcelToServerPath = folder + fileName;
            ftp.ftpUpload(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + fileName, uploadExcelToServerPath);

        });


        //METODER TIL KAMERA------------------------------------------------------------


        //ftp.sendPicToFTP();


        //binding.FTPButton.setOnClickListener(new Thread(() -> {
           //ftpMethodClass.sendPicToFTP();
     //   }).start());




        //METODER TIL CREATE USER-------------------------------------------------------
        binding.createBtn.setOnClickListener(v -> new Thread(() -> {
            if(!gUC.checkIfLetters(binding.registerFirstNameEt.getText().toString())
                || !gUC.checkIfLetters(binding.registerSurnameEt.getText().toString())
                || binding.registerFirstNameEt.getText().toString().isEmpty()
                    || binding.registerSurnameEt.getText().toString().isEmpty()) {
                gUC.toastAlert(getActivity(), "Fejl i navn");
            }
            else if(binding.registerEmailEt.getText().toString().isEmpty()
                    && binding.registerPhoneNumberEt.getText().toString().isEmpty()){
                gUC.toastAlert(getActivity(), "Udfyld enten email eller telefon");
            }
            else if(!binding.registerPhoneNumberEt.getText().toString().isEmpty()
                    && !gUC.checkIfNumber(binding.registerPhoneNumberEt.getText().toString(), 8)){
                gUC.toastAlert(getActivity(),"Telefonnummer ikke udfyldt korrekt");
            }
            else if(opDB.isPhonenumberOccupied(binding.registerPhoneNumberEt.getText().toString())){
                gUC.toastAlert(getActivity(), "Telefonnummer er allerede oprettet");
            }
            else if(!binding.registerEmailEt.getText().toString().isEmpty() &&
                    !gUC.checkIfEmail(binding.registerEmailEt.getText().toString()) ){
                gUC.toastAlert(getActivity(), "Ugyldig Email");
            }
            else if(opDB.isEmailOccupied(binding.registerEmailEt.getText().toString())){
                gUC.toastAlert(getActivity(), "Email er allerede oprettet");
            }
            else if(binding.registerPasswordEt.getText().toString().isEmpty()){
                gUC.toastAlert(getActivity(),"Password er ikke udfyldt");
            }
            else{
                User user = opUsr.CreateDefaultUserLoginInfo(
                        binding.registerFirstNameEt.getText().toString(),
                        binding.registerSurnameEt.getText().toString(),
                        binding.registerPhoneNumberEt.getText().toString(),
                        binding.registerEmailEt.getText().toString(),
                        Encryption.encrypt(binding.registerPasswordEt.getText().toString()));
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
        //METODER TIL CREATE USER-------------------------------------------------------

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