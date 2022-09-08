package com.wingsmight.pushwords.handlers;

import static com.wingsmight.pushwords.handlers.RepeatWordsReceiver.ENABLED_NOTIFICATION_TIME_PREF_NAME;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.DEFAULT_FROM_HOUR;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.DEFAULT_FROM_MINUTE;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.DEFAULT_TO_HOUR;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.DEFAULT_TO_MINUTE;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.FROM_HOUR_PREF_NAME;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.FROM_MINUTE_PREF_NAME;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.TO_HOUR_PREF_NAME;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.TO_MINUTE_PREF_NAME;
import static com.wingsmight.pushwords.ui.SettingsTab.DEFAULT_NOTIFICATION_FREQUENCY_INDEX;
import static com.wingsmight.pushwords.ui.SettingsTab.NOTIFICATION_FREQUENCY_INDEX_PREF_NAME;

import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.IBinder;

import com.wingsmight.pushwords.data.NotificationFrequency;
import com.wingsmight.pushwords.data.Preference;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    private Timer timer;
    private TimerTask timerTask;
    private RepeatWordsNotification repeatWordsNotification;
    private final Handler handler = new Handler();
    private SharedPreferences preferences;


    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        repeatWordsNotification = new RepeatWordsNotification(getApplicationContext());

        preferences = getSharedPreferences(Preference.SHARED, MODE_PRIVATE);

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

        int frequencyIndex = preferences.getInt(NOTIFICATION_FREQUENCY_INDEX_PREF_NAME,
                DEFAULT_NOTIFICATION_FREQUENCY_INDEX);
        NotificationFrequency currentFrequency = NotificationFrequency.values()[frequencyIndex];

        Date startNotificationDate = getStartNotificationDate();

        long delay = 0;
        Date now = new Date();
        if (new Date().before(startNotificationDate)) {
            delay = startNotificationDate.getTime() - new Date().getTime();
        }

        timer.schedule(timerTask,
                delay,
                currentFrequency.getMilliseconds());
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
        Date now = new Date();

        long enabledNotificationTime = preferences
                .getLong(ENABLED_NOTIFICATION_TIME_PREF_NAME, 0L);

        boolean isDateAvailable = now.getTime() > enabledNotificationTime;

        Date startNotificationDate = getStartNotificationDate();
        Date finishNotificationDate = getFinishNotificationDate();
        boolean isBetweenNotificationInterval = now.after(startNotificationDate)
                && now.before(finishNotificationDate);

        return !App.isForegrounded()
                && repeatWordsNotification != null
                && isDateAvailable
                && isBetweenNotificationInterval;
    }
    private Date getStartNotificationDate() {
        Date startNotificationDate = new Date();
        startNotificationDate.setHours(preferences.getInt(FROM_HOUR_PREF_NAME, DEFAULT_FROM_HOUR));
        startNotificationDate.setMinutes(preferences.getInt(FROM_MINUTE_PREF_NAME, DEFAULT_FROM_MINUTE));

        return startNotificationDate;
    }
    private Date getFinishNotificationDate() {
        Date finishNotificationDate = new Date();
        finishNotificationDate.setHours(preferences.getInt(TO_HOUR_PREF_NAME, DEFAULT_TO_HOUR));
        finishNotificationDate.setMinutes(preferences.getInt(TO_MINUTE_PREF_NAME, DEFAULT_TO_MINUTE));

        return finishNotificationDate;
    }
}