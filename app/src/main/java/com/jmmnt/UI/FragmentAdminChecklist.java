package com.jmmnt.UI;

import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.jmmnt.R;
import com.jmmnt.UseCase.OperateAssignment;
import com.jmmnt.databinding.FragmentAdminChecklistBinding;

import org.apache.poi.ss.formula.functions.T;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class FragmentAdminChecklist extends Fragment {

    private FragmentAdminChecklistBinding binding;
    private OperateAssignment opa = new OperateAssignment();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminChecklistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//---------------------------------------------------------------------------------------------------------
//        /**
//         * returner liste af cardviews som indeholder alle questions under én headline.
//         * parametre: Activity activtity, Resources resources
//         */
//        int headlineFirst = 0;
//        int headlineSecond = 0;
//        List<String> excelCardView;
//        ArrayList<CardView> headlineCardViews = new ArrayList<>();
//        ArrayList<String> excelChecklist = opa.getExcelAsArrayList("TjekListeNy.xls");
//        for (int i = 0; i < excelChecklist.size(); i++) {
//            if (excelChecklist.get(i).equals("<Headline>")) {
//                headlineSecond = i;
//                if (headlineSecond > headlineFirst) {
//                    excelCardView = excelChecklist.subList(headlineFirst + 1, headlineSecond - 1);
//                    headlineCardViews.add(genereteCardview(excelCardView, activity));
//                    headlineFirst = headlineSecond;
//                }
//            }
//        }
//        /**
//         * returner et cardview (generateCardView)
//         * parametre
//         */
//        public CardView genereteCardView(List<String> excelCardView, Activity activity){
//            CardView cardView = new CardView(activity);
//            LinearLayout cardviewLinearLayout = new LinearLayout(activity);
//            LinearLayout linearLayoutHeadlineCardview = new LinearLayout(activity);
//            LinearLayout linearLayoutBodyCardview = new LinearLayout(activity);
//
//            cardView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//            cardviewLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            cardviewLinearLayout.setOrientation(LinearLayout.VERTICAL);
//
//            linearLayoutHeadlineCardview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//
//            linearLayoutBodyCardview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//            linearLayoutBodyCardview.setTag(text);
//
//            linearLayoutHeadlineCardview.setOnClickListener();
//
//        }
//-----------------------------------------------------------------------------
        ArrayList<LinearLayout> expandableLayout = new ArrayList<>();

        LinearLayoutCompat nsvLinearLayout = new LinearLayoutCompat(getActivity());
        nsvLinearLayout.setOrientation(LinearLayoutCompat.VERTICAL);

        LinearLayout linearLayoutDropdownHolder = new LinearLayout(getActivity());
        linearLayoutDropdownHolder.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayoutDropdownHolder.setOrientation(LinearLayout.VERTICAL);

//        CardView cardView = new CardView(getActivity());
//        cardView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//        LinearLayout cardviewLinearLayout = new LinearLayout(getActivity());
//        cardviewLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        cardviewLinearLayout.setOrientation(LinearLayout.VERTICAL);

//        LinearLayout linearLayoutHeadlineCardview = new LinearLayout(getActivity());
//        linearLayoutHeadlineCardview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

//        String text = "1. Generelt";

//        TextView headline = new TextView(getActivity());
//        headline.setTextColor(getResources().getColor(R.color.black, getActivity().getTheme()));
//        headline.setText(text);
//        headline.setOnClickListener(v -> {
//            for (int i = 0; i < expandableLayout.size(); i++) {
//                if (expandableLayout.get(i).getTag().equals(headline.getText())) {
//                    if (expandableLayout.get(i).getVisibility() == view.GONE) {
//                        TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
//                        expandableLayout.get(i).setVisibility(View.VISIBLE);
//                    } else {
//                        TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
//                        expandableLayout.get(i).setVisibility(View.GONE);
//                    }
//                }
//            }
//        });
//        linearLayoutHeadlineCardview.addView(headline);
//
//        LinearLayout linearLayoutBodyCardview = new LinearLayout(getActivity());
//        linearLayoutBodyCardview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
//        linearLayoutBodyCardview.setTag(text);
//
//        TextView body = new TextView(getActivity());
//        body.setTextColor(getResources().getColor(R.color.black, getActivity().getTheme()));
//        body.setText("body");

//        linearLayoutBodyCardview.addView(body);
//
//        expandableLayout.add(linearLayoutBodyCardview);
//
//        //linearLayoutDropdownHolder.addView(tv, 0);
//        //linearLayoutDropdownHolder.addView(tv1, 1);
//
//        cardviewLinearLayout.addView(linearLayoutHeadlineCardview,0);
//        cardviewLinearLayout.addView(linearLayoutBodyCardview,1);
//        cardView.addView(cardviewLinearLayout);


        ArrayList<CardView> ra = opa.genereteCardviewArray(getActivity(),getResources(),"TjekListeNy.xls");
        for (int i = 0; i < ra.size(); i++) {
            linearLayoutDropdownHolder.addView(ra.get(i));
        }
//        linearLayoutDropdownHolder.addView(cardView);

        nsvLinearLayout.addView(linearLayoutDropdownHolder);


        binding.ChecklistNestedScrollView.addView(nsvLinearLayout);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}