package com.lost_found_it.uis.activity_home;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.lost_found_it.R;
import com.lost_found_it.adapter.MyPagerAdapter;
import com.lost_found_it.databinding.ActivityHomeBinding;
import com.lost_found_it.language.Language;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_base.BaseActivity;
import com.lost_found_it.uis.activity_home.fragments.FragmentAdDetails;
import com.lost_found_it.uis.activity_home.fragments.FragmentHome;
import com.lost_found_it.uis.activity_home.fragments.FragmentMain;
import com.lost_found_it.uis.activity_home.fragments.FragmentMecca;
import com.lost_found_it.uis.activity_home.fragments.FragmentMyAds;
import com.lost_found_it.uis.activity_home.fragments.FragmentNotifications;
import com.lost_found_it.uis.activity_home.fragments.FragmentSearch;
import com.lost_found_it.uis.activity_home.fragments.FragmentSettings;
import com.lost_found_it.uis.activity_home.fragments.FragmentTower;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import io.paperdb.Paper;

public class HomeActivity extends BaseActivity {
    private GeneralMvvm generalMvvm;
    private ActivityHomeBinding binding;
    private MyPagerAdapter adapter;
    private List<Fragment> fragments;
    private Stack<Integer> stack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        initView();


    }


    private void initView() {

        generalMvvm = ViewModelProviders.of(this).get(GeneralMvvm.class);
        stack = new Stack<>();
        binding.setLang(getLang());
        fragments = new ArrayList<>();
        fragments.add(FragmentMain.newInstance());
        fragments.add(FragmentNotifications.newInstance());
        fragments.add(FragmentMyAds.newInstance());
        fragments.add(FragmentMecca.newInstance());
        fragments.add(FragmentTower.newInstance());
        fragments.add(FragmentSearch.newInstance());
        fragments.add(FragmentAdDetails.newInstance());
        fragments.add(FragmentSettings.newInstance());

        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, null);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(fragments.size());


        if (stack.isEmpty()) {
            stack.push(Tags.fragment_main_pos);
        }

        generalMvvm.getMainNavigation().observe(this, this::updateStack);

        generalMvvm.getMainNavigationBackPress().observe(this, back -> {
            if (back) {
                onBackPressed();
            }
        });


    }

    private void updateStack(int pos) {
        stack.push(pos);
        binding.pager.setCurrentItem(pos);
    }


    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }

    @Override
    public void onBackPressed() {
        if (stack.size() > 1) {
            stack.pop();
            binding.pager.setCurrentItem(stack.peek());
        } else {
            finish();
        }

    }


}
