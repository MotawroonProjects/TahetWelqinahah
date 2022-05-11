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
import com.lost_found_it.adapter.BrandAdapter;
import com.lost_found_it.adapter.CategoryAdapter;
import com.lost_found_it.adapter.RelatedAdAdapter;
import com.lost_found_it.adapter.SliderAdapter;
import com.lost_found_it.databinding.FragmentAdDetailsBinding;
import com.lost_found_it.databinding.FragmentLostBinding;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

public class FragmentAdDetails extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentAdDetailsBinding binding;
    private HomeActivity activity;
    private SliderAdapter sliderAdapter;
    private RelatedAdAdapter adAdapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentAdDetails newInstance() {
        return new FragmentAdDetails();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_ad_details, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        setUpToolbar(binding.toolbarAdDetails,getString(R.string.details),R.color.white,R.color.black);
        binding.setLang(getLang());
        binding.toolbarAdDetails.llBack.setOnClickListener(v -> {
            generalMvvm.getMainNavigationBackPress().setValue(true);
        });

        sliderAdapter = new SliderAdapter(getActivity());
        binding.pager.setAdapter(sliderAdapter);
        binding.indicator.setViewPager(binding.pager);
        binding.indicator.setCount(sliderAdapter.getCount());


        adAdapter = new RelatedAdAdapter(activity,this,getLang());
        binding.recView.setLayoutManager(new LinearLayoutManager(activity,LinearLayoutManager.HORIZONTAL,false));
        binding.recView.setAdapter(adAdapter);
        binding.recView.setHasFixedSize(true);
        binding.recView.setDrawingCacheEnabled(true);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setItemViewCacheSize(20);

    }

}