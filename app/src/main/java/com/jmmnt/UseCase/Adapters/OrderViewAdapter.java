package com.jmmnt.UseCase.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jmmnt.Entities.Assignment;
import com.jmmnt.Entities.User;
import com.jmmnt.Entities.UserContainer;
import com.jmmnt.R;
import com.jmmnt.UseCase.GeneralUseCase;
import java.util.ArrayList;

public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewHolder> {

    private ArrayList<Assignment> items;
    private TextView customerFullName_tv, address_tv, orderNumber_tv, identification_et, date_tv, installedBy_tv;
    private Spinner verifiedBy_spinner;
    private Context context;
    private GeneralUseCase gUC = GeneralUseCase.getInstance();

    public OrderViewAdapter(ArrayList<Assignment> items, Context context) {
        this.items = items;
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
        customerFullName_tv = holder.itemView.findViewById(R.id.customerFullName_tv);
        address_tv = holder.itemView.findViewById(R.id.address_tv);
        orderNumber_tv = holder.itemView.findViewById(R.id.orderNumber_tv);
        identification_et = holder.itemView.findViewById(R.id.identification_et);
        date_tv = holder.itemView.findViewById(R.id.date_tv);
        installedBy_tv = holder.itemView.findViewById(R.id.installedBy_tv);
        verifiedBy_spinner = holder.itemView.findViewById(R.id.verifiedBy_spinner);

        if (position < items.size()) {
            customerFullName_tv.setText(gUC.convertMinusOneToEmptyString(items.get(position).getCustomerName()));
            address_tv.setText(gUC.convertMinusOneToEmptyString(items.get(position).getAddress()));
            orderNumber_tv.setText(gUC.convertMinusOneToEmptyString(items.get(position).getOrderNumber()));
            for (int i = 0; i < UserContainer.getUsers().size(); i++) {
                if (UserContainer.getUsers().get(i).getUserID() == items.get(position).getInstalledBy()){
                    String installedBy = UserContainer.getUsers().get(i).getFullName();
                    installedBy_tv.setText(gUC.convertMinusOneToEmptyString(installedBy));
                }
            }
            String statusDate = gUC.formatDate(items.get(position).getStatusDate());
            date_tv.setText(gUC.convertMinusOneToEmptyString(statusDate));
        }

        SpinnerAdapter adapter = new SpinnerAdapter(context, android.R.layout.simple_spinner_item, UserContainer.getUsers());
        verifiedBy_spinner.setAdapter(adapter);
        verifiedBy_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                User verifiedBy = (User) adapterView.getAdapter().getItem(pos);
                items.get(holder.getAdapterPosition()).setVerifiedBy(verifiedBy.getUserID());
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapter) {  }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

}

class OrderViewHolder extends RecyclerView.ViewHolder {

    private OrderViewAdapter adapter;

    public OrderViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    public OrderViewHolder linkAdapter(OrderViewAdapter orderViewAdapter) {
        this.adapter = orderViewAdapter;
        return this;
    }


}
