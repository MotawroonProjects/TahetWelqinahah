package com.lost_found_it.share;


import android.content.Context;

import androidx.multidex.MultiDexApplication;

import com.lost_found_it.language.Language;


public class App extends MultiDexApplication {

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(Language.updateResources(newBase,"ar"));
    }


    @Override
    public void onCreate() {
        super.onCreate();

    }
}

