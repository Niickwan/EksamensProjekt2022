package com.jmmnt.Controller.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.R;
import com.jmmnt.UseCase.Adapters.SearchCaseViewAdapter;
import com.jmmnt.UseCase.GeneralUseCase;
import com.jmmnt.UseCase.OperateAssignment;
import com.jmmnt.databinding.FragmentAdminSearchCaseBinding;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class FragmentSearchCase extends Fragment {

    private FragmentAdminSearchCaseBinding binding;
    private RecyclerView caseListView;
    private List<Assignment> assignments;
    private GeneralUseCase gUC = GeneralUseCase.getInstance();
    private OperateAssignment operateAssignment = OperateAssignment.getInstance();
    private static List<Assignment> assignmentsSorted = new ArrayList<>();

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

        //TODO START - TESTER HER --------------------------------------------------------------------
        assignments = new ArrayList<>();
        assignments.add(new Assignment(123, "Havnegade 19", "4700", "active", "223AJ2K33L8", LocalDate.now(), "HusCompagniet", 12));
        assignments.add(new Assignment(111, "Femøvej 1", "4700", "active", "12312414", LocalDate.of(2022,7,23), "Brugsen", 2));
        assignments.add(new Assignment(222, "Andersgade 21", "4700", "finished", "6787976867", LocalDate.of(2022,5,22), "Fakta", 3));
        assignments.add(new Assignment(333, "Gaden 9", "4700", "active", "234232ASD123", LocalDate.of(2022,3,23), "Netto", 4));
        assignments.add(new Assignment(555, "Vejen 22", "4760", "active", "223AJ2K33L8", LocalDate.of(2022,7,26), "Hardi", 5));
        assignments.add(new Assignment(777, "Gladsaxevej 5", "4700", "waiting", "223AJ2K33L8", LocalDate.of(2022,8,3), "SD", 6));
        assignments.add(new Assignment(77, "Køgevej 34", "4700", "waiting", "223AJ2K33L8", LocalDate.of(2022,5,2), "AK", 7));
        assignments.add(new Assignment(54, "Vejen 99", "4700", "finished", "223AJ2K33L8", LocalDate.of(2022,4,11), "Zealand", 7));
        assignments.add(new Assignment(32, "Skyttemarksvej 99", "4700", "waiting", "223AJ2K33L8", LocalDate.of(2022,5,9), "HusCompagniet", 7));
        assignments.add(new Assignment(553, "Havnegade 19", "4700", "active", "223AJ2K33L8", LocalDate.of(2022,7,21), "HusCompagniet", 8));
        assignments.add(new Assignment(211, "Havnegade 19", "4700", "waiting", "223AJ2K33L8", LocalDate.of(2022,7,27), "HusCompagniet", 9));
        assignments.add(new Assignment(2, "Havnegade 19", "4700", "active", "223AJ2K33L8", LocalDate.of(2022,7,15), "HusCompagniet", 99));
        assignments.add(new Assignment(45, "Havnegade 19", "4700", "active", "223AJ2K33L8", LocalDate.of(2022,7,4), "HusCompagniet", 26));
        assignments.add(new Assignment(87, "Havnegade 19", "4700", "finished", "223AJ2K33L8", LocalDate.of(2022,6,7), "HusCompagniet", 26));
        assignments.add(new Assignment(561, "Havnegade 19", "4700", "active", "223AJ2K33L8", LocalDate.of(2022,7,17), "HusCompagniet", 26));
        //TODO SLUT - TESTER HER --------------------------------------------------------------------

        binding.checkBoxSearchActiveCases.setChecked(true);
        binding.checkBoxSearchWaitingCases.setChecked(true);
        binding.checkBoxSearchUserCases.setChecked(true);

        //Sorting assignment by date and if checkbox is checked
        sortAssignments(assignments);

        caseListView = getActivity().findViewById(R.id.caseListView);
        SearchCaseViewAdapter[] sva = {new SearchCaseViewAdapter(assignmentsSorted, this)};
        caseListView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        caseListView.setAdapter(sva[0]);
        sva[0].notifyItemInserted(assignmentsSorted.size() - 1);




        View.OnClickListener clicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignmentsSorted = sortAssignments(assignments);
                sva[0] = new SearchCaseViewAdapter(assignmentsSorted, FragmentSearchCase.this);
                caseListView.setAdapter(sva[0]);
                sva[0].notifyItemInserted(assignmentsSorted.size() - 1);
            }
        };

        binding.checkBoxSearchWaitingCases.setOnClickListener(clicked);
        binding.checkBoxSearchActiveCases.setOnClickListener(clicked);
        binding.checkBoxSearchFinishedCases.setOnClickListener(clicked);
        binding.checkBoxSearchUserCases.setOnClickListener(clicked);

    }

    private List sortAssignments(List<Assignment> assignments) {
        gUC.clearList(assignmentsSorted);
        boolean activeCase = binding.checkBoxSearchActiveCases.isChecked();
        boolean waitingCase = binding.checkBoxSearchWaitingCases.isChecked();
        boolean finishedCase = binding.checkBoxSearchFinishedCases.isChecked();
        boolean userCase = binding.checkBoxSearchUserCases.isChecked();

        assignmentsSorted = operateAssignment.sortAssignmentsByCheckboxIsChecked(assignments, activeCase, waitingCase, finishedCase, userCase);
        assignmentsSorted = operateAssignment.bubbleSortAssignmentsByDate(assignmentsSorted);

        return assignmentsSorted;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
