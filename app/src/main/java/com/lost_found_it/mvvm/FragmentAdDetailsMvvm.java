package com.lost_found_it.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lost_found_it.model.AdModel;
import com.lost_found_it.model.AdsDataModel;
import com.lost_found_it.model.CategoryDataModel;
import com.lost_found_it.model.CategoryModel;
import com.lost_found_it.model.SingleAdModel;
import com.lost_found_it.model.SubCategoryModel;
import com.lost_found_it.remote.Api;
import com.lost_found_it.tags.Tags;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentAdDetailsMvvm extends AndroidViewModel {
    private final String TAG = FragmentAdDetailsMvvm.class.getName();
    public MutableLiveData<SingleAdModel.Data> onDataSuccess;

    public MutableLiveData<Boolean> isLoading;

    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentAdDetailsMvvm(@NonNull Application application) {
        super(application);


    }

    public MutableLiveData<SingleAdModel.Data> getOnDataSuccess() {
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

    public void getData(String country,String phone_token_id,String ad_id){
        getIsLoading().setValue(true);

        Api.getService(Tags.base_url)
                .getAdDetails(country,ad_id,phone_token_id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SingleAdModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<SingleAdModel> response) {
                        getIsLoading().setValue(false);

                        if (response.isSuccessful()){

                            if (response.body()!=null){
                                getOnDataSuccess().setValue(response.body().getData());
                            }

                        }else {
                            try {
                                Log.e(TAG,response.errorBody().string()+"___"+response.body().getMessage().toString());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        getIsLoading().setValue(false);

                        Log.e(TAG,e.getMessage());
                    }
                });
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
