package com.lost_found_it.uis.activity_home.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
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
import com.lost_found_it.model.ChatUserModel;
import com.lost_found_it.model.SingleAdModel;
import com.lost_found_it.mvvm.FragmentAdDetailsMvvm;
import com.lost_found_it.mvvm.GeneralMvvm;
import com.lost_found_it.uis.activity_base.BaseFragment;
import com.lost_found_it.uis.activity_chat.ChatActivity;
import com.lost_found_it.uis.activity_home.HomeActivity;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

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
    private static final int REQUEST_PHONE_CALL = 1;
    private Intent intent;
    private CompositeDisposable disposable = new CompositeDisposable();

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
        binding.setUserModel(getUserModel());

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
        generalMvvm.getOnAdDetailsSelected().observe(activity, ad_id -> {
            if (ad_id != null) {
                if (this.adModel != null) {
                    if (!this.adModel.getId().equals(ad_id)) {
                        binding.setModel(null);
                        String device_id = getUserSetting().getId();
                        if (getUserModel() != null) {
                            device_id = device_id + "_" + getUserModel().getData().getUser().getId();
                        }
                        mvvm.getData(getUserModel(),getUserSetting().getCountry(), device_id, ad_id);

                    } else {
                        updateUi(mvvm.getOnDataSuccess().getValue());
                    }


                } else {
                    String device_id = getUserSetting().getId();
                    if (getUserModel() != null) {
                        device_id = device_id + "_" + getUserModel().getData().getUser().getId();
                    }
                    mvvm.getData(getUserModel(),getUserSetting().getCountry(), device_id, ad_id);

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
        mvvm.getOnFollowDataSuccess().observe(activity, isFollowed -> {
            adModel.setIs_followed(isFollowed);
            binding.setModel(adModel);
        });

        mvvm.getOnLoveDataSuccess().observe(activity, isLove -> {
            adModel.setIs_loved(isLove);
            binding.setModel(adModel);
        });
        mvvm.getOnBadDataSuccess().observe(activity, isBad -> {
            adModel.setIs_bad(isBad);
            binding.setModel(adModel);
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
                mvvm.getData(getUserModel(),getUserSetting().getCountry(), device_id, adModel.getId());

            } else {
                binding.swipeRefresh.setRefreshing(false);
            }

        });



        binding.call.setOnClickListener(v -> {
            String phone = adModel.getPhone_code() + adModel.getPhone();
            intent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));

            if (intent != null) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);
                    } else {
                        startActivity(intent);
                    }
                } else {
                    startActivity(intent);
                }
            } else {
                Toast.makeText(activity, getResources().getString(R.string.not_avail_now), Toast.LENGTH_SHORT).show();

                // Common.CreateAlertDialog(QuestionsActivity.this, getResources().getString(R.string.phone_not_found));
            }
        });

        binding.whatsapp.setOnClickListener(v -> {
            String whatsApp = adModel.getPhone_code() + adModel.getWhatsapp();

            String url = "https://api.whatsapp.com/send?phone=" + whatsApp;
            try {
                PackageManager pm = activity.getPackageManager();
                pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            } catch (PackageManager.NameNotFoundException e) {
                Toast.makeText(activity, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }
        });

        binding.chat.setOnClickListener(v -> {
            String phone = adModel.getPhone_code() + adModel.getPhone();
            String name = adModel.getUser().getFirst_name() + " " + adModel.getUser().getLast_name();
            ChatUserModel model = new ChatUserModel(adModel.getUser().getId(), name, phone, adModel.getUser().getImage(), adModel.getId(), null, adModel);
            Intent intent = new Intent(activity, ChatActivity.class);
            intent.putExtra("data", model);
            startActivity(intent);
        });

        binding.checkboxLove.setOnClickListener(v -> mvvm.followUnFollow(activity,getUserSetting().getCountry(), getUserModel(), adModel.getId(), String.valueOf(binding.checkboxLove.isChecked()),"love"));

        binding.checkboxBad.setOnClickListener(v -> mvvm.followUnFollow(activity,getUserSetting().getCountry(), getUserModel(), adModel.getId(), String.valueOf(binding.checkboxBad.isChecked()),"bad"));



        binding.llFollow.setOnClickListener(v -> {
            mvvm.followUnFollow(activity,getUserSetting().getCountry(), getUserModel(), adModel.getId(), adModel.getIs_followed(),"follow");
        });
    }

    private void updateUi(SingleAdModel.Data singleAdModel) {
        if (singleAdModel != null) {
            this.adModel = singleAdModel.getAd();

            if (adModel.getImages().size() > 0) {
                sliderAdapter.updateList(adModel.getImages());
                binding.indicator.setCount(adModel.getImages().size());

                if (adModel.getImages().size() > 1) {
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

            if (getUserModel()!=null){
                if (adModel.getUser().getId().equals(getUserModel().getData().getUser().getId())){
                    binding.checkboxBad.setVisibility(View.GONE);
                    binding.checkboxLove.setVisibility(View.GONE);
                }else{
                    binding.checkboxBad.setVisibility(View.VISIBLE);
                    binding.checkboxLove.setVisibility(View.VISIBLE);
                }


            }else {

                binding.checkboxBad.setVisibility(View.GONE);
                binding.checkboxLove.setVisibility(View.GONE);
            }
        }


    }

    private void startSliderTimer() {
        if (timer != null && timerTask != null) {
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
        mvvm.getData(getUserModel(),getUserSetting().getCountry(), device_id, adModel.getId());
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
        disposable.clear();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_PHONE_CALL: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (activity.checkSelfPermission(Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                            // TODO: Consider calling
                            //    Activity#requestPermissions
                            // here to request the missing permissions, and then overriding
                            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                            //                                          int[] grantResults)
                            // to handle the case where the user grants the permission. See the documentation
                            // for Activity#requestPermissions for more details.
                            return;
                        }
                    }
                    startActivity(intent);
                } else {

                }
                return;
            }
        }
    }


}