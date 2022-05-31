package com.jmmnt.UseCase.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.jmmnt.Controller.UI.FragmentSearchCase;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.R;
import com.jmmnt.UseCase.GeneralUseCase;
import com.jmmnt.UseCase.OperateAssignment;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class SearchCaseViewAdapter extends RecyclerView.Adapter<SearchCaseViewHolder> {

    private List<Assignment> items;
    private TextView customerName_tv, address_tv, orderNumber_tv, statusDate_tv, statusCase_tv;
    private Fragment fragment;
    private GeneralUseCase gUC = GeneralUseCase.getInstance();
    private OperateAssignment oA = OperateAssignment.getInstance();


    public SearchCaseViewAdapter(List<Assignment> items, FragmentSearchCase fragmentSearchCase) {
        this.fragment = fragmentSearchCase;
        this.items = items;
    }


    @NonNull
    @Override
    public SearchCaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_cases, parent, false);
        return new SearchCaseViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchCaseViewHolder holder, int position) {

        customerName_tv = holder.itemView.findViewById(R.id.customerName_tv);
        address_tv = holder.itemView.findViewById(R.id.caseAddress_tv);
        orderNumber_tv = holder.itemView.findViewById(R.id.orderNumber_tv);
        statusDate_tv = holder.itemView.findViewById(R.id.statusDate_tv);
        statusCase_tv = holder.itemView.findViewById(R.id.caseStatus_tv);



        if (position < items.size()) {
            if (items.get(position) != null) {
                customerName_tv.setText(items.get(position).getCustomerName());
                address_tv.setText(items.get(position).getAddress() + " " + items.get(position).getPostalCode());
                orderNumber_tv.setText(items.get(position).getOrderNumber());
                statusDate_tv.setText(gUC.formatDate(items.get(position).getStatusDate()));
                statusCase_tv.setText(items.get(position).getStatus());
                if (items.get(position).getStatus().equalsIgnoreCase("active")) {
                    statusCase_tv.setTextColor(fragment.getActivity().getColor(R.color.purple_500));
                    statusCase_tv.setText("Aktiv");
                } else if (items.get(position).getStatus().equalsIgnoreCase("waiting")) {
                    statusCase_tv.setTextColor(fragment.getActivity().getColor(R.color.teal_700));
                    statusCase_tv.setText("Afventer");
                } else if (items.get(position).getStatus().equalsIgnoreCase("finished")) {
                    statusCase_tv.setTextColor(fragment.getActivity().getColor(R.color.black));
                    statusCase_tv.setText("Afsluttet");
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<Assignment> getItems() {
        return items;
    }

    public Fragment getFragment(){
        return this.fragment;
    }
}

class SearchCaseViewHolder extends RecyclerView.ViewHolder {

    private SearchCaseViewAdapter adapter;
    private LinearLayout openCaseBtn;


    public SearchCaseViewHolder(@NonNull View itemView) {
        super(itemView);

        openCaseBtn = itemView.findViewById(R.id.openCaseBtn);
        openCaseBtn.setOnClickListener(view -> {
            NavHostFragment.findNavController(adapter.getFragment()).navigate(R.id.action_fragmentSearchCase_to_FragmentAdminChecklist);
        });

        //TODO Vi kan aflæse felter herfra

    }

    public SearchCaseViewHolder linkAdapter(SearchCaseViewAdapter searchCaseViewAdapter) {
        this.adapter = searchCaseViewAdapter;
        return this;
    }


}
