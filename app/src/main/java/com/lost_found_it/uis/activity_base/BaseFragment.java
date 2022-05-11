package com.lost_found_it.uis.activity_base;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.lost_found_it.databinding.ToolbarBinding;
import com.lost_found_it.model.AppSettingModel;
import com.lost_found_it.model.UserModel;
import com.lost_found_it.preferences.Preferences;

import io.paperdb.Paper;

public class BaseFragment extends Fragment {
    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    protected String getLang() {
        Paper.init(context);
        String lang = Paper.book().read("lang", "ar");
        return lang;
    }

    protected UserModel getUserModel() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserData(context);

    }

    protected void clearUserModel(Context context) {
        Preferences preferences = Preferences.getInstance();
        preferences.clearUserData(context);

    }

    protected void setUserModel(UserModel userModel) {
        Preferences preferences = Preferences.getInstance();
        preferences.createUpdateUserData(context, userModel);
    }

    protected void setUserSetting(AppSettingModel setting) {
        Preferences preferences = Preferences.getInstance();
        preferences.createUpdateUserSetting(context, setting);
    }
    protected AppSettingModel getUserSetting() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserSettingData(context);
    }

    protected void setUpToolbar(ToolbarBinding binding, String title, int background, int arrowTitleColor) {
        binding.setLang(getLang());
        binding.setTitle(title);
        binding.arrow.setColorFilter(ContextCompat.getColor(context, arrowTitleColor));
        binding.tvTitle.setTextColor(ContextCompat.getColor(context, arrowTitleColor));
        binding.toolbar.setBackgroundResource(background);
    }



}
