package com.lost_found_it.mvvm;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lost_found_it.R;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.model.AdsDataModel;
import com.lost_found_it.model.CityModel;
import com.lost_found_it.model.UserModel;
import com.lost_found_it.remote.Api;
import com.lost_found_it.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.BiConsumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentSearchMvvm extends AndroidViewModel {
    private CompositeDisposable disposable = new CompositeDisposable();
    private MutableLiveData<Integer> filterDayPos;
    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<List<String>> lastDays;
    private MutableLiveData<List<AdModel>> onSearchSuccess;
    private MutableLiveData<CityModel> cityData;
    private MutableLiveData<String> added_later;
    public FragmentSearchMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading==null){
            isLoading=new MutableLiveData<>();
        }
        return isLoading;
    }

    public MutableLiveData<List<AdModel>> getOnSearchSuccess() {
        if (onSearchSuccess==null){
            onSearchSuccess=new MutableLiveData<>();
        }
        return onSearchSuccess;
    }



    public MutableLiveData<Integer> getFilterDayPos() {
        if (filterDayPos==null){
            filterDayPos=new MutableLiveData<>();
        }
        return filterDayPos;
    }

    public MutableLiveData<CityModel> getCityData() {
        if (cityData==null){
            cityData=new MutableLiveData<>();
        }
        return cityData;
    }
    public MutableLiveData<List<String>> getLastDays(Context context) {
        if (lastDays==null){
            lastDays=new MutableLiveData<>();
            List<String> days = Arrays.asList(context.getResources().getStringArray(R.array.lastDays));

            lastDays.setValue(days);
        }
        return lastDays;
    }

    public MutableLiveData<String> getAdded_later() {
        if (added_later==null){
            added_later=new MutableLiveData<>();
        }
        return added_later;
    }
    public void search(String country,String search,String added_later,String city_id,String lang){
        getIsLoading().setValue(true);

        Api.getService(Tags.base_url)
                .search(country,search,added_later,city_id,lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<AdsDataModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<AdsDataModel> response) {
//                        Log.e("respoonse",response.code()+" "+response.body().getCode()+" "+response.body().getMessage());
                        getIsLoading().setValue(false);
                        if (response.isSuccessful()){

                            if (response.body()!=null){
                                getOnSearchSuccess().setValue(response.body().getData());
                            }

                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getIsLoading().setValue(false);
                        Log.e("error",e.getMessage());
                    }
                });
    }
}
