package com.jmmnt.UseCase.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;
import com.jmmnt.Controller.UI.FragmentSearchCase;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.AssignmentContainer;
import com.jmmnt.R;
import com.jmmnt.UseCase.GeneralUseCase;
import java.util.ArrayList;

public class SearchCaseViewAdapter extends RecyclerView.Adapter<SearchCaseViewHolder> implements Filterable {

    private ArrayList<Assignment> itemList;
    private ArrayList<Assignment> itemsFilterable;
    private TextView customerName_tv, address_tv, orderNumber_tv, statusDate_tv, statusCase_tv;
    private Fragment fragment;
    private GeneralUseCase gUC = GeneralUseCase.getInstance();

    public SearchCaseViewAdapter(ArrayList<Assignment> items, FragmentSearchCase fragmentSearchCase) {
        this.fragment = fragmentSearchCase;
        this.itemList = items;
        itemsFilterable = new ArrayList<>(items);
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
        orderNumber_tv = holder.itemView.findViewById(R.id.orderNumber);
        statusDate_tv = holder.itemView.findViewById(R.id.statusDate_tv);
        statusCase_tv = holder.itemView.findViewById(R.id.caseStatus_tv);
        if (position < itemList.size()) {
            if (itemList.get(position) != null) {
                customerName_tv.setText(itemList.get(position).getCustomerName());
                address_tv.setText(itemList.get(position).getAddress() + " " + itemList.get(position).getPostalCode());
                orderNumber_tv.setText(itemList.get(position).getOrderNumber());
                statusDate_tv.setText(gUC.formatDate(itemList.get(position).getStatusDate()));
                statusCase_tv.setText(itemList.get(position).getStatus());
                if (itemList.get(position).getStatus().equalsIgnoreCase("active")) {
                    statusCase_tv.setTextColor(fragment.getActivity().getColor(R.color.darkgreen));
                    statusCase_tv.setText("Aktiv");
                } else if (itemList.get(position).getStatus().equalsIgnoreCase("waiting")) {
                    statusCase_tv.setTextColor(fragment.getActivity().getColor(R.color.darkred));
                    statusCase_tv.setText("Afventer");
                } else if (itemList.get(position).getStatus().equalsIgnoreCase("finished")) {
                    statusCase_tv.setTextColor(fragment.getActivity().getColor(R.color.black));
                    statusCase_tv.setText("Afsluttet");
                }

            }
        }
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public ArrayList<Assignment> getItemList() {
        return itemList;
    }

    public Fragment getFragment(){
        return this.fragment;
    }

    @Override
    public Filter getFilter() {
        return filter;
    }

    private Filter filter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            ArrayList<Assignment> filteredItemList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0) {
                filteredItemList.addAll(itemsFilterable);
            } else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (Assignment item : itemsFilterable) {
                    if (item.getCustomerName().toLowerCase().contains(filterPattern)
                            || item.getAddress().toLowerCase().contains(filterPattern)
                            || item.getPostalCode().toLowerCase().contains(filterPattern)
                            || item.getOrderNumber().toLowerCase().contains(filterPattern)
                            || item.getStatusDate().toString().toLowerCase().contains(filterPattern))
                        filteredItemList.add(item);
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredItemList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults results) {
            itemList.clear();
            itemList.addAll((ArrayList) results.values);
            notifyDataSetChanged();
        }
    };


}

class SearchCaseViewHolder extends RecyclerView.ViewHolder {

    private SearchCaseViewAdapter adapter;
    private LinearLayout openCaseBtn;

    public SearchCaseViewHolder(@NonNull View itemView) {
        super(itemView);

        openCaseBtn = itemView.findViewById(R.id.openCaseBtn);
        openCaseBtn.setOnClickListener(view -> {
            TextView t =view.findViewById(R.id.orderNumber);
            for (int i = 0; i < adapter.getItemList().size(); i++) {
                if (t.getText().toString().equals(adapter.getItemList().get(i).getOrderNumber())){
                    AssignmentContainer.getInstance().setCurrentAssignment(adapter.getItemList().get(i));
                }
            }
            NavHostFragment.findNavController(adapter.getFragment()).navigate(R.id.action_fragmentSearchCase_to_FragmentAdminChecklist);
        });
    }

    public SearchCaseViewHolder linkAdapter(SearchCaseViewAdapter searchCaseViewAdapter) {
        this.adapter = searchCaseViewAdapter;
        return this;
    }


}
