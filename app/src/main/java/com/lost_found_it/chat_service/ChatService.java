package com.lost_found_it.chat_service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.lost_found_it.model.AddChatMessageModel;
import com.lost_found_it.model.AppSettingModel;
import com.lost_found_it.model.SingleMessageModel;
import com.lost_found_it.model.UserModel;
import com.lost_found_it.preferences.Preferences;
import com.lost_found_it.remote.Api;
import com.lost_found_it.share.Common;
import com.lost_found_it.tags.Tags;

import org.greenrobot.eventbus.EventBus;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class ChatService extends Service {
    private AddChatMessageModel model;
    private CompositeDisposable disposable = new CompositeDisposable();
    private Preferences preferences;
    private UserModel userModel;
    private AppSettingModel appSettingModel;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);
        appSettingModel = preferences.getUserSettingData(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            model = (AddChatMessageModel) intent.getSerializableExtra("data");
            sendMessage();
        }
        return START_STICKY;

    }

    public void sendMessage() {

        RequestBody msg_part = Common.getRequestBodyText(model.getText());
        RequestBody country_part = Common.getRequestBodyText(appSettingModel.getCountry());
        RequestBody ad_id_part = Common.getRequestBodyText(model.getAd_id());
        RequestBody room_id_part = null;
       Log.e("ad",model.getAd_id()+"__"+model.getRoom_id());
        if (model.getRoom_id()!=null){
            room_id_part = Common.getRequestBodyText(model.getRoom_id());

        }
        RequestBody msg_type_part = Common.getRequestBodyText(model.getType());
        MultipartBody.Part imagePart = null;


        if (model.getType().equals("file") && model.getImage() != null) {
            imagePart = Common.getMultiPartFromPath(model.getImage(), "file");
        }


        Api.getService(Tags.base_url).sendMessages("Bearer " + userModel.getData().getAccess_token(), country_part, ad_id_part, room_id_part, msg_type_part, msg_part, imagePart)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SingleMessageModel>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SingleMessageModel> response) {
                        try {
                            Log.e("jfjj", response.code() + "" + response.errorBody().string());
                        } catch (Exception e) {
                            //e.printStackTrace();
                        }
                        if (response.isSuccessful()) {
                            Log.e("response",response.body().getCode()+"__"+response.body().getMessage().toString());
                            if (response.body() != null && response.body().getCode() == 200) {
                                EventBus.getDefault().post(response.body().getData());
                                stopSelf();

                            }
                        }

                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Log.e("chatError", e.toString());
                    }
                });

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
