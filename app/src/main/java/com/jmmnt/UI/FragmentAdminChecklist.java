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
import com.jmmnt.databinding.FragmentAdminChecklistBinding;

import org.apache.poi.ss.formula.functions.T;

import java.util.ArrayList;
import java.util.List;

public class FragmentAdminChecklist extends Fragment {

    private FragmentAdminChecklistBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminChecklistBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ArrayList<LinearLayout> expandableLayout = new ArrayList<>();

        LinearLayoutCompat nsvLinearLayout = new LinearLayoutCompat(getActivity());
        nsvLinearLayout.setOrientation(LinearLayoutCompat.VERTICAL);
        TextView tv = new TextView(getActivity());
        tv.setText("Hej med dig");
        tv.setTextSize(24);
        tv.setTextColor(getResources().getColor(R.color.black, getActivity().getTheme()));

        TextView tv1 = new TextView(getActivity());
        tv1.setText("Hej med dig");
        tv1.setTextSize(24);
        tv1.setTextColor(getResources().getColor(R.color.black, getActivity().getTheme()));

        LinearLayout linearLayoutDropdownHolder = new LinearLayout(getActivity());
        linearLayoutDropdownHolder.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayoutDropdownHolder.setOrientation(LinearLayout.VERTICAL);

        CardView cardView = new CardView(getActivity());
        cardView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout cardviewLinearLayout = new LinearLayout(getActivity());
        cardviewLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        cardviewLinearLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout linearLayoutHeadlineCardview = new LinearLayout(getActivity());
        linearLayoutHeadlineCardview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        String text = "1. Generelt";

        TextView headline = new TextView(getActivity());
        headline.setTextColor(getResources().getColor(R.color.black, getActivity().getTheme()));
        headline.setText(text);
        headline.setOnClickListener(v -> {
            for (int i = 0; i < expandableLayout.size(); i++) {
                if (expandableLayout.get(i).getTag().equals(headline.getText())) {
                    if (expandableLayout.get(i).getVisibility() == view.GONE) {
                        TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                        expandableLayout.get(i).setVisibility(View.VISIBLE);
                    } else {
                        TransitionManager.beginDelayedTransition(cardView,new AutoTransition());
                        expandableLayout.get(i).setVisibility(View.GONE);
                    }
                }
            }
        });
        linearLayoutHeadlineCardview.addView(headline);

        LinearLayout linearLayoutBodyCardview = new LinearLayout(getActivity());
        linearLayoutBodyCardview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayoutBodyCardview.setTag(text);

        TextView body = new TextView(getActivity());
        body.setTextColor(getResources().getColor(R.color.black, getActivity().getTheme()));
        body.setText("body\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\nFind mig");

        linearLayoutBodyCardview.addView(body);

        expandableLayout.add(linearLayoutBodyCardview);

        //linearLayoutDropdownHolder.addView(tv, 0);
        //linearLayoutDropdownHolder.addView(tv1, 1);

        cardviewLinearLayout.addView(linearLayoutHeadlineCardview,0);
        cardviewLinearLayout.addView(linearLayoutBodyCardview,1);
        cardView.addView(cardviewLinearLayout);

        linearLayoutDropdownHolder.addView(cardView);

        nsvLinearLayout.addView(linearLayoutDropdownHolder);


        binding.ChecklistNestedScrollView.addView(nsvLinearLayout);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}