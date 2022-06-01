package com.jmmnt.Controller.UI;

import android.os.Bundle;
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
import java.util.ArrayList;
import java.util.List;

public class FragmentSearchCase extends Fragment {

    private FragmentAdminSearchCaseBinding binding;
    private RecyclerView recyclerView;
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
