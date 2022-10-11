package com.lost_found_it.uis.activity_home.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.lost_found_it.R;
import com.lost_found_it.adapter.MyAdAdapter;
import com.lost_found_it.adapter.SearchAdapter;
import com.lost_found_it.adapter.SpinnerLastDayAdapter;
import com.lost_found_it.databinding.BottomSheetFilterBinding;
import com.lost_found_it.databinding.FragmentNotificationBinding;
import com.lost_found_it.databinding.FragmentSearchBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.model.CityModel;
import com.lost_found_it.mvvm.FragmentHomeMvvm;
import com.lost_found_it.mvvm.FragmentSearchMvvm;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_cities.CitiesActivity;
import com.lost_found_it.uis.activity_home.HomeActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class FragmentSearch extends BaseFragment {
    private GeneralMvvm generalMvvm;
    private FragmentSearchBinding binding;
    private HomeActivity activity;
    private FragmentSearchMvvm mvvm;
    private SearchAdapter searchAdapter;
    private SpinnerLastDayAdapter spinnerLastDayAdapter;
    private CompositeDisposable disposable = new CompositeDisposable();
    private BottomSheetDialog sheetDialog;
    private BottomSheetFilterBinding sheetFilterBinding;
    private ActivityResultLauncher<Intent> launcher;
    private int req;


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        activity = (HomeActivity) context;
        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            if (req == 1 && result.getData() != null && result.getData().hasExtra("data")) {
                CityModel cityModel = (CityModel) result.getData().getSerializableExtra("data");
                mvvm.getCityData().setValue(cityModel);
                sheetFilterBinding.setCity(cityModel.getName());
            }

        });
    }

    public static FragmentSearch newInstance() {
        return new FragmentSearch();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
    }

    private void initView() {
        generalMvvm = ViewModelProviders.of(activity).get(GeneralMvvm.class);
        mvvm = ViewModelProviders.of(this).get(FragmentSearchMvvm.class);
        binding.setLang(getLang());
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

        binding.filter.setOnClickListener(v -> {
            createBottomSheetFilter();
        });
    }

    private void loadUiData() {
        mvvm.getIsLoading().observe(activity, isLoading -> {
            binding.recViewLayout.swipeRefresh.setRefreshing(isLoading);
        });

        mvvm.getOnSearchSuccess().observe(activity, adModels -> {
            if (adModels != null && adModels.size() > 0) {
                binding.recViewLayout.tvNoData.setVisibility(View.GONE);
            } else {
                binding.recViewLayout.tvNoData.setVisibility(View.VISIBLE);
            }
            searchAdapter.updateList(adModels);
        });

        binding.edtSearch.requestFocus();
        binding.imageBack.setOnClickListener(v -> {
            generalMvvm.getMainNavigationBackPress().setValue(true);
        });
        mvvm.search(getUserSetting().getCountry(), binding.edtSearch.getText().toString(), null, null,getLang());
        binding.recViewLayout.swipeRefresh.setOnRefreshListener(() -> {
            String added_later = null;
            String city_id = null;
            if (mvvm.getAdded_later().getValue() != null) {
                added_later = mvvm.getAdded_later().getValue();
            }

            if (mvvm.getCityData().getValue() != null) {
                city_id = mvvm.getCityData().getValue().getId();
            }
            mvvm.search(getUserSetting().getCountry(), binding.edtSearch.getText().toString(), added_later, city_id,getLang());
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String added_later = null;
                String city_id = null;
                if (mvvm.getAdded_later().getValue() != null) {
                    added_later = mvvm.getAdded_later().getValue();
                }

                if (mvvm.getCityData().getValue() != null) {
                    city_id = mvvm.getCityData().getValue().getId();
                }
                mvvm.search(getUserSetting().getCountry(), s.toString(), added_later, city_id,getLang());
            }
        });

        searchAdapter = new SearchAdapter(activity, this, getLang());
        binding.recViewLayout.recView.setLayoutManager(new LinearLayoutManager(activity));
        binding.recViewLayout.recView.setHasFixedSize(true);
        binding.recViewLayout.recView.setDrawingCacheEnabled(true);
        binding.recViewLayout.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewLayout.recView.setItemViewCacheSize(20);
        binding.recViewLayout.recView.setAdapter(searchAdapter);
        binding.recViewLayout.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);

    }

    public void navigateToDetails(AdModel adModel) {
        generalMvvm.getOnAdDetailsSelected().setValue(adModel.getId());
        generalMvvm.getMainNavigation().setValue(Tags.fragment_ad_details_pos);
    }

    private void createBottomSheetFilter() {

        if (sheetDialog == null) {
            sheetFilterBinding = DataBindingUtil.inflate(LayoutInflater.from(activity), R.layout.bottom_sheet_filter, null, false);
            sheetFilterBinding.setLang(getLang());
            sheetDialog = new BottomSheetDialog(activity);
            sheetDialog.setCanceledOnTouchOutside(true);
            sheetDialog.setContentView(sheetFilterBinding.getRoot());
            sheetFilterBinding.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    mvvm.getFilterDayPos().setValue(position);
                    if (position == 0) {
                        mvvm.getAdded_later().setValue(null);

                    } else if (position == 1) {
                        mvvm.getAdded_later().setValue("1");

                    } else if (position == 2) {
                        mvvm.getAdded_later().setValue("3");

                    } else if (position == 3) {
                        mvvm.getAdded_later().setValue("7");

                    } else if (position == 4) {
                        mvvm.getAdded_later().setValue("30");

                    } else if (position == 5) {
                        mvvm.getAdded_later().setValue(null);

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            sheetFilterBinding.close.setOnClickListener(v -> {
                sheetDialog.dismiss();
            });

            sheetFilterBinding.llCity.setOnClickListener(v -> {
                req = 1;
                Intent intent = new Intent(activity, CitiesActivity.class);
                launcher.launch(intent);
            });
            sheetFilterBinding.btnApply.setOnClickListener(v -> {
                String added_later = null;
                String city_id = null;
                if (mvvm.getAdded_later().getValue() != null) {
                    added_later = mvvm.getAdded_later().getValue();
                }

                if (mvvm.getCityData().getValue() != null) {
                    city_id = mvvm.getCityData().getValue().getId();
                }
                mvvm.search(getUserSetting().getCountry(), binding.edtSearch.getText().toString().trim(), added_later, city_id,getLang());

                sheetDialog.dismiss();
            });
            spinnerLastDayAdapter = new SpinnerLastDayAdapter(activity);

        }

        if (mvvm.getCityData().getValue()!=null||mvvm.getAdded_later().getValue()!=null){
            sheetFilterBinding.btnClear.setVisibility(View.VISIBLE);
        }else {
            sheetFilterBinding.btnClear.setVisibility(View.GONE);

        }
        sheetFilterBinding.setCity(mvvm.getCityData().getValue() != null ? mvvm.getCityData().getValue().getName() : "");
        spinnerLastDayAdapter.updateList(mvvm.getLastDays(activity).getValue());
        sheetFilterBinding.spinner.setAdapter(spinnerLastDayAdapter);
        sheetFilterBinding.spinner.setSelection(mvvm.getFilterDayPos().getValue() != null ? mvvm.getFilterDayPos().getValue() : 0);
        sheetFilterBinding.btnClear.setOnClickListener(v -> {
            mvvm.getCityData().setValue(null);
            mvvm.getFilterDayPos().setValue(0);
            mvvm.getAdded_later().setValue(null);
            sheetFilterBinding.spinner.setSelection(0);
            sheetFilterBinding.btnClear.setVisibility(View.GONE);
            sheetFilterBinding.setCity("");
            mvvm.search(getUserSetting().getCountry(), binding.edtSearch.getText().toString().trim(), null, null,getLang());
            sheetDialog.dismiss();
        });
        sheetDialog.show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        disposable.clear();
    }
}