package com.wingsmight.pushwords.handlers;

import static com.wingsmight.pushwords.ui.SettingsTab.DEFAULT_NOTIFICATION_FREQUENCY_INDEX;
import static com.wingsmight.pushwords.ui.SettingsTab.NOTIFICATION_FREQUENCY_INDEX_PREF_NAME;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.wingsmight.pushwords.data.NotificationFrequency;
import com.wingsmight.pushwords.data.Preference;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.stores.WordPairStore;

import java.util.ArrayList;
import java.util.Calendar;

public class RepeatWordsNotification {
    private static final String CHANNEL_NAME = "Word repeating";
    private static final String CHANNEL_DESCRIPTION = "Word repeating";

    public static final String CHANNEL_ID = "REPEAT_WORDS_CHANNEL_ID";
    public static final String EXTRA_WORD_PAIR = "EXTRA_WORD_PAIR";
    public static final String EXTRA_NOTIFICATION_ID = "EXTRA_NOTIFICATION_ID";


    private Context context;
    private SharedPreferences preferences;


    public RepeatWordsNotification(Context context) {
        this.context = context;

        createNotificationChannel();
        preferences = context.getSharedPreferences(Preference.SHARED, Context.MODE_PRIVATE);
    }


    public void send(WordPair pushedWordPair) {
        int frequencyIndex = preferences.getInt(NOTIFICATION_FREQUENCY_INDEX_PREF_NAME,
                DEFAULT_NOTIFICATION_FREQUENCY_INDEX);
        NotificationFrequency currentFrequency = NotificationFrequency.values()[frequencyIndex];

        Intent alarmIntent = new Intent(context, RepeatWordScheduler.class);
        alarmIntent.putExtra(EXTRA_WORD_PAIR, pushedWordPair);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                pushedWordPair.hashCode(),
                alarmIntent,
                PendingIntent.FLAG_IMMUTABLE);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MILLISECOND, Math.toIntExact(currentFrequency.getMilliseconds()));

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                currentFrequency.getMilliseconds(), pendingIntent);
    }
    public void cancel(WordPair wordPair) {
        int notificationId = wordPair.hashCode();

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(notificationId);

        Intent alarmIntent = new Intent(context, RepeatWordScheduler.class);
        alarmIntent.putExtra(EXTRA_WORD_PAIR, wordPair);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,
                notificationId,
                alarmIntent,
                PendingIntent.FLAG_IMMUTABLE);

        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
    }
    public void cancelAll() {
        ArrayList<WordPair> pushedWordPairs = WordPairStore.getInstance(context)
                        .getPushedOnly();

        for (WordPair pushedWordPair : pushedWordPairs) {
            cancel(pushedWordPair);
        }
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance);
            channel.setDescription(CHANNEL_DESCRIPTION);
            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}
