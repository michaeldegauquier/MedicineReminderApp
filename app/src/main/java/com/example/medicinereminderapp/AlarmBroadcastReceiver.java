package com.example.medicinereminderapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.NotificationCompat;

public class AlarmBroadcastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        showNotification(context, intent);
    }

    void showNotification(Context context, Intent intent) {
        String CHANNEL_ID = "medchannel";// The id of the channel.
        CharSequence name = context.getResources().getString(R.string.app_name);// The user-visible name of the channel.
        NotificationCompat.Builder mBuilder;

        Intent notificationIntent = new Intent(context, MainActivity.class);
        Bundle bundle = new Bundle();
        notificationIntent.putExtras(bundle);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

        Bundle returnBundle = intent.getExtras();
        String title = returnBundle.getString("medicineName");
        int notificationId = returnBundle.getInt("notificationId");
        String amount = returnBundle.getString("amount");
        boolean cancelAll = returnBundle.getBoolean("cancelAll");

        PendingIntent contentIntent = PendingIntent.getActivity(context, notificationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >= 26) {
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(mChannel);
            mBuilder = new NotificationCompat.Builder(context, CHANNEL_ID)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLights(Color.BLUE, 300, 300)
                    .setChannelId(CHANNEL_ID)
                    .setContentTitle(title);
        } else {
            mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setPriority(Notification.PRIORITY_HIGH)
                    .setContentTitle(title);
        }

        mBuilder.setContentIntent(contentIntent);
        mBuilder.setContentText(amount);
        mBuilder.setAutoCancel(true);

        // if cancelAll is true -> both notifications (dateBegin, dateEnd) will be canceled
        if (cancelAll) {
            RemindersActivity.cancelNotification(context, notificationId);
            RemindersActivity.cancelNotification(context, -notificationId);
        }

        // if cancelAll is false
        if (!cancelAll) {
            mNotificationManager.notify(notificationId, mBuilder.build());
        }
    }
}

// Stackoverflow. Android - Notification at specific time every day. Geraadpleegd via
// https://stackoverflow.com/questions/51510509/android-notification-at-specific-time-every-day
// Geraadpleegd op 23 juli 2020