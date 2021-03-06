package com.jmmnt.UseCase.Adapters;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jmmnt.Entities.ShortCircuitCurrentAndVoltageDrop;
import com.jmmnt.R;
import com.jmmnt.UseCase.GeneralUseCase;
import java.util.ArrayList;

public class ShortCircuitCurrentAndVoltageDropViewAdapter extends RecyclerView.Adapter<ShortCircuitAndVoltageDropViewHolder> {

    private ArrayList<ShortCircuitCurrentAndVoltageDrop> items;
    private EditText shortCircuitGroupName, lk, ShortCircuitMeasuredOnLocation, voltageDropGroupName, deltaVoltage,
            voltageDropMeasuredOnLocation;
    private GeneralUseCase gUC = GeneralUseCase.getInstance();

    public ShortCircuitCurrentAndVoltageDropViewAdapter(ArrayList<ShortCircuitCurrentAndVoltageDrop> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ShortCircuitAndVoltageDropViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_short_circuit_current_and_voltage_drop, parent, false);
        return new ShortCircuitAndVoltageDropViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull ShortCircuitAndVoltageDropViewHolder holder, int position) {
        shortCircuitGroupName = holder.itemView.findViewById(R.id.shortCircuitCurrentGroupName_et);
        lk = holder.itemView.findViewById(R.id.shortCircuitCurrentlk_et);
        ShortCircuitMeasuredOnLocation = holder.itemView.findViewById(R.id.shortCircuitCurrentMeasuredOnLocation_et);
        voltageDropGroupName = holder.itemView.findViewById(R.id.voltageDropGroupName_et);
        deltaVoltage = holder.itemView.findViewById(R.id.voltageDropDelta_et);
        voltageDropMeasuredOnLocation = holder.itemView.findViewById(R.id.voltageDropMeasuredOnLocation_et);

        if (position < items.size()) {
            String shortCircuitGroupNameResultString =  items.get(position).getShortCircuitGroupName();
            String lkResultString = items.get(position).getShortCircuitLk();
            String ResultString = items.get(position).getShortCircuitMeasuredOnLocation();
            String shortCircuitMeasuredOnLocationResultString = items.get(position).getVoltageDropGroupName();
            String deltaVoltageResultString = items.get(position).getVoltageDropDeltaVoltage();
            String voltageDropMeasuredOnLocationResultString = items.get(position).getVoltageDropMeasuredOnLocation();
            shortCircuitGroupName.setText(gUC.convertMinusOneToEmptyString(shortCircuitGroupNameResultString));
            lk.setText(gUC.convertMinusOneToEmptyString(lkResultString));
            ShortCircuitMeasuredOnLocation.setText(gUC.convertMinusOneToEmptyString(ResultString));
            voltageDropGroupName.setText(gUC.convertMinusOneToEmptyString(shortCircuitMeasuredOnLocationResultString));
            deltaVoltage.setText(gUC.convertMinusOneToEmptyString(deltaVoltageResultString));
            voltageDropMeasuredOnLocation.setText(gUC.convertMinusOneToEmptyString(voltageDropMeasuredOnLocationResultString));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<ShortCircuitCurrentAndVoltageDrop> getItems() {
        return items;
    }


}

class ShortCircuitAndVoltageDropViewHolder extends RecyclerView.ViewHolder {

    private Button deleteBtn;
    private ShortCircuitCurrentAndVoltageDropViewAdapter adapter;

    public ShortCircuitAndVoltageDropViewHolder(@NonNull View itemView) {
        super(itemView);
        deleteBtn = itemView.findViewById(R.id.voltageDropDeleteBtn);
        deleteBtn.setOnLongClickListener(view -> {
            adapter.getItems().remove(getAdapterPosition());
            adapter.notifyItemRemoved(getAdapterPosition());
            return true;
        });

        EditText shortCircuitGroupName = itemView.findViewById(R.id.shortCircuitCurrentGroupName_et);
        EditText lk = itemView.findViewById(R.id.shortCircuitCurrentlk_et);
        EditText ShortCircuitMeasuredOnLocation = itemView.findViewById(R.id.shortCircuitCurrentMeasuredOnLocation_et);
        EditText voltageDropGroupName = itemView.findViewById(R.id.voltageDropGroupName_et);
        EditText deltaVoltage = itemView.findViewById(R.id.voltageDropDelta_et);
        EditText voltageDropMeasuredOnLocation = itemView.findViewById(R.id.voltageDropMeasuredOnLocation_et);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getItems().get(getAdapterPosition()).setShortCircuitGroupName(shortCircuitGroupName.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setShortCircuitLk(lk.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setShortCircuitMeasuredOnLocation(ShortCircuitMeasuredOnLocation.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setVoltageDropGroupName(voltageDropGroupName.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setVoltageDropDeltaVoltage(deltaVoltage.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setVoltageDropMeasuredOnLocation(voltageDropMeasuredOnLocation.getEditableText().toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
        shortCircuitGroupName.addTextChangedListener(textWatcher);
        lk.addTextChangedListener(textWatcher);
        ShortCircuitMeasuredOnLocation.addTextChangedListener(textWatcher);
        voltageDropGroupName.addTextChangedListener(textWatcher);
        deltaVoltage.addTextChangedListener(textWatcher);
        voltageDropMeasuredOnLocation.addTextChangedListener(textWatcher);
    }

    public ShortCircuitAndVoltageDropViewHolder linkAdapter(ShortCircuitCurrentAndVoltageDropViewAdapter shortCircuitAndVoltageDropViewAdapter){
        this.adapter = shortCircuitAndVoltageDropViewAdapter;
        return this;
    }


}
