package com.lost_found_it.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lost_found_it.model.AdsDataModel;
import com.lost_found_it.model.SettingDataModel;
import com.lost_found_it.remote.Api;
import com.lost_found_it.tags.Tags;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentSettingsMvvm extends AndroidViewModel {

    private MutableLiveData<SettingDataModel.Data> onDataSuccess;

    private CompositeDisposable disposable = new CompositeDisposable();

    public FragmentSettingsMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<SettingDataModel.Data> getOnDataSuccess() {
        if (onDataSuccess == null) {
            onDataSuccess = new MutableLiveData<>();
        }
        return onDataSuccess;
    }

    public void getSettings(Context context,String country){

        Api.getService(Tags.base_url)
                .getSettings(country)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SettingDataModel>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(Response<SettingDataModel> response) {
                        if (response.isSuccessful()){
                            if (response.body()!=null){
                                if (response.body().getCode()==200){
                                    getOnDataSuccess().setValue(response.body().getData());
                                }
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("error",e.getMessage());
                    }
                });
    }
}
