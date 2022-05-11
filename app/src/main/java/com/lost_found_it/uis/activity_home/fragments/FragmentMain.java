package com.lost_found_it.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.lost_found_it.R;
import com.lost_found_it.adapter.AdAdapter;
import com.lost_found_it.adapter.MyPagerAdapter;
import com.lost_found_it.databinding.FragmentHomeBinding;
import com.lost_found_it.databinding.FragmentMainBinding;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;

public class FragmentMain extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentMainBinding binding;
    private HomeActivity activity;
    private MyPagerAdapter adapter;
    private List<Fragment> fragments;
    private ActionBarDrawerToggle toggle;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentMain newInstance() {
        return new FragmentMain();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_main, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        toggle = new ActionBarDrawerToggle(activity,binding.drawerLayout,binding.toolbar,R.string.open,R.string.close);
        binding.setLang(getLang());
        toggle.syncState();
        binding.toolbar.setNavigationIcon(R.drawable.ic_drawer_icon);

        fragments = new ArrayList<>();
        fragments.add(FragmentHome.newInstance());
        fragments.add(FragmentLost.newInstance());
        fragments.add(FragmentFound.newInstance());



        adapter = new MyPagerAdapter(getChildFragmentManager(), fragments, null);
        binding.pager.setAdapter(adapter);


        binding.flHome.setOnClickListener(v -> {
            binding.flHome.setBackgroundResource(R.drawable.circle_color1);
            binding.imageHome.setColorFilter(ContextCompat.getColor(activity, R.color.white));

            binding.imageLost.setColorFilter(ContextCompat.getColor(activity, R.color.white));
            binding.iconLost.setColorFilter(ContextCompat.getColor(activity, R.color.color2));
            binding.tvLost.setTextColor(ContextCompat.getColor(activity, R.color.color2));

            binding.imageFound.setColorFilter(ContextCompat.getColor(activity, R.color.white));
            binding.iconFound.setColorFilter(ContextCompat.getColor(activity, R.color.color2));
            binding.tvFound.setTextColor(ContextCompat.getColor(activity, R.color.color2));

            binding.pager.setCurrentItem(0);

        });

        binding.flLost.setOnClickListener(v -> {
            binding.flHome.setBackgroundResource(R.drawable.circle_white);
            binding.imageHome.setColorFilter(ContextCompat.getColor(activity, R.color.color2));

            binding.imageLost.setColorFilter(ContextCompat.getColor(activity, R.color.colorPrimary));
            binding.iconLost.setColorFilter(ContextCompat.getColor(activity, R.color.white));
            binding.tvLost.setTextColor(ContextCompat.getColor(activity, R.color.white));

            binding.imageFound.setColorFilter(ContextCompat.getColor(activity, R.color.white));
            binding.iconFound.setColorFilter(ContextCompat.getColor(activity, R.color.color2));
            binding.tvFound.setTextColor(ContextCompat.getColor(activity, R.color.color2));
            binding.pager.setCurrentItem(1);


        });

        binding.flFound.setOnClickListener(v -> {
            binding.flHome.setBackgroundResource(R.drawable.circle_white);
            binding.imageHome.setColorFilter(ContextCompat.getColor(activity, R.color.color2));

            binding.imageLost.setColorFilter(ContextCompat.getColor(activity, R.color.white));
            binding.iconLost.setColorFilter(ContextCompat.getColor(activity, R.color.color2));
            binding.tvLost.setTextColor(ContextCompat.getColor(activity, R.color.color2));

            binding.imageFound.setColorFilter(ContextCompat.getColor(activity, R.color.color3));
            binding.iconFound.setColorFilter(ContextCompat.getColor(activity, R.color.white));
            binding.tvFound.setTextColor(ContextCompat.getColor(activity, R.color.white));
            binding.pager.setCurrentItem(2);


        });

        binding.cardMyAds.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            generalMvvm.getMainNavigation().setValue(Tags.fragment_my_ads_pos);
        });

        binding.cardSetting.setOnClickListener(v -> {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
            generalMvvm.getMainNavigation().setValue(Tags.fragment_setting_pos);
        });

        binding.imageNotification.setOnClickListener(v -> {
            generalMvvm.getMainNavigation().setValue(Tags.fragment_notification_pos);
        });
    }
}
