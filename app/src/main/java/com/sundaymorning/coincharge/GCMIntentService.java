package com.sundaymorning.coincharge;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.util.Random;

public class GCMIntentService extends IntentService {

    private NotificationManager mNotificationManager = null;

    public GCMIntentService() {
        super("GCMIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);
        String messageType = gcm.getMessageType(intent);
        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                String msg = intent.getStringExtra("msg");
                String type = intent.getStringExtra("APtype");

                if (msg != null && !msg.isEmpty()) {
                    //관리자
                    sendNotification(intent.getStringExtra("msg"), intent.getStringExtra("sound"));
                } else {

                    //서버

                    if (type != null && !type.isEmpty()) {
                        if (type.equals("4")) {
                            //수동 - 푸쉬소리나야함.

                            String dataMsg = intent.getStringExtra("APmsg");
                            String ticker = intent.getStringExtra("APticker");
                            String title = intent.getStringExtra("APtitle");

                            if (dataMsg == null) dataMsg = "";
                            if (ticker == null) ticker = "";
                            if (title == null) title = "";

                            sendNotification(dataMsg, ticker, title, true);
                        } else {
                            sendNotification(intent.getStringExtra("APmsg"), intent.getStringExtra("APticker"), intent.getStringExtra("APtitle"), false);
                        }
                    }
                }
            }
        }
        GCMBroadcastReceiver.completeWakefulIntent(intent);
    }

    private void sendNotification(String msg, String sound) {

        if (msg == null || TextUtils.isEmpty(msg))
            return;

        PushWakeLock.acquireCpuWakeLock(this);

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, MainActivity.class), 0);

        int defaults = Notification.DEFAULT_LIGHTS;

        if (sound != null && sound.equals("default")) {
            //소리//매너모드인 경우 진동이 울림//
            defaults = defaults | Notification.DEFAULT_ALL;
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(getString(R.string.app_name))
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setTicker(msg).setContentText(msg).setDefaults(defaults)
                .setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        Random randInt = new Random();
        int id = randInt.nextInt(10000) + 900001;
        mNotificationManager.notify(id, mBuilder.build());

    }

    private void sendNotification(String msg, String ticker, String title, boolean isSound) {

        if (msg == null || TextUtils.isEmpty(msg))
            return;

        if (ticker == null || TextUtils.isEmpty(ticker))
            return;

        if (title == null || TextUtils.isEmpty(title))
            return;

        if (isSound) {
            PushWakeLock.acquireCpuWakeLock(this);
        }

        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, new Intent(this, MainActivity.class), 0);

        int defaults = Notification.DEFAULT_LIGHTS;

        if (isSound) {
            //소리//매너모드인 경우 진동이 울림//
            defaults = defaults | Notification.DEFAULT_ALL;
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(
                this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setContentTitle(title)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(msg))
                .setTicker(ticker).setContentText(msg).setDefaults(defaults)
                .setAutoCancel(true);
        mBuilder.setContentIntent(contentIntent);
        Random randInt = new Random();
        int id = randInt.nextInt(10000) + 900001;
        mNotificationManager.notify(id, mBuilder.build());
    }

}
