package com.wingsmight.pushwords.handlers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.WordPairStore;

import java.util.ArrayList;
import java.util.Collections;

public class RepeatWordsNotification {
    private static final String CHANNEL_ID = "REPEAT_WORDS_CHANNEL_ID";
    private static final String CHANNEL_NAME = "Word repeating";
    private static final String CHANNEL_DESCRIPTION = "Word repeating";
    private static final String NOTIFICATION_TITLE = "Повторите слова";

    public static final String EXTRA_WORD_PAIR = "EXTRA_WORD_PAIR";
    public static final String EXTRA_NOTIFICATION_ID = "EXTRA_NOTIFICATION_ID";


    private Context context;


    public RepeatWordsNotification(Context context) {
        this.context = context;

        createNotificationChannel();
    }


    public void send() {
        ArrayList<WordPair> pushedWordPairs = WordPairStore.getInstance(context)
                .getPushedOnly();
        if (pushedWordPairs.size() <= 0)
            return;

        Collections.sort(pushedWordPairs, (lhs, rhs) ->
                rhs.getChangingDate().compareTo(lhs.getChangingDate()));

        for (WordPair pushedWordPair : pushedWordPairs) {
            int notificationId = pushedWordPair.getOriginal().hashCode();

            Intent learnIntent = new Intent(context, RepeatWordsReceiver.class);
            learnIntent.setAction(RepeatWordsReceiver.Action.Learn.getId());
            learnIntent.putExtra(EXTRA_WORD_PAIR, pushedWordPair);
            learnIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
            PendingIntent learnPendingIntent = PendingIntent
                    .getBroadcast(context, notificationId, learnIntent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Action learnAction = new NotificationCompat.Action(0,
                    RepeatWordsReceiver.Action.Learn.getDescription(),
                    learnPendingIntent);

            Intent doNotSendTodayIntent = new Intent(context, RepeatWordsReceiver.class);
            doNotSendTodayIntent.setAction(RepeatWordsReceiver.Action.DoNotSendToday.getId());
            doNotSendTodayIntent.putExtra(EXTRA_NOTIFICATION_ID, notificationId);
            PendingIntent doNotSendPendingIntent = PendingIntent
                    .getBroadcast(context, notificationId, doNotSendTodayIntent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Action doNotSendTodayAction = new NotificationCompat.Action(0,
                    RepeatWordsReceiver.Action.DoNotSendToday.getDescription(),
                    doNotSendPendingIntent);

            String notificationText = pushedWordPair.getOriginal() + " - " +
                    pushedWordPair.getTranslation();

            NotificationCompat.Builder notificationBuilder = new NotificationCompat
                    .Builder(context, CHANNEL_ID)
                    .setSmallIcon(context.getApplicationInfo().icon)
                    .setContentTitle(NOTIFICATION_TITLE)
                    .setContentText(notificationText)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .addAction(learnAction)
                    .addAction(doNotSendTodayAction);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            notificationManager.notify(notificationId,
                    notificationBuilder.build());
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
