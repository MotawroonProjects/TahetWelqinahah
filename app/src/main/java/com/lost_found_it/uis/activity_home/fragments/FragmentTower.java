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
import com.lost_found_it.databinding.FragmentMeccaBinding;
import com.lost_found_it.databinding.FragmentTowerBinding;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

public class FragmentTower extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentTowerBinding binding;
    private HomeActivity activity;
    private AdAdapter adAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentTower newInstance() {
        return new FragmentTower();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_tower, container, false);
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
        setUpToolbar(binding.toolbarTower,"",R.color.white,R.color.black);
        binding.toolbarTower.llBack.setOnClickListener(v -> {
            generalMvvm.getMainNavigationBackPress().setValue(true);
        });
        adAdapter = new AdAdapter(activity, this, getLang());
        binding.recViewLayoutTower.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayoutTower.recView.setAdapter(adAdapter);
        binding.recViewLayoutTower.recView.setHasFixedSize(true);
        binding.recViewLayoutTower.recView.setDrawingCacheEnabled(true);
        binding.recViewLayoutTower.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewLayoutTower.recView.setItemViewCacheSize(20);

        binding.recViewLayoutTower.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayoutTower.swipeRefresh.setOnRefreshListener(() -> {
            binding.recViewLayoutTower.swipeRefresh.setRefreshing(false);
        });


    }

}