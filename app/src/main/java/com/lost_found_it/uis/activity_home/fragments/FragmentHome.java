package com.lost_found_it.uis.activity_home.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lost_found_it.R;
import com.lost_found_it.adapter.AdAdapter;
import com.lost_found_it.adapter.SliderAdapter;
import com.lost_found_it.databinding.FragmentHomeBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.mvvm.FragmentHomeMvvm;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_add_ads.AddAdsActivity;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

import java.util.Timer;
import java.util.TimerTask;

public class FragmentHome extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentHomeMvvm mvvm;
    private FragmentHomeBinding binding;
    private SliderAdapter sliderAdapter;
    private AdAdapter adAdapter;
    private HomeActivity activity;
    private MyTimerTask timerTask;
    private Timer timer;

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
        binding.llData.setVisibility(View.GONE);
        mvvm = ViewModelProviders.of(this).get(FragmentHomeMvvm.class);

        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        generalMvvm.getOnCountrySuccess().observe(activity, isChanged -> {
            binding.setCountry(getUserSetting().getCountry());
            mvvm.getData(getUserSetting().getCountry());

        });

        binding.setLang(getLang());
        binding.setCountry(getUserSetting().getCountry());
        sliderAdapter = new SliderAdapter(getActivity());
        binding.pager.setAdapter(sliderAdapter);
        binding.indicator.setViewPager(binding.pager);

        adAdapter = new AdAdapter(activity, this, getLang());
        binding.recViewHome.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewHome.setAdapter(adAdapter);

        binding.homeSwipeLayout.setColorSchemeResources(R.color.colorPrimary);
        binding.llSearch.setOnClickListener(v -> {
            generalMvvm.getMainNavigation().setValue(Tags.fragment_search_pos);
        });


        mvvm.getIsLoading().observe(activity,isLoading->{
            binding.homeSwipeLayout.setRefreshing(isLoading);
        });
        mvvm.getOnDataSuccess().observe(activity,homeDataModel -> {
            binding.llData.setVisibility(View.VISIBLE);
            if (homeDataModel.getData().getSlider().size()>0){
                binding.flSlider.setVisibility(View.VISIBLE);
                binding.indicator.setCount(homeDataModel.getData().getSlider().size());

                if (homeDataModel.getData().getSlider().size()>1){
                    startSliderTimer();

                }
            }else {
                binding.flSlider.setVisibility(View.GONE);

            }
            sliderAdapter.updateList(homeDataModel.getData().getSlider());

            adAdapter.updateList(homeDataModel.getData().getAds());
            adAdapter.notifyDataSetChanged();
        });


        binding.cardMecca.setOnClickListener(v -> {
            generalMvvm.getOnMeccaFoundLost().setValue("lost");
            generalMvvm.getMainNavigation().setValue(Tags.fragment_mecca_pos);
        });

        binding.cardTower.setOnClickListener(v -> {
            generalMvvm.getOnTowerFoundLost().setValue("lost");
            generalMvvm.getMainNavigation().setValue(Tags.fragment_tower_pos);
        });

        binding.cardPostAd.setOnClickListener(v -> {
            Intent intent = new Intent(activity, AddAdsActivity.class);
            startActivity(intent);
        });

        mvvm.getData(getUserSetting().getCountry());

        binding.homeSwipeLayout.setOnRefreshListener(()->mvvm.getData(getUserSetting().getCountry()));

    }

    private void startSliderTimer() {
        timerTask = new MyTimerTask();
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask,5000,5000);

    }

    public void navigateToAdDetails(AdModel adModel) {
        generalMvvm.getOnAdDetailsSelected().setValue(adModel);
        generalMvvm.getMainNavigation().setValue(Tags.fragment_ad_details_pos);
    }

    public class MyTimerTask extends TimerTask{

        @Override
        public void run() {
            int currentPos = binding.pager.getCurrentItem();
            if (currentPos< sliderAdapter.getCount()-1){
                currentPos +=1;
            }else {
                currentPos = 0;

            }
            binding.pager.setCurrentItem(currentPos);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer!=null&&timerTask!=null){
            timer.purge();
            timer.cancel();
            timerTask.cancel();
        }
    }
}