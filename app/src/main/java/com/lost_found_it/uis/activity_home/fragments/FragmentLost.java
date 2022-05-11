package com.lost_found_it.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lost_found_it.R;
import com.lost_found_it.adapter.AdAdapter;
import com.lost_found_it.adapter.BrandAdapter;
import com.lost_found_it.adapter.CategoryAdapter;
import com.lost_found_it.databinding.FragmentHomeBinding;
import com.lost_found_it.databinding.FragmentLostBinding;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

public class FragmentLost extends BaseFragment {
    private FragmentLostBinding binding;
    private HomeActivity activity;
    private CategoryAdapter categoryAdapter;
    private BrandAdapter brandAdapter;
    private AdAdapter adAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentLost newInstance() {
        return new FragmentLost();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_lost, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        binding.setLang(getLang());
        binding.setCountry(getUserSetting().getCountry());
        binding.recViewCategory.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        categoryAdapter = new CategoryAdapter(activity, this, getLang());
        binding.recViewCategory.setAdapter(categoryAdapter);

        binding.recViewBrands.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        brandAdapter = new BrandAdapter(activity, this, getLang());
        binding.recViewBrands.setAdapter(brandAdapter);

        adAdapter = new AdAdapter(activity,this,getLang());
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setAdapter(adAdapter);
        binding.recViewLayout.recView.setHasFixedSize(true);
        binding.recViewLayout.recView.setDrawingCacheEnabled(true);
        binding.recViewLayout.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewLayout.recView.setItemViewCacheSize(20);

        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(()->{
            binding.recViewLayout.swipeRefresh.setRefreshing(false);
        });
    }

}