package com.lost_found_it.uis.activity_home;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;


import com.lost_found_it.R;
import com.lost_found_it.adapter.MyPagerAdapter;
import com.lost_found_it.databinding.ActivityHomeBinding;
import com.lost_found_it.language.Language;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_base.BaseActivity;
import com.lost_found_it.uis.activity_home.fragments.FragmentAdDetails;
import com.lost_found_it.uis.activity_home.fragments.FragmentHome;
import com.lost_found_it.uis.activity_home.fragments.FragmentMain;
import com.lost_found_it.uis.activity_home.fragments.FragmentMecca;
import com.lost_found_it.uis.activity_home.fragments.FragmentMyAds;
import com.lost_found_it.uis.activity_home.fragments.FragmentNotifications;
import com.lost_found_it.uis.activity_home.fragments.FragmentSearch;
import com.lost_found_it.uis.activity_home.fragments.FragmentSettings;
import com.lost_found_it.uis.activity_home.fragments.FragmentTower;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomeActivity extends BaseActivity {
    private GeneralMvvm generalMvvm;
    private ActivityHomeBinding binding;
    private MyPagerAdapter adapter;
    private List<Fragment> fragments;
    private Stack<Integer> stack;
    private boolean from_fire;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        getDataFromIntent();
        initView();


    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("from_fire")) {
            from_fire = true;
        }
    }


    private void initView() {


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

    }

    private void loadUiData() {
        generalMvvm = ViewModelProviders.of(this).get(GeneralMvvm.class);
        stack = new Stack<>();
        binding.setLang(getLang());
        fragments = new ArrayList<>();
        fragments.add(FragmentMain.newInstance());
        fragments.add(FragmentNotifications.newInstance());
        fragments.add(FragmentMyAds.newInstance());
        fragments.add(FragmentMecca.newInstance());
        fragments.add(FragmentTower.newInstance());
        fragments.add(FragmentSearch.newInstance());
        fragments.add(FragmentAdDetails.newInstance());
        fragments.add(FragmentSettings.newInstance());

        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, null);
        binding.pager.setAdapter(adapter);
        binding.pager.setOffscreenPageLimit(fragments.size());


        if (stack.isEmpty()) {
            stack.push(Tags.fragment_main_pos);
        }



        generalMvvm.getMainNavigation().observe(this, this::updateStack);

        generalMvvm.getMainNavigationBackPress().observe(this, back -> {
            if (back) {
                onBackPressed();
            }
        });
        generalMvvm.getOnUserLoggedOut().observe(this, loggedOut -> {
            if (EventBus.getDefault().isRegistered(this)) {
                EventBus.getDefault().unregister(this);
            }
            setUserModel(null);
        });
        generalMvvm.onTokenSuccess().observe(this, this::setUserModel);
        generalMvvm.getOnUserLoggedIn().observe(this, loggedIn -> {
            generalMvvm.updateToken(getUserModel(), getUserSetting().getCountry());

        });


        if (getUserModel() != null) {
            EventBus.getDefault().register(this);
            generalMvvm.updateToken(getUserModel(), getUserSetting().getCountry());
        }

        if (getUserModel() != null && from_fire) {
            updateStack(1);
        }
    }

    private void updateStack(int pos) {
        stack.push(pos);
        binding.pager.setCurrentItem(pos);
    }


    public void refreshActivity(String lang) {
        Paper.book().write("lang", lang);
        Language.setNewLocale(this, lang);
        new Handler()
                .postDelayed(() -> {

                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                }, 500);


    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenFoAdAdded(AdModel adModel) {
        generalMvvm.getOnNewAdAdded().setValue(adModel);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void listenFoAdAdded(Boolean updated) {
        generalMvvm.getOnAdUpdated().setValue(true);
    }

    @Override
    public void onBackPressed() {
        if (stack.size() > 1) {
            stack.pop();
            binding.pager.setCurrentItem(stack.peek());
        } else {
            finish();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
        disposable.clear();
    }
}
