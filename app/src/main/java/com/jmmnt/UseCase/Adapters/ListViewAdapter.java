package com.jmmnt.UseCase.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.jmmnt.Controller.UI.FragmentSearchCase;
import com.jmmnt.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {

    private ArrayList<String> caseList;
    private LayoutInflater inflater;
    private TextView customerName, caseAddress, orderNumber, statusDate, statusCase;
    private LinearLayout caseBtn;

    public ListViewAdapter(Context applicationContext, ArrayList<String> questionsList) {
        this.caseList = questionsList;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return caseList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.item_cases, null);

        customerName = view.findViewById(R.id.customerName_tv);
        caseAddress = view.findViewById(R.id.caseAddress_tv);
        orderNumber = view.findViewById(R.id.orderNumber_tv);
        statusDate = view.findViewById(R.id.statusDate_tv);
        statusCase = view.findViewById(R.id.caseStatus_tv);

        caseBtn = view.findViewById(R.id.caseBtn);
        caseBtn.setOnClickListener(view1 -> {
        });

        return view;
    }
}
