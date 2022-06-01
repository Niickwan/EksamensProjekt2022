package com.jmmnt.UseCase.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jmmnt.Entities.CircuitDetails;
import com.jmmnt.R;
import java.util.List;

public class CircuitDetailsViewAdapter extends RecyclerView.Adapter<CircuitDetailsViewHolder> {

    private List<CircuitDetails> items;
    private EditText groupName, ob, characteristic, crossSection, maxOB, omegaResist, megaOmegaResist;
    private CheckBox zs, ra;

    public CircuitDetailsViewAdapter(List<CircuitDetails> items) {
        this.items = items;
    }


    @NonNull
    @Override
    public CircuitDetailsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_circuit_details, parent, false);
        return new CircuitDetailsViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull CircuitDetailsViewHolder holder, int position) {
        groupName = holder.itemView.findViewById(R.id.rcdGroupName_et);
        ob = holder.itemView.findViewById(R.id.shortCircuitlk_et);
        characteristic = holder.itemView.findViewById(R.id.shortCircuitMeasuredOnLocation_et);
        crossSection = holder.itemView.findViewById(R.id.voltageDropGroupName_et);
        maxOB = holder.itemView.findViewById(R.id.voltageDropDelta_et);
        zs = holder.itemView.findViewById(R.id.checkBoxZs);
        ra = holder.itemView.findViewById(R.id.checkBoxRa);
        omegaResist = holder.itemView.findViewById(R.id.zsra_et);
        megaOmegaResist = holder.itemView.findViewById(R.id.voltageDropMeasuredOnLocation_et);

        if (position < items.size()) {
            if (items.get(position) != null) {
                groupName.setText(items.get(position).getGroupName());
                ob.setText(items.get(position).getOb());
                characteristic.setText(items.get(position).getCharacteristics());
                crossSection.setText(items.get(position).getCrossSection());
                maxOB.setText(items.get(position).getMaxOB());
                if (items.get(position).getCheckbox() == 1) zs.setChecked(true);
                if (items.get(position).getCheckbox() == 2) ra.setChecked(true);
                omegaResist.setText(items.get(position).getOmega());
                megaOmegaResist.setText(items.get(position).getMegaOmega());
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<CircuitDetails> getItems() {
        return items;
    }
}


class CircuitDetailsViewHolder extends RecyclerView.ViewHolder {

    private Button deleteBtn;
    private CheckBox checkBoxZs, checkboxRa;
    private CircuitDetailsViewAdapter adapter;

    public CircuitDetailsViewHolder(@NonNull View itemView) {
        super(itemView);

        deleteBtn = itemView.findViewById(R.id.rdcDeleteBtn);
        deleteBtn.setOnLongClickListener(view -> {
            checkboxRa.setChecked(false);
            checkBoxZs.setChecked(false);
            adapter.getItems().remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());
            return true;
        });
        checkBoxZs = itemView.findViewById(R.id.checkBoxZs);
        checkboxRa = itemView.findViewById(R.id.checkBoxRa);
        checkBoxZs.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) checkboxRa.setChecked(false);
        });
        checkboxRa.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) checkBoxZs.setChecked(false);
        });

        //TODO Vi kan aflæse felter herfra

    }

    public CircuitDetailsViewHolder linkAdapter(CircuitDetailsViewAdapter demoAdapter) {
        this.adapter = demoAdapter;
        return this;
    }


}