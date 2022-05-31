package com.jmmnt.UseCase.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.UserContainer;
import com.jmmnt.R;

import java.util.ArrayList;
import java.util.List;

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    private List<Assignment> itemList;
    private TextView customerName_tv, address_tv, orderNumber_tv, identification_tv, date_tv;
    private Spinner installedBy_spinner, verifiedBy_spinner;


    public OrderViewAdapter(List<Assignment> items) {
        this.itemList = items;
    }


    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assignment_info, parent, false);
        return new OrderViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        customerName_tv = holder.itemView.findViewById(R.id.customerName_tv);
        address_tv = holder.itemView.findViewById(R.id.address_et);
        orderNumber_tv = holder.itemView.findViewById(R.id.orderNumber_et);
        identification_tv = holder.itemView.findViewById(R.id.identification_tv);
        date_tv = holder.itemView.findViewById(R.id.date_tv);
        installedBy_spinner = holder.itemView.findViewById(R.id.installedBy_spinner);
        verifiedBy_spinner = holder.itemView.findViewById(R.id.verifiedBy_spinner);

        if (position < itemList.size()) {
            if (itemList.get(position) != null) {
                customerName_tv.setText(itemList.get(position).getCustomerName());
                address_tv.setText(itemList.get(position).getAddress());
                orderNumber_tv.setText(itemList.get(position).getOrderNumber());
                identification_tv.setText(itemList.get(position).getIdentificationOfInstallation());
                //installedBy_spinner.set
                //TODO ARBEJD VIDERE HER
            }
        }

        /*List<String> spinnerArray = new ArrayList<>();
        spinnerArray.add(); //Default value //TODO kan den bruges til som afventer value?
        for (int i = 1; i < UserContainer.getUsers().size(); i++) {
            if (UserContainer.getUsers().get(i).getPhonenumber() != null)
                spinnerArray.add(UserContainer.getUsers().get(i).getFullName() + " " + UserContainer.getUsers().get(i).getPhonenumber());
            else
                spinnerArray.add(UserContainer.getUsers().get(i).getFullName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, spinnerArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = binding.chooseUserSpinner;
        spinner.setAdapter(adapter);*/
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public List<Assignment> getItemList() {
        return itemList;
    }

}

class OrderViewHolder extends RecyclerView.ViewHolder {

    private OrderViewAdapter adapter;



    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);



        //TODO Vi kan aflæse felter herfra

    }

    public OrderViewHolder linkAdapter(OrderViewAdapter orderViewAdapter) {
        this.adapter = orderViewAdapter;
        return this;
    }


}
