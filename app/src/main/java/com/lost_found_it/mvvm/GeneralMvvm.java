package com.lost_found_it.mvvm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class GeneralMvvm extends AndroidViewModel {
    private MutableLiveData<Integer> mainNavigation;
    private MutableLiveData<Boolean> mainNavigationBackPress;

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
}
