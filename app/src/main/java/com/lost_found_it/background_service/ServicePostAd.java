package com.lost_found_it.background_service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.lost_found_it.R;
import com.lost_found_it.model.AddAdModel;
import com.lost_found_it.model.SingleAd;
import com.lost_found_it.model.SingleAdModel;
import com.lost_found_it.model.UserModel;
import com.lost_found_it.preferences.Preferences;
import com.lost_found_it.remote.Api;
import com.lost_found_it.share.App;
import com.lost_found_it.share.Common;
import com.lost_found_it.tags.Tags;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Response;

public class ServicePostAd extends Service {
    private CompositeDisposable disposable = new CompositeDisposable();
    private AddAdModel model;
    private Preferences preferences;
    private UserModel userModel;
    private String action;
    private String country = "";


    @Override
    public void onCreate() {
        super.onCreate();
        preferences = Preferences.getInstance();
        userModel = preferences.getUserData(this);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        model = (AddAdModel) intent.getSerializableExtra("data");
        country = intent.getStringExtra("country");
        action = intent.getStringExtra("action");
        createNotification();
        if (action.equals("add")) {
            postAds();
        } else {
            updateAds();
        }

        return START_STICKY;
    }

    private void postAds() {
        Log.e("token",userModel.getData().getAccess_token()+"____");
        RequestBody country_part = Common.getRequestBodyText(country);
        RequestBody title_part = Common.getRequestBodyText(model.getTitle());
        RequestBody description_part = Common.getRequestBodyText(model.getDescription());
        RequestBody category_id_part = Common.getRequestBodyText(model.getCategory_id());
        RequestBody sub_category_id_part = Common.getRequestBodyText(model.getSub_category_id());
        RequestBody address_part = Common.getRequestBodyText(model.getAddress());
        RequestBody lat_part = Common.getRequestBodyText(model.getLat() + "");
        RequestBody lng_part = Common.getRequestBodyText(model.getLng() + "");
        RequestBody phone_code_part = Common.getRequestBodyText(model.getPhone());
        RequestBody phone_part = Common.getRequestBodyText(model.getPhone());
        RequestBody whatsapp_part = Common.getRequestBodyText(model.getWhatsapp());
        RequestBody type_part = Common.getRequestBodyText(model.getAd_type());
        RequestBody place_type_part = Common.getRequestBodyText(model.getPlace_type());

        Api.getService(Tags.base_url).addAds("Bearer " + userModel.getData().getAccess_token(), country_part, title_part, description_part, category_id_part, sub_category_id_part, address_part, lat_part, lng_part, whatsapp_part, phone_code_part, phone_part, type_part, place_type_part, getImages())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SingleAd>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SingleAd> response) {
                        Log.e("err",response.body().getCode()+"__"+response.body().getMessage().toString()+"__");

                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null){
                            EventBus.getDefault().post(response.body().getData());
                        }else {
                            try {
                                Log.e("err",response.errorBody().string()+"__"+response.code());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        stopSelf();


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        stopSelf();
                        Log.e("adsError", e.toString());
                    }
                });

    }

    private void updateAds() {
        RequestBody country_part = Common.getRequestBodyText(country);
        RequestBody ad_id_part = Common.getRequestBodyText(model.getAd_id());

        RequestBody title_part = Common.getRequestBodyText(model.getTitle());
        RequestBody description_part = Common.getRequestBodyText(model.getDescription());
        RequestBody category_id_part = Common.getRequestBodyText(model.getCategory_id());
        RequestBody sub_category_id_part = Common.getRequestBodyText(model.getSub_category_id());
        RequestBody address_part = Common.getRequestBodyText(model.getAddress());
        RequestBody lat_part = Common.getRequestBodyText(model.getLat() + "");
        RequestBody lng_part = Common.getRequestBodyText(model.getLng() + "");
        RequestBody phone_code_part = Common.getRequestBodyText(model.getPhone());
        RequestBody phone_part = Common.getRequestBodyText(model.getPhone());
        RequestBody whatsapp_part = Common.getRequestBodyText(model.getWhatsapp());
        RequestBody type_part = Common.getRequestBodyText(model.getAd_type());
        RequestBody place_type_part = Common.getRequestBodyText(model.getPlace_type());


        Api.getService(Tags.base_url).updateAds("Bearer " + userModel.getData().getAccess_token(), country_part, ad_id_part, title_part, description_part, category_id_part, sub_category_id_part, address_part, lat_part, lng_part, whatsapp_part, phone_code_part, phone_part, type_part, place_type_part, getImages())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleObserver<Response<SingleAd>>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        disposable.add(d);
                    }

                    @Override
                    public void onSuccess(@NonNull Response<SingleAd> response) {
                        Log.e("err",response.body().getCode()+"__"+response.body().getMessage().toString()+"__");

                        if (response.isSuccessful()&&response.body()!=null&&response.body().getData()!=null){
                            EventBus.getDefault().post(true);

                        }else {
                            try {
                                Log.e("err",response.errorBody().string()+"__"+response.code());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        stopSelf();


                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        stopSelf();

                        Log.e("chatError", e.toString());
                    }
                });

    }

    private List<MultipartBody.Part> getImages() {
        List<MultipartBody.Part> parts = new ArrayList<>();
        for (String uri : model.getImages()) {
            MultipartBody.Part part = Common.getMultiPartImage(this, Uri.parse(uri), "images[]");
            parts.add(part);
        }
        return parts;
    }

    private void createNotification() {
        String title = "Add ad";
        String content = "Uploading...";
        Intent cancelIntent = new Intent(this, BroadCastCancelNotificationImages.class);
        PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(this, 0, cancelIntent, PendingIntent.FLAG_MUTABLE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, App.CHANNEL_ID);
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setCategory(Notification.CATEGORY_SERVICE);
        builder.setOngoing(false);
        builder.setAutoCancel(false);
        builder.addAction(0, "Dismiss", cancelPendingIntent);
        builder.setProgress(100, 100, true);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(Tags.not_ad_id, builder.build());

        } else {
            manager.notify(Tags.not_ad_tag, Tags.not_ad_id, builder.build());

        }

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.cancel(Tags.not_ad_tag, Tags.not_ad_id);
    }
}
