package com.example.pushwords.handlers;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import com.example.pushwords.data.Preference;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    private static final Long DELAY_SECONDS = 0L;
    private static final Long PERIOD_SECONDS = 5L * 60L;

    private Timer timer;
    private TimerTask timerTask;
    private RepeatWordsNotification repeatWordsNotification;
    private final Handler handler = new Handler();


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        repeatWordsNotification = new RepeatWordsNotification(getApplicationContext());

        startTimer();

        return START_STICKY;
    }
    @Override
    public void onDestroy() {
        stopTimerTask();

        super.onDestroy();
    }

    public void startTimer() {
        timer = new Timer();

        initializeTimerTask();

        timer.schedule(timerTask,
                DELAY_SECONDS * 1000L,
                PERIOD_SECONDS * 1000L);
    }
    public void stopTimerTask() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }
    public void initializeTimerTask() {
        timerTask = new TimerTask() {
            public void run() {
                handler.post(() -> {
                    if (isAvailable()) {
                        repeatWordsNotification.send();
                    }
                });
            }
        };
    }

    private boolean isAvailable() {
        SharedPreferences preferences = getSharedPreferences(Preference.SHARED, MODE_PRIVATE);
        long enabledNotificationTime = preferences
                .getLong(RepeatWordsReceiver.ENABLED_NOTIFICATION_TIME_PREF_NAME,
                        0L);
        boolean isDateAvailable = new Date().getTime() > enabledNotificationTime;

        return !App.isForegrounded()
                && repeatWordsNotification != null
                && isDateAvailable;
    }
}