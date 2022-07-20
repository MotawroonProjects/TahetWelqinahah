package com.lost_found_it.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lost_found_it.model.CityDataModel;
import com.lost_found_it.model.CityModel;
import com.lost_found_it.model.NotificationDataModel;
import com.lost_found_it.model.NotificationModel;
import com.lost_found_it.model.UserModel;
import com.lost_found_it.remote.Api;
import com.lost_found_it.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class ActivityCitiesMvvm extends AndroidViewModel {
    private static final String TAG = "ActivityCityMvvm";

    private MutableLiveData<List<CityModel>> onDataSuccess;

    private MutableLiveData<Boolean> isLoading;

    private CompositeDisposable disposable = new CompositeDisposable();


    public ActivityCitiesMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<List<CityModel>> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }




    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoading == null) {
            isLoading = new MutableLiveData<>();
        }
        return isLoading;
    }

    //_________________________hitting api_________________________________

    public void getCities(String country, String lang,String governorate_id) {
        if (getOnDataSuccess().getValue()!=null) {
            getOnDataSuccess().getValue().clear();
        }
        getIsLoading().postValue(true);
        Api.getService(Tags.base_url)
                .getCities(country,governorate_id,lang)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<CityDataModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<CityDataModel> response) {
                        getIsLoading().setValue(false);
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getCode() == 200) {
                                    getOnDataSuccess().postValue(response.body().getData());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.d(TAG, "onError: ", e);
                        getIsLoading().postValue(false);
                    }
                });

    }

    public void search(String query,String country,String lang,String governorate_id) {
        Log.e("search",query+"_"+getOnDataSuccess().getValue().size()+"____");
        if (query.isEmpty()){
           getCities(country, lang,governorate_id);
        }else {
            if (getOnDataSuccess().getValue() != null) {
                Observable.fromIterable(getOnDataSuccess().getValue())
                        .filter(cityModel -> cityModel.getName().contains(query))
                        .toList()
                        .toObservable()
                        .subscribe(new Observer<List<CityModel>>() {
                            @Override
                            public void onSubscribe(Disposable d) {
                                disposable.add(d);
                            }

                            @Override
                            public void onNext(List<CityModel> cityModels) {
                                getOnDataSuccess().postValue(cityModels);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
            }else {
                getOnDataSuccess().postValue(new ArrayList<>());
            }
        }

    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

    }

}
