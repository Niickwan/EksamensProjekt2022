package com.jmmnt.Controller.UI;

import android.app.Activity;
import android.app.Dialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import com.jmmnt.Entities.LoggedInUser;
import com.jmmnt.R;
import com.jmmnt.UseCase.GeneralUseCase;

public class FragmentPopupMenu extends Fragment {

    private static FragmentPopupMenu fragmentPopupMenu;
    private GeneralUseCase gUC = GeneralUseCase.getInstance();
    private TextView username, email, phone;

    private FragmentPopupMenu(){
    }

    public static FragmentPopupMenu getInstance() {
        if (fragmentPopupMenu  == null) {
            return fragmentPopupMenu = new FragmentPopupMenu();
        }else
            return fragmentPopupMenu;
    }

    public void showProfileMenu(View view, LayoutInflater layoutInflater, Activity activity, int fragment) {
        PopupWindow pw = new PopupWindow(layoutInflater.inflate(R.layout.popup_menu_profile,
                null, true), ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pw.showAsDropDown(view, 0,0, Gravity.RIGHT | Gravity.TOP);

        username = pw.getContentView().findViewById(R.id.popupMenuUserName_tv);
        email = pw.getContentView().findViewById(R.id.popupMenuEmail_tv);
        phone = pw.getContentView().findViewById(R.id.popupMenuPhone_tv);
        username.setText(LoggedInUser.getInstance().getUser().getFullName());
        email.setText(LoggedInUser.getInstance().getUser().getEmail());
        phone.setText(LoggedInUser.getInstance().getUser().getPhonenumber());

        View.OnClickListener pwMenuItemClicked = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (view.getId() == R.id.popupMenuAbout_tv) {
                    popupMenuShowAbout(activity);
                    pw.dismiss();
                    return;
                }
                if (view.getId() == R.id.popupMenuEditProfile_tv) {
                    int navHostFragmentContent = 0;
                    int navToFragment = 0;
                    if (LoggedInUser.getInstance().getUser().getUserRights() == 1) {
                        navHostFragmentContent = R.id.nav_host_fragment_content_admin;
                        navToFragment = R.id.adminFragmentEditProfile;
                    }
                    else if (LoggedInUser.getInstance().getUser().getUserRights() == 2) {
                        navHostFragmentContent = R.id.nav_host_fragment_content_user;
                        navToFragment = R.id.userFragmentEditProfile;
                    }
                    NavController navController = Navigation.findNavController(activity, navHostFragmentContent);
                    navController.navigateUp();
                    navController.navigate(navToFragment);
                    pw.dismiss();
                    return;
                }
                if (view.getId() == R.id.popupMenuLogout_tv){
                    gUC.switchScene(activity, ActivityLogin.class);
                    pw.dismiss();
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