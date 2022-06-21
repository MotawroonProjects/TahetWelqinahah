package com.lost_found_it.uis.activity_cities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import com.lost_found_it.R;
import com.lost_found_it.adapter.CityAdapter;
import com.lost_found_it.adapter.RoomAdapter;
import com.lost_found_it.databinding.ActivityCitiesBinding;
import com.lost_found_it.model.CityModel;
import com.lost_found_it.mvvm.ActivityCitiesMvvm;
import com.lost_found_it.mvvm.ActivityContactUsMvvm;
import com.lost_found_it.uis.activity_base.BaseActivity;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CitiesActivity extends BaseActivity {
    private ActivityCitiesBinding binding;
    private ActivityCitiesMvvm mvvm;
    private CityAdapter adapter;
    private CompositeDisposable disposable = new CompositeDisposable();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cities);
        initView();
    }

    @SuppressLint("CheckResult")
    private void initView() {
        mvvm = ViewModelProviders.of(this).get(ActivityCitiesMvvm.class);

        setUpToolbar(binding.toolbar, getString(R.string.cities), R.color.white, R.color.black);

        binding.setLang(getLang());
        mvvm.getIsLoading().observe(this, loading -> {
            binding.recViewLayoutCity.swipeRefresh.setRefreshing(loading);

        });
        mvvm.getOnDataSuccess().observe(this, list -> {
            if (list.size() > 0) {
                adapter.updateList(list);
                binding.recViewLayoutCity.tvNoData.setVisibility(View.GONE);
            } else {
                binding.recViewLayoutCity.tvNoData.setVisibility(View.VISIBLE);
                adapter.updateList(new ArrayList<>());

            }
        });


        adapter = new CityAdapter(this);
        binding.recViewLayoutCity.recView.setLayoutManager(new LinearLayoutManager(this));
        binding.recViewLayoutCity.recView.setHasFixedSize(true);
        binding.recViewLayoutCity.recView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        binding.recViewLayoutCity.recView.setDrawingCacheEnabled(true);
        binding.recViewLayoutCity.recView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        binding.recViewLayoutCity.recView.setItemViewCacheSize(20);
        binding.recViewLayoutCity.recView.setAdapter(adapter);
        binding.recViewLayoutCity.swipeRefresh.setColorSchemeResources(R.color.colorPrimary);
        binding.recViewLayoutCity.tvNoData.setText(R.string.no_data_to_show);
        binding.recViewLayoutCity.swipeRefresh.setOnRefreshListener(() -> mvvm.getCities(getUserSetting().getCountry(), getLang()));
        mvvm.getCities(getUserSetting().getCountry(), getLang());

        Observable.create(emitter -> {
                    binding.edtSearch.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            emitter.onNext(s.toString());
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                })
                .debounce(1, TimeUnit.SECONDS)
                .distinctUntilChanged()
                .subscribe(query -> {
                    mvvm.search(query.toString(),getUserSetting().getCountry(),getLang());
                });


    }


    public void setItemData(CityModel cityModel) {
        Intent intent = getIntent();
        intent.putExtra("data", cityModel);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}