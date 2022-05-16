package com.lost_found_it.uis.activity_country;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;

import com.lost_found_it.R;
import com.lost_found_it.databinding.ActivityCountryBinding;
import com.lost_found_it.databinding.ActivitySplashBinding;
import com.lost_found_it.model.AppSettingModel;
import com.lost_found_it.uis.activity_base.BaseActivity;
import com.lost_found_it.uis.activity_home.HomeActivity;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class CountryActivity extends BaseActivity {
    private ActivityCountryBinding binding;
    private String country;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_country);
        initView();

    }

    private void initView() {
        country = getUserSetting().getCountry();
        binding.setCountry(country);
        binding.llEg.setOnClickListener(v -> {
            country = "eg";
            binding.setCountry(country);
        });

        binding.llSa.setOnClickListener(v -> {
            country = "sa";
            binding.setCountry(country);

        });

        binding.llUae.setOnClickListener(v -> {
            country = "ae";
            binding.setCountry(country);

        });

        binding.btnConfirm.setOnClickListener(v -> {
            AppSettingModel model = getUserSetting();
            model.setCountry(country);
            model.setFirstTime(false);
            setUserSetting(model);
            setResult(RESULT_OK);
            finish();
        });
    }


}