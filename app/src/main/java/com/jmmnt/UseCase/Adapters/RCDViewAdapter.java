package com.jmmnt.UseCase.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jmmnt.Entities.TestingRCD;
import com.jmmnt.R;
import java.util.List;

public class RCDViewAdapter extends RecyclerView.Adapter<RCDViewHolder> {

    private List<TestingRCD> items;
    private EditText groupName, firstResult, secondResult, thirdResult, fourthResult, fifthResult, sixthResult;
    private CheckBox checkBoxTestOK;

    public RCDViewAdapter(List<TestingRCD> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public RCDViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_rcd, parent, false);
        return new RCDViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull RCDViewHolder holder, int position) {
        groupName = holder.itemView.findViewById(R.id.rcdGroupName_et);
        firstResult = holder.itemView.findViewById(R.id.rcdFirstResult_et);
        secondResult = holder.itemView.findViewById(R.id.rcdSecondResult_et);
        thirdResult = holder.itemView.findViewById(R.id.rcdThirdResult_et);
        fourthResult = holder.itemView.findViewById(R.id.rcdFourthResult_et);
        checkBoxTestOK = holder.itemView.findViewById(R.id.checkBoxRCD);
        fifthResult = holder.itemView.findViewById(R.id.rcdFifthResult_et);
        sixthResult = holder.itemView.findViewById(R.id.rcdSixthResult_et);

        if (position < items.size()) {
            if (items.get(position) != null) {
                groupName.setText(items.get(position).getGroupName());
                firstResult.setText(items.get(position).getFirstResult());
                secondResult.setText(items.get(position).getSecondResult());
                thirdResult.setText(items.get(position).getThirdResult());
                fourthResult.setText(items.get(position).getFourthResult());
                if (items.get(position).getCheckboxOK() == 1) checkBoxTestOK.setChecked(true);
                fifthResult.setText(items.get(position).getFifthResult());
                sixthResult.setText(items.get(position).getSixthResult());
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<TestingRCD> getItems() {
        return items;
    }
}

class RCDViewHolder extends RecyclerView.ViewHolder {

    private Button deleteBtn;
    private CheckBox checkBoxTestOK;
    private RCDViewAdapter adapter;

    public RCDViewHolder(@NonNull View itemView) {
        super(itemView);
        checkBoxTestOK = itemView.findViewById(R.id.checkBoxRCD);
        deleteBtn = itemView.findViewById(R.id.rdcDeleteBtn);
        deleteBtn.setOnLongClickListener(view -> {
            adapter.getItems().remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());
            checkBoxTestOK.setChecked(false);
            return true;
        });

        //TODO Vi kan aflæse felter herfra

    }

    public RCDViewHolder linkAdapter(RCDViewAdapter rcdViewAdapter){
        this.adapter = rcdViewAdapter;
        return this;
    }

}
