package com.jmmnt.Controller.UI;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.text.InputType;
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
import com.itextpdf.layout.borders.Border;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.AssignmentContainer;
import com.jmmnt.Entities.CircuitDetails;
import com.jmmnt.Entities.Questions;
import com.jmmnt.Entities.ShortCircuitCurrentAndVoltageDrop;
import com.jmmnt.Entities.TestingRCD;
import com.jmmnt.R;
import com.jmmnt.UseCase.Adapters.AdapterFactory;
import com.jmmnt.UseCase.GeneralUseCase;
import com.jmmnt.UseCase.OperateAssignment;
import com.jmmnt.UseCase.OperateDB;
import com.jmmnt.databinding.FragmentAdminChecklistBinding;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class FragmentAdminChecklist extends Fragment {

    private FragmentAdminChecklistBinding binding;
    private OperateAssignment opa = OperateAssignment.getInstance();
    private OperateDB oDB = OperateDB.getInstance();
    private GeneralUseCase gUC = GeneralUseCase.getInstance();
    private AssignmentContainer assignmentContainer = AssignmentContainer.getInstance();
    private boolean isNewAssignment;

    private Button addFloorBtn;
    private LayerDrawable selectedFloor;
    private LayerDrawable unSelectedFloor;
    private ArrayList<String> floors = new ArrayList<>();
    private ArrayList<Button> floorButtons = new ArrayList<>();

    private List<Object> assignment;
    private List<Object> circuitDetailsResults;
    private List<Object> voltageDropResults;
    private List<Object> testingRCDResults;
    private LinearLayout parentLLH;

    // TODO SKAL VÆRE VÆRDIER FRA SERVER/DB
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

        setFloorHorizontalScrollBar();

        //TODO START - KUN FOR TESTING------------------------------------------------------------------------------------------------------------------------
        //TODO DATABASE KALD - hent data

        //List<Object> general = new ArrayList<>();
        //general.add(new Questions("Tavlen", 2, "Hvad sker der?"));
        //general.add(new Questions("Elskab", 1, "Kom nu"));

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

        circuitDetailsResults = new LinkedList<>();
        circuitDetailsResults.add(new CircuitDetails("Tavlen", "400", "Lampe", "6", "500", 1, "340", "20"));
        circuitDetailsResults.add(new CircuitDetails("Stik", "40", "kontakt", "3", "300", 2, "34", "2"));

        voltageDropResults = new LinkedList<>();
        voltageDropResults.add(new ShortCircuitCurrentAndVoltageDrop("Elskab", "lk", "Kælderen", "1.sal", "560", "Kælderen"));
        voltageDropResults.add(new ShortCircuitCurrentAndVoltageDrop("Fryser", "lk", "Stue", "4.sal", "60", "Stue"));

        testingRCDResults = new LinkedList<>();
        testingRCDResults.add(new TestingRCD("Test", 1, "res1", "res2", "res3", "res4", "res5", "res6"));
        testingRCDResults.add(new TestingRCD("Test", 1, "res1", "res2", "res3", "res4", "res5", "res6"));

        assignment = new ArrayList<>();
        assignment.add(new Assignment("123ASD123SD34", "Arne Pedersen", "Køgevej 2", "4700", "123984", "Lampe", 26, 26, LocalDate.now(), "active"));


        ArrayList<Assignment> assignmentContainer = AssignmentContainer.getInstance().getAssignments();

        if (isNewAssignment) {

        }

        List<String> template = opa.getExcelAsArrayList("TjeklisteTemplate.xls");


        //Dropdown titles
        String objectTag = "question";
        String objectTag2 = "circuitDetails";
        String objectTag3 = "RCD";
        String objectTag4 = "ShortCircuitCurrent";
        String objectTag5 = "Assignment";
        //TODO tags for adapterfactory

        //TODO SLUT - KUN FOR TESTING------------------------------------------------------------------------------------------------------------------------


        //Build UI Dynamically
        //Setting parent layout for the UI
        parentLLH = getActivity().findViewById(R.id.parentLLH);

        //Building dropdowns
        buildDropdownDynamically("Ordre", assignmentContainer, objectTag5, "vertical");

        int headlineCounter = 0;
        for (int i = 0; i < template.size(); i++) {
            if (template.get(i).equalsIgnoreCase("<Headline>")){
                if (headlineCounter == 0){
                    List<Object> general = new ArrayList<>();
                    i = readQuestionFromExcel(template, i, general);
                    buildDropdownDynamically(template.get(i+1), general, objectTag, "vertical");
                    headlineCounter++;
                }
                else if (headlineCounter == 1){
                    buildDropdownDynamically(template.get(i+1), electricalPanel, objectTag, "vertical");
                    headlineCounter++;
                }
                else if (headlineCounter == 2){
                    buildDropdownDynamically(template.get(i+1), installation, objectTag, "vertical");
                    headlineCounter++;
                }
                else if (headlineCounter == 3){
                    buildDropdownDynamically(template.get(i+1), protection, objectTag, "vertical");
                    headlineCounter++;
                }
                else if (headlineCounter == 4){
                    buildDropdownDynamically(template.get(i+1), error, objectTag, "vertical");
                    headlineCounter++;
                }
                else if (headlineCounter == 5){
                    buildDropdownDynamically(template.get(i+1), section, objectTag, "vertical");
                    headlineCounter++;
                }
            }
        }





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

        //Building dropdowns
        buildDropdownDynamically("Kredsdetaljer", circuitDetailsResults, objectTag2, "horizontal");
        buildDropdownDynamically("Afprøvning af RCD'er", testingRCDResults, objectTag3, "horizontal");

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

        //Adding edittext to parent
        parentLLH.addView(groundElectrodeResultEt);

        //Building dropdown
        buildDropdownDynamically("Kortslutningsstrøm / Spændingsfald", voltageDropResults, objectTag4, "horizontal");

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

        //Adding button for finishing the case
        Button finishCaseBtn = new Button(getActivity());
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

    private int readQuestionFromExcel(List<String> template, int i, List<Object> general) {
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
        Thread t = new Thread(() -> {
            ArrayList<String> hsvStructure = new ArrayList<>();
            try {
                hsvStructure = oDB.getAssignmentStructure(orderNr);
            } finally {
                floors = gUC.sortStringBeforeNumbers(gUC.getSplittedString(hsvStructure, orderNr, "/<"));
                selectedFloorName = floors.get(0);
//                setHorizontalFloorBar(floors);

                getActivity().runOnUiThread(() -> {
                    binding.hsvFloor.removeAllViews();
                    setHorizontalFloorBar(floors);
                    binding.hsvFloor.addView(floorLinearLayout);
//                        binding.hsvRoom.addView(roomLinearLayout);
                    binding.hsvRoom.setVisibility(View.GONE);
                });
            }
        });
        t.start();
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

    private void editOrDeleteRoom() {
        /**
         * GØR SÅ MAN KAN REDIGERE NAVNET PÅ ETAGEN ELLER SLETTE EN ETAGE
         */
    }

    // TODO Gøres til generelle metoder
    // TODO man får ikke fejl hvis man opretter flere af samme navn RET!
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
                    error.setText("Fejl prøv igen");
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
            // TODO Remove from server
            String deleteDirLocation = orderNr + "/" + selectedFloorName;
            opa.deleteDirectoryOnServer(deleteDirLocation);
            getActivity().runOnUiThread(() -> {
                // TODO remove from list and set new name instance variable
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
        //        bSelected.setLayerInset(0, 0, 7, 0, 5);
        return new LayerDrawable(new Drawable[]{borderSelected});
    }

    private LayerDrawable floorIsNotSelected() {
        GradientDrawable borderNotSelected = new GradientDrawable();
        borderNotSelected.setColor(getResources().getColor(R.color.purple_700, getActivity().getTheme()));
        borderNotSelected.setShape(GradientDrawable.RECTANGLE);
        borderNotSelected.setAlpha(220);
        //        bNotSelected.setLayerInset(0, 0, 0, 0, 0);
        return new LayerDrawable(new Drawable[]{borderNotSelected});
    }


    /**
     * @param title       - the name for the dropdown
     * @param dataList    - is the items for the dropdown
     * @param objectTag   - used for adapterFactory
     * @param orientation - used for defining the view orientation (vertical/horizontal)
     */
    public void buildDropdownDynamically(String title, List<?> dataList, String objectTag, String orientation) {
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
            setAdapter(rv, (List<Object>) dataList, recyclerViewOrientation, objectTag);
        else
            setAdapter(rv, addBtn, (List<Object>) dataList, recyclerViewOrientation, objectTag);
    }

    public void setAdapter(RecyclerView rv, Button addBtn, List<Object> dataList, int orientation, String objectTag) {
        AdapterFactory adapterFactory = new AdapterFactory();
        adapterFactory.setAdapterType(objectTag, dataList, getContext());
        Object adapter = adapterFactory.setAdapterType(objectTag, dataList, getContext());

        rv.setLayoutManager(new LinearLayoutManager(getActivity(), orientation, false));
        rv.setAdapter((RecyclerView.Adapter) adapter);

        Object addNewObject = adapterFactory.setObjectType(objectTag);
        addBtn.setOnClickListener(view -> {
            if (objectTag.equals("question")) {
                addQuestion(dataList, rv, adapter);
            } else {
                dataList.add(addNewObject);
                rv.smoothScrollToPosition(dataList.size() - 1);
                ((RecyclerView.Adapter<?>) adapter).notifyItemInserted(dataList.size() - 1);
            }
        });
    }

    public void setAdapter(RecyclerView rv, List<Object> dataList, int orientation, String objectTag) {
        AdapterFactory adapterFactory = new AdapterFactory();
        adapterFactory.setAdapterType(objectTag, dataList, getContext());
        Object adapter = adapterFactory.setAdapterType(objectTag, dataList, getContext());
        rv.setLayoutManager(new LinearLayoutManager(getActivity(), orientation, false));
        rv.setAdapter((RecyclerView.Adapter) adapter);
    }

    public void addQuestion(List<Object> dataList, RecyclerView rv, Object adapter) {
        Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.popup_add_question);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        TextInputLayout inputField = dialog.getWindow().findViewById(R.id.question_et);
        dialog.getWindow().findViewById(R.id.add_question_text_btn).setOnClickListener(view -> {
            Questions q = new Questions(inputField.getEditText().getText().toString(), -1, "");
            if (!q.getQuestion().isEmpty()) { //TODO må kun indeholde tal og bogstaver?
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