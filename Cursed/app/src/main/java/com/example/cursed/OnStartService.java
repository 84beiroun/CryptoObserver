package com.example.cursed;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class OnStartService extends Service implements AsyncResponse {
    private final int UPDATE_INTERVAL = 60 * 1000;
    private final Timer timer = new Timer();
    SharedPreferences sharedPref;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ScheduledExecutorService scheduler =
                Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate
                (new TimerTask() {
                    public void run() {
                        sharedPref = getApplicationContext().getSharedPreferences("currName", Context.MODE_PRIVATE);
                        NetworkTask nT = new NetworkTask();
                        nT.fullUrl = sharedPref.getString("requestCursed", null);
                        nT.delegate = OnStartService.this;
                        nT.execute();
                    }

                }, 0, 5, TimeUnit.SECONDS);


        return START_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void processFinish(String output, String name, String symbol, float value, int code) {
        if (code == 0) {
            NotificationCompat.Builder mBuilder =
                    new NotificationCompat.Builder(getApplicationContext(), "notify_001");
            mBuilder.setSmallIcon(R.drawable.icon);
            mBuilder.setContentTitle("Info about Cryptocurrency:");
            mBuilder.setContentText(output);
            mBuilder.setPriority(Notification.PRIORITY_LOW);
            NotificationManager nM = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            String channelId = "id1";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel one",
                    NotificationManager.IMPORTANCE_LOW);
            nM.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
            nM.notify(0, mBuilder.build());
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("lastCurr", output);
            editor.apply();
        }
    }
}
