package com.lost_found_it.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lost_found_it.R;

import com.lost_found_it.adapter.NotificationAdapter;

import com.lost_found_it.databinding.FragmentNotificationBinding;
import com.lost_found_it.mvvm.FragmentNotificationMvvm;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

public class FragmentNotifications extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentNotificationBinding binding;
    private HomeActivity activity;
    private NotificationAdapter adapter;
    private FragmentNotificationMvvm mvvm;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentNotifications newInstance() {
        return new FragmentNotifications();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notification, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        generalMvvm.getOnCountrySuccess().observe(activity, isChanged -> {
            mvvm.getNotifications(getUserSetting().getCountry(),getUserModel());
        });
        binding.setLang(getLang());
        setUpToolbar(binding.toolbarNotification, getString(R.string.notifications), R.color.white, R.color.black);
        binding.toolbarNotification.llBack.setOnClickListener(v -> {
            generalMvvm.getMainNavigationBackPress().setValue(true);
        });

        mvvm = ViewModelProviders.of(this).get(FragmentNotificationMvvm.class);
        mvvm.getIsLoading().observe(activity, loading -> {
            binding.recViewLayout.swipeRefresh.setRefreshing(loading);

        });
        mvvm.getNotification().observe(activity, list -> {
            binding.recViewLayout.tvNoData.setText(R.string.no_notify);
            if (list.size() > 0) {
                adapter.updateList(list);
                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
            } else {
                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);

            }
        });

        adapter = new NotificationAdapter(activity, this);
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setHasFixedSize(true);
        binding.recViewLayout.recView.setDrawingCacheEnabled(true);
        binding.recViewLayout.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewLayout.recView.setItemViewCacheSize(20);
        binding.recViewLayout.recView.setAdapter(adapter);

        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(() -> mvvm.getNotifications(getUserSetting().getCountry(),getUserModel()));
        mvvm.getNotifications(getUserSetting().getCountry(),getUserModel());


    }

}