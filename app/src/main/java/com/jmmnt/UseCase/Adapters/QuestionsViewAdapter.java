package com.jmmnt.UseCase.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.jmmnt.Entities.Questions;
import com.jmmnt.R;
import java.util.ArrayList;

public class QuestionsViewAdapter extends RecyclerView.Adapter<QuestionsViewHolder> {

    private ArrayList<Questions> items;
    private TextView question;
    private CheckBox checkBoxYes, checkBoxNo, checkBoxNotRelevant;

    public QuestionsViewAdapter(ArrayList<Questions> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_questions, parent, false);
        return new QuestionsViewHolder(view).linkAdapter(this);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsViewHolder holder, int position) {
        question = holder.itemView.findViewById(R.id.cutomerName);
        checkBoxYes = holder.itemView.findViewById(R.id.yesCheckBox);
        checkBoxNo = holder.itemView.findViewById(R.id.noCheckBox);
        checkBoxNotRelevant = holder.itemView.findViewById(R.id.notReleventCheckBox);
        if (position < items.size()) {
            if (items.get(position) != null) {
                question.setText(items.get(position).getQuestion());
                if (items.get(position).getAnswer() == 1) checkBoxYes.setChecked(true);
                if (items.get(position).getAnswer() == 2) checkBoxNo.setChecked(true);
                if (items.get(position).getAnswer() == 3) checkBoxNotRelevant.setChecked(true);
            }
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public ArrayList<Questions> getItems() {
        return items;
    }

}

class QuestionsViewHolder extends RecyclerView.ViewHolder {

    private CheckBox checkBoxYes, checkBoxNo, checkBoxNotRelevant;
    private QuestionsViewAdapter adapter;

    public QuestionsViewHolder(@NonNull View itemView) {
        super(itemView);

        checkBoxYes = itemView.findViewById(R.id.yesCheckBox);
        checkBoxNo = itemView.findViewById(R.id.noCheckBox);
        checkBoxNotRelevant = itemView.findViewById(R.id.notReleventCheckBox);
        checkBoxYes.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                checkBoxNo.setChecked(false);
                checkBoxNotRelevant.setChecked(false);
                adapter.getItems().get(getAdapterPosition()).setAnswer(1);
            }
            areCheckBoxesUnchecked();
        });
        checkBoxNo.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                checkBoxYes.setChecked(false);
                checkBoxNotRelevant.setChecked(false);
                adapter.getItems().get(getAdapterPosition()).setAnswer(2);
            }
            areCheckBoxesUnchecked();
        });
        checkBoxNotRelevant.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (isChecked) {
                checkBoxYes.setChecked(false);
                checkBoxNo.setChecked(false);
                adapter.getItems().get(getAdapterPosition()).setAnswer(3);
            }
            areCheckBoxesUnchecked();
        });

    }

    private void areCheckBoxesUnchecked(){
        if(!checkBoxNo.isChecked() && !checkBoxYes.isChecked() && !checkBoxNotRelevant.isChecked()){
            adapter.getItems().get(getAdapterPosition()).setAnswer(-1);
        }
    }

    public QuestionsViewHolder linkAdapter(QuestionsViewAdapter questionsViewAdapter){
        this.adapter = questionsViewAdapter;
        return this;
    }
}