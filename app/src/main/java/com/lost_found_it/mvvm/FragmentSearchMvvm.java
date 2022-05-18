package com.lost_found_it.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lost_found_it.model.AdModel;
import com.lost_found_it.model.AdsDataModel;
import com.lost_found_it.model.UserModel;
import com.lost_found_it.remote.Api;
import com.lost_found_it.tags.Tags;

import java.io.IOException;
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

    private MutableLiveData<Boolean> isLoading;
    private MutableLiveData<List<AdModel>> onSearchSuccess;
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

    public void search(String country,String search){
        getIsLoading().setValue(true);

        Api.getService(Tags.base_url)
                .search(country,search)
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