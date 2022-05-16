package com.lost_found_it.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.lost_found_it.model.AdModel;

public class GeneralMvvm extends AndroidViewModel {
    private MutableLiveData<Integer> mainNavigation;
    private MutableLiveData<Boolean> mainNavigationBackPress;
    private MutableLiveData<Boolean> onUserLoggedIn;
    private MutableLiveData<Boolean> onUserLoggedOut;
    private MutableLiveData<Boolean> onCountrySuccess;
    private MutableLiveData<AdModel> onAdDetailsSelected;

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
}
