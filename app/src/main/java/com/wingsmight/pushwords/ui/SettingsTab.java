package com.wingsmight.pushwords.ui;

import static com.wingsmight.pushwords.ui.NotificationIntervalTab.DEFAULT_FROM_HOUR;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.DEFAULT_FROM_MINUTE;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.DEFAULT_TO_HOUR;
import static com.wingsmight.pushwords.ui.NotificationIntervalTab.DEFAULT_TO_MINUTE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.wingsmight.pushwords.BuildConfig;
import com.wingsmight.pushwords.PushWordsApplication;
import com.wingsmight.pushwords.R;
import com.wingsmight.pushwords.data.BillingProduct;
import com.wingsmight.pushwords.data.NotificationFrequency;
import com.wingsmight.pushwords.data.Preference;
import com.wingsmight.pushwords.data.stores.UserStore;
import com.wingsmight.pushwords.handlers.AppCycle;
import com.wingsmight.pushwords.ui.learnedTab.TestSettingsTab;
import com.wingsmight.pushwords.ui.signUpTab.SignUpTab;

import java.util.TimeZone;

public class SettingsTab extends AppCompatActivity {
    public static final String NOTIFICATION_WORD_COUNT_PREF_NAME = "NOTIFICATION_WORD_COUNT_PREF_NAME";
    public static final int NOTIFICATION_WORD_COUNT_DEFAULT = 1;
    public static final String NOTIFICATION_FREQUENCY_INDEX_PREF_NAME = "NOTIFICATION_FREQUENCY_INDEX_PREF_NAME";
    public static final int DEFAULT_NOTIFICATION_FREQUENCY_INDEX = 0;

    private static final String ACTION_BAR_TITLE = "Настройки";
    private static final int NON_SUBSCRIPTION_NOTIFICATION_MAX_WORD_COUNT = 1;
    private static final int SUBSCRIPTION_NOTIFICATION_MAX_WORD_COUNT = 4;


    private TextView timeZoneText;
    private NumberPicker notificationWordCountPicker;
    private Spinner notificationFrequencySpinner;
    private TextView notificationIntervalText;
    private View testSettingsButton;
    private View shareAppButton;
    private View signOutButton;
    private View subscriptionButton;


    private SharedPreferences.Editor preferencesEditor;
    private SharedPreferences preferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings_tab);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        preferences = getSharedPreferences(Preference.SHARED, MODE_PRIVATE);
        preferencesEditor = preferences.edit();

        // Time zone text
        timeZoneText = findViewById(R.id.timeZoneText);
        TimeZone timeZone = TimeZone.getDefault();
        String timeZoneDisplayName = timeZone.getDisplayName(false, TimeZone.SHORT)
            + " " + timeZone.getID();
        timeZoneText.setText(timeZoneDisplayName);

        notificationWordCountPicker = findViewById(R.id.notificationWordCountPicker);
        initPicker(notificationWordCountPicker,
                NOTIFICATION_WORD_COUNT_PREF_NAME,
                NOTIFICATION_WORD_COUNT_DEFAULT,
                NON_SUBSCRIPTION_NOTIFICATION_MAX_WORD_COUNT,
                getNotificationMaxWordCount());

        // Notification frequency spinner
        notificationFrequencySpinner = findViewById(R.id.notificationFrequencySpinner);

        String[] notificationFrequencyDescriptions
                = new String[NotificationFrequency.values().length];
        NotificationFrequency[] notificationFrequencies = NotificationFrequency.values();
        for (int i = 0; i < notificationFrequencies.length; i++) {
            notificationFrequencyDescriptions[i] = notificationFrequencies[i].getDescription();
        }
        notificationFrequencySpinner
                .setAdapter(new ArrayAdapter<>(this,
                        android.R.layout.simple_spinner_item,
                        notificationFrequencyDescriptions));

        int currentNotificationFrequencyIndex = preferences
                .getInt(NOTIFICATION_FREQUENCY_INDEX_PREF_NAME,
                        DEFAULT_NOTIFICATION_FREQUENCY_INDEX);
        notificationFrequencySpinner.setSelection(currentNotificationFrequencyIndex);
        notificationFrequencySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int index, long l) {
                preferencesEditor.putInt(NOTIFICATION_FREQUENCY_INDEX_PREF_NAME, index);
                preferencesEditor.commit();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });

        // Notification interval text
        notificationIntervalText = findViewById(R.id.notificationInterval);
        notificationIntervalText.setOnClickListener(view ->
                startActivity(new Intent(this, NotificationIntervalTab.class)));

        // Test settings button
        testSettingsButton = findViewById(R.id.testSettingsButton);
        testSettingsButton.setOnClickListener(view ->
                startActivity(new Intent(this, TestSettingsTab.class)));

        // Share app button
        shareAppButton = findViewById(R.id.shareAppButton);
        shareAppButton.setOnClickListener(view -> {
            try {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, getApplicationInfo().name);
                String shareMessage= "\nРекомендую приложение\n\n";
                shareMessage = shareMessage + "https://play.google.com/store/apps/details?id="
                        + BuildConfig.APPLICATION_ID +"\n\n";
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                startActivity(Intent.createChooser(shareIntent, "выберите одно"));
            } catch(Exception e) {
                e.printStackTrace();
            }
        });

        // Sign out button
        signOutButton = findViewById(R.id.signOutButton);
        signOutButton.setOnClickListener(view -> {
            UserStore.getInstance(this).setUser(null);
            FirebaseAuth.getInstance().signOut();

            startActivity(new Intent(SettingsTab.this, SignUpTab.class));

            finish();
        });

        // Subscription button
        subscriptionButton = findViewById(R.id.subscriptionButton);
        subscriptionButton.setOnClickListener(view -> ((PushWordsApplication) getApplicationContext())
                .getBillingHandler()
                .subscribe(this, BillingProduct.Subscription));
    }
    @Override
    protected void onPause() {
        super.onPause();

        AppCycle.quit(this);
    }
    @Override
    protected void onStart() {
        super.onStart();

        getSupportActionBar().setTitle(ACTION_BAR_TITLE);

        int fromHour = preferences.getInt(NotificationIntervalTab.FROM_HOUR_PREF_NAME, DEFAULT_FROM_HOUR);
        int fromMinute = preferences.getInt(NotificationIntervalTab.FROM_MINUTE_PREF_NAME, DEFAULT_FROM_MINUTE);
        int toHour = preferences.getInt(NotificationIntervalTab.TO_HOUR_PREF_NAME, DEFAULT_TO_HOUR);
        int toMinute = preferences.getInt(NotificationIntervalTab.TO_MINUTE_PREF_NAME, DEFAULT_TO_MINUTE);

        String intervalText = fromHour + ":" + String.format("%02d", fromMinute)
                + " - "
                + toHour + ":" + String.format("%02d", toMinute);
        notificationIntervalText.setText(intervalText);
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();

        return true;
    }

    // TODO: fix repeating from TestSettingsTab
    private void initPicker(NumberPicker picker,
                            String name,
                            int defaultValue,
                            int minValue,
                            int maxValue) {
        int value = preferences.getInt(name, defaultValue);

        picker.setMinValue(minValue);
        picker.setMaxValue(maxValue);
        picker.setValue(value);
        picker.setOnValueChangedListener((numberPicker, i, i1) ->
                preferencesEditor
                        .putInt(name, numberPicker.getValue())
                        .apply());
    }
    private Integer getNotificationMaxWordCount() {
        Boolean isSubscribed = UserStore.getInstance(this)
                .getUser()
                .getSubscribed();

        int notificationMaxWordCount = isSubscribed
                ? SUBSCRIPTION_NOTIFICATION_MAX_WORD_COUNT
                : SettingsTab.NOTIFICATION_WORD_COUNT_DEFAULT;

        return notificationMaxWordCount;
    }
}