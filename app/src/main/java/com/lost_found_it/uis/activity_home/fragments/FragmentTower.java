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
import com.lost_found_it.databinding.FragmentMeccaBinding;
import com.lost_found_it.databinding.FragmentTowerBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.mvvm.FragmentMeccaTowerMvvm;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentTower extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentTowerBinding binding;
    private HomeActivity activity;
    private AdAdapter adAdapter;
    private FragmentMeccaTowerMvvm mvvm;
    private String type="";
    private CompositeDisposable disposable = new CompositeDisposable();

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
        mvvm = ViewModelProviders.of(this).get(FragmentMeccaTowerMvvm.class);
        binding.setLang(getLang());
        binding.toolbarTower.llBack.setOnClickListener(v -> {
            generalMvvm.getMainNavigationBackPress().setValue(true);
        });

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
        mvvm.getIsLoading().observe(activity,isLoading->{
            binding.recViewLayoutTower.swipeRefresh.setRefreshing(isLoading);
        });
        mvvm.getOnDataSuccess().observe(activity,list->{
            binding.recViewLayoutTower.tvNoData.setText(R.string.no_item);
            if (list.size()>0){
                binding.recViewLayoutTower.tvNoData.setVisibility(View.GONE);
            }else {
                binding.recViewLayoutTower.tvNoData.setVisibility(View.VISIBLE);
            }
            adAdapter.updateList(list);
        });

        generalMvvm.getOnNewAdAdded().observe(activity, adModel -> {
            if (mvvm.getOnDataSuccess().getValue() != null) {
                if (adModel.getPlace_type().equals("special")) {
                    mvvm.getOnDataSuccess().getValue().add(0, adModel);
                    if (adAdapter != null) {
                        adAdapter.notifyItemInserted(0);
                    }
                }

            }
        });

        generalMvvm.getOnAdUpdated().observe(activity, mBoolean -> {
            mvvm.getData(getUserSetting().getCountry(), type);
        });
        generalMvvm.getOnMeccaFoundLost().observe(activity, type -> {
            this.type=type;
            String title ="";
            if (type.equals("found")){
                title = getString(R.string.found_things_in_tower);
            }else {
                title = getString(R.string.missing_things_in_tower);

            }
            setUpToolbar(binding.toolbarTower,title,R.color.white,R.color.black);

            mvvm.getData(getUserSetting().getCountry(),type);

        });

        adAdapter = new AdAdapter(activity, this, getLang());
        binding.recViewLayoutTower.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayoutTower.recView.setAdapter(adAdapter);
        binding.recViewLayoutTower.recView.setHasFixedSize(true);
        binding.recViewLayoutTower.recView.setDrawingCacheEnabled(true);
        binding.recViewLayoutTower.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewLayoutTower.recView.setItemViewCacheSize(20);

        binding.recViewLayoutTower.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayoutTower.swipeRefresh.setOnRefreshListener(()->{
            if (type!=null){
                mvvm.getData(getUserSetting().getCountry(),type);

            }else {
                binding.recViewLayoutTower.swipeRefresh.setRefreshing(false);
            }
        });
    }

    public void navigateToAdDetails(AdModel adModel) {
        generalMvvm.getOnAdDetailsSelected().setValue(adModel.getId());
        generalMvvm.getMainNavigation().setValue(Tags.fragment_ad_details_pos);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
    }
}