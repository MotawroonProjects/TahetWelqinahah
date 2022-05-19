package com.lost_found_it.preferences;

import android.content.Context;
import android.content.SharedPreferences;

import com.lost_found_it.model.AppSettingModel;
import com.lost_found_it.model.ChatUserModel;
import com.lost_found_it.model.UserModel;

import com.google.gson.Gson;

public class Preferences {

    private static Preferences instance = null;

    private Preferences() {
    }

    public static Preferences getInstance() {
        if (instance == null) {
            instance = new Preferences();
        }
        return instance;
    }

    public UserModel getUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = preferences.getString("user_data", "");
        UserModel userModel = gson.fromJson(user_data, UserModel.class);
        return userModel;
    }

    public void createUpdateUserData(Context context, UserModel userModel) {

        if (userModel!=null){
            SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String user_data = gson.toJson(userModel);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString("user_data", user_data);
            editor.apply();
        }else {
            clearUserData(context);
        }


    }

    public void createUpdateUserSetting(Context context, AppSettingModel appSettingModel) {
        SharedPreferences preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String user_data = gson.toJson(appSettingModel);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("setting_data", user_data);
        editor.apply();

    }


    public AppSettingModel getUserSettingData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("settings", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String setting_data = preferences.getString("setting_data", "");
        AppSettingModel appSettingModel = gson.fromJson(setting_data, AppSettingModel.class);
        if (appSettingModel == null) {
            appSettingModel = new AppSettingModel();
            appSettingModel.setFirstTime(true);
        }
        return appSettingModel;
    }


    public void clearUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();

    }

    public void create_update_room(Context context, ChatUserModel model) {
        SharedPreferences preferences = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        String data = new Gson().toJson(model);
        editor.putString("order_id", data);
        editor.apply();


    }

    public ChatUserModel getRoomId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        ChatUserModel model = new Gson().fromJson(preferences.getString("room_id",""),ChatUserModel.class);

        return model;
    }

    public void clearRoomId(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("room", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

}
