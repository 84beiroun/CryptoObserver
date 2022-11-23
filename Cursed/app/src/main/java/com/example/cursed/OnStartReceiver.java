package com.example.cursed;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class OnStartReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service = new Intent(context, OnStartService.class);
        context.startService(service);

    }
}
