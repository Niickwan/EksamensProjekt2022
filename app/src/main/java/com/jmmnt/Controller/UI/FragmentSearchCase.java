package com.jmmnt.Controller.UI;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.AssignmentContainer;
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
    private RecyclerView recyclerView;
    private List<Assignment> assignments;
    private AssignmentContainer assignmentContainer = AssignmentContainer.getInstance();
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
        //TODO DETTE SKAL SÆTTES OP MED EN CONTAINER
        assignments = new ArrayList<>();
        assignments.add(new Assignment(123, "223AJ2K33L8", "HusCompagniet", "Havnegade 19", "4700", "Næstved", LocalDate.now(), "active"));
        assignments.add(new Assignment(412, "1651653", "Fakta", "Andersgade 21", "1111", "Næstved", LocalDate.now(), "active"));
        assignments.add(new Assignment(23, "6549584464", "Netto", "Femøvej 1", "2730", "Næstved", LocalDate.now(), "waiting"));
        assignments.add(new Assignment(1254, "641659684161", "Hardi", "Gaden 9", "4700", "Næstved", LocalDate.now(), "active"));
        assignments.add(new Assignment(5436, "65461949651", "SD", "Vejen 22", "4700", "Næstved", LocalDate.now(), "active"));
        assignments.add(new Assignment(75, "32132656", "AK", "Gladsaxevej 59", "4800", "Næstved", LocalDate.now(), "waiting"));
        assignments.add(new Assignment(6, "987941623", "Zealand", "Køgevej 34", "4700", "Næstved", LocalDate.now(), "finished"));
        assignments.add(new Assignment(86, "ASDWW65465", "HM", "Vejen 99", "4950", "Næstved", LocalDate.now(), "waiting"));
        assignments.add(new Assignment(97, "WW6546546", "Arne Pedersen", "Skyttemarksvej 99", "4200", "Næstved", LocalDate.now(), "finished"));
        assignments.add(new Assignment(679, "223AJ2K33L8", "AO Tæpper", "Havnegade 19", "2600", "Næstved", LocalDate.now(), "active"));
        assignments.add(new Assignment(7, "9875964S98654S", "Arkolit", "Krejbjergvej 8", "4700", "Næstved", LocalDate.now(), "finished"));
        assignments.add(new Assignment(67, "S132LKHS21", "Stark", "Aprilvej 2", "4900", "Næstved", LocalDate.now(), "finished"));
        assignments.add(new Assignment(45, "GEWG65465FSA", "LM", "Novembervej 76", "2900", "Næstved", LocalDate.now(), "active"));
        assignments.add(new Assignment(67, "123289DSA", "Mærsk", "Oktobervej 4", "4700", "Næstved", LocalDate.now(), "waiting"));
        assignments.add(new Assignment(123, "879845663", "Tiger", "Vildtbanevej 11", "4700", "Næstved", LocalDate.now(), "waiting"));
        assignments.add(new Assignment(65, "3214987913", "Nordea", "Svingkærvej 334", "4733", "Næstved", LocalDate.now(), "waiting"));
        assignments.add(new Assignment(88, "65149878452361", "Mercedes", "Gaderne 23", "4600", "Næstved", LocalDate.now(), "active"));
        assignments.add(new Assignment(5, "3218956", "SKAT", "Udevej 998", "5500", "Næstved", LocalDate.now(), "finished"));
        //TODO SLUT - TESTER HER --------------------------------------------------------------------

        binding.checkBoxSearchActiveCases.setChecked(true);
        binding.checkBoxSearchWaitingCases.setChecked(true);
        binding.checkBoxSearchUserCases.setChecked(true);

        //Sorting assignment by date and if checkbox is checked
        sortAssignments(assignmentContainer.getAssignments());

        recyclerView = getActivity().findViewById(R.id.caseListView);
        SearchCaseViewAdapter[] sva = {new SearchCaseViewAdapter(assignmentsSorted, this)};
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(sva[0]);
        sva[0].notifyItemInserted(assignmentsSorted.size() - 1);

        View.OnClickListener clicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assignmentsSorted = sortAssignments(assignmentContainer.getAssignments());
                sva[0] = new SearchCaseViewAdapter(assignmentsSorted, FragmentSearchCase.this);
                recyclerView.setAdapter(sva[0]);
                sva[0].notifyItemInserted(assignmentsSorted.size() - 1);
            }
        };

        binding.checkBoxSearchWaitingCases.setOnClickListener(clicked);
        binding.checkBoxSearchActiveCases.setOnClickListener(clicked);
        binding.checkBoxSearchFinishedCases.setOnClickListener(clicked);
        binding.checkBoxSearchUserCases.setOnClickListener(clicked);

        //This binding is for filtering the item list with cases when using the searchbar.
        binding.searchBarSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String searchText) {
                sva[0].getFilter().filter(searchText);
                return false;
            }
        });
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
