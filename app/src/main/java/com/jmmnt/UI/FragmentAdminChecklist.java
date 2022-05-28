package com.jmmnt.UI;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputLayout;
import com.jmmnt.R;
import com.jmmnt.UseCase.GeneralUseCase;
import com.jmmnt.UseCase.OperateAssignment;
import com.jmmnt.UseCase.OperateDB;
import com.jmmnt.databinding.FragmentAdminChecklistBinding;

import java.util.ArrayList;

public class FragmentAdminChecklist extends Fragment {

    private FragmentAdminChecklistBinding binding;
    private OperateAssignment opa = new OperateAssignment();
    private OperateDB oDB = OperateDB.getInstance();
    private GeneralUseCase gUC = GeneralUseCase.getInstance();

    private Button addFloorBtn;
    private LayerDrawable selectedFloor;
    private LayerDrawable unSelectedFloor;
    private ArrayList<String> floors = new ArrayList<>();
    private ArrayList<Button> floorButtons = new ArrayList<>();

    // TODO SKAL VÆRE VÆRDIER FRA SERVER/DB
    private String orderNr = "8888";
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
    addFloorBtn.setOnClickListener(v -> popupAddFloor(getActivity()));

    binding.hsvFloor.setHorizontalScrollBarEnabled(false);
    floorLinearLayout = new LinearLayout(getActivity());
    floorLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
    floorLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

    binding.hsvRoom.setHorizontalScrollBarEnabled(false);
    LinearLayout roomLinearLayout = new LinearLayout(getActivity());
    roomLinearLayout.setOrientation(LinearLayout.HORIZONTAL);

        Thread t = new Thread(() -> {
            ArrayList<String> hsvStructure = new ArrayList<>();
            try {
                hsvStructure = oDB.getAssignmentStructure(orderNr);
            } finally {
                floors = gUC.sortStringBeforeNumbers(gUC.getSplittedString(hsvStructure, orderNr, "/<"));
                selectedFloorName = floors.get(0);
                setHorizontalFloorBar(floors);

                getActivity().runOnUiThread(() -> {
                    binding.hsvFloor.addView(floorLinearLayout);
//                        binding.hsvRoom.addView(roomLinearLayout);
                    binding.hsvRoom.setVisibility(View.GONE);
                });
            }
        });
        t.start();

        ArrayList<LinearLayout> expandableLayout = new ArrayList<>();

        LinearLayoutCompat nsvLinearLayout = new LinearLayoutCompat(getActivity());
        nsvLinearLayout.setOrientation(LinearLayoutCompat.VERTICAL);

        LinearLayout linearLayoutDropdownHolder = new LinearLayout(getActivity());
        linearLayoutDropdownHolder.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayoutDropdownHolder.setOrientation(LinearLayout.VERTICAL);

        ArrayList<CardView> ra = opa.genereteCardviewArray(getActivity(),getResources(),"TjekListeNy.xls");
        for (int i = 0; i < ra.size(); i++) {
            linearLayoutDropdownHolder.addView(ra.get(i));
        }
//        linearLayoutDropdownHolder.addView(cardView);

        nsvLinearLayout.addView(linearLayoutDropdownHolder);


        binding.ChecklistNestedScrollView.addView(nsvLinearLayout);

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
                editOrDeleteRoom();
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

    public void popupAddFloor(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.popup_add_floor);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextInputLayout newFloorName = dialog.getWindow().findViewById(R.id.new_floor_et);
        dialog.getWindow().findViewById(R.id.create_new_floor_btn).setOnClickListener(v -> new Thread(() -> {
            opa.createFolderOnServer(orderNr, newFloorName.getEditText().getText().toString(), "");
            getActivity().runOnUiThread(() -> {
                floors.add(newFloorName.getEditText().getText().toString());
                selectedFloorName = newFloorName.getEditText().getText().toString();
                setHorizontalFloorBar(floors);
                dialog.dismiss();
            });
        }).start());
        dialog.getWindow().findViewById(R.id.cancel_new_floor_btn).setOnClickListener(v -> {
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
        borderSelected.setShape(GradientDrawable.RECTANGLE);
        LayerDrawable bSelected = new LayerDrawable(new Drawable[]{borderSelected});
//        bSelected.setLayerInset(0, 0, 7, 0, 5);
        return bSelected;
    }

    private LayerDrawable floorIsNotSelected() {
        GradientDrawable borderNotSelected = new GradientDrawable();
        borderNotSelected.setColor(getResources().getColor(R.color.purple_700, getActivity().getTheme()));
        borderNotSelected.setShape(GradientDrawable.RECTANGLE);
        borderNotSelected.setAlpha(220);
        LayerDrawable bNotSelected = new LayerDrawable(new Drawable[]{borderNotSelected});
//        bNotSelected.setLayerInset(0, 0, 0, 0, 0);
        return bNotSelected;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}