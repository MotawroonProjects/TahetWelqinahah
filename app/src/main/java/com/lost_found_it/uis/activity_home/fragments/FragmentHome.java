package com.lost_found_it.uis.activity_home.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lost_found_it.R;
import com.lost_found_it.adapter.AdAdapter;
import com.lost_found_it.adapter.ClosedAdAdapter;
import com.lost_found_it.adapter.SliderAdapter;
import com.lost_found_it.databinding.FragmentHomeBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.mvvm.FragmentHomeMvvm;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_add_ads.AddAdsActivity;
import com.lost_found_it.uis.activity_base.BaseActivity;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;
import com.lost_found_it.uis.activity_login.LoginActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentHome extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentHomeMvvm mvvm;
    private FragmentHomeBinding binding;
    private SliderAdapter sliderAdapter;
    private AdAdapter adAdapter;
    private ClosedAdAdapter closedAdAdapter;
    private HomeActivity activity;
    private MyTimerTask timerTask;
    private Timer timer;
    private int req = 1;
    private ActivityResultLauncher<Intent> launcher;
    private CompositeDisposable disposable = new CompositeDisposable();
    private ActivityResultLauncher<String> permissionLauncher;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getResultCode() == BaseActivity.RESULT_OK) {
                generalMvvm.getOnUserLoggedIn().setValue(true);
            } else {
                mvvm.startLocationUpdate();
            }
        });

        permissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
            if (isGranted) {
                mvvm.initGoogleApi();

            } else {
                Toast.makeText(activity, "Permission denied", Toast.LENGTH_SHORT).show();

            }
        });
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
        binding.setLang(getLang());
        binding.setCountry(getUserSetting().getCountry());
        mvvm = ViewModelProviders.of(this).get(FragmentHomeMvvm.class);
        mvvm.setActivity(activity);
        mvvm.getOnLocationUpdated().observe(activity, location -> {
            mvvm.getNearByAds(getUserSetting().getCountry(), location.getLatitude(), location.getLongitude());

        });

        mvvm.getOnNearAdsSuccess().observe(activity,ads->{
            if (ads.size()>0){
                binding.llClosedAds.setVisibility(View.VISIBLE);
                closedAdAdapter.updateList(ads);
            }else {
                binding.llClosedAds.setVisibility(View.GONE);
            }
        });
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        Observable.timer(10, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Long>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onNext(@NonNull Long aLong) {

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {
                        loadUiData();
                    }
                });
        CheckPermission();

        closedAdAdapter = new ClosedAdAdapter(activity, this, getLang());
        binding.recViewClosedAds.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewClosedAds.setAdapter(closedAdAdapter);

    }

    private void loadUiData() {
        generalMvvm.getOnCountrySuccess().observe(activity, isChanged -> {
            binding.setCountry(getUserSetting().getCountry());
            mvvm.getData(getUserSetting().getCountry());

        });

        generalMvvm.getOnAdUpdated().observe(activity, mBoolean -> {
            mvvm.getData(getUserSetting().getCountry());
        });

        generalMvvm.getOnNewAdAdded().observe(activity, model -> {
            mvvm.getData(getUserSetting().getCountry());
        });


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


        mvvm.getIsLoading().observe(activity, isLoading -> {
            binding.homeSwipeLayout.setRefreshing(isLoading);
        });
        mvvm.getOnDataSuccess().observe(activity, homeDataModel -> {
            binding.llData.setVisibility(View.VISIBLE);
            if (homeDataModel.getData().getSlider().size() > 0) {
                binding.flSlider.setVisibility(View.VISIBLE);
                binding.indicator.setCount(homeDataModel.getData().getSlider().size());

                if (homeDataModel.getData().getSlider().size() > 1) {
                    startSliderTimer();

                }
            } else {
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
            if (getUserModel() == null) {
                navigateToLoginActivity();
            } else {
                Intent intent = new Intent(activity, AddAdsActivity.class);
                launcher.launch(intent);
            }

        });

        mvvm.getData(getUserSetting().getCountry());

        binding.homeSwipeLayout.setOnRefreshListener(() -> mvvm.getData(getUserSetting().getCountry()));

    }

    private void CheckPermission() {
        if (ActivityCompat.checkSelfPermission(activity, BaseActivity.fineLocPerm) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(BaseActivity.fineLocPerm);
        } else {

            mvvm.initGoogleApi();
        }
    }

    private void navigateToLoginActivity() {
        req = 1;
        Intent intent = new Intent(activity, LoginActivity.class);
        launcher.launch(intent);
    }

    private void startSliderTimer() {
        timerTask = new MyTimerTask();
        timer = new Timer();
        timer.scheduleAtFixedRate(timerTask, 5000, 5000);

    }

    public void navigateToAdDetails(AdModel adModel) {
        generalMvvm.getOnAdDetailsSelected().setValue(adModel.getId());
        generalMvvm.getMainNavigation().setValue(Tags.fragment_ad_details_pos);
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
        }
        disposable.clear();

    }

}