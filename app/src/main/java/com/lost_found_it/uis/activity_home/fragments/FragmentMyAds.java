package com.lost_found_it.uis.activity_home.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.lost_found_it.R;
import com.lost_found_it.adapter.MyAdAdapter;
import com.lost_found_it.databinding.FragmentMyAdsBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.mvvm.FragmentMyAdsMvvm;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_add_ads.AddAdsActivity;
import com.lost_found_it.uis.activity_base.BaseActivity;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_home.HomeActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentMyAds extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentMyAdsBinding binding;
    private HomeActivity activity;
    private MyAdAdapter adapter;
    private FragmentMyAdsMvvm mvvm;
    private ActivityResultLauncher<Intent> launcher;
    private int req;
    private CompositeDisposable disposable = new CompositeDisposable();


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (result.getResultCode() == BaseActivity.RESULT_OK) {
                if (req == 1) {
                    generalMvvm.getOnAdUpdated().setValue(true);

                }
            }
        });
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
        setUpToolbar(binding.toolbarMyAds, getString(R.string.my_ads), R.color.white, R.color.black);
        binding.setLang(getLang());
        binding.toolbarMyAds.llBack.setOnClickListener(v -> {
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
        generalMvvm.getOnNewAdAdded().observe(activity, adModel -> {
            if (mvvm.getOnDataSuccess().getValue() != null) {
                mvvm.getOnDataSuccess().getValue().add(0, adModel);
                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
                if (adapter != null) {
                    adapter.notifyItemInserted(0);
                }

            }
        });

        generalMvvm.getOnAdUpdated().observe(activity, mBoolean -> {
            if (getUserModel() != null) {
                mvvm.getMyAds(getUserSetting().getCountry(), getUserModel(),getLang());

            }
        });
        generalMvvm.getOnUserLoggedIn().observe(activity, mBoolean -> {
            if (getUserModel() != null) {
                mvvm.getMyAds(getUserSetting().getCountry(), getUserModel(),getLang());

            }
        });


        mvvm.getIsLoading().observe(activity, isLoading -> {
            binding.recViewLayout.swipeRefresh.setRefreshing(isLoading);
        });

        mvvm.getOnDataSuccess().observe(activity, adModels -> {
            if (adModels != null && adModels.size() > 0) {
                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
            } else {
                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
            }
            adapter.updateList(adModels);
        });

        mvvm.getOnDelete().observe(activity, pos -> {
            if (adapter != null) {
                adapter.notifyItemRemoved(pos);
            }

            if (mvvm.getOnDataSuccess().getValue() != null) {
                if (mvvm.getOnDataSuccess().getValue().size() == 0) {
                    binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
                }
            }

            generalMvvm.getOnAdUpdated().setValue(true);
            Toast.makeText(activity, R.string.add_deleted, Toast.LENGTH_SHORT).show();


        });

        if (getUserModel() != null) {
            mvvm.getMyAds(getUserSetting().getCountry(), getUserModel(),getLang());

        }

        binding.recViewLayout.swipeRefresh.setOnRefreshListener(() -> mvvm.getMyAds(getUserSetting().getCountry(), getUserModel(),getLang()));
        adapter = new MyAdAdapter(activity, this, getLang());
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setHasFixedSize(true);
        binding.recViewLayout.recView.setDrawingCacheEnabled(true);
        binding.recViewLayout.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewLayout.recView.setItemViewCacheSize(20);
        binding.recViewLayout.recView.setAdapter(adapter);

        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

        binding.cardPostAd.setOnClickListener(v -> {
            Intent intent = new Intent(activity, AddAdsActivity.class);
            startActivity(intent);
        });
    }

    public void navigateToDetails(AdModel adModel) {
        generalMvvm.getOnAdDetailsSelected().setValue(adModel.getId());
        generalMvvm.getMainNavigation().setValue(Tags.fragment_ad_details_pos);
    }

    public void editAdd(int adapterPosition, AdModel adModel) {
        req = 1;
        Intent intent = new Intent(activity, AddAdsActivity.class);
        intent.putExtra("data", adModel);
        launcher.launch(intent);
    }

    public void delete(int adapterPosition, AdModel adModel) {
        mvvm.deleteAd(getUserSetting().getCountry(), getUserModel(), adModel.getId(), adapterPosition);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
    }

}