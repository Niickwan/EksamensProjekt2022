package com.jmmnt.Controller.UI;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import com.jmmnt.Controller.Database.DB_Con;
import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.Entities.User;
import com.jmmnt.R;
import com.jmmnt.databinding.FragmentAdminHomeBinding;
import java.sql.SQLException;

public class FragmentAdminHome extends Fragment {

    private FragmentAdminHomeBinding binding;
    private FragmentPopupMenu fpm = FragmentPopupMenu.getInstance();
    private User user = LoggedInUser.getInstance().getUser();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAdminHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.createNewAssignmentBtn.setOnClickListener(view1 -> new Thread(() -> {
            try {
                DB_Con.getInstance().fillUserContainer();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                getActivity().runOnUiThread(() -> NavHostFragment.findNavController(FragmentAdminHome.this).navigate(R.id.action_FragmentAdminHome_to_fragmentCreateOrder));
            }
        }).start());

        binding.searchBtn.setOnClickListener(view1 -> new Thread(() -> {
            try {
                DB_Con.getInstance().fillAssignmentContainer();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                getActivity().runOnUiThread(() -> NavHostFragment.findNavController(FragmentAdminHome.this).navigate(R.id.action_FragmentAdminHome_to_fragmentSearchCase));
            }

        }).start());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}