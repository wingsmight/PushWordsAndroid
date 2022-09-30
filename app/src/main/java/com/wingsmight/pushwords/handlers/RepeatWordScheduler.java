package com.wingsmight.pushwords.handlers;

import static android.content.Context.MODE_PRIVATE;

import static com.wingsmight.pushwords.ui.NotificationIntervalTab.DEFAULT_FROM_HOUR;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.DEFAULT_FROM_MINUTE;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.DEFAULT_TO_HOUR;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.DEFAULT_TO_MINUTE;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.FROM_HOUR_PREF_NAME;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.FROM_MINUTE_PREF_NAME;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.TO_HOUR_PREF_NAME;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.TO_MINUTE_PREF_NAME;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.wingsmight.pushwords.data.Preference;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.stores.WordPairStore;

import java.util.Date;

public class RepeatWordScheduler extends BroadcastReceiver {
    private static final String NOTIFICATION_TITLE = "Повторите слова";


    @Override
    public void onReceive(Context context, Intent intent) {
        WordPair pushedWordPair = intent
                .getParcelableExtra(RepeatWordsNotification.EXTRA_WORD_PAIR);

        boolean isPushed = WordPairStore.getInstance(context)
                .get(pushedWordPair)
                .isPushed();
        if (!isPushed) {
            return;
        }

        Date startNotificationDate = getStartNotificationDate(context);
        Date finishNotificationDate = getFinishNotificationDate(context);

        Date now = new Date();
        if (now.before(startNotificationDate)
            || now.after(finishNotificationDate)) {
            return;
        }

        int notificationId = pushedWordPair.hashCode();

        Intent learnIntent = new Intent(context, RepeatWordsReceiver.class);
        learnIntent.setAction(RepeatWordsReceiver.Action.Learn.getId());
        learnIntent.putExtra(RepeatWordsNotification.EXTRA_WORD_PAIR, pushedWordPair);
        learnIntent.putExtra(RepeatWordsNotification.EXTRA_NOTIFICATION_ID, notificationId);
        PendingIntent learnPendingIntent = PendingIntent
                .getBroadcast(context,
                        notificationId,
                        learnIntent,
                        PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Action learnAction = new NotificationCompat.Action(0,
                RepeatWordsReceiver.Action.Learn.getDescription(),
                learnPendingIntent);

        Intent doNotSendTodayIntent = new Intent(context, RepeatWordsReceiver.class);
        doNotSendTodayIntent.setAction(RepeatWordsReceiver.Action.DoNotSendToday.getId());
        doNotSendTodayIntent.putExtra(RepeatWordsNotification.EXTRA_NOTIFICATION_ID, notificationId);
        PendingIntent doNotSendPendingIntent = PendingIntent
                .getBroadcast(context, notificationId, doNotSendTodayIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Action doNotSendTodayAction = new NotificationCompat.Action(0,
                RepeatWordsReceiver.Action.DoNotSendToday.getDescription(),
                doNotSendPendingIntent);

        String notificationText = pushedWordPair.getOriginal() + " - " +
                pushedWordPair.getTranslation();

        NotificationCompat.Builder notificationBuilder = new NotificationCompat
                .Builder(context, RepeatWordsNotification.CHANNEL_ID)
                .setSmallIcon(context.getApplicationInfo().icon)
                .setContentTitle(NOTIFICATION_TITLE)
                .setContentText(notificationText)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .addAction(learnAction)
                .addAction(doNotSendTodayAction);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        notificationManager.cancel(notificationId);
        notificationManager.notify(notificationId,
                notificationBuilder.build());
    }

    private Date getStartNotificationDate(Context context) {
        SharedPreferences preferences = context
                .getSharedPreferences(Preference.SHARED, MODE_PRIVATE);

        Date startNotificationDate = new Date();
        startNotificationDate.setHours(preferences.getInt(FROM_HOUR_PREF_NAME, DEFAULT_FROM_HOUR));
        startNotificationDate.setMinutes(preferences.getInt(FROM_MINUTE_PREF_NAME, DEFAULT_FROM_MINUTE));

        return startNotificationDate;
    }
    private Date getFinishNotificationDate(Context context) {
        SharedPreferences preferences = context
                .getSharedPreferences(Preference.SHARED, MODE_PRIVATE);

        Date finishNotificationDate = new Date();
        finishNotificationDate.setHours(preferences.getInt(TO_HOUR_PREF_NAME, DEFAULT_TO_HOUR));
        finishNotificationDate.setMinutes(preferences.getInt(TO_MINUTE_PREF_NAME, DEFAULT_TO_MINUTE));

        return finishNotificationDate;
    }

}