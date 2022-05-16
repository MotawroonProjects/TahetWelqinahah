package com.lost_found_it.uis.activity_home.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import com.lost_found_it.adapter.SliderAdAdapter;
import com.lost_found_it.adapter.SliderAdapter;
import com.lost_found_it.databinding.FragmentAdDetailsBinding;
import com.lost_found_it.databinding.FragmentLostBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.model.SingleAdModel;
import com.lost_found_it.mvvm.FragmentAdDetailsMvvm;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

import java.util.Timer;
import java.util.TimerTask;

public class FragmentAdDetails extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentAdDetailsMvvm mvvm;
    private FragmentAdDetailsBinding binding;
    private HomeActivity activity;
    private SliderAdAdapter sliderAdapter;
    private RelatedAdAdapter adAdapter;
    private AdModel adModel;
    private MyTimerTask timerTask;
    private Timer timer;

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
        mvvm = ViewModelProviders.of(this).get(FragmentAdDetailsMvvm.class);

        setUpToolbar(binding.toolbarAdDetails, getString(R.string.details), R.color.white, R.color.black);
        binding.setLang(getLang());
        binding.toolbarAdDetails.llBack.setOnClickListener(v -> {
            generalMvvm.getMainNavigationBackPress().setValue(true);
        });

        generalMvvm.getOnAdDetailsSelected().observe(activity, adModel1 -> {
            if (adModel1 != null) {
                if (this.adModel!=null) {
                    if (!this.adModel.getId().equals(adModel1.getId())){
                        binding.setModel(null);
                        String device_id = getUserSetting().getId();
                        if (getUserModel() != null) {
                            device_id = device_id + "_" + getUserModel().getData().getUser().getId();
                        }
                        mvvm.getData(getUserSetting().getCountry(), device_id, adModel1.getId());

                    }else {
                        updateUi(mvvm.getOnDataSuccess().getValue());
                    }


                } else {
                    String device_id = getUserSetting().getId();
                    if (getUserModel() != null) {
                        device_id = device_id + "_" + getUserModel().getData().getUser().getId();
                    }
                    mvvm.getData(getUserSetting().getCountry(), device_id, adModel1.getId());

                }
            }
        });
        mvvm.getOnDataSuccess().observe(activity, this::updateUi);
        mvvm.getIsLoading().observe(activity, isLoading -> {
            if (isLoading) {
                binding.setModel(null);
            }
            binding.swipeRefresh.setRefreshing(isLoading);

        });
        sliderAdapter = new SliderAdAdapter(getActivity());
        binding.pager.setAdapter(sliderAdapter);
        binding.indicator.setViewPager(binding.pager);
        binding.indicator.setCount(sliderAdapter.getCount());


        adAdapter = new RelatedAdAdapter(activity, this, getLang());
        binding.recView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        binding.recView.setAdapter(adAdapter);
        binding.recView.setHasFixedSize(true);
        binding.recView.setDrawingCacheEnabled(true);
        binding.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recView.setItemViewCacheSize(20);

        binding.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.swipeRefresh.setOnRefreshListener(() -> {
            if (this.adModel != null) {

                String device_id = getUserSetting().getId();
                if (getUserModel() != null) {
                    device_id = device_id + "_" + getUserModel().getData().getUser().getId();
                }
                mvvm.getData(getUserSetting().getCountry(), device_id, adModel.getId());

            } else {
                binding.swipeRefresh.setRefreshing(false);
            }

        });
    }

    private void updateUi(SingleAdModel.Data singleAdModel) {
        if (singleAdModel!=null){
            this.adModel = singleAdModel.getAd();

            if (adModel.getImages().size() > 0) {
                sliderAdapter.updateList(adModel.getImages());
                binding.indicator.setCount(adModel.getImages().size());

                if (adModel.getImages().size()>1){
                    startSliderTimer();
                }
            }

            if (singleAdModel.getRelated().size() > 0) {
                adAdapter.updateList(singleAdModel.getRelated());
                binding.llRelated.setVisibility(View.VISIBLE);
            } else {
                binding.llRelated.setVisibility(View.GONE);

            }

            binding.setModel(adModel);
        }


    }

    private void startSliderTimer() {
        if (timer!=null&&timerTask!=null){
            timer.purge();
            timer.cancel();
            timerTask.cancel();
            timerTask = null;
            timer = null;
        }

        timerTask = new MyTimerTask();
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 5000, 5000);


    }


    public void setAdItemData(AdModel adModel) {
        String device_id = getUserSetting().getId();
        if (getUserModel() != null) {
            device_id = device_id + "_" + getUserModel().getData().getUser().getId();
        }
        mvvm.getData(getUserSetting().getCountry(),device_id,adModel.getId());
    }

    public class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            int currentPos = binding.pager.getCurrentItem();
            if (currentPos < sliderAdapter.getCount() - 1) {
                currentPos += 1;
            } else {
                currentPos = 0;

            }
            binding.pager.setCurrentItem(currentPos);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null && timerTask != null) {
            timer.purge();
            timer.cancel();
            timerTask.cancel();
            timerTask = null;
            timer = null;
        }
    }

}