package com.jmmnt.UseCase.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jmmnt.Entities.ShortCircuitCurrentAndVoltageDrop;
import com.jmmnt.R;

import java.util.List;

public class ShortCircuitCurrentAndVoltageDropViewAdapter extends RecyclerView.Adapter<ShortCircuitAndVoltageDropViewHolder> {

    private List<ShortCircuitCurrentAndVoltageDrop> items;
    private EditText shortCircuitGroupName, lk, ShortCircuitMeasuredOnLocation, voltageDropGroupName, deltaVoltage,
            voltageDropMeasuredOnLocation;

    public ShortCircuitCurrentAndVoltageDropViewAdapter(List<ShortCircuitCurrentAndVoltageDrop> items) {
        this.items = items;
    }

    public ShortCircuitCurrentAndVoltageDropViewAdapter() {

    }

    @NonNull
    @Override
    public ShortCircuitAndVoltageDropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_short_circuit_current_and_voltage_drop, parent, false);
        return new ShortCircuitAndVoltageDropViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortCircuitAndVoltageDropViewHolder holder, int position) {
        shortCircuitGroupName = holder.itemView.findViewById(R.id.rcdGroupName_et);
        lk = holder.itemView.findViewById(R.id.shortCircuitlk_et);
        ShortCircuitMeasuredOnLocation = holder.itemView.findViewById(R.id.shortCircuitMeasuredOnLocation_et);
        voltageDropGroupName = holder.itemView.findViewById(R.id.voltageDropGroupName_et);
        deltaVoltage = holder.itemView.findViewById(R.id.voltageDropDelta_et);
        voltageDropMeasuredOnLocation = holder.itemView.findViewById(R.id.voltageDropMeasuredOnLocation_et);

        if (position < items.size()) {
            shortCircuitGroupName.setText(items.get(position).getShortCircuitGroupName());
            lk.setText(items.get(position).getShortCircuitLk());
            ShortCircuitMeasuredOnLocation.setText(items.get(position).getShortCircuitMeasuredOnLocation());
            voltageDropGroupName.setText(items.get(position).getVoltageDropGroupName());
            deltaVoltage.setText(items.get(position).getVoltageDropDeltaVoltage());
            voltageDropMeasuredOnLocation.setText(items.get(position).getVoltageDropMeasuredOnLocation());
        }

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public List<ShortCircuitCurrentAndVoltageDrop> getItems() {
        return items;
    }
}


class ShortCircuitAndVoltageDropViewHolder extends RecyclerView.ViewHolder {

    private Button deleteBtn;
    private ShortCircuitCurrentAndVoltageDropViewAdapter adapter;

    public ShortCircuitAndVoltageDropViewHolder(@NonNull View itemView) {
        super(itemView);
        deleteBtn = itemView.findViewById(R.id.rdcDeleteBtn);
        deleteBtn.setOnLongClickListener(view -> {
            adapter.getItems().remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());
            return true;
        });

        //TODO Vi kan aflæse felter herfra

    }

    public ShortCircuitAndVoltageDropViewHolder linkAdapter(ShortCircuitCurrentAndVoltageDropViewAdapter shortCircuitAndVoltageDropViewAdapter){
        this.adapter = shortCircuitAndVoltageDropViewAdapter;
        return this;
    }


}
