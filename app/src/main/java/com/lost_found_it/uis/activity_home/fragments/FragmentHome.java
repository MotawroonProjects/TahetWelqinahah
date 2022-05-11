package com.lost_found_it.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lost_found_it.R;
import com.lost_found_it.adapter.AdAdapter;
import com.lost_found_it.adapter.SliderAdapter;
import com.lost_found_it.databinding.FragmentHomeBinding;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

public class FragmentHome extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentHomeBinding binding;
    private SliderAdapter sliderAdapter;
    private AdAdapter adAdapter;
    private HomeActivity activity;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentHome newInstance() {
        return new FragmentHome();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false);
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
            binding.setCountry(getUserSetting().getCountry());
        });
        binding.setLang(getLang());
        binding.setCountry(getUserSetting().getCountry());
        sliderAdapter = new SliderAdapter(getActivity());
        binding.pager.setAdapter(sliderAdapter);
        binding.indicator.setViewPager(binding.pager);
        binding.indicator.setCount(sliderAdapter.getCount());

        adAdapter = new AdAdapter(activity, this, getLang());
        binding.recViewHome.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewHome.setAdapter(adAdapter);

        binding.llSearch.setOnClickListener(v -> {
            generalMvvm.getMainNavigation().setValue(Tags.fragment_search_pos);
        });

        binding.cardMecca.setOnClickListener(v -> {
            generalMvvm.getMainNavigation().setValue(Tags.fragment_mecca_pos);
        });

        binding.cardTower.setOnClickListener(v -> {
            generalMvvm.getMainNavigation().setValue(Tags.fragment_tower_pos);
        });
    }

    public void navigateToAdDetails() {
        generalMvvm.getMainNavigation().setValue(Tags.fragment_ad_details_pos);
    }
}