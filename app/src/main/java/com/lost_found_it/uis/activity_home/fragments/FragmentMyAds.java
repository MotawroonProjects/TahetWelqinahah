package com.lost_found_it.uis.activity_home.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lost_found_it.R;
import com.lost_found_it.adapter.AdAdapter;
import com.lost_found_it.adapter.MyAdAdapter;
import com.lost_found_it.databinding.FragmentMeccaBinding;
import com.lost_found_it.databinding.FragmentMyAdsBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.mvvm.FragmentMyAdsMvvm;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_add_ads.AddAdsActivity;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

import java.util.List;

public class FragmentMyAds extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentMyAdsBinding binding;
    private HomeActivity activity;
    private MyAdAdapter myAdAdapter;
    private FragmentMyAdsMvvm mvvm;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
    }

    public static FragmentMyAds newInstance() {
        return new FragmentMyAds();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_ads, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        mvvm = ViewModelProviders.of(this).get(FragmentMyAdsMvvm.class);

        setUpToolbar(binding.toolbarMyAds,getString(R.string.my_ads),R.color.white,R.color.black);
        binding.setLang(getLang());
        binding.toolbarMyAds.llBack.setOnClickListener(v -> {
            generalMvvm.getMainNavigationBackPress().setValue(true);
        });

        mvvm.getIsLoading().observe(activity,isLoading->{
            binding.recViewLayout.swipeRefresh.setRefreshing(isLoading);
        });

        mvvm.getOnDataSuccess().observe(activity, adModels -> {
            if (adModels!=null&&adModels.size()>0){
                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
            }else {
                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
            }
            myAdAdapter.updateList(adModels);
        });

        mvvm.onDelete.observe(activity, aBoolean -> {
            if (aBoolean){
                mvvm.getMyData(getUserSetting().getCountry());
                Toast.makeText(activity, R.string.add_deleted, Toast.LENGTH_SHORT).show();
            }

        });

        mvvm.getMyData(getUserSetting().getCountry());

        binding.recViewLayout.swipeRefresh.setOnRefreshListener(()->mvvm.getMyData(getUserSetting().getCountry()));
        myAdAdapter = new MyAdAdapter(activity, this, getLang());
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setHasFixedSize(true);
        binding.recViewLayout.recView.setDrawingCacheEnabled(true);
        binding.recViewLayout.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewLayout.recView.setItemViewCacheSize(20);
        binding.recViewLayout.recView.setAdapter(myAdAdapter);

        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

    }

    public void navigateToDetails(AdModel adModel) {
        generalMvvm.getOnAdDetailsSelected().setValue(adModel);
        generalMvvm.getMainNavigation().setValue(Tags.fragment_ad_details_pos);
    }

    public void editAdd(int adapterPosition, AdModel adModel) {
        Intent intent=new Intent(activity, AddAdsActivity.class);
        intent.putExtra("data",adModel);
        startActivity(intent);
    }

    public void delete(int adapterPosition, AdModel adModel) {
        mvvm.deleteAdd(getUserSetting().getCountry(),adModel.getId());
    }


}