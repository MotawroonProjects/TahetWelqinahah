package com.lost_found_it.uis.activity_home.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.lost_found_it.R;
import com.lost_found_it.databinding.FragmentSearchBinding;
import com.lost_found_it.databinding.FragmentSettingBinding;
import com.lost_found_it.model.SettingDataModel;
import com.lost_found_it.mvvm.FragmentSettingsMvvm;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_about_app.AboutAppActivity;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_contact_us.ContactUsActivity;
import com.lost_found_it.uis.activity_country.CountryActivity;
import com.lost_found_it.uis.activity_home.HomeActivity;

public class FragmentSettings extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentSettingsMvvm settingsMvvm;
    private FragmentSettingBinding binding;
    private HomeActivity activity;
    private ActivityResultLauncher<Intent> launcher;
    private int req;
    private SettingDataModel.Data settingModel;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1) {
                binding.setCountry(getUserSetting().getCountry());
                generalMvvm.getOnCountrySuccess().setValue(true);
            }
        });
    }

    public static FragmentSettings newInstance() {
        return new FragmentSettings();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_setting, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {

        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        settingsMvvm = ViewModelProviders.of(activity).get(FragmentSettingsMvvm.class);
        binding.setLang(getLang());
        binding.setCountry(getUserSetting().getCountry());
        setUpToolbar(binding.toolbarSetting, getString(R.string.settings), R.color.white, R.color.black);
        binding.toolbarSetting.llBack.setOnClickListener(v -> {
            generalMvvm.getMainNavigationBackPress().setValue(true);
        });

        settingsMvvm.getOnDataSuccess().observe(activity, model -> {
            settingModel = model;
        });
        settingsMvvm.getSettings(activity, getUserSetting().getCountry());

        binding.tvLanguage.setOnClickListener(v -> {
            if (getLang().equals("ar")) {
                activity.refreshActivity("en");
            } else {
                activity.refreshActivity("ar");

            }
        });

        binding.llContactUs.setOnClickListener(v -> {
            Intent intent = new Intent(activity, ContactUsActivity.class);
            startActivity(intent);
        });

        binding.llCountry.setOnClickListener(v -> {
            req = 1;
            Intent intent = new Intent(activity, CountryActivity.class);
            launcher.launch(intent);
        });


        binding.llTerms.setOnClickListener(v -> {
            if (settingModel != null && settingModel.getTerms_condition() != null) {
                String url = Tags.base_url + "webView?type=" + settingModel.getTerms_condition();
                navigateToAboutAppActivity("1", url);
            } else {
                Toast.makeText(activity, R.string.inv_link, Toast.LENGTH_SHORT).show();
            }

        });

        binding.llPrivacy.setOnClickListener(v -> {
            if (settingModel != null && settingModel.getPrivacy_policy() != null) {
                String url = Tags.base_url + "webView?type=" + settingModel.getPrivacy_policy();
                navigateToAboutAppActivity("2", url);
            } else {
                Toast.makeText(activity, R.string.inv_link, Toast.LENGTH_SHORT).show();
            }
        });

        binding.llRateApp.setOnClickListener(v -> {
            rateApp();
        });
        binding.llShareApp.setOnClickListener(v -> {
            share();
        });

        binding.imageFacebook.setOnClickListener(v -> {

            if (settingModel != null && settingModel.getFacebook() != null) {
                String url = settingModel.getFacebook();
                createIntentForSocial(url);
            } else {
                Toast.makeText(activity, R.string.inv_link, Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageTwitter.setOnClickListener(v -> {

            if (settingModel != null && settingModel.getTwitter() != null) {
                String url = settingModel.getTwitter();
                createIntentForSocial(url);
            } else {
                Toast.makeText(activity, R.string.inv_link, Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageInstagram.setOnClickListener(v -> {

            if (settingModel != null && settingModel.getInstagram() != null) {
                String url = settingModel.getInstagram();
                createIntentForSocial(url);
            } else {
                Toast.makeText(activity, R.string.inv_link, Toast.LENGTH_SHORT).show();
            }
        });

        binding.imageSnapChat.setOnClickListener(v -> {

            if (settingModel != null && settingModel.getSnapchat() != null) {
                String url = settingModel.getSnapchat();
                createIntentForSocial(url);
            } else {
                Toast.makeText(activity, R.string.inv_link, Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void createIntentForSocial(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }

    private void navigateToAboutAppActivity(String type, String url) {

        Intent intent = new Intent(activity, AboutAppActivity.class);
        intent.putExtra("type", type);
        intent.putExtra("url", url);
        startActivity(intent);
    }

    private void share() {
        String app_url = "https://play.google.com/store/apps/details?id=" + activity.getPackageName();
        String content = getString(R.string.app_name) + "\n" + app_url;
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("plain/text");
        intent.putExtra(Intent.EXTRA_TITLE, getString(R.string.app_name));
        intent.putExtra(Intent.EXTRA_TEXT, content);
        startActivity(Intent.createChooser(intent, "Share"));

    }

    private void rateApp() {
        final String appPackageName = activity.getPackageName();
        try {
            Intent appStoreIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName));
            appStoreIntent.setPackage("com.android.vending");

            startActivity(appStoreIntent);
        } catch (android.content.ActivityNotFoundException exception) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        }
    }


}