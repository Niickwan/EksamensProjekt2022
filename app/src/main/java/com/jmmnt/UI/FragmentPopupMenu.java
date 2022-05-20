package com.jmmnt.UI;

import android.app.Activity;
import android.app.Dialog;
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
import androidx.navigation.fragment.NavHostFragment;

import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.Entities.User;
import com.jmmnt.R;
import com.jmmnt.UseCase.GeneralUseCase;

public class FragmentPopupMenu extends Fragment {

    private static FragmentPopupMenu fragmentPopupMenu = null;
    private GeneralUseCase gUC = GeneralUseCase.getInstance();
    private TextView username, email, phone;
    private User user = LoggedInUser.getInstance().getUser();


    private FragmentPopupMenu(){
    }

    public static FragmentPopupMenu getInstance() {
        if (fragmentPopupMenu  == null)
            fragmentPopupMenu  = new FragmentPopupMenu();
        return fragmentPopupMenu;
    }

    public void showProfileMenu(View view, LayoutInflater layoutInflater, Activity activity) {
        PopupWindow pw = new PopupWindow(layoutInflater.inflate(R.layout.popup_menu_profile,
                null, false), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.showAsDropDown(view, 0,0, Gravity.RIGHT | Gravity.TOP);

        username = pw.getContentView().findViewById(R.id.popupMenuUserName_tv);
        email = pw.getContentView().findViewById(R.id.popupMenuEmail_tv);
        phone = pw.getContentView().findViewById(R.id.popupMenuPhone_tv);

        System.out.println(user.getEmail());
        username.setText(user.getEmail());


        System.out.println("USERNAME "+username.getText().toString());

        View.OnClickListener pwMenuItemClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.popupMenuAbout_tv) {
                    popupMenuShowAbout(activity);
                    pw.dismiss();
                    return;
                }
                if (view.getId() == R.id.popupMenuEditProfile_tv){
                    System.out.println("REDIGER"); //TODO sout
                    pw.dismiss();
                    return;
                }
                if (view.getId() == R.id.popupMenuLogout_tv){
                    gUC.switchScene(activity, ActivityLogin.class);
                    pw.dismiss();
                    return;
                }
            }
        };
        pw.getContentView().findViewById(R.id.popupMenuAbout_tv).setOnClickListener(pwMenuItemClicked);
        pw.getContentView().findViewById(R.id.popupMenuEditProfile_tv).setOnClickListener(pwMenuItemClicked);
        pw.getContentView().findViewById(R.id.popupMenuLogout_tv).setOnClickListener(pwMenuItemClicked);
    }

    public void popupMenuShowAbout(Activity activity) {
        Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.fragment_login_company_info);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }


}
