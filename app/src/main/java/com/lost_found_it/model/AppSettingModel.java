package com.lost_found_it.model;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.ObservableField;


import java.io.Serializable;

public class AppSettingModel extends BaseObservable implements Serializable {
    private String country;
    private boolean isFirstTime;

    public String getCountry() {
        if (country==null){
            country = "eg";
        }
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public boolean isFirstTime() {
        return isFirstTime;
    }

    public void setFirstTime(boolean firstTime) {
        isFirstTime = firstTime;
    }
}