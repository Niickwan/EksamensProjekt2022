package com.jmmnt.UseCase.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.jmmnt.Entities.User;
import com.jmmnt.Entities.UserContainer;
import java.util.List;

public class SpinnerAdapter extends ArrayAdapter {

    private Context context;
    private List<User> container;

    public SpinnerAdapter(Context context, int textViewResourceId, List<User> container) {
        super(context, textViewResourceId);
        this.container = container;
        this.context = context;
    }

    @Override
    public int getCount(){
        return UserContainer.getUsers().size();
    }

    @Override
    public User getItem(int position){
        return UserContainer.getUsers().get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }


    //This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        super.getView(position, convertView, parent);
        TextView outerLabel = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.topMargin = 5;
        params.bottomMargin = 5;
        outerLabel.setLayoutParams(params);
        outerLabel.setPadding(5,0,5,0);
        outerLabel.setTextSize(18);
        outerLabel.setTypeface(Typeface.DEFAULT_BOLD);
        outerLabel.setLayoutParams(params);
        outerLabel.setTextColor(Color.WHITE);
        boolean isPhoneNumberEmpty = isPhoneNumberEmpty(container, position);
        if (isPhoneNumberEmpty)
            outerLabel.setText(container.get(position).getFullName() + " "
                    + container.get(position).getPhonenumber());
        else
            outerLabel.setText(container.get(position).getFullName());
        return outerLabel;
    }

    //Here is when the "chooser" is popped up
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        super.getDropDownView(position, convertView, parent);
        TextView innerLabel = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        innerLabel.setLayoutParams(params);
        innerLabel.setPadding(5,15,5,15);
        innerLabel.setTextSize(20);
        innerLabel.setTypeface(Typeface.DEFAULT_BOLD);
        innerLabel.setLayoutParams(params);
        innerLabel.setTextColor(Color.WHITE);
        boolean isPhoneNumberEmpty = isPhoneNumberEmpty(container, position);
        if (isPhoneNumberEmpty)
            innerLabel.setText(container.get(position).getFullName() + " "
                    + container.get(position).getPhonenumber());
        else
            innerLabel.setText(container.get(position).getFullName());

        return innerLabel;
    }

    private boolean isPhoneNumberEmpty(List<User> container, int position){
        return container.get(position).getPhonenumber() != null;
    }
}
