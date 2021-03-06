package com.jmmnt.Controller.UI;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.textfield.TextInputLayout;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.AssignmentContainer;
import com.jmmnt.Entities.CircuitDetails;
import com.jmmnt.Entities.Questions;
import com.jmmnt.Entities.ShortCircuitCurrentAndVoltageDrop;
import com.jmmnt.Entities.TestingRCD;
import com.jmmnt.Entities.TransitionResistance;
import com.jmmnt.R;
import com.jmmnt.UseCase.Adapters.AdapterFactory;
import com.jmmnt.UseCase.CreateExcelFile;
import com.jmmnt.UseCase.FTP.FTPClientFunctions;
import com.jmmnt.UseCase.GeneralUseCase;
import com.jmmnt.UseCase.OperateAssignment;
import com.jmmnt.UseCase.OperateDB;
import com.jmmnt.UseCase.PDFGeneration.PDFGenerator;
import com.jmmnt.databinding.FragmentAdminChecklistBinding;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class FragmentAdminChecklist extends Fragment {

    private FragmentAdminChecklistBinding binding;
    private OperateAssignment opa = OperateAssignment.getInstance();
    private OperateDB oDB = OperateDB.getInstance();
    private GeneralUseCase gUC = GeneralUseCase.getInstance();
    private AssignmentContainer assignmentContainer = AssignmentContainer.getInstance();
    private FTPClientFunctions ftp = new FTPClientFunctions();
    private CreateExcelFile cEF = new CreateExcelFile();
    private Button addFloorBtn;
    private LayerDrawable selectedFloor;
    private LayerDrawable unSelectedFloor;
    private ArrayList<String> floors = new ArrayList<>();
    private ArrayList<Button> floorButtons = new ArrayList<>();
    private ArrayList<Object> general = new ArrayList<>();
    private ArrayList<Object> electricalPanel = new ArrayList<>();
    private ArrayList<Object> installation = new ArrayList<>();
    private ArrayList<Object> recessedLuminaire = new ArrayList<>();
    private ArrayList<Object> protectiveConductors = new ArrayList<>();
    private ArrayList<Object> faultyProtection = new ArrayList<>();
    private ArrayList<Object> circuitDetailList = new ArrayList<>();
    private ArrayList<Object> transitionResistance = new ArrayList<>();
    private ArrayList<Object> testingRCDResults = new ArrayList<>();
    private ArrayList<Object> voltageDropResults = new ArrayList<>();
    private ArrayList<ArrayList<Object>> completeAssignment = new ArrayList<>();
    private String documentNote = "";
    private LinearLayout parentLLH;
    private String orderNr = assignmentContainer.getCurrentAssignment().getOrderNumber();
    private LinearLayout floorLinearLayout;
    private String selectedFloorName = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminChecklistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        selectedFloor = floorIsSelected();
        unSelectedFloor = floorIsNotSelected();

        addFloorBtn = gUC.createBtnForHSV("+", getActivity(), 150, 300);
        addFloorBtn.setBackground(unSelectedFloor);
        addFloorBtn.setTextColor(Color.GREEN);
        addFloorBtn.setOnClickListener(v -> popupAddFloor());

        binding.hsvFloor.setHorizontalScrollBarEnabled(false);
        floorLinearLayout = new LinearLayout(getActivity());
        floorLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
        floorLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        binding.hsvRoom.setHorizontalScrollBarEnabled(false);
        LinearLayout roomLinearLayout = new LinearLayout(getActivity());
        roomLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        Thread t1 = new Thread(() -> setFloorHorizontalScrollBar());
        Thread t2 = new Thread(() -> {
            String excelFileName = orderNr + "_" + selectedFloorName + ".xls";
            ftp.ftpDownload("/public_html/assignments/" + orderNr + "/" + selectedFloorName + "/" + excelFileName, "current_assignment.xls");
        });
        Thread t3 = new Thread(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            getActivity().runOnUiThread(() -> generateUI());
        });
        try {
            t1.start();
            t1.join();
            t2.start();
            t2.join();
            t3.start();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private void generateUI() {
        ArrayList<String> template = opa.getExcelAsArrayList("current_assignment.xls");
        //Dropdown titles
        String objectTag = "question";
        String objectTag2 = "circuitDetails";
        String objectTag3 = "RCD";
        String objectTag4 = "ShortCircuitCurrent";
        String objectTag5 = "Assignment";

        //Build UI Dynamically
        //Setting parent layout for the UI
        parentLLH = getActivity().findViewById(R.id.parentLLH);

        //Building dropdowns
        ArrayList<Assignment> currentListAssignment = new ArrayList<>();
        currentListAssignment.add(AssignmentContainer.getInstance().getCurrentAssignment());
        buildDropdownDynamically("Ordre", currentListAssignment, objectTag5, "vertical");
        int headlineCounter = 0;
        int inputHeadlineCounter = 0;
        for (int i = 0; i < template.size(); i++) {
            if (template.get(i).equalsIgnoreCase("<Headline>")) {
                if (headlineCounter == 0) {
                    String headline = template.get(i + 1);
                    i = readQuestionFromExcel(template, i, general);
                    buildDropdownDynamically(headline, general, objectTag, "vertical");
                    headlineCounter++;
                } else if (headlineCounter == 1) {
                    String headline = template.get(i + 1);
                    i = readQuestionFromExcel(template, i, electricalPanel);
                    buildDropdownDynamically(headline, electricalPanel, objectTag, "vertical");
                    headlineCounter++;
                } else if (headlineCounter == 2) {
                    String headline = template.get(i + 1);
                    i = readQuestionFromExcel(template, i, installation);
                    buildDropdownDynamically(headline, installation, objectTag, "vertical");
                    headlineCounter++;
                } else if (headlineCounter == 3) {
                    String headline = template.get(i + 1);
                    i = readQuestionFromExcel(template, i, recessedLuminaire);
                    buildDropdownDynamically(headline, recessedLuminaire, objectTag, "vertical");
                    headlineCounter++;
                } else if (headlineCounter == 4) {
                    String headline = template.get(i + 1);
                    i = readQuestionFromExcel(template, i, protectiveConductors);
                    buildDropdownDynamically(headline, protectiveConductors, objectTag, "vertical");
                    headlineCounter++;
                } else if (headlineCounter == 5) {
                    String headline = template.get(i + 1);
                    i = readQuestionFromExcel(template, i, faultyProtection);
                    buildDropdownDynamically(headline, faultyProtection, objectTag, "vertical");
                    headlineCounter++;

                    //Adding textview
                    TextView testResultsTitle = new TextView(getActivity());
                    testResultsTitle.setId(View.generateViewId());
                    LinearLayout.LayoutParams paramsTRT = new LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.WRAP_CONTENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT
                    );
                    paramsTRT.setMargins(10, 10, 10, 30);
                    testResultsTitle.setLayoutParams(paramsTRT);
                    testResultsTitle.setTypeface(Typeface.DEFAULT_BOLD);
                    testResultsTitle.setText(getString(R.string.test_results_title_name));
                    testResultsTitle.setTextSize(34);
                    testResultsTitle.setTextColor(getActivity().getColor(R.color.darkblue));

                    //Adding textview to parent
                    parentLLH.addView(testResultsTitle);
                }
            } else if (template.get(i).equalsIgnoreCase("<InputHeadline>")) {
                if (inputHeadlineCounter == 0) {
                    i = readExcelCircuitDetails(template, i, circuitDetailList);
                    buildDropdownDynamically("Kredsdetaljer", circuitDetailList, objectTag2, "horizontal");
                    inputHeadlineCounter++;

                } else if (inputHeadlineCounter == 1) {
                    i = readExcelTestingRCD(template, i, testingRCDResults);
                    buildDropdownDynamically("Afpr??vning af RCD'er", testingRCDResults, objectTag3, "horizontal");
                    inputHeadlineCounter++;
                } else if (inputHeadlineCounter == 2) {
                    i = readExcelShortCircuitAndVoltageDrop(template, i, voltageDropResults);
                    buildDropdownDynamically("Kortslutningsstr??m / Sp??ndingsfald", voltageDropResults, objectTag4, "horizontal");
                    inputHeadlineCounter++;
                }
            } else if (template.get(i).equalsIgnoreCase("<SingleInput>")) {
                TransitionResistance transitionResistance = new TransitionResistance();
                this.transitionResistance.add(transitionResistance);
                //Adding textview
                TextView groundElectrodeTv = new TextView(getActivity());
                groundElectrodeTv.setId(View.generateViewId());
                LinearLayout.LayoutParams paramsGET = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                paramsGET.gravity = Gravity.CENTER_HORIZONTAL;
                paramsGET.setMargins(16, 16, 16, 5);
                groundElectrodeTv.setLayoutParams(paramsGET);
                groundElectrodeTv.setText(getString(R.string.ground_electrode_name));
                groundElectrodeTv.setTextColor(getActivity().getColor(R.color.black));
                groundElectrodeTv.setTextSize(14);
                groundElectrodeTv.setTypeface(Typeface.DEFAULT_BOLD);

                //Adding textview to parent layout
                parentLLH.addView(groundElectrodeTv);

                //Adding edittext
                EditText groundElectrodeResultEt = new EditText(getActivity());
                groundElectrodeResultEt.setId(View.generateViewId());
                LinearLayout.LayoutParams paramsGER = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                paramsGER.gravity = Gravity.CENTER_HORIZONTAL;
                paramsGER.setMargins(16, 0, 16, 25);
                groundElectrodeResultEt.setLayoutParams(paramsGER);
                groundElectrodeResultEt.setBackground(getActivity().getDrawable(R.drawable.design_edittext));
                groundElectrodeResultEt.setPadding(10, 0, 10, 0);
                groundElectrodeResultEt.setTextColor(getActivity().getColor(R.color.black));
                groundElectrodeResultEt.setSingleLine(true);
                groundElectrodeResultEt.setEms(20);
                groundElectrodeResultEt.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_CLASS_NUMBER);
                groundElectrodeResultEt.setText(gUC.convertMinusOneToEmptyString(template.get(i + 2)));
                groundElectrodeResultEt.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (!charSequence.toString().isEmpty())
                            transitionResistance.setTransitionResistance(Double.parseDouble(groundElectrodeResultEt.getEditableText().toString()));
                        else transitionResistance.setTransitionResistance(-1.0);
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });

                //Adding edittext to parent
                parentLLH.addView(groundElectrodeResultEt);
            } else if (template.get(i).equals("<Document Note>")) {
                //Adding edittext with multiple lines
                ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.ViewWithScrollbars);
                EditText remarkEtML = new EditText(ctw);
                remarkEtML.setId(View.generateViewId());
                LinearLayout.LayoutParams paramsRETML = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                paramsRETML.setMargins(10, 50, 10, 100);
                remarkEtML.setLayoutParams(paramsRETML);
                remarkEtML.setGravity(Gravity.START);
                remarkEtML.setPadding(10, 0, 10, 0);
                remarkEtML.setBackground(getActivity().getDrawable(R.drawable.design_edittext));
                remarkEtML.setTextColor(getActivity().getColor(R.color.black));
                remarkEtML.setInputType(InputType.TYPE_TEXT_FLAG_IME_MULTI_LINE);
                remarkEtML.setHint(getString(R.string.comment));
                remarkEtML.setHintTextColor(getActivity().getColor(R.color.black));
                remarkEtML.setSingleLine(false);
                remarkEtML.setLines(10);
                remarkEtML.setScrollBarFadeDuration(1000);
                documentNote = template.get(i + 1);
                if (template.get(i + 1).equals("-1")) remarkEtML.setText("");
                else remarkEtML.setText(template.get(i + 1));
                remarkEtML.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    }
                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                        if (!charSequence.toString().isEmpty())
                            documentNote = remarkEtML.getEditableText().toString();
                        else documentNote = "-1";
                    }
                    @Override
                    public void afterTextChanged(Editable editable) {
                    }
                });
                //This make it possible to scroll inside the textarea by disallowing the parentView to intercept touch event on the textarea.
                remarkEtML.setOnTouchListener((v, motionEvent) -> {
                    if (v.getId() == remarkEtML.getId()) {
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        switch (motionEvent.getAction() & MotionEvent.ACTION_MASK) {
                            case MotionEvent.ACTION_UP:
                                v.getParent().requestDisallowInterceptTouchEvent(false);
                                break;
                        }
                    }
                    return false;
                });

                //Adding edittext with multiple lines to parent
                parentLLH.addView(remarkEtML);
            }
        }

        //Adding button for finishing the case
        Button finishCaseBtn = new Button(getActivity());
        finishCaseBtn.setOnClickListener(v -> {
            Thread createExcelT = new Thread(() -> {

                for (int i = 0; i < circuitDetailList.size(); i++) {
                    CircuitDetails objVar = (CircuitDetails) circuitDetailList.get(i);
                    if (objVar.getGroupName().isEmpty()) ((CircuitDetails) circuitDetailList.get(i)).setGroupName("-1");
                    if (objVar.getOb().isEmpty()) ((CircuitDetails) circuitDetailList.get(i)).setOb("-1");
                    if (objVar.getCharacteristics().isEmpty()) ((CircuitDetails) circuitDetailList.get(i)).setCharacteristics("-1");
                    if (objVar.getCrossSection().isEmpty()) ((CircuitDetails) circuitDetailList.get(i)).setCrossSection("-1");
                    if (objVar.getMaxOB().isEmpty()) ((CircuitDetails) circuitDetailList.get(i)).setMaxOB("-1");
                    if (objVar.getOmega().isEmpty()) ((CircuitDetails) circuitDetailList.get(i)).setOmega("-1");
                    if (objVar.getOmega().isEmpty()) ((CircuitDetails) circuitDetailList.get(i)).setMilliOmega("-1");
                }

                for (int i = 0; i < testingRCDResults.size(); i++) {
                    TestingRCD objVar = (TestingRCD) testingRCDResults.get(i);
                    if (objVar.getGroupName().isEmpty()) ((TestingRCD) testingRCDResults.get(i)).setGroupName("-1");
                    if (objVar.getFirstResult().isEmpty()) ((TestingRCD) testingRCDResults.get(i)).setFirstResult("-1");
                    if (objVar.getSecondResult().isEmpty()) ((TestingRCD) testingRCDResults.get(i)).setSecondResult("-1");
                    if (objVar.getThirdResult().isEmpty()) ((TestingRCD) testingRCDResults.get(i)).setThirdResult("-1");
                    if (objVar.getFourthResult().isEmpty()) ((TestingRCD) testingRCDResults.get(i)).setFourthResult("-1");
                    if (objVar.getFifthResult().isEmpty()) ((TestingRCD) testingRCDResults.get(i)).setFifthResult("-1");
                    if (objVar.getSixthResult().isEmpty()) ((TestingRCD) testingRCDResults.get(i)).setSixthResult("-1");
                }

                for (int i = 0; i < voltageDropResults.size(); i++) {
                    ShortCircuitCurrentAndVoltageDrop objVar = (ShortCircuitCurrentAndVoltageDrop) voltageDropResults.get(i);
                    if (objVar.getShortCircuitGroupName().isEmpty()) ((ShortCircuitCurrentAndVoltageDrop) voltageDropResults.get(i)).setShortCircuitGroupName("-1");
                    if (objVar.getShortCircuitLk().isEmpty()) ((ShortCircuitCurrentAndVoltageDrop) voltageDropResults.get(i)).setShortCircuitLk("-1");
                    if (objVar.getShortCircuitMeasuredOnLocation().isEmpty()) ((ShortCircuitCurrentAndVoltageDrop) voltageDropResults.get(i)).setShortCircuitMeasuredOnLocation("-1");
                    if (objVar.getVoltageDropGroupName().isEmpty()) ((ShortCircuitCurrentAndVoltageDrop) voltageDropResults.get(i)).setVoltageDropGroupName("-1");
                    if (objVar.getVoltageDropDeltaVoltage().isEmpty()) ((ShortCircuitCurrentAndVoltageDrop) voltageDropResults.get(i)).setVoltageDropDeltaVoltage("-1");
                    if (objVar.getVoltageDropMeasuredOnLocation().isEmpty()) ((ShortCircuitCurrentAndVoltageDrop) voltageDropResults.get(i)).setVoltageDropMeasuredOnLocation("-1");
                }

                completeAssignment.add(general);
                completeAssignment.add(electricalPanel);
                completeAssignment.add(installation);
                completeAssignment.add(recessedLuminaire);
                completeAssignment.add(protectiveConductors);
                completeAssignment.add(faultyProtection);
                completeAssignment.add(circuitDetailList);
                completeAssignment.add(transitionResistance);
                completeAssignment.add(testingRCDResults);
                completeAssignment.add(voltageDropResults);

                cEF.createExcelSheet("current_assignment.xls", completeAssignment, documentNote);
            });
            String filename = orderNr + "_" + selectedFloorName;
            Thread createPdfT = new Thread(new Runnable() {
                @Override
                public void run() {
                    PDFGenerator pdfg = new PDFGenerator(assignmentContainer.getCurrentAssignment());
                    try {
                        pdfg.createPDF(getContext(), filename);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            Thread uploadExcelT = new Thread(() -> ftp.ftpUpload(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/current_assignment.xls",
                    "/public_html/assignments/" + orderNr + "/" + selectedFloorName + "/" + filename + ".xls"));
            Thread uploadPdfT = new Thread(new Runnable() {
                @Override
                public void run() {
                    ftp.ftpUpload(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/" + filename + ".pdf",
                            "/public_html/assignments/" + orderNr + "/" + selectedFloorName + "/" + filename + ".pdf");
                }
            });
            try {
                createExcelT.start();
                createExcelT.join();
                createPdfT.start();
                createPdfT.join();
                uploadExcelT.start();
                uploadExcelT.join();
                uploadPdfT.start();
                uploadPdfT.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        finishCaseBtn.setId(View.generateViewId());
        LinearLayout.LayoutParams paramsFCB = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        finishCaseBtn.setLayoutParams(paramsFCB);
        finishCaseBtn.setText(getString(R.string.checklist_finish));
        finishCaseBtn.setTextColor(getActivity().getColor(R.color.white));
        finishCaseBtn.setBackground(getActivity().getDrawable(R.drawable.design_button_darkblue));

        //Adding button for sending the case
        Button sendCaseBtn = new Button(getActivity());
        sendCaseBtn.setId(View.generateViewId());
        LinearLayout.LayoutParams paramsSCB = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        sendCaseBtn.setLayoutParams(paramsSCB);
        sendCaseBtn.setText(getString(R.string.checklist_send));
        sendCaseBtn.setTextColor(getActivity().getColor(R.color.white));
        sendCaseBtn.setBackground(getActivity().getDrawable(R.drawable.design_button_darkblue));
        sendCaseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               //TODO Send tjekliste
            }
        });

        //Adding a constraint layout
        ConstraintLayout cl = new ConstraintLayout(getActivity());
        cl.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        cl.setId(View.generateViewId());
        LinearLayout.LayoutParams paramsCL = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        paramsCL.setMargins(0, 0, 0, 50);
        cl.setLayoutParams(paramsCL);

        //Adding buttons to constraint layout
        cl.addView(finishCaseBtn);
        cl.addView(sendCaseBtn);

        //Adding constraintSet for the finish button
        ConstraintSet constraintSetFCB = new ConstraintSet();
        constraintSetFCB.clone(cl);
        constraintSetFCB.connect(finishCaseBtn.getId(), ConstraintSet.START, cl.getId(), ConstraintSet.START, 5);
        constraintSetFCB.applyTo(cl);

        //Adding constraintSet for the send button
        ConstraintSet constraintSetSCB = new ConstraintSet();
        constraintSetSCB.clone(cl);
        constraintSetSCB.connect(sendCaseBtn.getId(), ConstraintSet.END, cl.getId(), ConstraintSet.END, 5);
        constraintSetSCB.applyTo(cl);

        //Adding the constraint layout to the parent
        parentLLH.addView(cl);
    }

    private int readExcelShortCircuitAndVoltageDrop(ArrayList<String> template, int i, ArrayList<Object> voltageDropResults) {
        int counter = 0;
        for (int j = i; j < template.size(); j++) {
            if (template.get(j).equalsIgnoreCase("<inputAnswer>")) {
                ShortCircuitCurrentAndVoltageDrop s = new ShortCircuitCurrentAndVoltageDrop();
                s.setShortCircuitGroupName(template.get(j + 1));
                s.setShortCircuitLk(template.get(j + 2));
                s.setShortCircuitMeasuredOnLocation(template.get(j + 3));
                for (int k = j; k < template.size(); k++) {
                    if (template.get(k).equalsIgnoreCase("<InputHeadline>")) {
                        for (int l = k; l < template.size(); l++) {
                            if (template.get(l).equalsIgnoreCase("<inputAnswer>")) {
                                s.setVoltageDropGroupName(template.get(l + 1 + counter));
                                s.setVoltageDropDeltaVoltage(template.get(l + 2 + counter));
                                s.setVoltageDropMeasuredOnLocation(template.get(l + 3 + counter));
                                counter += 4;
                                break;
                            }
                        }
                        break;
                    }
                }
                voltageDropResults.add(s);
            } else if (template.get(j).equals("<InputHeadlineEnd>")) {
                i = j;
                break;
            }
        }
        return i;
    }

    private int readExcelTestingRCD(ArrayList<String> template, int i, ArrayList<Object> testingRCDResults) {
        for (int j = i; j < template.size(); j++) {
            if (template.get(j).equals("<inputAnswer>")) {
                TestingRCD t = new TestingRCD();
                t.setGroupName(template.get(j + 1));
                t.setFirstResult(template.get(j + 2));
                t.setSecondResult(template.get(j + 3));
                t.setThirdResult(template.get(j + 4));
                t.setFourthResult(template.get(j + 5));
                t.setFifthResult(template.get(j + 6));
                t.setSixthResult(template.get(j + 7));
                if (template.get(j + 8).equalsIgnoreCase("ok")) {{
                    t.setCheckboxOK(1);
                }} else {
                    t.setCheckboxOK(-1);
                }
                testingRCDResults.add(t);
            } else if (template.get(j).equals("<InputHeadlineEnd>")) {
                i = j;
                break;
            }
        }
        return i;
    }

    private int readExcelCircuitDetails(ArrayList<String> template, int i, ArrayList<Object> circuitDetailList) {
        for (int j = i; j < template.size(); j++) {
            if (template.get(j).equals("<inputAnswer>")) {
                CircuitDetails circuitDetails = new CircuitDetails();
                circuitDetails.setGroupName(template.get(j + 1));
                circuitDetails.setOb(template.get(j + 2));
                circuitDetails.setCharacteristics(template.get(j + 3));
                circuitDetails.setCrossSection(template.get(j + 4));
                circuitDetails.setMaxOB(template.get(j + 5));
                if (template.get(j + 7).equals("-1") && template.get(j + 6).equals("-1")) {
                    circuitDetails.setCheckbox(0);
                    circuitDetails.setOmega("");
                } else if (template.get(j + 7).equals("-1")) {
                    circuitDetails.setOmega(template.get(j + 6));
                    circuitDetails.setCheckbox(1);
                } else {
                    circuitDetails.setOmega(template.get(j + 7));
                    circuitDetails.setCheckbox(2);
                }
                circuitDetails.setMilliOmega(template.get(j + 8));
                circuitDetailList.add(circuitDetails);
            } else if (template.get(j).equals("<InputHeadlineEnd>")) {
                i = j;
                break;
            }
        }
        return i;
    }


    private int readQuestionFromExcel(ArrayList<String> template, int i, ArrayList<Object> general) {
        for (int j = i; j < template.size(); j++) {
            if (template.get(j).equals("<Question>")){
                Questions question = new Questions();
                int answer = Integer.parseInt(template.get(j +4));
                question.setQuestion(template.get(j +1) + " " + template.get(j +2));
                question.setAnswer(answer);
                question.setComment(template.get(j +6));
                int excelRowCounter = 8;
                while(!template.get(j + excelRowCounter).equals("<ImagesEnd>")) {
                    question.getImages().add(template.get(j +excelRowCounter));
                    excelRowCounter++;
                }
                general.add(question);
            }
            else if (template.get(j).equals("<HeadlineEnd>")){
                i = j;
                break;
            }
        }
        return i;
    }

    private void setFloorHorizontalScrollBar() {
        ArrayList<String> hsvStructure = new ArrayList<>();
        try {
            hsvStructure = oDB.getAssignmentStructure(orderNr);
        } finally {
            floors = gUC.sortStringBeforeNumbers(gUC.getSplittedString(hsvStructure, orderNr, "/<"));
            selectedFloorName = floors.get(0);
            getActivity().runOnUiThread(() -> {
                binding.hsvFloor.removeAllViews();
                setHorizontalFloorBar(floors);
                binding.hsvFloor.addView(floorLinearLayout);
                binding.hsvRoom.setVisibility(View.GONE);
            });
        }
    }

    private void setHorizontalFloorBar(ArrayList<String> floors) {
        floorLinearLayout.removeAllViews();
        floorButtons.clear();
        for (int i = 0; i < floors.size(); i++) {
            Button b = gUC.createBtnForHSV(floors.get(i), getActivity(), 150, 300);
            b.setBackgroundColor(getResources().getColor(R.color.purple_700, getActivity().getTheme()));
            b.setOnClickListener(v -> {
                selectedFloorName = b.getText().toString();
                changeSelectedMenuButton();
            });
            b.setOnLongClickListener(v -> {
                popupEditOrDeleteFloor(b.getText().toString());
                return true;
            });
            floorButtons.add(b);
            floorLinearLayout.addView(b);
            changeSelectedMenuButton();
        }
        floorLinearLayout.addView(addFloorBtn);
    }

    // TODO G??res til generelle metoder
    // TODO man f??r ikke fejl hvis man opretter flere af samme navn RET!
    public void popupAddFloor() {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.popup_add_floor);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextInputLayout newFloorName = dialog.getWindow().findViewById(R.id.floor_et);
        dialog.getWindow().findViewById(R.id.rename_floor_btn).setOnClickListener(v -> new Thread(() -> {
            String createFloorName = newFloorName.getEditText().getText().toString();
            opa.createFolderOnServer(orderNr, createFloorName, "");
            opa.copyFilesOnServer("DefaultElectricianChecklist.xls", orderNr, createFloorName, orderNr + "_" + createFloorName + ".xls");
            getActivity().runOnUiThread(() -> {
                floors.add(newFloorName.getEditText().getText().toString());
                selectedFloorName = newFloorName.getEditText().getText().toString();
                setHorizontalFloorBar(floors);
                dialog.dismiss();
            });
        }).start());
        dialog.getWindow().findViewById(R.id.cancel_btn).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    public void popupEditOrDeleteFloor(String oldName) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.popup_edit_or_delete_floor);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button deleteBtn = dialog.getWindow().findViewById(R.id.delete_floor_btn);
        TextInputLayout floorName = dialog.getWindow().findViewById(R.id.floor_et);
        floorName.getEditText().setText(selectedFloorName);
        dialog.getWindow().findViewById(R.id.rename_floor_btn).setOnClickListener(v -> new Thread(() -> {
            if (opa.renameFolderOnServer(orderNr, oldName, floorName.getEditText().getText().toString())) {
                getActivity().runOnUiThread(() -> {
                    for (int i = 0; i < floorButtons.size(); i++) {
                        if (floorButtons.get(i).getText().equals(oldName)) {
                            floorButtons.get(i).setText(floorName.getEditText().getText().toString());
                            selectedFloorName = floorName.getEditText().getText().toString();
                        }
                        deleteBtn.setVisibility(View.GONE);
                        dialog.dismiss();
                    }
                });
            } else {
                getActivity().runOnUiThread(() -> {
                    TextView error = dialog.getWindow().findViewById(R.id.error_message);
                    error.setText(getString(R.string.checklist_unable_to_create_floor));
                });
            }
        }).start());
        dialog.getWindow().findViewById(R.id.enable_delete_switch).setOnClickListener(v -> {
            if(floorButtons.size() < 2) {
                Thread t = new Thread(() -> gUC.toastAlert(getActivity(), getString(R.string.checklist_unable_to_delete_floor)));
                t.start();
                Switch s = v.findViewById(R.id.enable_delete_switch);
                s.setChecked(false);
            } else {
                if (deleteBtn.getVisibility() == View.VISIBLE) {
                    deleteBtn.setVisibility(View.GONE);
                } else {
                    deleteBtn.setVisibility(View.VISIBLE);
                }
            }
        });
        dialog.getWindow().findViewById(R.id.delete_floor_btn).setOnClickListener(v -> new Thread(() -> {
            String deleteDirLocation = orderNr + "/" + selectedFloorName;
            opa.deleteDirectoryOnServer(deleteDirLocation);
            getActivity().runOnUiThread(() -> {
                //Remove from list and set new name instance variable
                for (int i = 0; i < floorButtons.size(); i++) {
                    setFloorHorizontalScrollBar();
                }
                deleteBtn.setVisibility(View.GONE);
                dialog.dismiss();
            });
        }).start());
        dialog.getWindow().findViewById(R.id.cancel_btn).setOnClickListener(v -> {
            deleteBtn.setVisibility(View.GONE);
            dialog.dismiss();
        });
        dialog.show();
    }

    public void changeSelectedMenuButton() {
        for (int i = 0; i < floorButtons.size(); i++) {
            if (floorButtons.get(i).getText().equals(selectedFloorName)) {
                floorButtons.get(i).setBackground(selectedFloor);
            } else {
                floorButtons.get(i).setBackground(unSelectedFloor);
            }
        }
    }

    private LayerDrawable floorIsSelected() {
        GradientDrawable borderSelected = new GradientDrawable();
        borderSelected.setColor(getResources().getColor(R.color.purple_700, getActivity().getTheme()));
        borderSelected.setAlpha(150);
        borderSelected.setStroke(7, Color.BLACK);
        borderSelected.setShape(GradientDrawable.RECTANGLE);
        return new LayerDrawable(new Drawable[]{borderSelected});
    }

    private LayerDrawable floorIsNotSelected() {
        GradientDrawable borderNotSelected = new GradientDrawable();
        borderNotSelected.setColor(getResources().getColor(R.color.purple_700, getActivity().getTheme()));
        borderNotSelected.setShape(GradientDrawable.RECTANGLE);
        borderNotSelected.setAlpha(220);
        return new LayerDrawable(new Drawable[]{borderNotSelected});
    }


    /**
     * @param title       - the name for the dropdown
     * @param dataList    - is the items for the dropdown
     * @param objectTag   - used for adapterFactory
     * @param orientation - used for defining the view orientation (vertical/horizontal)
     */
    public void buildDropdownDynamically(String title, ArrayList<?> dataList, String objectTag, String orientation) {
        ContextThemeWrapper ctw = new ContextThemeWrapper(getActivity(), R.style.ViewWithScrollbars);
        ImageView img = new ImageView(getActivity());
        RecyclerView rv = new RecyclerView(ctw);
        Button addBtn = new Button(getActivity());

        //Creating CardView
        CardView cv = new CardView(getActivity());
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        cv.setLayoutParams(cardViewParams);
        ViewGroup.MarginLayoutParams cardViewMarginParams = (ViewGroup.MarginLayoutParams) cv.getLayoutParams();
        cardViewMarginParams.setMargins(0, 15, 0, 15);
        cv.requestLayout();
        cv.setCardBackgroundColor(getActivity().getColor(R.color.background_color_zealand));
        cv.setElevation(10);
        cv.setOnClickListener(view -> {
            if (rv.getVisibility() == View.GONE) {
                img.setBackgroundResource(R.drawable.ic_baseline_arrow_up_24);
                rv.setVisibility(View.VISIBLE);
                addBtn.setVisibility(View.VISIBLE);
            } else {
                rv.setVisibility(View.GONE);
                addBtn.setVisibility(View.GONE);
                img.setBackgroundResource(R.drawable.ic_baseline_arrow_down_24);
            }
        });

        //Creating LinearLayout - Vertically
        LinearLayout llv = new LinearLayout(getActivity());
        llv.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llv.setBackgroundResource(R.drawable.design_dropdown_cardview);
        llv.setOrientation(LinearLayout.VERTICAL);

        //Creating LinearLayout - Horizontally
        LinearLayout llh = new LinearLayout(getActivity());
        llh.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        llh.setOrientation(LinearLayout.HORIZONTAL);

        //Set dropdown title
        TextView dropdownTitle = new TextView(getActivity());
        dropdownTitle.setText(title);
        dropdownTitle.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        dropdownTitle.setId(View.generateViewId());
        dropdownTitle.setPadding(10, 10, 10, 10);
        dropdownTitle.setTextColor(getActivity().getColor(R.color.darkblue));
        dropdownTitle.setTextSize(25);
        dropdownTitle.setTypeface(Typeface.DEFAULT_BOLD);

        //Dropdown arrow
        img.setBackgroundResource(R.drawable.ic_baseline_arrow_down_24);
        img.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        img.setTextAlignment(View.TEXT_ALIGNMENT_TEXT_END);
        img.setId(View.generateViewId());

        //Creating a constraint layout
        ConstraintLayout cl = new ConstraintLayout(getActivity());
        cl.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        cl.setId(View.generateViewId());

        //Adding the dropdown title and the arrow to the constraint layout
        cl.addView(dropdownTitle);
        cl.addView(img);

        //Setting constraints for the dropdown tittle
        ConstraintSet constraintSetTitle = new ConstraintSet();
        constraintSetTitle.clone(cl);
        constraintSetTitle.connect(dropdownTitle.getId(), ConstraintSet.LEFT, cl.getId(), ConstraintSet.LEFT, 10);
        constraintSetTitle.connect(dropdownTitle.getId(), ConstraintSet.RIGHT, cl.getId(), ConstraintSet.RIGHT, 85);
        constraintSetTitle.setHorizontalBias(dropdownTitle.getId(), 0);
        constraintSetTitle.applyTo(cl);

        //Setting constraints for the arrow
        ConstraintSet constraintSetImg = new ConstraintSet();
        constraintSetImg.clone(cl);
        constraintSetImg.connect(img.getId(), ConstraintSet.RIGHT, cl.getId(), ConstraintSet.RIGHT, 40);
        constraintSetImg.connect(img.getId(), ConstraintSet.TOP, cl.getId(), ConstraintSet.TOP, 25);
        constraintSetImg.setHorizontalBias(img.getId(), 100);
        constraintSetImg.applyTo(cl);

        //Setting the recyclerview
        rv.setId(View.generateViewId());
        RecyclerView.LayoutParams rvlp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        rv.setLayoutParams(rvlp);
        ViewGroup.MarginLayoutParams vMLP = (ViewGroup.MarginLayoutParams) rv.getLayoutParams();
        vMLP.setMargins(5, 0, 5, 10);
        rv.setHorizontalScrollBarEnabled(true);
        rv.setVisibility(View.GONE);

        //Creating the add button
        addBtn.setId(View.generateViewId());
        addBtn.setGravity(Gravity.CENTER | Gravity.CENTER_HORIZONTAL);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(445, 20, 445, 20);
        addBtn.setLayoutParams(params);
        addBtn.setBackground(getActivity().getDrawable(R.drawable.design_add_button));
        addBtn.setText("+");
        addBtn.setTextSize(16);
        addBtn.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        addBtn.setTextColor(getActivity().getColor(R.color.white));
        addBtn.setVisibility(View.GONE);

        //Adding the different layouts to each other
        llv.addView(cl);
        llv.addView(rv);
        if (!objectTag.equalsIgnoreCase("assignment")) {
            llv.addView(addBtn);
        }
        cv.addView(llv);
        parentLLH.addView(cv);

        //Checking the param tag
        int recyclerViewOrientation = 0;
        if (orientation.equalsIgnoreCase("vertical"))
            recyclerViewOrientation = LinearLayoutManager.VERTICAL;
        else if (orientation.equalsIgnoreCase("horizontal"))
            recyclerViewOrientation = LinearLayoutManager.HORIZONTAL;

        //Setting up the adapter
        if (objectTag.equalsIgnoreCase("Assignment"))
            setAdapter(rv, (ArrayList<Object>) dataList, recyclerViewOrientation, objectTag);
        else
            setAdapter(rv, addBtn, (ArrayList<Object>) dataList, recyclerViewOrientation, objectTag);
    }

    public void setAdapter(RecyclerView rv, Button addBtn, ArrayList<Object> dataList, int orientation, String objectTag) {
        AdapterFactory adapterFactory = new AdapterFactory();
        adapterFactory.setAdapterType(objectTag, dataList, getContext());
        Object adapter = adapterFactory.setAdapterType(objectTag, dataList, getContext());

        rv.setLayoutManager(new LinearLayoutManager(getActivity(), orientation, false));
        rv.setAdapter((RecyclerView.Adapter) adapter);

        addBtn.setOnClickListener(view -> {
            if (objectTag.equals("question")) {
                addQuestion(dataList, rv, adapter);
            } else {
                Object addNewObject = adapterFactory.setObjectType(objectTag);
                dataList.add(addNewObject);
                rv.smoothScrollToPosition(dataList.size());
                ((RecyclerView.Adapter<?>) adapter).notifyItemInserted(dataList.size());
            }
        });
    }

    public void setAdapter(RecyclerView rv, ArrayList<Object> dataList, int orientation, String objectTag) {
        AdapterFactory adapterFactory = new AdapterFactory();
        adapterFactory.setAdapterType(objectTag, dataList, getContext());
        Object adapter = adapterFactory.setAdapterType(objectTag, dataList, getContext());
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), orientation, false));
        rv.setAdapter((RecyclerView.Adapter) adapter);
    }

    public void addQuestion(ArrayList<Object> dataList, RecyclerView rv, Object adapter) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.popup_add_question);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextInputLayout inputField = dialog.getWindow().findViewById(R.id.question_et);
        dialog.getWindow().findViewById(R.id.add_question_text_btn).setOnClickListener(view -> {
            Questions q = new Questions(inputField.getEditText().getText().toString(), -1, "");
            if (!q.getQuestion().isEmpty()) {
                dataList.add(q);
                rv.setAdapter((RecyclerView.Adapter) adapter);
                rv.smoothScrollToPosition(dataList.size() - 1);
                ((RecyclerView.Adapter<?>) adapter).notifyItemInserted(dataList.size() - 1);
                dialog.dismiss();
            } else
                gUC.toastAlert(getActivity(), getString(R.string.adding_new_question_failed));
        });
        dialog.getWindow().findViewById(R.id.cancel_btn).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}