package com.jmmnt.Controller.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.jmmnt.R;
import com.jmmnt.UseCase.Adapters.ListViewAdapter;
import com.jmmnt.databinding.FragmentAdminSearchCaseBinding;

import java.sql.SQLException;
import java.util.ArrayList;

public class FragmentSearchCase extends Fragment {

    private FragmentAdminSearchCaseBinding binding;
    private ListView simpleList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminSearchCaseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //TODO START - TESTER HER --------------------------------------------------------------------
        ArrayList<String> caseListTest = new ArrayList<>();
        caseListTest.add("Hejsa");
        caseListTest.add("Jeg vil hjem!!!");
        //TODO SLUT - TESTER HER --------------------------------------------------------------------

        simpleList = getActivity().findViewById(R.id.simpleListView);
        ListViewAdapter customAdapter = new ListViewAdapter(getContext(), caseListTest);
        simpleList.setAdapter(customAdapter);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
