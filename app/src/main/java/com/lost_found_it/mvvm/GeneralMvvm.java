package com.lost_found_it.mvvm;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.messaging.FirebaseMessaging;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.model.StatusResponse;
import com.lost_found_it.model.UserModel;
import com.lost_found_it.remote.Api;
import com.lost_found_it.tags.Tags;

import java.io.IOException;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class GeneralMvvm extends AndroidViewModel {
    private MutableLiveData<Integer> mainNavigation;
    private MutableLiveData<Boolean> mainNavigationBackPress;
    private MutableLiveData<Boolean> onUserLoggedIn;
    private MutableLiveData<Boolean> onUserLoggedOut;
    private MutableLiveData<Boolean> onCountrySuccess;
    private MutableLiveData<AdModel> onAdDetailsSelected;
    private MutableLiveData<UserModel> onTokenSuccess;
    private CompositeDisposable disposable = new CompositeDisposable();

    public GeneralMvvm(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<Integer> getMainNavigation() {
        if (mainNavigation == null) {
            mainNavigation = new MutableLiveData<>();

        }

        return mainNavigation;
    }

    public MutableLiveData<Boolean> getMainNavigationBackPress() {
        if (mainNavigationBackPress == null) {
            mainNavigationBackPress = new MutableLiveData<>();

        }

        return mainNavigationBackPress;
    }

    public MutableLiveData<Boolean> getOnUserLoggedIn() {
        if (onUserLoggedIn == null) {
            onUserLoggedIn = new MutableLiveData<>();

        }

        return onUserLoggedIn;
    }

    public MutableLiveData<Boolean> getOnUserLoggedOut() {
        if (onUserLoggedOut == null) {
            onUserLoggedOut = new MutableLiveData<>();

        }

        return onUserLoggedOut;
    }

    public MutableLiveData<Boolean> getOnCountrySuccess() {
        if (onCountrySuccess == null) {
            onCountrySuccess = new MutableLiveData<>();
        }
        return onCountrySuccess;
    }

    public MutableLiveData<AdModel> getOnAdDetailsSelected() {
        if (onAdDetailsSelected == null) {
            onAdDetailsSelected = new MutableLiveData<>();
        }
        return onAdDetailsSelected;
    }


    public MutableLiveData<UserModel> onTokenSuccess() {
        if (onTokenSuccess == null) {
            onTokenSuccess = new MutableLiveData<>();
        }

        return onTokenSuccess;
    }


    public void updateToken(UserModel userModel) {
        if (userModel == null) {
            return;
        }
       FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();
                Api.getService(Tags.base_url)
                        .updateFireBaseToken("Bearer "+userModel.getData().getAccess_token(), token, "android")
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new SingleObserver<Response<StatusResponse>>() {
                            @Override
                            public void onSubscribe(@NonNull Disposable d) {
                                disposable.add(d);
                            }

                            @Override
                            public void onSuccess(@NonNull Response<StatusResponse> response) {

                                if (response.isSuccessful()) {
                                    if (response.body() != null) {

                                        if (response.body().getCode() == 200) {
                                            userModel.getData().setFirebase_token(token);
                                            onTokenSuccess().setValue(userModel);
                                            Log.e("token", "updated");

                                        }
                                    }

                                } else {
                                    try {
                                        Log.e("error", response.errorBody().string() + "__" + response.code());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }

                            @Override
                            public void onError(@NonNull Throwable e) {
                                Log.e("token", e.toString());

                            }
                        });
            }
        });


    }

    @Override
    protected void onCleared() {
        super.onCleared();
        disposable.clear();
    }
}
