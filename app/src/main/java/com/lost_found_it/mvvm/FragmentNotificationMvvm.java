package com.lost_found_it.mvvm;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;


import com.lost_found_it.model.NotificationDataModel;
import com.lost_found_it.model.NotificationModel;
import com.lost_found_it.model.UserModel;
import com.lost_found_it.remote.Api;
import com.lost_found_it.tags.Tags;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class FragmentNotificationMvvm extends AndroidViewModel {
    private static final String TAG = "ActivityNotiMvvm";
    private Context context;

    private MutableLiveData<List<NotificationModel>> notificationLiveData;
    private MutableLiveData<Boolean> isLoadingLivData;

    private CompositeDisposable disposable = new CompositeDisposable();


    public FragmentNotificationMvvm(@NonNull Application application) {
        super(application);
        context = application.getApplicationContext();
    }

    public MutableLiveData<List<NotificationModel>> getNotification() {
        if (notificationLiveData == null) {
            notificationLiveData = new MutableLiveData<>();
        }
        return notificationLiveData;
    }


    public MutableLiveData<Boolean> getIsLoading() {
        if (isLoadingLivData == null) {
            isLoadingLivData = new MutableLiveData<>();
        }
        return isLoadingLivData;
    }

    //_________________________hitting api_________________________________

    public void getNotifications(String country,UserModel userModel) {
        isLoadingLivData.setValue(true);
if(userModel==null){
    isLoadingLivData.setValue(false);
    getNotification().setValue(new ArrayList<>());
}
else {
    Api.getService(Tags.base_url)
            .getNotifications("Bearer " + userModel.getData().getAccess_token(), country)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(new SingleObserver<Response<NotificationDataModel>>() {
                @Override
                public void onSubscribe(@NonNull Disposable d) {
                    disposable.add(d);
                }

                @Override
                public void onSuccess(@NonNull Response<NotificationDataModel> response) {
                    isLoadingLivData.setValue(false);
//                    Log.e("statuss", response.code() + " " + response.body().getCode() + " " + response.body().getMessage());
                    if (response.isSuccessful()) {
                        if (response.body() != null) {
                            if (response.body().getCode() == 200) {
                                notificationLiveData.setValue(response.body().getData());
                            }
                        }
                    }
                }

                @Override
                public void onError(@NonNull Throwable e) {
                    Log.d(TAG, "onError: ", e);
                }
            });
}
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();

    }

}
