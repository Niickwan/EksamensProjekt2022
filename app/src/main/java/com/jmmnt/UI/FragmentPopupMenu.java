package com.jmmnt.UI;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.jmmnt.R;

public class FragmentPopupMenu extends Fragment {

    private static FragmentPopupMenu fragmentPopupMenu = null;
    private TextView tv;

    private FragmentPopupMenu(){
    }

    public static FragmentPopupMenu getInstance() {
        if (fragmentPopupMenu  == null)
            fragmentPopupMenu  = new FragmentPopupMenu();
        return fragmentPopupMenu;
    }

    public void showProfileMenu(View view, LayoutInflater layoutInflater) {
        PopupWindow pw = new PopupWindow(layoutInflater.inflate(R.layout.popup_menu_profile,
                null, false), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.showAsDropDown(view, 0,0, Gravity.RIGHT | Gravity.TOP);

        pw.getContentView().findViewById(R.id.popupMenuAbout_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                System.out.println("WTF WTF WTRF");
            }
        });

    }


}
