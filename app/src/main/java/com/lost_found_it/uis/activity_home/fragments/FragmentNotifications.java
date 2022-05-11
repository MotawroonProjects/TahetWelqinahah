package com.lost_found_it.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lost_found_it.R;
import com.lost_found_it.adapter.AdAdapter;
import com.lost_found_it.adapter.SliderAdapter;
import com.lost_found_it.databinding.FragmentHomeBinding;
import com.lost_found_it.databinding.FragmentNotificationBinding;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

public class FragmentNotifications extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentNotificationBinding binding;
    private HomeActivity activity;


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
        binding.setLang(getLang());
        setUpToolbar(binding.toolbarNotification,getString(R.string.notifications),R.color.white,R.color.black);
        binding.toolbarNotification.llBack.setOnClickListener(v -> {
            generalMvvm.getMainNavigationBackPress().setValue(true);
        });
    }

}