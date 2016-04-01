package com.care.antimosquito;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.care.core.Constants;
import com.care.core.SharedDataManager;

/**
 * Created by laliu on 2015/8/12.
 */
public class AntiMosquitoService extends Service {

    private static int NotificationId = 209;
    private NotificationManager mNotificationManager;

    private MediaPlayer mMediaPlayer;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        int index = SharedDataManager.getInstance().getCurrentModeIndex();
        mMediaPlayer = MediaPlayer.create(AntiMosquitoService.this, Constants.VoiceIds[index]);
        mMediaPlayer.setLooping(true);
        mMediaPlayer.start();
        //setNotification(R.mipmap.ic_launcher, NotificationId, getString(R.string.app_name), getString(R.string.notify_content));
    }

    @Override
    public void onDestroy() {
        try {
            mMediaPlayer.stop();
            mMediaPlayer.release();
            //deleteNotification(NotificationId);
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        super.onDestroy();
    }

    public void setNotification(int iconResourceId, int notificationId, String notificationTitle, String notificationText) {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent appIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setAutoCancel(true)
                .setContentTitle(notificationTitle)
                .setContentText(notificationText)
                .setContentIntent(appIntent)
                .setSmallIcon(iconResourceId)
                .setWhen(System.currentTimeMillis())
                .build();

        mNotificationManager.notify(notificationId, notification);
    }

    public void deleteNotification(int notificationId) {
        mNotificationManager.cancel(notificationId);
    }
}
