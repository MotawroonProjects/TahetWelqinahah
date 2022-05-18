package com.lost_found_it.background_service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class BroadCastCancelNotificationImages extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent intent1 = new Intent(context,ServicePostAd.class);
        context.stopService(intent1);
    }
}
