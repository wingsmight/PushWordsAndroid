package com.wingsmight.pushwords.handlers;

import static android.content.Context.MODE_PRIVATE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import androidx.core.app.NotificationManagerCompat;

import com.wingsmight.pushwords.data.Preference;
import com.wingsmight.pushwords.data.WordPair;
import com.wingsmight.pushwords.data.stores.WordPairStore;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class RepeatWordsReceiver extends BroadcastReceiver {
    private static final long MILLISECONDS_IN_DAY = 24 * 60 * 60 * 1000L;

    public static final String ENABLED_NOTIFICATION_TIME_PREF_NAME =
            "ENABLED_NOTIFICATION_TIME_PREF_NAME";


    @Override
    public void onReceive(Context context, Intent intent) {
        String usedActionAsString = intent.getAction();
        Action usedAction = null;
        List<Action> actions = Arrays.asList(Action.values());
        for (Action action : actions) {
            if (action.getId().equals(usedActionAsString)) {
                usedAction = action;

                break;
            }
        }

        if (usedAction == null) {
            return;
        }

        int notificationId = intent
                .getIntExtra(RepeatWordsNotification.EXTRA_NOTIFICATION_ID, 0);

        WordPairStore wordPairStore = WordPairStore.getInstance(context);
        NotificationManagerCompat notificationManager = NotificationManagerCompat
                .from(context);

        switch (usedAction) {
            case Learn:
                WordPair wordPair = intent
                        .getParcelableExtra(RepeatWordsNotification.EXTRA_WORD_PAIR);
                wordPair = wordPairStore.get(wordPair);
                wordPair.setPushed(false);
                wordPair.setState(WordPair.State.Learned);

                wordPairStore.save();

                notificationManager.cancel(notificationId);

                break;
            case DoNotSendToday:
                SharedPreferences preferences = context
                        .getSharedPreferences(Preference.SHARED, MODE_PRIVATE);
                SharedPreferences.Editor preferencesEditor = preferences.edit();

                long enabledNotificationTime = new Date(System.currentTimeMillis()
                        + MILLISECONDS_IN_DAY).getTime();
                preferencesEditor.putLong(ENABLED_NOTIFICATION_TIME_PREF_NAME,
                        enabledNotificationTime);
                preferencesEditor.commit();

                notificationManager.cancelAll();

                break;
        }
    }


    public enum Action {
        Learn {
            public String getId() {
                return "LEARN";
            }
            public String getDescription() {
                return "Выучено";
            }
        },
        DoNotSendToday {
            public String getId() {
                return "DO_NOT_SEND_TODAY";
            }
            public String getDescription() {
                return "Не отправлять сегодня";
            }
        };


        public abstract String getId();
        public abstract String getDescription();
    }
}
