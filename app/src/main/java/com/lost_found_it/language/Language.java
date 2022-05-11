package com.lost_found_it.language;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.preference.PreferenceManager;

import java.util.Locale;

public class Language {

    public static void setNewLocale(Context c, String language) {
        updateResources(c, language);
    }
    public static Context updateResources(Context context, String language) {
        Locale locale = new Locale(language);
        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        res.updateConfiguration(config, res.getDisplayMetrics());

        config.setLocale(locale);
        context = context.createConfigurationContext(config);

        return context;


    }

}