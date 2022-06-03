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
import com.jmmnt.Entities.TestingRCD;
import com.jmmnt.R;
import com.jmmnt.UseCase.GeneralUseCase;

import java.util.List;

public class RCDViewAdapter extends RecyclerView.Adapter<RCDViewHolder> {

    private List<TestingRCD> items;
    private EditText groupName, firstResult, secondResult, thirdResult, fourthResult, fifthResult, sixthResult;
    private CheckBox checkBoxTestOK;
    private GeneralUseCase gUC = GeneralUseCase.getInstance();

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
                groupName.setText(gUC.convertMinusOneToEmptyString(items.get(position).getGroupName()));
                firstResult.setText(gUC.convertMinusOneToEmptyString(items.get(position).getFirstResult()));
                secondResult.setText(gUC.convertMinusOneToEmptyString(items.get(position).getSecondResult()));
                thirdResult.setText(gUC.convertMinusOneToEmptyString(items.get(position).getThirdResult()));
                fourthResult.setText(gUC.convertMinusOneToEmptyString(items.get(position).getFourthResult()));
                if (items.get(position).getCheckboxOK() == 1) checkBoxTestOK.setChecked(true);
                fifthResult.setText(gUC.convertMinusOneToEmptyString(items.get(position).getFifthResult()));
                sixthResult.setText(gUC.convertMinusOneToEmptyString(items.get(position).getSixthResult()));
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

        EditText groupName = itemView.findViewById(R.id.rcdGroupName_et);
        EditText firstResult = itemView.findViewById(R.id.rcdFirstResult_et);
        EditText secondResult = itemView.findViewById(R.id.rcdSecondResult_et);
        EditText thirdResult = itemView.findViewById(R.id.rcdThirdResult_et);
        EditText fourthResult = itemView.findViewById(R.id.rcdFourthResult_et);
        EditText fifthResult = itemView.findViewById(R.id.rcdFifthResult_et);
        EditText sixthResult = itemView.findViewById(R.id.rcdSixthResult_et);

        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                adapter.getItems().get(getAdapterPosition()).setGroupName(groupName.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setFirstResult(firstResult.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setSecondResult(secondResult.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setThirdResult(thirdResult.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setFourthResult(fourthResult.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setFifthResult(fifthResult.getEditableText().toString());
                adapter.getItems().get(getAdapterPosition()).setSixthResult(sixthResult.getEditableText().toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };

        checkBoxTestOK.setOnClickListener(v -> {
            if (checkBoxTestOK.isChecked()) adapter.getItems().get(getAdapterPosition()).setCheckboxOK(1);
            else adapter.getItems().get(getAdapterPosition()).setCheckboxOK(-1);
        });

        groupName.addTextChangedListener(textWatcher);
        firstResult.addTextChangedListener(textWatcher);
        secondResult.addTextChangedListener(textWatcher);
        thirdResult.addTextChangedListener(textWatcher);
        fourthResult.addTextChangedListener(textWatcher);
        fifthResult.addTextChangedListener(textWatcher);
        sixthResult.addTextChangedListener(textWatcher);
    }

    public RCDViewHolder linkAdapter(RCDViewAdapter rcdViewAdapter){
        this.adapter = rcdViewAdapter;
        return this;
    }

}
