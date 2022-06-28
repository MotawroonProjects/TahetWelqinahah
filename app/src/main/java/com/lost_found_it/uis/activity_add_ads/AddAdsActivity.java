package com.lost_found_it.uis.activity_add_ads;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.lost_found_it.R;
import com.lost_found_it.adapter.MyPagerAdapter;
import com.lost_found_it.databinding.ActivityAddAdsBinding;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.model.AddAdModel;
import com.lost_found_it.model.SliderImages;
import com.lost_found_it.uis.activity_base.BaseActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class AddAdsActivity extends BaseActivity {
    private ActivityAddAdsBinding binding;
    private List<Fragment> fragments;
    private MyPagerAdapter adapter;
    private AdModel adModel;
    private AddAdModel addAdModel;
    private CompositeDisposable disposable = new CompositeDisposable();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_ads);
        getDataFromIntent();
        initView();

    }

    private void getDataFromIntent() {
        Intent intent = getIntent();
        if (intent.hasExtra("data")) {
            adModel = (AdModel) intent.getSerializableExtra("data");
        }
    }


    private void initView() {
        setUpToolbar(binding.toolBar, getString(R.string.post_your_ad), R.color.white, R.color.black);
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
                        LoadUiData();
                    }
                });


    }

    private void LoadUiData() {
        addAdModel = new AddAdModel();
        if (adModel != null) {

            addAdModel.setAd_id(adModel.getId());
            addAdModel.setTitle(adModel.getTitle());
            addAdModel.setDescription(adModel.getDescription());
            addAdModel.setCity_id(adModel.getCity_id() != null ? addAdModel.getCity_id() : "");
            addAdModel.setCategory_id(adModel.getCategory().getId());
            addAdModel.setAd_type(adModel.getType());
            addAdModel.setPlace_type(adModel.getPlace_type());
            addAdModel.setAddress(adModel.getAddress());
            addAdModel.setLat(Double.parseDouble(adModel.getLatitude()));
            addAdModel.setLng(Double.parseDouble(adModel.getLongitude()));
            addAdModel.setWhatsapp(adModel.getWhatsapp());
            addAdModel.setPhone_code(adModel.getPhone_code());
            addAdModel.setPhone(adModel.getPhone());
            addAdModel.setAgree_terms(true);
            addAdModel.setCountry(getUserSetting().getCountry());
            addAdModel.setAction("update");
            List<String> onlineImages = new ArrayList<>();
            for (SliderImages image : adModel.getImages()) {
                onlineImages.add(image.getImage());
            }
            addAdModel.setOnlineImages(onlineImages);
            if (adModel.getSubCategory() != null) {
                addAdModel.setSub_category_id(adModel.getSubCategory().getId());
                addAdModel.setHasSubCategory(true);
            } else {
                addAdModel.setSub_category_id("");
                addAdModel.setHasSubCategory(false);

            }
        }
        fragments = new ArrayList<>();
        fragments.add(FragmentAddAdStep1.newInstance(addAdModel));
        fragments.add(FragmentAddAdStep2.newInstance());
        adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments, null);
        binding.pager.setAdapter(adapter);
    }

    public void navigateToStep2(AddAdModel model) {
        binding.pager.setCurrentItem(1);
        FragmentAddAdStep2 fragmentAddAdStep2 = (FragmentAddAdStep2) adapter.getItem(1);
        fragmentAddAdStep2.updateModel(model);
    }

    public void navigateToStep1() {
        binding.pager.setCurrentItem(0);

    }

    @Override
    public void onBackPressed() {
        if (binding.pager.getCurrentItem() == 0) {
            super.onBackPressed();

        } else {
            navigateToStep1();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}