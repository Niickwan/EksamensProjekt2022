package com.jmmnt.UseCase.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.AssignmentContainer;
import com.jmmnt.Entities.User;
import com.jmmnt.Entities.UserContainer;
import com.jmmnt.R;

import java.util.ArrayList;
import java.util.List;

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    private List<Assignment> itemList;
    private TextView customerFullName_tv, address_tv, orderNumber_tv, identification_et, date_tv, installedBy_tv;
    private Spinner verifiedBy_spinner;
    private Context context;

    public OrderViewAdapter(List<Assignment> items, Context context) {
        this.itemList = items;
        this.context = context;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_assignment_info, parent, false);
        return new OrderViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        customerFullName_tv = holder.itemView.findViewById(R.id.customerName_tv);
        address_tv = holder.itemView.findViewById(R.id.address_et);
        orderNumber_tv = holder.itemView.findViewById(R.id.orderNumber_et);
        identification_et = holder.itemView.findViewById(R.id.identification_et);
        date_tv = holder.itemView.findViewById(R.id.date_tv);
        installedBy_tv = holder.itemView.findViewById(R.id.installedBy_tv);
        verifiedBy_spinner = holder.itemView.findViewById(R.id.verifiedBy_spinner);

        int lastIndex = AssignmentContainer.getInstance().getAssignments().size()-1;
        System.out.println("COTAINER "+ AssignmentContainer.getInstance().getAssignments().get(lastIndex));

//        if (position < itemList.size()) {
//            if (itemList.get(position) != null) {
//                customerFullName_tv.setText(itemList.get(position).getCustomerName());
//                address_tv.setText(itemList.get(position).getAddress());
//                orderNumber_tv.setText(itemList.get(position).getOrderNumber());
//
//
//            }
//        }

        SpinnerAdapter adapter = new SpinnerAdapter(context, android.R.layout.simple_spinner_item, UserContainer.getUsers());
        verifiedBy_spinner.setAdapter(adapter);
        verifiedBy_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                User verifiedBy = (User) adapterView.getAdapter().getItem(position);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });
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
