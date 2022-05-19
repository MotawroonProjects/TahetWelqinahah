package com.lost_found_it.notifications;


import android.app.ActivityManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.lost_found_it.R;
import com.lost_found_it.model.AdModel;
import com.lost_found_it.model.ChatUserModel;
import com.lost_found_it.model.MessageModel;
import com.lost_found_it.model.NotiFire;
import com.lost_found_it.model.StatusResponse;
import com.lost_found_it.model.UserModel;
import com.lost_found_it.preferences.Preferences;
import com.lost_found_it.remote.Api;
import com.lost_found_it.share.App;
import com.lost_found_it.tags.Tags;
import com.lost_found_it.uis.activity_chat.ChatActivity;
import com.lost_found_it.uis.activity_home.HomeActivity;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.Map;

import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;


public class FireBaseNotifications extends FirebaseMessagingService {
    private CompositeDisposable disposable = new CompositeDisposable();
    private Map<String, String> map;

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        map = remoteMessage.getData();

        for (String key : map.keySet()) {
            Log.e("Key=", key + "_value=" + map.get(key));
        }

        manageNotification(map);


    }

    private void manageNotification(Map<String, String> map) {
        String title = map.get("title");
        String body = map.get("body");
        String notification_type = map.get("notification_type");

        String sound_Path = "";
        Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        sound_Path = uri.toString();
        Intent cancelIntent = new Intent(this, BroadcastCancelNotification.class);
        PendingIntent cancelPending = PendingIntent.getBroadcast(this, 0, cancelIntent, 0);

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(this, App.CHANNEL_ID)
                .setAutoCancel(true)
                .setOngoing(false)
                .setChannelId(App.CHANNEL_ID_CHAT)
                .setDeleteIntent(cancelPending)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setSound(Uri.parse(sound_Path), AudioManager.STREAM_NOTIFICATION);


        if (notification_type.equals("chat")) {
            title = getChatUserModel(map).getUser_name();
            String image = map.get("file");

            if (image != null && !image.isEmpty() && !map.get("type").equals("text")) {
                body = getString(R.string.attachment_sent);
            } else {
                body = map.get("message");
            }


            notificationCompat.setContentTitle(title);
            notificationCompat.setStyle(new NotificationCompat.BigTextStyle().bigText(body));

            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("data", getChatUserModel(map));
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addNextIntent(intent);
            notificationCompat.setContentIntent(taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT));

            ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
            String className = activityManager.getRunningTasks(1).get(0).topActivity.getClassName();
            if (className.equals("com.lost_found_it.uis.activity_chat.ChatActivity")) {

                EventBus.getDefault().post(getMessageModel(map));

            } else {


                if (getChatUserModel(map).getUser_image() != null && !getChatUserModel(map).getUser_image().isEmpty()) {

                    Glide.with(this)
                            .asBitmap()
                            .load(Uri.parse(getChatUserModel(map).getUser_image()))
                            .into(new SimpleTarget<Bitmap>() {
                                @Override
                                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                    notificationCompat.setLargeIcon(resource);
                                    manager.notify(Tags.not_id, notificationCompat.build());

                                }
                            });
                } else {
                    Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                    notificationCompat.setLargeIcon(bitmap);
                    manager.notify(Tags.not_id, notificationCompat.build());
                }


            }


        }else {

            notificationCompat.setContentTitle(title);
            notificationCompat.setContentText(body);
            notificationCompat.setStyle(new NotificationCompat.BigTextStyle().bigText(body));

            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("from_fire", true);

            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(this);
            taskStackBuilder.addNextIntent(intent);
            notificationCompat.setContentIntent(taskStackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT));

            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
            notificationCompat.setLargeIcon(bitmap);
            manager.notify(Tags.not_id, notificationCompat.build());
            EventBus.getDefault().post(new NotiFire(""));

        }


    }



    private MessageModel getMessageModel(Map<String, String> map) {
        String id = map.get("id");
        String user_id = map.get("user_id");
        String type = map.get("type");
        String message = map.get("message");
        String file = map.get("file");
        String time = map.get("time");
        String date = map.get("date");

        if (file == null) {
            file = "";

        }
        UserModel.User user = new Gson().fromJson(map.get("user"), UserModel.User.class);

        MessageModel messageModel = new MessageModel(id, user_id, type, message, file, time, date, user);

        return messageModel;
    }

    private ChatUserModel getChatUserModel(Map<String, String> map) {
        String ad_id = map.get("ad_id");
        String room_id = map.get("room_id");

        ChatUserModel model;
        UserModel.User user = new Gson().fromJson(map.get("user"), UserModel.User.class);
        AdModel adModel = new Gson().fromJson(map.get("ad"), AdModel.class);

        if (!user.getId().equals(getUserModel().getData().getUser().getId())) {
            String user_id = user.getId();
            String user_name = user.getFirst_name() + " " + user.getLast_name();
            String user_phone = user.getPhone_code() + user.getPhone();
            String user_image = user.getImage();

            model = new ChatUserModel(user_id, user_name, user_phone, user_image,ad_id,room_id,adModel);

        } else {
            String user_id = getUserModel().getData().getUser().getId();
            String user_name = getUserModel().getData().getUser().getFirst_name() + " " + getUserModel().getData().getUser().getLast_name();
            String user_phone = getUserModel().getData().getUser().getPhone_code() + getUserModel().getData().getUser().getPhone();
            String user_image = getUserModel().getData().getUser().getImage();

            model = new ChatUserModel(user_id, user_name, user_phone, user_image,ad_id,room_id,adModel);

        }


        return model;

    }
    @Override
    public void onNewToken(@NonNull String s) {
        super.onNewToken(s);
        if (getUserModel() == null) {
            return;
        }
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                String token = task.getResult();

                Api.getService(Tags.base_url)
                        .updateFireBaseToken("Bearer " + getUserModel().getData().getAccess_token(), getCoutry(), token, "android")
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
                                    Log.e("errToken", response.body().getMessage().toString() + "__");
                                    if (response.body() != null) {

                                        if (response.body().getCode() == 200) {

                                            UserModel userModel = getUserModel();
                                            userModel.getData().setFirebase_token(token);
                                            setUserModel(userModel);
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

    public UserModel getUserModel() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserData(this);
    }


    public void setUserModel(UserModel userModel) {
        Preferences preferences = Preferences.getInstance();
        preferences.createUpdateUserData(this, userModel);

    }


    public ChatUserModel getRoomId() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getRoomId(this);
    }

    public String getCoutry() {
        Preferences preferences = Preferences.getInstance();
        return preferences.getUserSettingData(this).getCountry();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        disposable.clear();
    }
}
