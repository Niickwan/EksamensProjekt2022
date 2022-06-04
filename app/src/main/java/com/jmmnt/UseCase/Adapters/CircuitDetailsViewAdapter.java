package com.jmmnt.UseCase.Adapters;

import android.text.Editable;
import android.text.TextWatcher;
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
import com.jmmnt.UseCase.GeneralUseCase;

import java.util.List;

public class CircuitDetailsViewAdapter extends RecyclerView.Adapter<CircuitDetailsViewHolder> {

    private List<CircuitDetails> items;
    private EditText groupName, ob, characteristic, crossSection, maxOB, omegaResist, isolation;
    private CheckBox zs, ra;
    private GeneralUseCase gUC = GeneralUseCase.getInstance();

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
        groupName = holder.itemView.findViewById(R.id.circuitDetailsGroupName_et);
        ob = holder.itemView.findViewById(R.id.circuitDetailsOB_et);
        characteristic = holder.itemView.findViewById(R.id.circuitDetailsCharacteristic_et);
        crossSection = holder.itemView.findViewById(R.id.circuitDetailsCrossSection_et);
        maxOB = holder.itemView.findViewById(R.id.circuitDetailsMaxOB_et);
        zs = holder.itemView.findViewById(R.id.checkBoxZs);
        ra = holder.itemView.findViewById(R.id.checkBoxRa);
        omegaResist = holder.itemView.findViewById(R.id.zsra_et);
        isolation = holder.itemView.findViewById(R.id.circuitDetailsIsolation_et);

        if (position < items.size()) {
            if (items.get(position) != null) {
                groupName.setText(gUC.convertMinusOneToEmptyString(items.get(position).getGroupName()));
                ob.setText(gUC.convertMinusOneToEmptyString(items.get(position).getOb()));
                characteristic.setText(gUC.convertMinusOneToEmptyString(items.get(position).getCharacteristics()));
                crossSection.setText(gUC.convertMinusOneToEmptyString(items.get(position).getCrossSection()));
                maxOB.setText(gUC.convertMinusOneToEmptyString(items.get(position).getMaxOB()));
                if (items.get(position).getCheckbox() == 1) zs.setChecked(true);
                if (items.get(position).getCheckbox() == 2) ra.setChecked(true);
                omegaResist.setText(gUC.convertMinusOneToEmptyString(items.get(position).getOmega()));
                isolation.setText(gUC.convertMinusOneToEmptyString(items.get(position).getMegaOmega()));
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
        EditText groupName = itemView.findViewById(R.id.circuitDetailsGroupName_et);
        EditText ob = itemView.findViewById(R.id.circuitDetailsOB_et);
        EditText characteristic = itemView.findViewById(R.id.circuitDetailsCharacteristic_et);
        EditText crossSection = itemView.findViewById(R.id.circuitDetailsCrossSection_et);
        EditText maxOB = itemView.findViewById(R.id.circuitDetailsMaxOB_et);
        EditText omegaResist = itemView.findViewById(R.id.zsra_et);
        EditText megaOmegaResist = itemView.findViewById(R.id.circuitDetailsIsolation_et);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getItems().get(getAdapterPosition()).setGroupName(groupName.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setOb(ob.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setCharacteristics(characteristic.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setCrossSection(crossSection.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setMaxOB(maxOB.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setOmega(omegaResist.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setMegaOmega(megaOmegaResist.getEditableText().toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {
            }
        };

        groupName.addTextChangedListener(textWatcher);
        ob.addTextChangedListener(textWatcher);
        characteristic.addTextChangedListener(textWatcher);
        crossSection.addTextChangedListener(textWatcher);
        maxOB.addTextChangedListener(textWatcher);
        omegaResist.addTextChangedListener(textWatcher);
        megaOmegaResist.addTextChangedListener(textWatcher);

        deleteBtn = itemView.findViewById(R.id.circuitDetailsDeleteBtn);
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
            if (isChecked) {
                adapter.getItems().get(getAdapterPosition()).setCheckbox(1);
                checkboxRa.setChecked(false);
            }
        });
        checkboxRa.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                adapter.getItems().get(getAdapterPosition()).setCheckbox(2);
                checkBoxZs.setChecked(false);
            }
        });

        //TODO Vi kan aflæse felter herfra

    }

    public CircuitDetailsViewHolder linkAdapter(CircuitDetailsViewAdapter demoAdapter) {
        this.adapter = demoAdapter;
        return this;
    }


}