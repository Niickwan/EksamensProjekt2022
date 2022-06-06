package com.jmmnt.Controller.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.AssignmentContainer;
import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.Entities.UserAssignmentContainer;
import com.jmmnt.R;
import com.jmmnt.UseCase.Adapters.OrderViewAdapter;
import com.jmmnt.UseCase.Adapters.SearchCaseViewAdapter;
import com.jmmnt.UseCase.GeneralUseCase;
import com.jmmnt.UseCase.OperateAssignment;
import com.jmmnt.UseCase.OperateDB;
import com.jmmnt.databinding.FragmentAdminSearchCaseBinding;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class FragmentSearchCase extends Fragment {

    private FragmentAdminSearchCaseBinding binding;
    private RecyclerView recyclerView;
    private AssignmentContainer assignmentContainer = AssignmentContainer.getInstance();
    private GeneralUseCase gUC = GeneralUseCase.getInstance();
    private OperateAssignment operateAssignment = OperateAssignment.getInstance();
    private static List<Assignment> assignmentsSorted = new ArrayList<>();
    private OperateDB oDB = OperateDB.getInstance();
    private OrderViewAdapter orderViewAdapter = new OrderViewAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminSearchCaseBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        binding.createNewAssignmentBtn.setOnClickListener(view1 -> new Thread(() -> {
            try {
                oDB.fillUserContainer();
            } finally {
                getActivity().runOnUiThread(() -> NavHostFragment.findNavController(this).navigate(R.id.action_fragmentSearchCase_to_fragmentCreateOrder));
            }
        }).start());

        binding.checkBoxSearchActiveCases.setChecked(true);
        binding.checkBoxSearchWaitingCases.setChecked(true);
        binding.checkBoxSearchUserCases.setChecked(true);

        assignmentsSorted = sortAssignments();

        recyclerView = binding.caseListView;
        SearchCaseViewAdapter sva = new SearchCaseViewAdapter(assignmentsSorted, FragmentSearchCase.this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(sva);

        View.OnClickListener clicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (assignmentsSorted != null) assignmentsSorted.clear();
                assignmentsSorted.addAll(sortAssignments());
                sva.notifyDataSetChanged();
            }
        };

        binding.checkBoxSearchWaitingCases.setOnClickListener(clicked);
        binding.checkBoxSearchActiveCases.setOnClickListener(clicked);
        binding.checkBoxSearchFinishedCases.setOnClickListener(clicked);
        binding.checkBoxSearchUserCases.setOnClickListener(clicked);

        //This binding is for filtering the item list with cases when using the search bar.
        binding.searchBarSv.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String searchText) {
                sva.getFilter().filter(searchText);
                return false;
            }
        });

        //This refreshes the two assignment containers when the listView is swiped from top towards the bottom
        SwipeRefreshLayout sFL = binding.swipeRefreshLayout.findViewById(R.id.swipeRefreshLayout);
        sFL.setOnRefreshListener(() -> {
            Thread t1 = new Thread(() -> {
                oDB.fillAssignmentContainer();
                oDB.fillUserAssignmentsIDs(LoggedInUser.getInstance().getUser().getUserID());
            });
            Thread t2 = new Thread(() -> {
                getActivity().runOnUiThread(() -> {
                    if (assignmentsSorted != null) assignmentsSorted.clear();
                    assignmentsSorted.addAll(sortAssignments());
                    sva.notifyDataSetChanged();
                });
                sFL.setRefreshing(false);
            });
            try {
                t1.start();
                t1.join();
                t2.start();
                t2.join();
            } catch (InterruptedException e) {
                e.printStackTrace();  //TODO Test om det virker rigtigt med mere data
            }
        });
    }

    private List sortAssignments() {
        List<Assignment> sorted;
        boolean activeCase = binding.checkBoxSearchActiveCases.isChecked();
        boolean waitingCase = binding.checkBoxSearchWaitingCases.isChecked();
        boolean finishedCase = binding.checkBoxSearchFinishedCases.isChecked();
        boolean userCase = binding.checkBoxSearchUserCases.isChecked();
        sorted = operateAssignment.sortAssignmentsByCheckboxIsChecked(assignmentContainer.getAssignments(),
                UserAssignmentContainer.getInstance().getUserAssignments(), activeCase, waitingCase, finishedCase, userCase);
        sorted = operateAssignment.bubbleSortAssignmentsByDate(sorted);
        return sorted;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}
